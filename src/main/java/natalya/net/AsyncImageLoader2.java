package natalya.net;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import io.fabric.sdk.android.services.common.AbstractSpiCall;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import natalya.app.ExternalStorageUtils;
import natalya.codec.DigestUtils;
import natalya.graphics.BitmapUtils;
import natalya.io.FileUtils;

public class AsyncImageLoader2 {
    public static final int BIG_SINGLE = 1;
    private static final int IMAGEVIEW_TAG_KEY = 2131296255;
    private static final int MSG_CLEAR_BITMAP = 0;
    public static final int SMALL_MULTI = 0;
    private static final String TAG = "AID";
    private final int DELAY_BEFORE_PURGE = AbstractSpiCall.DEFAULT_TIMEOUT;
    private int HARD_CACHE_CAPACITY = 64;
    private final int KEEP_ALIVE = 1;
    private boolean LOCAL_CACHE = true;
    private int MAX_WIDTH = 640;
    private int MODE = 0;
    private boolean SCALE_BITMAP = false;
    private Handler clearHandler;
    private Application context;
    private CustomBitmapLoader customBitmapLoader = null;
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            Object[] data = (Object[]) message.obj;
            data[1].attach(data[0]);
        }
    };
    private Handler purgeHandler;
    private Runnable purger;
    private ThreadPoolExecutor sExecutor;
    private ThreadPoolExecutor sExecutor2;
    private ThreadPoolExecutor sExecutor3;
    private HashMap<String, Bitmap> sHardBitmapCache;
    private ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache;
    private final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncImageLoader #" + this.mCount.getAndIncrement());
        }
    };
    private BlockingQueue<Runnable> sWorkQueue;
    private BlockingQueue<Runnable> sWorkQueue2;
    private BlockingQueue<Runnable> sWorkQueue3;

    public interface CustomBitmapLoader {
        Bitmap getBitmap(Object obj);
    }

    public interface ImageAttacher {
        void attach(Bitmap bitmap);

        Bitmap prepare(Bitmap bitmap);
    }

    public static class DefaultImageAttacher implements ImageAttacher {
        private String fileName;
        private ImageView v;

        public DefaultImageAttacher(ImageView v, String fileName) {
            this.v = v;
            this.fileName = fileName;
        }

        public Bitmap prepare(Bitmap bitmap) {
            return bitmap;
        }

        public void attach(Bitmap bitmap) {
            String fileName0 = (String) this.v.getTag(AsyncImageLoader2.IMAGEVIEW_TAG_KEY);
            if (this.v == null) {
                return;
            }
            if (fileName0 == null || fileName0.equals(this.fileName)) {
                this.v.setImageBitmap(bitmap);
            }
        }
    }

    public static class DummyImageAttacher implements ImageAttacher {
        public Bitmap prepare(Bitmap bitmap) {
            return bitmap;
        }

        public void attach(Bitmap bitmap) {
        }
    }

    public static class FadeinImageAttacher implements ImageAttacher {
        private String fileName;
        private ImageView v;

        public FadeinImageAttacher(ImageView v, String fileName) {
            this.v = v;
            this.fileName = fileName;
        }

        public Bitmap prepare(Bitmap bitmap) {
            return bitmap;
        }

        public void attach(Bitmap bitmap) {
            String fileName0 = (String) this.v.getTag(AsyncImageLoader2.IMAGEVIEW_TAG_KEY);
            if (this.v == null) {
                return;
            }
            if (fileName0 == null || fileName0.equals(this.fileName)) {
                this.v.setImageBitmap(bitmap);
                AlphaAnimation fadein = new AlphaAnimation(0.1f, 1.0f);
                fadein.setDuration(500);
                fadein.setInterpolator(new AccelerateDecelerateInterpolator());
                fadein.setRepeatCount(0);
                this.v.startAnimation(fadein);
            }
        }
    }

    public AsyncImageLoader2(Application app) {
        this.context = app;
    }

    public void setMode(int mode) {
        this.MODE = mode;
    }

    public void setBitmapScale(boolean enable, int maxWidth) {
        this.SCALE_BITMAP = enable;
        this.MAX_WIDTH = maxWidth;
    }

    public void setLocalCache(boolean enable, int size) {
        this.LOCAL_CACHE = enable;
        this.HARD_CACHE_CAPACITY = size;
    }

    public void setCustomBitmapLoader(CustomBitmapLoader loader) {
        this.customBitmapLoader = loader;
    }

    public void init() {
        if (this.LOCAL_CACHE) {
            this.sHardBitmapCache = new LinkedHashMap<String, Bitmap>(this.HARD_CACHE_CAPACITY / 2, 0.75f, true) {
                public boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
                    if (size() <= AsyncImageLoader2.this.HARD_CACHE_CAPACITY) {
                        return false;
                    }
                    AsyncImageLoader2.this.sSoftBitmapCache.put(eldest.getKey(), new SoftReference(eldest.getValue()));
                    return true;
                }
            };
            this.sSoftBitmapCache = new ConcurrentHashMap(this.HARD_CACHE_CAPACITY / 2);
            this.purgeHandler = new Handler();
            this.clearHandler = new Handler() {
                public void handleMessage(Message m) {
                    switch (m.what) {
                        case 0:
                            String url = m.obj;
                            if (url != null) {
                                AsyncImageLoader2.this.doClearBitmap(url);
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                }
            };
            this.purger = new Runnable() {
                public void run() {
                    AsyncImageLoader2.this.clearCache(false);
                }
            };
        }
        DiscardOldestPolicy policy = new DiscardOldestPolicy();
        switch (this.MODE) {
            case 0:
                this.sWorkQueue = new LinkedBlockingQueue(32);
                this.sExecutor = new ThreadPoolExecutor(2, 4, 1, TimeUnit.SECONDS, this.sWorkQueue, this.sThreadFactory, policy);
                this.sWorkQueue2 = new LinkedBlockingQueue(8);
                this.sExecutor2 = new ThreadPoolExecutor(1, 2, 1, TimeUnit.SECONDS, this.sWorkQueue2, this.sThreadFactory, policy);
                this.sWorkQueue3 = new LinkedBlockingQueue(8);
                this.sExecutor3 = new ThreadPoolExecutor(1, 2, 1, TimeUnit.SECONDS, this.sWorkQueue3, this.sThreadFactory, policy);
                return;
            case 1:
                this.sWorkQueue = new LinkedBlockingQueue(3);
                this.sExecutor = new ThreadPoolExecutor(1, 3, 1, TimeUnit.SECONDS, this.sWorkQueue, this.sThreadFactory, policy);
                this.sWorkQueue2 = new LinkedBlockingQueue(6);
                this.sExecutor2 = new ThreadPoolExecutor(2, 6, 1, TimeUnit.SECONDS, this.sWorkQueue2, this.sThreadFactory, policy);
                this.sWorkQueue3 = new LinkedBlockingQueue(6);
                this.sExecutor3 = new ThreadPoolExecutor(1, 3, 1, TimeUnit.SECONDS, this.sWorkQueue3, this.sThreadFactory, policy);
                return;
            default:
                return;
        }
    }

    public void loadImage(String url, ImageView iv) {
        loadImage(url, iv, -1, false);
    }

    public void loadImage(String url, ImageView iv, int defaultResource, boolean fadeinAnim) {
        loadImage(url, null, iv, defaultResource, fadeinAnim);
    }

    public void loadImage(String url, Object bitmapKey, ImageView iv, int defaultResource, boolean fadeinAnim) {
        if (!TextUtils.isEmpty(url)) {
            ImageAttacher attacher;
            String fileName = getFileName(url);
            if (fadeinAnim) {
                attacher = new FadeinImageAttacher(iv, fileName);
            } else {
                attacher = new DefaultImageAttacher(iv, fileName);
            }
            iv.setTag(IMAGEVIEW_TAG_KEY, fileName);
            Bitmap bitmap = getBitmapFromCache(fileName);
            if (bitmap != null) {
                iv.setImageBitmap(bitmap);
                return;
            }
            if (defaultResource > 0) {
                iv.setImageResource(defaultResource);
            } else {
                iv.setImageDrawable(null);
            }
            async(url, bitmapKey, fileName, attacher);
        } else if (defaultResource > 0) {
            iv.setImageResource(defaultResource);
        }
    }

    public void loadImage(String url, ImageAttacher attacher) {
        if (!TextUtils.isEmpty(url)) {
            String fileName = getFileName(url);
            Bitmap bitmap = getBitmapFromCache(fileName);
            if (bitmap != null) {
                bitmap = attacher.prepare(bitmap);
                Message msg = this.handler.obtainMessage();
                msg.obj = new Object[]{bitmap, attacher};
                this.handler.sendMessage(msg);
                return;
            }
            async(url, null, fileName, attacher);
        }
    }

    private void resetPurgeTimer() {
        if (this.purgeHandler != null) {
            this.purgeHandler.removeCallbacks(this.purger);
            this.purgeHandler.postDelayed(this.purger, 10000);
        }
    }

    public void clearCache(boolean force) {
        if (this.LOCAL_CACHE) {
            if (force) {
                Bitmap b;
                Set<String> keys = this.sHardBitmapCache.keySet();
                for (String s : keys) {
                    b = (Bitmap) this.sHardBitmapCache.get(s);
                    if (b != null) {
                        try {
                            b.recycle();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                Set<String> keys2 = this.sSoftBitmapCache.keySet();
                for (String s2 : keys) {
                    SoftReference<Bitmap> bitmapReference = (SoftReference) this.sSoftBitmapCache.get(s2);
                    if (bitmapReference != null) {
                        b = (Bitmap) bitmapReference.get();
                        if (b != null) {
                            try {
                                b.recycle();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }
            }
            this.sHardBitmapCache.clear();
            this.sSoftBitmapCache.clear();
        }
    }

    public void clearBitmap(String url) {
        clearBitmap(url, 0);
    }

    public void clearBitmap(String url, long delayMillis) {
        if (!this.LOCAL_CACHE) {
            return;
        }
        if (delayMillis > 0) {
            Message msg = this.clearHandler.obtainMessage(0);
            msg.obj = url;
            this.clearHandler.sendMessageDelayed(msg, delayMillis);
            return;
        }
        doClearBitmap(url);
    }

    private void doClearBitmap(String url) {
        if (this.LOCAL_CACHE) {
            String fileName = getFileName(url);
            if (this.sHardBitmapCache.containsKey(fileName)) {
                Bitmap bitmap = (Bitmap) this.sHardBitmapCache.get(fileName);
                if (bitmap != null) {
                    try {
                        bitmap.recycle();
                        this.sHardBitmapCache.remove(fileName);
                        this.sSoftBitmapCache.remove(fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (this.sSoftBitmapCache.containsKey(fileName)) {
                SoftReference<Bitmap> bitmapReference = (SoftReference) this.sSoftBitmapCache.get(fileName);
                if (bitmapReference != null) {
                    try {
                        ((Bitmap) bitmapReference.get()).recycle();
                        this.sSoftBitmapCache.remove(fileName);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    public void clearFiles() {
        try {
            File directory = new File(getFileDir(this.context));
            if (directory.exists()) {
                for (File f : directory.listFiles()) {
                    f.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getFileDir(Application app) {
        return ExternalStorageUtils.getExternalFilesDir(app.getPackageName(), "Pictures").getAbsolutePath();
    }

    public static String getFileName(String url) {
        return DigestUtils.md5Hex(url) + ".jpg";
    }

    public boolean isImageDownloaded(String url) {
        return isImageDownloaded(this.context, url);
    }

    public static boolean isImageDownloaded(Application app, String url) {
        return FileUtils.isFileExists(getFileDir(app), getFileName(url));
    }

    public static Uri getFileUri(Application app, String url) {
        String fileDir = getFileDir(app);
        String fileName = getFileName(url);
        if (FileUtils.isFileExists(fileDir, fileName)) {
            try {
                return Uri.fromFile(new File(fileDir + File.separator + fileName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Bitmap createBitmap(String fileDir, String fileName) {
        Bitmap pic = null;
        try {
            InputStream stream = FileUtils.openInputStream(fileDir, fileName);
            pic = BitmapFactory.decodeStream(stream);
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
        } catch (OutOfMemoryError e2) {
            e2.printStackTrace();
        }
        return pic;
    }

    private Bitmap createScaleBitmap(String fileDir, String fileName) {
        Bitmap pic = null;
        try {
            InputStream stream = FileUtils.openInputStream(fileDir, fileName);
            Options options = new Options();
            options.inJustDecodeBounds = true;
            pic = BitmapFactory.decodeStream(stream, null, options);
            int be = options.outWidth / this.MAX_WIDTH;
            if (be < 0) {
                be = 1;
            }
            options.inSampleSize = be;
            options.inJustDecodeBounds = false;
            if (options.outWidth > this.MAX_WIDTH) {
                options.inScaled = true;
                options.inDensity = options.outWidth;
                options.inTargetDensity = this.MAX_WIDTH;
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
            stream = FileUtils.openInputStream(fileDir, fileName);
            pic = BitmapFactory.decodeStream(stream, null, options);
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e2) {
                }
            }
        } catch (OutOfMemoryError e3) {
            e3.printStackTrace();
        }
        return pic;
    }

    private void addBitmapToCache(String url, Bitmap bitmap) {
        if (this.LOCAL_CACHE && bitmap != null) {
            synchronized (this.sHardBitmapCache) {
                this.sHardBitmapCache.put(url, bitmap);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.graphics.Bitmap getBitmapFromCache(java.lang.String r6) {
        /*
        r5 = this;
        r2 = 0;
        r3 = r5.LOCAL_CACHE;
        if (r3 != 0) goto L_0x0007;
    L_0x0005:
        r0 = r2;
    L_0x0006:
        return r0;
    L_0x0007:
        r3 = r5.sHardBitmapCache;
        monitor-enter(r3);
        r4 = r5.sHardBitmapCache;	 Catch:{ all -> 0x0020 }
        r0 = r4.get(r6);	 Catch:{ all -> 0x0020 }
        r0 = (android.graphics.Bitmap) r0;	 Catch:{ all -> 0x0020 }
        if (r0 == 0) goto L_0x0023;
    L_0x0014:
        r2 = r5.sHardBitmapCache;	 Catch:{ all -> 0x0020 }
        r2.remove(r6);	 Catch:{ all -> 0x0020 }
        r2 = r5.sHardBitmapCache;	 Catch:{ all -> 0x0020 }
        r2.put(r6, r0);	 Catch:{ all -> 0x0020 }
        monitor-exit(r3);	 Catch:{ all -> 0x0020 }
        goto L_0x0006;
    L_0x0020:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0020 }
        throw r2;
    L_0x0023:
        monitor-exit(r3);	 Catch:{ all -> 0x0020 }
        r3 = r5.sSoftBitmapCache;
        r1 = r3.get(r6);
        r1 = (java.lang.ref.SoftReference) r1;
        if (r1 == 0) goto L_0x003b;
    L_0x002e:
        r0 = r1.get();
        r0 = (android.graphics.Bitmap) r0;
        if (r0 != 0) goto L_0x0006;
    L_0x0036:
        r3 = r5.sSoftBitmapCache;
        r3.remove(r6);
    L_0x003b:
        r0 = r2;
        goto L_0x0006;
        */
        throw new UnsupportedOperationException("Method not decompiled: natalya.net.AsyncImageLoader2.getBitmapFromCache(java.lang.String):android.graphics.Bitmap");
    }

    public Bitmap getLocalBitmap(String url) {
        if (isImageDownloaded(url)) {
            return getLocalBitmap(getFileDir(this.context), getFileName(url));
        }
        return null;
    }

    private Bitmap getLocalBitmap(String fileDir, String fileName) {
        resetPurgeTimer();
        Bitmap bitmap = getBitmapFromCache(fileName);
        if (bitmap != null) {
            return bitmap;
        }
        if (this.SCALE_BITMAP) {
            bitmap = createScaleBitmap(fileDir, fileName);
        } else {
            bitmap = createBitmap(fileDir, fileName);
        }
        addBitmapToCache(fileName, bitmap);
        return bitmap;
    }

    private Bitmap getNetBitmap(String url, final String fileDir, final String fileName) {
        Bitmap bitmap = null;
        InputStream stream = null;
        try {
            stream = Crawler.crawlUrl(url);
            if (stream != null) {
                if (!this.SCALE_BITMAP) {
                    try {
                        bitmap = BitmapFactory.decodeStream(stream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null) {
                        final Bitmap image = bitmap;
                        this.sExecutor3.execute(new Runnable() {
                            public void run() {
                                try {
                                    FileUtils.writeStreamToFile(new ByteArrayInputStream(BitmapUtils.generateBitstream(image, CompressFormat.JPEG, 85)), fileDir, fileName);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } else if (FileUtils.writeStreamToFile(stream, fileDir, fileName)) {
                    bitmap = createScaleBitmap(fileDir, fileName);
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e2) {
                }
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e4) {
                }
            }
        } catch (Throwable th) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e5) {
                }
            }
        }
        return bitmap;
    }

    private Bitmap getCustomBitmap(Object bitmapKey, final String fileDir, final String fileName) {
        Bitmap bitmap = null;
        if (!(this.customBitmapLoader == null || bitmapKey == null)) {
            bitmap = this.customBitmapLoader.getBitmap(bitmapKey);
        }
        if (bitmap != null && this.SCALE_BITMAP) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (width > this.MAX_WIDTH) {
                try {
                    Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, this.MAX_WIDTH, height * (this.MAX_WIDTH / width), true);
                    if (newBitmap != null) {
                        bitmap.recycle();
                        bitmap = newBitmap;
                    }
                } catch (Exception e) {
                }
            }
            final Bitmap image = bitmap;
            this.sExecutor3.execute(new Runnable() {
                public void run() {
                    try {
                        FileUtils.writeStreamToFile(new ByteArrayInputStream(BitmapUtils.generateBitstream(image, CompressFormat.JPEG, 85)), fileDir, fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return bitmap;
    }

    private void async(String url, Object bitmapKey, String fileName, ImageAttacher attacher) {
        final String fileDir = getFileDir(this.context);
        if ((this.customBitmapLoader == null || bitmapKey == null) && !FileUtils.isFileExists(fileDir, fileName)) {
            final String str = url;
            final String str2 = fileDir;
            final String str3 = fileName;
            final ImageAttacher imageAttacher = attacher;
            this.sExecutor.execute(new Runnable() {
                public void run() {
                    Bitmap bitmap = AsyncImageLoader2.this.getNetBitmap(str, str2, str3);
                    AsyncImageLoader2.this.addBitmapToCache(str3, bitmap);
                    bitmap = imageAttacher.prepare(bitmap);
                    Message msg = AsyncImageLoader2.this.handler.obtainMessage();
                    msg.obj = new Object[]{bitmap, imageAttacher};
                    AsyncImageLoader2.this.handler.sendMessage(msg);
                }
            });
            return;
        }
        final String str4 = fileName;
        final Object obj = bitmapKey;
        final ImageAttacher imageAttacher2 = attacher;
        str2 = url;
        this.sExecutor2.execute(new Runnable() {
            public void run() {
                Bitmap bitmap = null;
                if (FileUtils.isFileExists(fileDir, str4)) {
                    bitmap = AsyncImageLoader2.this.getLocalBitmap(fileDir, str4);
                }
                if (bitmap == null && obj != null) {
                    bitmap = AsyncImageLoader2.this.getCustomBitmap(obj, fileDir, str4);
                }
                if (bitmap != null) {
                    AsyncImageLoader2.this.addBitmapToCache(str4, bitmap);
                    bitmap = imageAttacher2.prepare(bitmap);
                    Message msg = AsyncImageLoader2.this.handler.obtainMessage();
                    msg.obj = new Object[]{bitmap, imageAttacher2};
                    AsyncImageLoader2.this.handler.sendMessage(msg);
                    return;
                }
                AsyncImageLoader2.this.sExecutor.execute(new Runnable() {
                    public void run() {
                        Bitmap bitmap = AsyncImageLoader2.this.getNetBitmap(str2, fileDir, str4);
                        AsyncImageLoader2.this.addBitmapToCache(str4, bitmap);
                        bitmap = imageAttacher2.prepare(bitmap);
                        Message msg = AsyncImageLoader2.this.handler.obtainMessage();
                        msg.obj = new Object[]{bitmap, imageAttacher2};
                        AsyncImageLoader2.this.handler.sendMessage(msg);
                    }
                });
            }
        });
    }
}
