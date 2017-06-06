package com.sina.weibo.sdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.douban.amonsul.StatConstant;
import com.igexin.sdk.PushBuildConfig;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.NetUtils;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.tencent.connect.common.Constants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.locks.ReentrantLock;
import javax.crypto.Cipher;
import org.json.JSONException;
import org.json.JSONObject;

public class AidTask {
    private static final String AID_FILE_NAME = "weibo_sdk_aid";
    private static final int MAX_RETRY_NUM = 3;
    private static final String TAG = "AidTask";
    private static final int VERSION = 1;
    public static final int WHAT_LOAD_AID_ERR = 1002;
    public static final int WHAT_LOAD_AID_SUC = 1001;
    private static AidTask sInstance;
    private AidInfo mAidInfo;
    private String mAppKey;
    private Context mContext;
    private CallbackHandler mHandler;
    private volatile ReentrantLock mTaskLock = new ReentrantLock(true);

    public static final class AidInfo {
        private String mAid;
        private String mSubCookie;

        public String getAid() {
            return this.mAid;
        }

        public String getSubCookie() {
            return this.mSubCookie;
        }

        public static AidInfo parseJson(String response) throws WeiboException {
            AidInfo instance = new AidInfo();
            try {
                JSONObject resObj = new JSONObject(response);
                if (resObj.has(StatConstant.STAT_EVENT_ID_ERROR) || resObj.has("error_code")) {
                    LogUtil.d(AidTask.TAG, "loadAidFromNet has error !!!");
                    throw new WeiboException("loadAidFromNet has error !!!");
                }
                instance.mAid = resObj.optString("aid", "");
                instance.mSubCookie = resObj.optString("sub", "");
                return instance;
            } catch (JSONException e) {
                LogUtil.d(AidTask.TAG, "loadAidFromNet JSONException Msg : " + e.getMessage());
                throw new WeiboException("loadAidFromNet has error !!!");
            }
        }

        AidInfo cloneAidInfo() {
            AidInfo aidInfo = new AidInfo();
            aidInfo.mAid = this.mAid;
            aidInfo.mSubCookie = this.mSubCookie;
            return aidInfo;
        }
    }

    public interface AidResultCallBack {
        void onAidGenFailed(Exception exception);

        void onAidGenSuccessed(AidInfo aidInfo);
    }

    private static class CallbackHandler extends Handler {
        private WeakReference<AidResultCallBack> callBackReference;

        public CallbackHandler(Looper looper) {
            super(looper);
        }

        public void setCallback(AidResultCallBack mCallBack) {
            if (this.callBackReference == null) {
                this.callBackReference = new WeakReference(mCallBack);
            } else if (((AidResultCallBack) this.callBackReference.get()) != mCallBack) {
                this.callBackReference = new WeakReference(mCallBack);
            }
        }

