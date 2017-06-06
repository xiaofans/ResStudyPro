package com.alipay.apmobilesecuritysdk.d;

import android.content.Context;

public class a {
    private static String a = "";
    private static volatile boolean b = false;
    private static Context c = null;
    private static a d = null;

    private a() {
    }

    public static a a(Context context) {
        if (d == null) {
            synchronized (a.class) {
                if (d == null) {
                    d = new a();
                    c = context;
                }
            }
        }
        return d;
    }

    public static String a() {
        com.alipay.security.mobile.module.commonutils.a.a(a);
        return a;
    }

    public static String b() {
        return a;
    }
}
