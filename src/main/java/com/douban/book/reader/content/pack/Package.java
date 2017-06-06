package com.douban.book.reader.content.pack;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.SparseArray;
import com.crashlytics.android.Crashlytics;
import com.douban.book.reader.constant.Key;
import com.douban.book.reader.content.Book.ImageSize;
import com.douban.book.reader.content.cipher.CipherFactory;
import com.douban.book.reader.content.pack.WorksData.Status;
import com.douban.book.reader.entity.Manifest;
import com.douban.book.reader.entity.Manifest.PackMeta;
import com.douban.book.reader.entity.Manifest.PackagePayload;
import com.douban.book.reader.exception.CipherException;
import com.douban.book.reader.exception.DataException;
import com.douban.book.reader.exception.ManifestException;
import com.douban.book.reader.exception.PackageException;
import com.douban.book.reader.exception.WorksException;
import com.douban.book.reader.exception.WrongPackageException;
import com.douban.book.reader.helper.DownloadHelper;
import com.douban.book.reader.task.DownloadManager;
import com.douban.book.reader.util.AppInfo;
import com.douban.book.reader.util.DebugSwitch;
import com.douban.book.reader.util.ExceptionUtils;
import com.douban.book.reader.util.FilePath;
import com.douban.book.reader.util.IOUtils;
import com.douban.book.reader.util.JsonUtils;
import com.douban.book.reader.util.Logger;
import com.douban.book.reader.util.Pref;
import com.douban.book.reader.util.ReaderUri;
import com.douban.book.reader.util.ReaderUriUtils;
import com.douban.book.reader.util.StringUtils;
import com.douban.book.reader.util.Utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.util.Date;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

public class Package {
    private static final String[] CHAPTER_FILE_ENTRIES = new String[]{"content"};
    private static final String TAG = Package.class.getSimpleName();
    private static SparseArray<Package> hInstances = new SparseArray();
    private CipherFactory mCipherFactory = null;
    private DownloadHelper mDownloadHelper = null;
    private File mFile = null;
    private int mPackageId;
    private Uri mUri = null;
    private int mWorksId;
    private ZipFile mZipFile = null;

    public Package(int worksId, int packageId) {
        this.mWorksId = worksId;
        this.mPackageId = packageId;
        this.mUri = ReaderUri.pack(this.mWorksId, this.mPackageId);
        this.mFile = getFile();
        this.mDownloadHelper = new DownloadHelper(this.mUri, this.mFile);
        addToCache(packageId, this);
    }

    public static Package get(Uri uri) throws PackageException {
        switch (ReaderUriUtils.getType(uri)) {
            case 2:
            case 3:
            case 4:
                return get(ReaderUriUtils.getWorksId(uri), ReaderUriUtils.getPackageId(uri));
            default:
                throw new PackageException(String.format("Unsupported uri %s", new Object[]{uri}));
        }
    }

    public static Package get(int bookId, int packageId) {
        Package pack = (Package) hInstances.get(packageId);
        if (pack == null) {
            return new Package(bookId, packageId);
        }
        return pack;
    }

    public static void clearFromCache(int bookId) {
        hInstances.delete(bookId);
    }

    private static void addToCache(int bookId, Package pack) {
        if (pack != null) {
            hInstances.append(bookId, pack);
        }
    }

    public int getDownloadProgress() {
        return this.mDownloadHelper.getDownloadProgress();
    }

    public long getCurrentSize() {
        return this.mDownloadHelper.getCurrentSize();
    }

    public long getRemainedSize() {
        return this.mDownloadHelper.getRemainedSize();
    }

    public long getTotalSize() {
        return this.mDownloadHelper.getTotalSize();
    }

    public void selfCheck() throws WorksException {
        if (!isPackageReady()) {
            if (fileUpdatedAfterLastCheck()) {
                throw new PackageException("Check failed. (No data update since last failed check.)");
            }
            doSelfCheck();
        }
    }

    public void forceSelfCheck() throws PackageException {
        doSelfCheck();
    }

    public boolean newVersionAvailable() {
        CharSequence etag = Pref.ofObj(this.mUri).getString(Key.PACKAGE_ETAG);
        PackMeta packMeta = getMetaData();
        if (packMeta == null || !StringUtils.equals(packMeta.etag, etag)) {
            return true;
        }
        return false;
    }

    public void open() throws IOException {
        if (this.mZipFile == null) {
            this.mFile = getFile();
            this.mZipFile = new ZipFile(this.mFile);
        }
    }

    public void close() throws IOException {
        if (this.mZipFile != null) {
            this.mZipFile.close();
        }
        this.mZipFile = null;
    }