        public void handleMessage(Message msg) {
            AidResultCallBack callBack = (AidResultCallBack) this.callBackReference.get();
            switch (msg.what) {
                case 1001:
                    if (callBack != null) {
                        callBack.onAidGenSuccessed(((AidInfo) msg.obj).cloneAidInfo());
                        return;
                    }
                    return;
                case AidTask.WHAT_LOAD_AID_ERR /*1002*/:
                    if (callBack != null) {
                        callBack.onAidGenFailed((WeiboException) msg.obj);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private AidTask(Context context) {
        this.mContext = context.getApplicationContext();
        this.mHandler = new CallbackHandler(this.mContext.getMainLooper());
        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 1; i++) {
                    try {
                        AidTask.this.getAidInfoFile(i).delete();
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    public static synchronized AidTask getInstance(Context context) {
        AidTask aidTask;
        synchronized (AidTask.class) {
            if (sInstance == null) {
                sInstance = new AidTask(context);
            }
            aidTask = sInstance;
        }
        return aidTask;
    }

    public void aidTaskInit(String appKey) {
        if (!TextUtils.isEmpty(appKey)) {
            LogUtil.e(TAG, "aidTaskInit ");
            initAidInfo(appKey);
        }
    }

    private void initAidInfo(String appkey) {
        if (!TextUtils.isEmpty(appkey)) {
            this.mAppKey = appkey;
            new Thread(new Runnable() {
                public void run() {
                    if (AidTask.this.mTaskLock.tryLock()) {
                        AidInfo aidInfo = AidTask.this.loadAidInfoFromCache();
                        if (aidInfo == null) {
                            int retry = 1;
                            do {
                                retry++;
                                try {
                                    String response = AidTask.this.loadAidFromNet();
                                    aidInfo = AidInfo.parseJson(response);
                                    AidTask.this.cacheAidInfo(response);
                                    AidTask.this.mAidInfo = aidInfo;
                                    break;
                                } catch (WeiboException e) {
                                    LogUtil.e(AidTask.TAG, "AidTaskInit WeiboException Msg : " + e.getMessage());
                                    if (retry >= 3) {
                                    }
                                }
                            } while (retry >= 3);
                        } else {
                            AidTask.this.mAidInfo = aidInfo;
                        }
                        AidTask.this.mTaskLock.unlock();
                        return;
                    }
                    LogUtil.e(AidTask.TAG, "tryLock : false, return");
                }
            }).start();
        }
    }

    public AidInfo getAidSync(String appkey) throws WeiboException {
        if (TextUtils.isEmpty(appkey)) {
            return null;
        }
        LogUtil.e(TAG, "getAidSync ");
        if (this.mAidInfo == null) {
            aidTaskInit(appkey);
        }
        return this.mAidInfo;
    }

    public void getAidAsync(String appKey, AidResultCallBack callback) {
        if (!TextUtils.isEmpty(appKey)) {
            if (this.mAidInfo == null || callback == null) {
                generateAid(appKey, callback);
            } else {
                callback.onAidGenSuccessed(this.mAidInfo.cloneAidInfo());
            }
        }
    }

    private void generateAid(String appkey, final AidResultCallBack callback) {
        if (!TextUtils.isEmpty(appkey)) {
            this.mAppKey = appkey;
            new Thread(new Runnable() {
                public void run() {
                    AidTask.this.mTaskLock.lock();
                    AidInfo aidInfo = AidTask.this.loadAidInfoFromCache();
                    Exception throwable = null;
                    if (aidInfo == null) {
                        try {
                            String response = AidTask.this.loadAidFromNet();
                            aidInfo = AidInfo.parseJson(response);
                            AidTask.this.cacheAidInfo(response);
                            AidTask.this.mAidInfo = aidInfo;
                        } catch (Exception e) {
                            throwable = e;
                            LogUtil.e(AidTask.TAG, "AidTaskInit WeiboException Msg : " + e.getMessage());
                        }
                    }
                    AidTask.this.mTaskLock.unlock();
                    Message msg = Message.obtain();
                    if (aidInfo != null) {
                        msg.what = 1001;
                        msg.obj = aidInfo;
                    } else {
                        msg.what = AidTask.WHAT_LOAD_AID_ERR;
                        msg.obj = throwable;
                    }
                    AidTask.this.mHandler.setCallback(callback);
                    AidTask.this.mHandler.sendMessage(msg);
                }
            }).start();
        }
    }

    private synchronized AidInfo loadAidInfoFromCache() {
        AidInfo parseJson;
        Throwable th;
        FileInputStream fis = null;
        try {
            FileInputStream fis2 = new FileInputStream(getAidInfoFile(1));
            try {
                byte[] buffer = new byte[fis2.available()];
                fis2.read(buffer);
                parseJson = AidInfo.parseJson(new String(buffer));
                if (fis2 != null) {
                    try {
                        fis2.close();
                    } catch (IOException e) {
                    } catch (Throwable th2) {
                        th = th2;
                        fis = fis2;
                        throw th;
                    }
                }
                fis = fis2;
            } catch (Exception e2) {
                fis = fis2;
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e3) {
                    }
                }
                parseJson = null;
                return parseJson;
            } catch (Throwable th3) {
                th = th3;
                fis = fis2;
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e4) {
                    }
                }
                try {
                    throw th;
                } catch (Throwable th4) {
                    th = th4;
                }
            }
        } catch (Exception e5) {
            if (fis != null) {
                fis.close();
            }
            parseJson = null;
            return parseJson;
        } catch (Throwable th5) {
            th = th5;
            if (fis != null) {
                fis.close();
            }
            throw th;
        }
    }

    private File getAidInfoFile(int version) {
        return new File(this.mContext.getFilesDir(), new StringBuilder(AID_FILE_NAME).append(version).toString());
    }

    private String loadAidFromNet() throws WeiboException {
        String url = "https://api.weibo.com/oauth2/getaid.json";
        String pkgName = this.mContext.getPackageName();
        String keyHash = Utility.getSign(this.mContext, pkgName);
        String mfp = getMfp(this.mContext);
        WeiboParameters params = new WeiboParameters(this.mAppKey);
        params.put("appkey", this.mAppKey);
        params.put("mfp", mfp);
        params.put("packagename", pkgName);
        params.put("key_hash", keyHash);
        try {
            String response = NetUtils.internalHttpRequest(this.mContext, "https://api.weibo.com/oauth2/getaid.json", "GET", params);
            LogUtil.d(TAG, "loadAidFromNet response : " + response);
            return response;
        } catch (WeiboException e) {
            LogUtil.d(TAG, "loadAidFromNet WeiboException Msg : " + e.getMessage());
            throw e;
        }
    }

    private synchronized void cacheAidInfo(String json) {
        Throwable th;
        if (!TextUtils.isEmpty(json)) {
            FileOutputStream fos = null;
            try {
                FileOutputStream fos2 = new FileOutputStream(getAidInfoFile(1));
                try {
                    fos2.write(json.getBytes());
                    if (fos2 != null) {
                        try {
                            fos2.close();
                            fos = fos2;
                        } catch (IOException e) {
                            fos = fos2;
                        }
                    }
                } catch (Exception e2) {
                    fos = fos2;
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e3) {
                        }
                    }
                    return;
                } catch (Throwable th2) {
                    th = th2;
                    fos = fos2;
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e4) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
                if (fos != null) {
                    fos.close();
                }
                return;
            } catch (Throwable th3) {
                th = th3;
                if (fos != null) {
                    fos.close();
                }
                throw th;
            }
        }
        return;
    }

