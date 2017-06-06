package com.sina.weibo.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import com.sina.weibo.sdk.utils.LogUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class WeiboAppManager {
    private static final String SDK_INT_FILE_NAME = "weibo_for_sdk.json";
    private static final String TAG = WeiboAppManager.class.getName();
    private static final String WEIBO_IDENTITY_ACTION = "com.sina.weibo.action.sdkidentity";
    private static final Uri WEIBO_NAME_URI = Uri.parse("content://com.sina.weibo.sdkProvider/query/package");
    private static WeiboAppManager sInstance;
    private Context mContext;

    public static class WeiboInfo {
        private String mPackageName;
        private int mSupportApi;

        private void setPackageName(String packageName) {
            this.mPackageName = packageName;
        }

        public String getPackageName() {
            return this.mPackageName;
        }

        private void setSupportApi(int supportApi) {
            this.mSupportApi = supportApi;
        }

        public int getSupportApi() {
            return this.mSupportApi;
        }

        public boolean isLegal() {
            if (TextUtils.isEmpty(this.mPackageName) || this.mSupportApi <= 0) {
                return false;
            }
            return true;
        }

        public String toString() {
            return "WeiboInfo: PackageName = " + this.mPackageName + ", supportApi = " + this.mSupportApi;
        }
    }

    private com.sina.weibo.sdk.WeiboAppManager.WeiboInfo queryWeiboInfoByProvider(android.content.Context r15) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x006b in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r14 = this;
        r0 = r15.getContentResolver();
        r6 = 0;
        r1 = WEIBO_NAME_URI;	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r2 = 0;	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r3 = 0;	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r4 = 0;	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r5 = 0;	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r6 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        if (r6 != 0) goto L_0x0019;
    L_0x0011:
        if (r6 == 0) goto L_0x0017;
    L_0x0013:
        r6.close();
        r6 = 0;
    L_0x0017:
        r13 = 0;
    L_0x0018:
        return r13;
    L_0x0019:
        r1 = "support_api";	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r11 = r6.getColumnIndex(r1);	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r1 = "package";	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r8 = r6.getColumnIndex(r1);	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r1 = r6.moveToFirst();	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        if (r1 == 0) goto L_0x0075;	 Catch:{ Exception -> 0x005b, all -> 0x006d }
    L_0x002b:
        r12 = -1;	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r10 = r6.getString(r11);	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r12 = java.lang.Integer.parseInt(r10);	 Catch:{ NumberFormatException -> 0x0056 }
    L_0x0034:
        r9 = r6.getString(r8);	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r1 = android.text.TextUtils.isEmpty(r9);	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        if (r1 != 0) goto L_0x0075;	 Catch:{ Exception -> 0x005b, all -> 0x006d }
    L_0x003e:
        r1 = com.sina.weibo.sdk.ApiUtils.validateWeiboSign(r15, r9);	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        if (r1 == 0) goto L_0x0075;	 Catch:{ Exception -> 0x005b, all -> 0x006d }
    L_0x0044:
        r13 = new com.sina.weibo.sdk.WeiboAppManager$WeiboInfo;	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r13.<init>();	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r13.setPackageName(r9);	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r13.setSupportApi(r12);	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        if (r6 == 0) goto L_0x0018;
    L_0x0051:
        r6.close();
        r6 = 0;
        goto L_0x0018;
    L_0x0056:
        r7 = move-exception;
        r7.printStackTrace();	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        goto L_0x0034;
    L_0x005b:
        r7 = move-exception;
        r1 = TAG;	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        r2 = r7.getMessage();	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        com.sina.weibo.sdk.utils.LogUtil.e(r1, r2);	 Catch:{ Exception -> 0x005b, all -> 0x006d }
        if (r6 == 0) goto L_0x006b;
    L_0x0067:
        r6.close();
        r6 = 0;
    L_0x006b:
        r13 = 0;
        goto L_0x0018;
    L_0x006d:
        r1 = move-exception;
        if (r6 == 0) goto L_0x0074;
    L_0x0070:
        r6.close();
        r6 = 0;
    L_0x0074:
        throw r1;
    L_0x0075:
        if (r6 == 0) goto L_0x006b;
    L_0x0077:
        r6.close();
        r6 = 0;
        goto L_0x006b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sina.weibo.sdk.WeiboAppManager.queryWeiboInfoByProvider(android.content.Context):com.sina.weibo.sdk.WeiboAppManager$WeiboInfo");
    }

    private WeiboAppManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static synchronized WeiboAppManager getInstance(Context context) {
        WeiboAppManager weiboAppManager;
        synchronized (WeiboAppManager.class) {
            if (sInstance == null) {
                sInstance = new WeiboAppManager(context);
            }
            weiboAppManager = sInstance;
        }
        return weiboAppManager;
    }

    public synchronized WeiboInfo getWeiboInfo() {
        return queryWeiboInfoInternal(this.mContext);
    }

    private WeiboInfo queryWeiboInfoInternal(Context context) {
        boolean hasWinfo1;
        boolean hasWinfo2 = true;
        WeiboInfo winfo1 = queryWeiboInfoByProvider(context);
        WeiboInfo winfo2 = queryWeiboInfoByAsset(context);
        if (winfo1 != null) {
            hasWinfo1 = true;
        } else {
            hasWinfo1 = false;
        }
        if (winfo2 == null) {
            hasWinfo2 = false;
        }
        if (hasWinfo1 && hasWinfo2) {
            if (winfo1.getSupportApi() >= winfo2.getSupportApi()) {
                return winfo1;
            }
            return winfo2;
        } else if (hasWinfo1) {
            return winfo1;
        } else {
            if (hasWinfo2) {
                return winfo2;
            }
            return null;
        }
    }

    private WeiboInfo queryWeiboInfoByAsset(Context context) {
        Intent intent = new Intent(WEIBO_IDENTITY_ACTION);
        intent.addCategory("android.intent.category.DEFAULT");
        List<ResolveInfo> list = context.getPackageManager().queryIntentServices(intent, 0);
        if (list == null || list.isEmpty()) {
            return null;
        }
        WeiboInfo weiboInfo = null;
        for (ResolveInfo ri : list) {
            if (!(ri.serviceInfo == null || ri.serviceInfo.applicationInfo == null || TextUtils.isEmpty(ri.serviceInfo.applicationInfo.packageName))) {
                WeiboInfo tmpWeiboInfo = parseWeiboInfoByAsset(ri.serviceInfo.applicationInfo.packageName);
                if (tmpWeiboInfo != null) {
                    if (weiboInfo == null) {
                        weiboInfo = tmpWeiboInfo;
                    } else if (weiboInfo.getSupportApi() < tmpWeiboInfo.getSupportApi()) {
                        weiboInfo = tmpWeiboInfo;
                    }
                }
            }
        }
        return weiboInfo;
    }

    public WeiboInfo parseWeiboInfoByAsset(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        InputStream is = null;
        try {
            byte[] buf = new byte[4096];
            is = this.mContext.createPackageContext(packageName, 2).getAssets().open(SDK_INT_FILE_NAME);
            StringBuilder sbContent = new StringBuilder();
            while (true) {
                int readNum = is.read(buf, 0, 4096);
                if (readNum == -1) {
                    break;
                }
                sbContent.append(new String(buf, 0, readNum));
            }
            if (TextUtils.isEmpty(sbContent.toString()) || !ApiUtils.validateWeiboSign(this.mContext, packageName)) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        LogUtil.e(TAG, e.getMessage());
                    }
                }
                return null;
            }
            int supportApi = new JSONObject(sbContent.toString()).optInt("support_api", -1);
            WeiboInfo winfo = new WeiboInfo();
            winfo.setPackageName(packageName);
            winfo.setSupportApi(supportApi);
            if (is == null) {
                return winfo;
            }
            try {
                is.close();
                return winfo;
            } catch (IOException e2) {
                LogUtil.e(TAG, e2.getMessage());
                return winfo;
            }
        } catch (NameNotFoundException e3) {
            LogUtil.e(TAG, e3.getMessage());
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e22) {
                    LogUtil.e(TAG, e22.getMessage());
                }
            }
        } catch (IOException e222) {
            LogUtil.e(TAG, e222.getMessage());
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e2222) {
                    LogUtil.e(TAG, e2222.getMessage());
                }
            }
        } catch (JSONException e4) {
            LogUtil.e(TAG, e4.getMessage());
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e22222) {
                    LogUtil.e(TAG, e22222.getMessage());
                }
            }
        } catch (Exception e5) {
            LogUtil.e(TAG, e5.getMessage());
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e222222) {
                    LogUtil.e(TAG, e222222.getMessage());
                }
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e2222222) {
                    LogUtil.e(TAG, e2222222.getMessage());
                }
            }
        }
        return null;
    }
}