    public void delete() throws IOException {
        close();
        if (!this.mFile.exists() || this.mFile.delete()) {
            Pref.ofPackage(this.mPackageId).clear();
            Pref.ofObj(this.mUri).clear();
            return;
        }
        throw new IOException("failed to delete file " + this.mFile.getPath());
    }

    public Status getStatus() {
        if (DownloadManager.isScheduled(this.mUri)) {
            if (DownloadManager.isDownloading(this.mUri)) {
                return Status.DOWNLOADING;
            }
            return Status.PENDING;
        } else if (!this.mFile.exists()) {
            return Status.EMPTY;
        } else {
            if (isPackageReady()) {
                return Status.READY;
            }
            return Status.FAILED;
        }
    }

    public InputStream getInputStream(String entryPath) throws IOException {
        DataException e;
        if (DebugSwitch.on(Key.APP_DEBUG_DECIPHER_PACKAGE_ENTRIES)) {
            decipherEntry(entryPath);
        }
        try {
            open();
            return new CipherInputStream(new BufferedInputStream(this.mZipFile.getInputStream(this.mZipFile.getEntry(entryPath))), getCipher());
        } catch (CipherException e2) {
            e = e2;
            throw new IOException(e);
        } catch (ManifestException e3) {
            e = e3;
            throw new IOException(e);
        }
    }

    public InputStream getIllusInputStream(int illusSeq, ImageSize size) throws IOException {
        String path = getValidIllusPath(illusSeq, size);
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return getInputStream(path);
    }

    public Drawable getIllusDrawable(int illusSeq) {
        try {
            return Drawable.createFromStream(getIllusInputStream(illusSeq, ImageSize.NORMAL), "");
        } catch (IOException e) {
            return null;
        }
    }

    public void obtainPackageSize() throws WorksException, IOException, InterruptedException {
        PackMeta packMeta = Manifest.load(this.mWorksId).getPackMeta(this.mPackageId);
        this.mDownloadHelper.obtainPackageSize(new URL(packMeta.url), packMeta.etag);
    }

    public void download() throws WorksException, IOException, InterruptedException {
        PackMeta packMeta = Manifest.load(this.mWorksId).getPackMeta(this.mPackageId);
        URL url = new URL(packMeta.url);
        close();
        this.mDownloadHelper.download(url, packMeta.etag);
        try {
            doSelfCheck();
        } catch (PackageException e) {
            Pref.ofObj(this.mUri).remove(Key.PACKAGE_ETAG);
            throw e;
        }
    }

    public PackMeta getMetaData() {
        try {
            return Manifest.get(this.mWorksId).getPackMeta(this.mPackageId);
        } catch (ManifestException e) {
            return null;
        }
    }

    private PackagePayload getPayload() {
        if (getMetaData() == null) {
            return null;
        }
        return getMetaData().payload;
    }

    public String getTitle() {
        if (getPayload() == null) {
            return "";
        }
        return getPayload().title;
    }

    public String getAbstractText() {
        if (getPayload() == null) {
            return "";
        }
        return getPayload().abstractText;
    }

    public int getPrice() {
        if (getPayload() == null) {
            return 0;
        }
        return getPayload().price;
    }

    public Date getPublishDate() {
        if (getPayload() == null) {
            return null;
        }
        return getPayload().publishTime;
    }

    public boolean isPurchaseNeeded() {
        return getMetaData() == null || StringUtils.isEmpty(getMetaData().url);
    }

    private String getIllusPath(int illusSeq, ImageSize size) throws IOException {
        String path = String.format("image/%s", new Object[]{Integer.valueOf(illusSeq)});
        if (size == ImageSize.LARGE) {
            path = path + "l";
        }
        open();
        return (this.mZipFile == null || this.mZipFile.getEntry(path) == null) ? null : path;
    }

    private String getValidIllusPath(int illusSeq, ImageSize size) throws IOException {
        String path = getIllusPath(illusSeq, size);
        if (!TextUtils.isEmpty(path)) {
            return path;
        }
        if (size == ImageSize.NORMAL) {
            size = ImageSize.LARGE;
        } else {
            size = ImageSize.NORMAL;
        }
        return getIllusPath(illusSeq, size);
    }

    private File getFile() {
        return FilePath.pack(this.mWorksId, this.mPackageId);
    }

