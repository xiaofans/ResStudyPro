package com.sina.weibo.sdk.cmd;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Pair;
import com.sina.weibo.sdk.WeiboAppManager;
import com.sina.weibo.sdk.WeiboAppManager.WeiboInfo;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.MD5;
import com.sina.weibo.sdk.utils.ResourceManager;
import com.sina.weibo.sdk.utils.SDKNotification.SDKNotificationBuilder;
import java.io.File;
import java.util.List;

class AppInstallCmdExecutor implements CmdExecutor<AppInstallCmd> {
    private static final int MESSAGE_DO_CMD = 1;
    private static final int MESSAGE_QUIT_LOOP = 2;
    private static final String TAG = AppInstallCmdExecutor.class.getName();
    private static final String WB_APK_FILE_DIR = (Environment.getExternalStorageDirectory() + "/Android/org_share_data/");
    private boolean isStarted = false;
    private Context mContext;
    private InstallHandler mHandler;
    private Looper mLooper;
    private HandlerThread thread;

    private class InstallHandler extends Handler {
        public InstallHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    AppInstallCmdExecutor.this.handleCmd((AppInstallCmd) msg.obj);
                    return;
                case 2:
                    AppInstallCmdExecutor.this.mLooper.quit();
                    AppInstallCmdExecutor.this.isStarted = false;
                    return;
                default:
                    return;
            }
        }
    }

    private static final class NOTIFICATION_CONSTANTS {
        private static final int NOTIFICATIONID = 1;
        private static final String WEIBO = "Weibo";
        private static final String WEIBO_ZH_CN = "微博";
        private static final String WEIBO_ZH_TW = "微博";

        private NOTIFICATION_CONSTANTS() {
        }
    }

    private void handleCmd(com.sina.weibo.sdk.cmd.AppInstallCmd r15) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:80)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r14 = this;
        r10 = r14.mContext;
        r5 = needActivate(r10, r15);
        if (r5 != 0) goto L_0x0009;
    L_0x0008:
        return;
    L_0x0009:
        r0 = WB_APK_FILE_DIR;
        r1 = r15.getDownloadUrl();
        r8 = r15.getAppVersion();
        r10 = r14.mContext;
        r6 = walkDir(r10, r0, r15);
        if (r6 == 0) goto L_0x003a;
    L_0x001b:
        r10 = r6.second;
        if (r10 == 0) goto L_0x003a;
    L_0x001f:
        r10 = r6.first;
        r10 = (java.lang.Integer) r10;
        r10 = r10.intValue();
        r10 = (long) r10;
        r10 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1));
        if (r10 < 0) goto L_0x003a;
    L_0x002c:
        r11 = r14.mContext;
        r10 = r6.second;
        r10 = (java.io.File) r10;
        r10 = r10.getAbsolutePath();
        showNotification(r11, r15, r10);
        goto L_0x0008;
    L_0x003a:
        r10 = r14.mContext;
        r10 = com.sina.weibo.sdk.utils.NetworkHelper.isWifiValid(r10);
        if (r10 == 0) goto L_0x0008;
    L_0x0042:
        r10 = android.text.TextUtils.isEmpty(r1);
        if (r10 != 0) goto L_0x0008;
    L_0x0048:
        r4 = "";
        r10 = r14.mContext;	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        r11 = "GET";	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        r12 = new com.sina.weibo.sdk.net.WeiboParameters;	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        r13 = "";	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        r12.<init>(r13);	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        r7 = com.sina.weibo.sdk.net.NetUtils.internalGetRedirectUri(r10, r1, r11, r12);	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        r3 = generateSaveFileName(r7);	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        r10 = android.text.TextUtils.isEmpty(r3);	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        if (r10 != 0) goto L_0x006b;	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
    L_0x0063:
        r10 = ".apk";	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        r10 = r3.endsWith(r10);	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        if (r10 != 0) goto L_0x007e;	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
    L_0x006b:
        r10 = TAG;	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        r11 = "redirectDownloadUrl is illeagle";	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        com.sina.weibo.sdk.utils.LogUtil.e(r10, r11);	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        r10 = android.text.TextUtils.isEmpty(r4);
        if (r10 != 0) goto L_0x0008;
    L_0x0078:
        r10 = r14.mContext;
        showNotification(r10, r15, r4);
        goto L_0x0008;
    L_0x007e:
        r10 = r14.mContext;	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        r4 = com.sina.weibo.sdk.net.NetUtils.internalDownloadFile(r10, r7, r0, r3);	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        r10 = android.text.TextUtils.isEmpty(r4);
        if (r10 != 0) goto L_0x0008;
    L_0x008a:
        r10 = r14.mContext;
        showNotification(r10, r15, r4);
        goto L_0x0008;
    L_0x0091:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ WeiboException -> 0x0091, all -> 0x00a2 }
        r10 = android.text.TextUtils.isEmpty(r4);
        if (r10 != 0) goto L_0x0008;
    L_0x009b:
        r10 = r14.mContext;
        showNotification(r10, r15, r4);
        goto L_0x0008;
    L_0x00a2:
        r10 = move-exception;
        r11 = android.text.TextUtils.isEmpty(r4);
        if (r11 != 0) goto L_0x00ae;
    L_0x00a9:
        r11 = r14.mContext;
        showNotification(r11, r15, r4);
    L_0x00ae:
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sina.weibo.sdk.cmd.AppInstallCmdExecutor.handleCmd(com.sina.weibo.sdk.cmd.AppInstallCmd):void");
    }

    public AppInstallCmdExecutor(Context ctx) {
        this.mContext = ctx.getApplicationContext();
    }

    private static boolean needActivate(Context ctx, AppInstallCmd cmd) {
        List<String> packages = cmd.getAppPackage();
        if (packages == null || packages.size() == 0 || TextUtils.isEmpty(cmd.getAppSign()) || TextUtils.isEmpty(cmd.getDownloadUrl()) || TextUtils.isEmpty(cmd.getNotificationText())) {
            return false;
        }
        if (packages.contains("com.sina.weibo")) {
            WeiboInfo mWeiboInfo = WeiboAppManager.getInstance(ctx).getWeiboInfo();
            if (mWeiboInfo == null || !mWeiboInfo.isLegal()) {
                return true;
            }
            return false;
        }
        for (String packageName : packages) {
            if (checkApkInstalled(ctx, packageName)) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkApkInstalled(Context ctx, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            if (ctx.getPackageManager().getPackageInfo(packageName, 1) != null) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public void start() {
        if (!this.isStarted) {
            this.isStarted = true;
            this.thread = new HandlerThread("");
            this.thread.start();
            this.mLooper = this.thread.getLooper();
            this.mHandler = new InstallHandler(this.mLooper);
        }
    }

    public void stop() {
        if (this.thread == null || this.mHandler == null) {
            LogUtil.w(TAG, "no thread running. please call start method first!");
            return;
        }
        Message msg = this.mHandler.obtainMessage();
        msg.what = 2;
        this.mHandler.sendMessage(msg);
    }

    public boolean doExecutor(AppInstallCmd cmd) {
        if (this.thread == null || this.mHandler == null) {
            throw new RuntimeException("no thread running. please call start method first!");
        }
        if (cmd != null) {
            Message msg = this.mHandler.obtainMessage();
            msg.what = 1;
            msg.obj = cmd;
            this.mHandler.sendMessage(msg);
        }
        return false;
    }

    private static Pair<Integer, File> walkDir(Context ctx, String dir, AppInstallCmd cmd) {
        if (TextUtils.isEmpty(dir)) {
            return null;
        }
        File dirFile = new File(dir);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return null;
        }
        File[] files = dirFile.listFiles();
        if (files == null) {
            return null;
        }
        int newestVersion = 0;
        File weiboApkFile = null;
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile() && fileName.endsWith(".apk")) {
                PackageInfo pkgInfo = ctx.getPackageManager().getPackageArchiveInfo(file.getAbsolutePath(), 64);
                if (isSpecifiedApk(pkgInfo, cmd.getAppPackage(), cmd.getAppSign()) && pkgInfo.versionCode > newestVersion) {
                    newestVersion = pkgInfo.versionCode;
                    weiboApkFile = file;
                }
            }
        }
        return new Pair(Integer.valueOf(newestVersion), weiboApkFile);
    }

    private static boolean isSpecifiedApk(PackageInfo pkgInfo, List<String> packageNames, String appSign) {
        boolean packageChecked = false;
        for (String packageName : packageNames) {
            if (checkPackageName(pkgInfo, packageName)) {
                packageChecked = true;
                break;
            }
        }
        boolean signChecked = checkApkSign(pkgInfo, appSign);
        if (packageChecked && signChecked) {
            return true;
        }
        return false;
    }

    private static boolean checkPackageName(PackageInfo pkgInfo, String packageName) {
        if (pkgInfo == null) {
            return false;
        }
        return packageName.equals(pkgInfo.packageName);
    }

    private static boolean checkApkSign(PackageInfo pkgInfo, String appSign) {
        if (pkgInfo == null) {
            return false;
        }
        if (pkgInfo.signatures != null) {
            String md5Sign = "";
            for (Signature toByteArray : pkgInfo.signatures) {
                byte[] str = toByteArray.toByteArray();
                if (str != null) {
                    md5Sign = MD5.hexdigest(str);
                }
            }
            if (md5Sign != null) {
                return md5Sign.equals(appSign);
            }
            return false;
        } else if (VERSION.SDK_INT < 11) {
            return true;
        } else {
            return false;
        }
    }

    private static String generateSaveFileName(String downloadUrl) {
        String fileName = "";
        int index = downloadUrl.lastIndexOf("/");
        if (index != -1) {
            return downloadUrl.substring(index + 1, downloadUrl.length());
        }
        return fileName;
    }

    private static void showNotification(Context ctx, AppInstallCmd cmd, String apkPath) {
        SDKNotificationBuilder.buildUpon().setNotificationContent(cmd.getNotificationText()).setNotificationPendingIntent(buildInstallApkIntent(ctx, apkPath)).setNotificationTitle(getNotificationTitle(ctx, cmd.getNotificationTitle())).setTickerText(cmd.getNotificationText()).build(ctx).show(1);
    }

    private static PendingIntent buildInstallApkIntent(Context ctx, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return PendingIntent.getActivity(ctx, 0, new Intent(), 16);
        }
        Intent intentInstall = new Intent("android.intent.action.VIEW");
        intentInstall.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        return PendingIntent.getActivity(ctx, 0, intentInstall, 16);
    }

    private static String getNotificationTitle(Context ctx, String title) {
        if (TextUtils.isEmpty(title)) {
            return ResourceManager.getString(ctx, "Weibo", "微博", "微博");
        }
        return title;
    }
}
