package com.igexin.push.core.a.a;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import com.douban.amonsul.StatConstant;
import com.igexin.push.core.a;
import com.igexin.push.core.a.e;
import com.igexin.push.core.b;
import com.igexin.push.core.bean.BaseAction;
import com.igexin.push.core.bean.PushTaskBean;
import com.igexin.push.core.bean.m;
import com.igexin.push.core.f;
import com.igexin.push.core.g;
import com.igexin.sdk.PushConsts;
import com.sina.weibo.sdk.exception.WeiboAuthException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class l implements a {
    private static final String b = a.n;
    private static final String c = a.p;
    private static final String d = a.o;
    private PackageManager a = null;

    private String a(String str) {
        try {
            List<PackageInfo> installedPackages = g.g.getPackageManager().getInstalledPackages(4);
            if (installedPackages != null) {
                for (PackageInfo packageInfo : installedPackages) {
                    if (str.equals(packageInfo.packageName)) {
                        for (ServiceInfo serviceInfo : packageInfo.services) {
                            if (b.equals(serviceInfo.name) || d.equals(serviceInfo.name) || c.equals(serviceInfo.name)) {
                                return serviceInfo.name;
                            }
                        }
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            com.igexin.a.a.c.a.b(e.toString());
        }
        return null;
    }

    private List a(int i, String str) {
        Exception exception;
        List list = null;
        File file = new File(Environment.getExternalStorageDirectory() + "/libs");
        if (!file.exists()) {
            return null;
        }
        String[] list2 = file.list();
        if (list2 == null) {
            return null;
        }
        int i2 = 0;
        while (i2 < list2.length) {
            if (!(list2[i2].indexOf(".db") <= 0 || list2[i2].equals("app.db") || list2[i2].equals("imsi.db") || list2[i2].equals("com.igexin.sdk.deviceId.db"))) {
                String substring = list2[i2].substring(0, list2[i2].length() - 3);
                try {
                    File file2 = new File(file + "/" + list2[i2]);
                    byte[] bArr = new byte[1024];
                    try {
                        InputStream fileInputStream = new FileInputStream(file2);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        while (true) {
                            int read = fileInputStream.read(bArr);
                            if (read == -1) {
                                break;
                            }
                            byteArrayOutputStream.write(bArr, 0, read);
                        }
                        String[] split = new String(com.igexin.a.a.a.a.a(byteArrayOutputStream.toByteArray(), com.igexin.a.b.a.a(g.u == null ? "cantgetimei" : g.u))).split("\\|");
                        if (split[0].startsWith("v")) {
                            if (split[0].indexOf("null") >= 0) {
                                split[0] = split[0].substring(7);
                            } else {
                                split[0] = split[0].substring(20);
                            }
                        }
                        String a = com.igexin.a.b.a.a(split[0]);
                        if (i == 0) {
                            if (str.equals(a)) {
                                List arrayList = new ArrayList();
                                try {
                                    arrayList.add(substring);
                                    return arrayList;
                                } catch (Exception e) {
                                    Exception exception2 = e;
                                    list = arrayList;
                                    exception = exception2;
                                }
                            } else {
                                continue;
                            }
                        } else if (split.length > 1 && str.equals(split[1])) {
                            if (list == null) {
                                list = new ArrayList();
                            }
                            list.add(substring);
                        }
                    } catch (Exception e2) {
                        exception = e2;
                        com.igexin.a.a.c.a.b(exception.toString());
                        i2++;
                    }
                } catch (Exception exception3) {
                    com.igexin.a.a.c.a.b(exception3.toString());
                }
            }
            i2++;
        }
        return list;
    }

    private void a(String str, String str2, String str3, String str4, String str5) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(g.g.getPackageName());
        stringBuilder.append("#");
        stringBuilder.append(str4);
        stringBuilder.append("#");
        stringBuilder.append(str5);
        stringBuilder.append("#");
        stringBuilder.append(WeiboAuthException.DEFAULT_AUTH_ERROR_CODE);
        b("30025", stringBuilder.toString(), str, str2, str3);
        com.igexin.a.a.c.a.b("feedback actionId=30025 result=" + stringBuilder.toString());
    }

    private void a(String str, boolean z, PushTaskBean pushTaskBean, BaseAction baseAction) {
        try {
            String a = a(str);
            String messageId = pushTaskBean.getMessageId();
            String taskId = pushTaskBean.getTaskId();
            String a2 = ((m) baseAction).a();
            b(str);
            if (a != null) {
                Map hashMap = new HashMap();
                hashMap.put("messageId", messageId);
                hashMap.put("taskId", taskId);
                hashMap.put("id", a2);
                hashMap.put("pkgName", str);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(g.g.getPackageName());
                stringBuffer.append("#");
                stringBuffer.append(d(str));
                stringBuffer.append("#");
                stringBuffer.append(str);
                stringBuffer.append("/");
                if (a.equals(b)) {
                    stringBuffer.append(b);
                    stringBuffer.append("#");
                    if (a(str, b)) {
                        stringBuffer.append("0");
                    } else {
                        if (z) {
                            try {
                                Intent intent = new Intent();
                                intent.setClassName(str, a);
                                intent.putExtra("action", PushConsts.ACTION_SERVICE_INITIALIZE_SLAVE);
                                intent.putExtra("op_app", g.e);
                                intent.putExtra("isSlave", true);
                                g.g.startService(intent);
                            } catch (Exception e) {
                                com.igexin.a.a.c.a.b(e.toString());
                                a(stringBuffer, messageId, taskId, a2);
                                return;
                            }
                        } else if (!b(str, a)) {
                            a(stringBuffer, messageId, taskId, a2);
                            return;
                        }
                        hashMap.put("serviceName", b);
                        a(hashMap);
                        stringBuffer.append("1");
                    }
                } else if (a.equals(d)) {
                    stringBuffer.append(d);
                    stringBuffer.append("#");
                    if (a(str, d)) {
                        stringBuffer.append("0");
                    } else if (b(str, a)) {
                        hashMap.put("serviceName", d);
                        a(hashMap);
                        stringBuffer.append("1");
                    } else {
                        a(stringBuffer, messageId, taskId, a2);
                        return;
                    }
                } else if (a.equals(c)) {
                    stringBuffer.append(c);
                    stringBuffer.append("#");
                    if (a(str, c)) {
                        stringBuffer.append("0");
                    } else if (b(str, a)) {
                        hashMap.put("serviceName", c);
                        a(hashMap);
                        stringBuffer.append("1");
                    } else {
                        a(stringBuffer, messageId, taskId, a2);
                        return;
                    }
                }
                b("30025", stringBuffer.toString(), messageId, taskId, a2);
                com.igexin.a.a.c.a.b("feedback actionId=30025 result=" + stringBuffer.toString());
                return;
            }
            a(messageId, taskId, a2, ((m) baseAction).d() != null ? ((m) baseAction).d() : "", ((m) baseAction).c() != null ? ((m) baseAction).c() : "");
        } catch (Exception e2) {
            com.igexin.a.a.c.a.b(e2.toString());
        }
    }

    private void a(StringBuffer stringBuffer, String str, String str2, String str3) {
        stringBuffer.append(WeiboAuthException.DEFAULT_AUTH_ERROR_CODE);
        b("30025", stringBuffer.toString(), str, str2, str3);
        com.igexin.a.a.c.a.b("feedback actionId=30025 result=" + stringBuffer.toString());
    }

    private void a(Map map) {
        f.a().a(new m(this, 180000, map));
    }

    public static boolean a(String str, String str2) {
        List runningServices = ((ActivityManager) g.g.getSystemService("activity")).getRunningServices(2000);
        if (runningServices.size() <= 0) {
            return false;
        }
        int i = 0;
        while (i < runningServices.size()) {
            if (((RunningServiceInfo) runningServices.get(i)).service.getClassName().equals(str2) && ((RunningServiceInfo) runningServices.get(i)).service.getPackageName().equals(str)) {
                return true;
            }
            i++;
        }
        return false;
    }

    private void b(String str) {
        if (c(str)) {
            try {
                Cursor query = g.g.getContentResolver().query(Uri.parse("content://downloads." + str + "/download"), null, null, null, null);
                if (query != null) {
                    query.close();
                }
            } catch (Exception e) {
                com.igexin.a.a.c.a.b(e.toString());
            }
        }
    }

    private void b(String str, String str2, String str3, String str4, String str5) {
        PushTaskBean pushTaskBean = new PushTaskBean();
        pushTaskBean.setAppid(g.a);
        pushTaskBean.setMessageId(str3);
        pushTaskBean.setTaskId(str4);
        pushTaskBean.setId(str5);
        pushTaskBean.setAppKey(g.b);
        e.a().a(pushTaskBean, str, str2);
    }

    private boolean b(String str, String str2) {
        try {
            Intent intent = new Intent();
            intent.setClassName(str, str2);
            g.g.startService(intent);
            return true;
        } catch (Exception e) {
            com.igexin.a.a.c.a.b(e.toString());
            return false;
        }
    }

    private boolean c(String str) {
        try {
            this.a = g.g.getPackageManager();
            PackageInfo packageInfo = this.a.getPackageInfo(str, 8);
            if (packageInfo == null) {
                return false;
            }
            ProviderInfo[] providerInfoArr = packageInfo.providers;
            if (providerInfoArr == null || providerInfoArr.length == 0) {
                return false;
            }
            for (ProviderInfo providerInfo : providerInfoArr) {
                if (providerInfo.name.equals("com.igexin.download.DownloadProvider") && providerInfo.authority.equals("downloads." + str)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private String d(String str) {
        try {
            this.a = g.g.getPackageManager();
            Bundle bundle = this.a.getApplicationInfo(str, 128).metaData;
            if (bundle != null) {
                for (String str2 : bundle.keySet()) {
                    if (str2.equals("PUSH_APPID")) {
                        return bundle.get(str2).toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    public b a(PushTaskBean pushTaskBean, BaseAction baseAction) {
        return b.success;
    }

    public BaseAction a(JSONObject jSONObject) {
        try {
            if (com.igexin.push.config.l.r && jSONObject.has("do") && jSONObject.has("actionid") && jSONObject.has("type") && (jSONObject.has("pkgname") || jSONObject.has("appid") || jSONObject.has(StatConstant.JSON_KEY_CELLID))) {
                BaseAction mVar = new m();
                mVar.setType("wakeupsdk");
                mVar.setActionId(jSONObject.getString("actionid"));
                mVar.setDoActionId(jSONObject.getString("do"));
                if (jSONObject.has("pkgname")) {
                    mVar.b(jSONObject.getString("pkgname"));
                } else if (jSONObject.has(StatConstant.JSON_KEY_CELLID)) {
                    mVar.d(jSONObject.getString(StatConstant.JSON_KEY_CELLID));
                } else if (jSONObject.has("appid")) {
                    mVar.c(jSONObject.getString("appid"));
                }
                if (jSONObject.has("is_forcestart")) {
                    mVar.a(jSONObject.getBoolean("is_forcestart"));
                }
                if (!jSONObject.has("id")) {
                    return mVar;
                }
                mVar.a(jSONObject.getString("id"));
                return mVar;
            }
        } catch (JSONException e) {
            com.igexin.a.a.c.a.b(e.toString());
        }
        return null;
    }

    public boolean b(PushTaskBean pushTaskBean, BaseAction baseAction) {
        if (!(pushTaskBean == null || baseAction == null)) {
            boolean z;
            boolean z2;
            m mVar = (m) baseAction;
            String c = mVar.c();
            if (c != null || mVar.e() == null) {
                z = true;
            } else {
                List a = a(0, mVar.e());
                if (a == null || a.size() != 1) {
                    z = false;
                } else {
                    c = (String) a.get(0);
                    z = true;
                }
            }
            if (c != null) {
                a(c, mVar.b(), pushTaskBean, baseAction);
                z2 = z;
            } else if (mVar.d() != null) {
                List<String> a2 = a(1, mVar.d());
                if (a2 == null || a2.size() <= 0) {
                    z2 = false;
                } else {
                    for (String c2 : a2) {
                        a(c2, mVar.b(), pushTaskBean, baseAction);
                    }
                    z2 = z;
                }
            } else {
                z2 = z;
            }
            if (!z2) {
                a(pushTaskBean.getMessageId(), pushTaskBean.getTaskId(), ((m) baseAction).a(), ((m) baseAction).d() != null ? ((m) baseAction).d() : "", ((m) baseAction).c() != null ? ((m) baseAction).c() : "");
            }
            if (!baseAction.getDoActionId().equals("")) {
                e.a().a(pushTaskBean.getTaskId(), pushTaskBean.getMessageId(), baseAction.getDoActionId());
            }
        }
        return true;
    }
}