    private void doSelfCheck() throws PackageException {
        int i = 0;
        int length;
        try {
            open();
            for (String entry : CHAPTER_FILE_ENTRIES) {
                if (this.mZipFile.getEntry(entry) == null) {
                    throw new PackageException(String.format("Package %d missing entry: %s", new Object[]{Integer.valueOf(this.mPackageId), r7[length]}));
                }
            }
            setPackageReady(true);
            updateLastCheckedTime();
            return;
        } catch (Throwable e) {
            setPackageReady(false);
            if (ExceptionUtils.isCausedBy(e, ZipException.class)) {
                Crashlytics.logException(e);
            } else if (ExceptionUtils.isCausedBy(e, FileNotFoundException.class)) {
                Logger.dc(TAG, "Package data %s not found.", getFile());
                File[] packs = FilePath.packRoot(this.mWorksId).listFiles();
                length = packs.length;
                while (i < length) {
                    File pack = packs[i];
                    Logger.dc(TAG, "Found pack in path: %s", pack);
                    i++;
                }
                Logger.dc(TAG, "Connection: %s", Utils.getNetworkInfo());
                Crashlytics.logException(new WrongPackageException(e));
            }
        } catch (Throwable th) {
        }
        throw new PackageException(String.format("Failed to open package %d", new Object[]{Integer.valueOf(this.mPackageId)}), e);
    }

    private long getLastModified() {
        return this.mFile.lastModified();
    }

    private void updateLastCheckedTime() {
        Pref.ofPackage(this.mPackageId).set(Key.PACKAGE_LAST_CHECKED_FILE_TIME, Long.valueOf(getLastModified()));
    }

    private boolean fileUpdatedAfterLastCheck() {
        return Pref.ofPackage(this.mPackageId).getLong(Key.PACKAGE_LAST_CHECKED_FILE_TIME, -1) < getLastModified();
    }

    private void setPackageReady(boolean ready) {
        Pref.ofPackage(this.mPackageId).set(Key.PACKAGE_IS_LAST_CHECK_SUCCEED, Boolean.valueOf(ready));
    }

    private boolean isPackageReady() {
        return !fileUpdatedAfterLastCheck() && Pref.ofPackage(this.mPackageId).getBoolean(Key.PACKAGE_IS_LAST_CHECK_SUCCEED, false);
    }

    private Cipher getCipher() throws CipherException, ManifestException {
        try {
            return doGetCipher();
        } catch (CipherException e) {
            Logger.dc(TAG, "Failed to get cipher for %s (%s). Trying to reload manifest", this, e);
            Manifest.loadFromNetwork(this.mWorksId);
            this.mCipherFactory = null;
            return doGetCipher();
        }
    }

    private Cipher doGetCipher() throws CipherException {
        if (this.mCipherFactory == null) {
            PackMeta metaData = getMetaData();
            if (metaData == null) {
                throw new CipherException(String.format("Failed loading metadata for package %s", new Object[]{this}));
            }
            this.mCipherFactory = new CipherFactory(metaData.key, metaData.iv);
        }
        try {
            return this.mCipherFactory.getCipher();
        } catch (CipherException e) {
            if (ExceptionUtils.isCausedBy(e, InvalidKeyException.class)) {
                Logger.dc(TAG, "Manifest.PackMeta: %s", JsonUtils.toJson(getMetaData()));
            }
        } catch (Throwable th) {
        }
        throw e;
    }

    private void decipherEntry(String entryPath) {
        Exception e;
        Throwable th;
        if (AppInfo.isDebug()) {
            Logger.v(TAG, "deciphering %s to %s ...", entryPath, String.format("%s_%s_decipher", new Object[]{getFile(), entryPath}));
            InputStream in = null;
            OutputStream out = null;
            try {
                OutputStream out2;
                open();
                InputStream in2 = new BufferedInputStream(new CipherInputStream(this.mZipFile.getInputStream(this.mZipFile.getEntry(entryPath)), getCipher()));
                try {
                    out2 = new BufferedOutputStream(new FileOutputStream(new File(decipherPath)));
                } catch (Exception e2) {
                    e = e2;
                    in = in2;
                    try {
                        Logger.e(TAG, e);
                        IOUtils.closeSilently(in);
                        IOUtils.closeSilently(out);
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                        IOUtils.closeSilently(in);
                        IOUtils.closeSilently(out);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    in = in2;
                    IOUtils.closeSilently(in);
                    IOUtils.closeSilently(out);
                    throw th;
                }
                try {
                    IOUtils.copyStream(in2, out2);
                    IOUtils.closeSilently(in2);
                    IOUtils.closeSilently(out2);
                    out = out2;
                    in = in2;
                    return;
                } catch (Exception e3) {
                    e = e3;
                    out = out2;
                    in = in2;
                    Logger.e(TAG, e);
                    IOUtils.closeSilently(in);
                    IOUtils.closeSilently(out);
                    return;
                } catch (Throwable th4) {
                    th = th4;
                    out = out2;
                    in = in2;
                    IOUtils.closeSilently(in);
                    IOUtils.closeSilently(out);
                    throw th;
                }
            } catch (Exception e4) {
                e = e4;
                Logger.e(TAG, e);
                IOUtils.closeSilently(in);
                IOUtils.closeSilently(out);
                return;
            }
        }
        Logger.e(TAG, new SecurityException("WARNING!! decipherFile should never be called in release mode."));
    }
}