    private static String getMfp(Context ctx) {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDHHM0Fi2Z6+QYKXqFUX2Cy6AaWq3cPi+GSn9oeAwQbPZR75JB7Netm0HtBVVbtPhzT7UO2p1JhFUKWqrqoYuAjkgMVPmA0sFrQohns5EE44Y86XQopD4ZO+dE5KjUZFE6vrPO3rWW3np2BqlgKpjnYZri6TJApmIpGcQg9/G/3zQIDAQAB";
        String mfpJsonUtf8 = "";
        try {
            mfpJsonUtf8 = new String(genMfpString(ctx).getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        LogUtil.d(TAG, "genMfpString() utf-8 string : " + mfpJsonUtf8);
        try {
            String rsaMfp = encryptRsa(mfpJsonUtf8, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDHHM0Fi2Z6+QYKXqFUX2Cy6AaWq3cPi+GSn9oeAwQbPZR75JB7Netm0HtBVVbtPhzT7UO2p1JhFUKWqrqoYuAjkgMVPmA0sFrQohns5EE44Y86XQopD4ZO+dE5KjUZFE6vrPO3rWW3np2BqlgKpjnYZri6TJApmIpGcQg9/G/3zQIDAQAB");
            LogUtil.d(TAG, "encryptRsa() string : " + rsaMfp);
            return rsaMfp;
        } catch (Exception e2) {
            LogUtil.e(TAG, e2.getMessage());
            return "";
        }
    }

    private static String genMfpString(Context ctx) {
        JSONObject mfpObj = new JSONObject();
        try {
            String os = getOS();
            if (!TextUtils.isEmpty(os)) {
                mfpObj.put("1", os);
            }
            String imei = getImei(ctx);
            if (!TextUtils.isEmpty(imei)) {
                mfpObj.put("2", imei);
            }
            String meid = getMeid(ctx);
            if (!TextUtils.isEmpty(meid)) {
                mfpObj.put("3", meid);
            }
            String imsi = getImsi(ctx);
            if (!TextUtils.isEmpty(imsi)) {
                mfpObj.put("4", imsi);
            }
            String mac = getMac(ctx);
            if (!TextUtils.isEmpty(mac)) {
                mfpObj.put("5", mac);
            }
            String iccid = getIccid(ctx);
            if (!TextUtils.isEmpty(iccid)) {
                mfpObj.put(Constants.VIA_SHARE_TYPE_INFO, iccid);
            }
            String serial = getSerialNo();
            if (!TextUtils.isEmpty(serial)) {
                mfpObj.put("7", serial);
            }
            String androidId = getAndroidId(ctx);
            if (!TextUtils.isEmpty(androidId)) {
                mfpObj.put(Constants.VIA_REPORT_TYPE_SHARE_TO_QQ, androidId);
            }
            String cpu = getCpu();
            if (!TextUtils.isEmpty(cpu)) {
                mfpObj.put(Constants.VIA_REPORT_TYPE_JOININ_GROUP, cpu);
            }
            String model = getModel();
            if (!TextUtils.isEmpty(model)) {
                mfpObj.put(Constants.VIA_REPORT_TYPE_MAKE_FRIEND, model);
            }
            String sdcard = getSdSize();
            if (!TextUtils.isEmpty(sdcard)) {
                mfpObj.put(Constants.VIA_REPORT_TYPE_WPA_STATE, sdcard);
            }
            String resolution = getResolution(ctx);
            if (!TextUtils.isEmpty(resolution)) {
                mfpObj.put(Constants.VIA_REPORT_TYPE_START_WAP, resolution);
            }
            String ssid = getSsid(ctx);
            if (!TextUtils.isEmpty(ssid)) {
                mfpObj.put(Constants.VIA_REPORT_TYPE_START_GROUP, ssid);
            }
            String deviceName = getDeviceName();
            if (!TextUtils.isEmpty(deviceName)) {
                mfpObj.put("18", deviceName);
            }
            String connectType = getConnectType(ctx);
            if (!TextUtils.isEmpty(connectType)) {
                mfpObj.put(Constants.VIA_ACT_TYPE_NINETEEN, connectType);
            }
            return mfpObj.toString();
        } catch (JSONException e) {
            return "";
        }
    }

    private static String encryptRsa(String src, String publicKeyStr) throws Exception {
        Throwable th;
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(1, getPublicKey(publicKeyStr));
        ByteArrayOutputStream bos = null;
        byte[] plainText = src.getBytes("UTF-8");
        try {
            byte[] enBytes;
            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
            int offset = 0;
            while (true) {
                int len = splite(plainText, offset, 117);
                if (len == -1) {
                    break;
                }
                try {
                    enBytes = cipher.doFinal(plainText, offset, len);
                    bos2.write(enBytes);
                    LogUtil.d(TAG, "encryptRsa offset = " + offset + "     len = " + len + "     enBytes len = " + enBytes.length);
                    offset += len;
                } catch (Throwable th2) {
                    th = th2;
                    bos = bos2;
                }
            }
            bos2.flush();
            enBytes = bos2.toByteArray();
            LogUtil.d(TAG, "encryptRsa total enBytes len = " + enBytes.length);
            byte[] base64byte = Base64.encodebyte(enBytes);
            LogUtil.d(TAG, "encryptRsa total base64byte len = " + base64byte.length);
            String VERSION = "01";
            String base64string = "01" + new String(base64byte, "UTF-8");
            LogUtil.d(TAG, "encryptRsa total base64string : " + base64string);
            if (bos2 != null) {
                try {
                    bos2.close();
                } catch (IOException e) {
                }
            }
            return base64string;
        } catch (Throwable th3) {
            th = th3;
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e2) {
                }
            }
            throw th;
        }
    }

    private static int splite(byte[] src, int offset, int limit) {
        if (offset >= src.length) {
            return -1;
        }
        return Math.min(src.length - offset, limit);
    }

    private static PublicKey getPublicKey(String key) throws Exception {
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(key.getBytes())));
    }

    private static String getOS() {
        try {
            return "Android " + VERSION.RELEASE;
        } catch (Exception e) {
            return "";
        }
    }

    private static String getImei(Context ctx) {
        try {
            return ((TelephonyManager) ctx.getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }

    private static String getMeid(Context ctx) {
        try {
            return ((TelephonyManager) ctx.getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }

    private static String getImsi(Context ctx) {
        try {
            return ((TelephonyManager) ctx.getSystemService("phone")).getSubscriberId();
        } catch (Exception e) {
            return "";
        }
    }

    private static String getMac(Context ctx) {
        try {
            WifiManager wifi = (WifiManager) ctx.getSystemService("wifi");
            if (wifi == null) {
                return "";
            }
            WifiInfo info = wifi.getConnectionInfo();
            return info != null ? info.getMacAddress() : "";
        } catch (Exception e) {
            return "";
        }
    }

    private static String getIccid(Context ctx) {
        try {
            return ((TelephonyManager) ctx.getSystemService("phone")).getSimSerialNumber();
        } catch (Exception e) {
            return "";
        }
    }

    private static String getSerialNo() {
        String serialnum = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            return (String) c.getMethod("get", new Class[]{String.class, String.class}).invoke(c, new Object[]{"ro.serialno", EnvironmentCompat.MEDIA_UNKNOWN});
        } catch (Exception e) {
            return serialnum;
        }
    }

    private static String getAndroidId(Context ctx) {
        try {
            return Secure.getString(ctx.getContentResolver(), "android_id");
        } catch (Exception e) {
            return "";
        }
    }

    private static String getCpu() {
        try {
            return Build.CPU_ABI;
        } catch (Exception e) {
            return "";
        }
    }

    private static String getModel() {
        try {
            return Build.MODEL;
        } catch (Exception e) {
            return "";
        }
    }

    private static String getSdSize() {
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return Long.toString(((long) stat.getBlockCount()) * ((long) stat.getBlockSize()));
        } catch (Exception e) {
            return "";
        }
    }

    private static String getResolution(Context ctx) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) ctx.getSystemService("window")).getDefaultDisplay().getMetrics(dm);
            return new StringBuilder(String.valueOf(String.valueOf(dm.widthPixels))).append("*").append(String.valueOf(dm.heightPixels)).toString();
        } catch (Exception e) {
            return "";
        }
    }

    private static String getSsid(Context ctx) {
        try {
            WifiInfo wifiInfo = ((WifiManager) ctx.getSystemService("wifi")).getConnectionInfo();
            if (wifiInfo != null) {
                return wifiInfo.getSSID();
            }
        } catch (Exception e) {
        }
        return "";
    }

    private static String getDeviceName() {
        try {
            return Build.BRAND;
        } catch (Exception e) {
            return "";
        }
    }

    private static String getConnectType(Context ctx) {
        String network = PushBuildConfig.sdk_conf_debug_level;
        try {
            NetworkInfo info = ((ConnectivityManager) ctx.getSystemService("connectivity")).getActiveNetworkInfo();
            if (info == null) {
                return network;
            }
            if (info.getType() == 0) {
                switch (info.getSubtype()) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                        return "2G";
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                        return "3G";
                    case 13:
                        return "4G";
                    default:
                        return PushBuildConfig.sdk_conf_debug_level;
                }
            } else if (info.getType() == 1) {
                return "wifi";
            } else {
                return network;
            }
        } catch (Exception e) {
            return network;
        }
    }
}
