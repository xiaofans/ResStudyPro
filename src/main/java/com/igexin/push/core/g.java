package com.igexin.push.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import com.igexin.a.b.a;
import com.igexin.push.config.SDKUrlConfig;
import com.igexin.push.config.k;
import com.igexin.push.config.l;
import com.igexin.push.config.p;
import com.igexin.push.core.bean.f;
import com.igexin.sdk.a.c;
import com.igexin.sdk.a.d;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class g {
    public static String A;
    public static String B;
    public static String C = "";
    public static long D = -1;
    public static long E = -1;
    public static long F = 0;
    public static long G = 0;
    public static long H = 0;
    public static long I = 0;
    public static long J = 0;
    public static long K = 0;
    public static long L = 0;
    public static String M = null;
    public static boolean N = p.a.equals("debug");
    public static long O = 0;
    public static long P;
    public static long Q;
    public static long R = 0;
    public static String S;
    public static long T = 0;
    public static int U = 0;
    public static long V;
    public static String W;
    public static String X;
    public static String Y;
    public static String Z;
    public static String a = "";
    public static String aa;
    public static String ab;
    public static String ac;
    public static String ad;
    public static byte[] ae;
    public static boolean af = false;
    public static boolean ag = false;
    public static boolean ah = false;
    public static Map ai;
    public static Map aj;
    public static HashMap ak;
    public static HashMap al;
    public static HashMap am;
    public static int an = 0;
    public static Map ao;
    public static int ap = 0;
    public static int aq = 0;
    public static int ar = 0;
    public static f as;
    public static boolean at = false;
    public static String au;
    public static com.igexin.push.e.b.f av;
    public static long aw;
    private static final String ax = k.a;
    private static HashMap ay = new HashMap();
    private static Map az;
    public static String b = "";
    public static String c = "";
    public static String d = "";
    public static String e = "";
    public static boolean f = false;
    public static Context g;
    public static AtomicBoolean h = new AtomicBoolean(false);
    public static boolean i = true;
    public static boolean j = false;
    public static boolean k = false;
    public static boolean l = true;
    public static boolean m = false;
    public static boolean n = false;
    public static boolean o = true;
    public static int p = 0;
    public static int q = 0;
    public static long r = 0;
    public static String s;
    public static String t;
    public static String u;
    public static String v;
    public static String w;
    public static String x;
    public static String y;
    public static String z;

    public static int a(String str, boolean z) {
        int intValue;
        synchronized (az) {
            if (az.get(str) == null) {
                az.put(str, Integer.valueOf(0));
            }
            intValue = ((Integer) az.get(str)).intValue();
            if (z) {
                intValue--;
                az.put(str, Integer.valueOf(intValue));
                if (intValue == 0) {
                    az.remove(str);
                }
            }
        }
        return intValue;
    }

    public static String a() {
        return SDKUrlConfig.getConfigServiceUrl();
    }

    public static void a(long j) {
        r = j;
        s = a.a(String.valueOf(r));
    }

    public static boolean a(Context context) {
        String str = null;
        g = context;
        e = context.getPackageName();
        String str2 = "";
        str2 = "";
        str2 = "";
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(e, 128);
            if (applicationInfo == null || applicationInfo.metaData == null) {
                return false;
            }
            String string = applicationInfo.metaData.getString("PUSH_APPID");
            str2 = applicationInfo.metaData.getString("PUSH_APPSECRET");
            if (applicationInfo.metaData.get("PUSH_APPKEY") != null) {
                str = applicationInfo.metaData.get("PUSH_APPKEY").toString();
            }
            if (string != null) {
                string = string.trim();
            }
            if (str2 != null) {
                str2 = str2.trim();
            }
            if (str != null) {
                str = str.trim();
            }
            if (string == null || str2 == null || str == null) {
                return false;
            }
            a = string;
            b = str;
            c = str2;
            d = SDKUrlConfig.getLocation();
            ae = a.a(string + str2 + str + context.getPackageName()).getBytes();
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(e, 4096);
                if (packageInfo == null || packageInfo.requestedPermissions == null) {
                    return false;
                }
                String[] strArr = packageInfo.requestedPermissions;
                for (String string2 : strArr) {
                    if (string2.equals("android.permission.CALL_PHONE")) {
                        f = true;
                    }
                }
                context.getFilesDir();
                File file = new File("/sdcard/libs");
                if (!file.exists()) {
                    file.mkdir();
                }
                file = new File(Environment.getExternalStorageDirectory().getPath() + "/system/tmp/local");
                if (!file.exists()) {
                    file.mkdirs();
                }
                ac = file.getAbsolutePath();
                ad = context.getFilesDir().getPath();
                Y = "/sdcard/libs/" + e + ".db";
                Z = "/sdcard/libs/com.igexin.sdk.deviceId.db";
                aa = "/sdcard/libs/app.db";
                ab = "/sdcard/libs/imsi.db";
                X = "/sdcard/libs/" + e + ".properties";
                try {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                    u = telephonyManager.getDeviceId();
                    v = telephonyManager.getSubscriberId();
                    if (y == null || y.equals("")) {
                        if (l.g) {
                            HashMap b = b();
                            if (!(b == null || v == null || v.equals(""))) {
                                y = (String) b.get(v);
                            }
                            if (y == null || !y.equals("")) {
                                y = null;
                            }
                        } else {
                            y = null;
                        }
                    }
                    w = Build.MODEL;
                    if (f.a().j().getActiveNetworkInfo() == null || !f.a().j().getActiveNetworkInfo().isAvailable()) {
                        i = false;
                        B = a.a(u != null ? "cantgetimei" : u);
                        ai = new HashMap();
                        aj = new HashMap();
                        ak = new HashMap();
                        al = new HashMap();
                        am = new HashMap();
                        ao = new HashMap();
                        V = System.currentTimeMillis();
                        W = "com.igexin.sdk.action.snlresponse." + e;
                        j = new d(context).c();
                        k = new c(context).c();
                        az = new HashMap();
                        return true;
                    }
                    i = true;
                    if (u != null) {
                    }
                    B = a.a(u != null ? "cantgetimei" : u);
                    ai = new HashMap();
                    aj = new HashMap();
                    ak = new HashMap();
                    al = new HashMap();
                    am = new HashMap();
                    ao = new HashMap();
                    V = System.currentTimeMillis();
                    W = "com.igexin.sdk.action.snlresponse." + e;
                    j = new d(context).c();
                    k = new c(context).c();
                    az = new HashMap();
                    return true;
                } catch (Exception e) {
                } catch (Throwable th) {
                }
            } catch (Exception e2) {
                return false;
            }
        } catch (Exception e3) {
            return false;
        }
    }

    public static boolean a(String str, Integer num, boolean z) {
        boolean z2;
        synchronized (az) {
            int intValue = num.intValue();
            if (z && az.get(str) != null) {
                intValue = ((Integer) az.get(str)).intValue() + num.intValue();
                if (intValue == 0) {
                    az.remove(str);
                    z2 = false;
                }
            }
            az.put(str, Integer.valueOf(intValue));
            z2 = true;
        }
        return z2;
    }

    public static HashMap b() {
        if (!new File(ab).exists()) {
            return null;
        }
        try {
            ObjectInput objectInputStream = new ObjectInputStream(new FileInputStream(ab));
            HashMap hashMap = (HashMap) objectInputStream.readObject();
            try {
                objectInputStream.close();
                return hashMap;
            } catch (Exception e) {
                return hashMap;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public static HashMap c() {
        return ay;
    }
}
