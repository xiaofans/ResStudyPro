package com.alipay.mobilesecuritysdk.face;

import android.content.Context;
import com.alipay.apmobilesecuritysdk.e.f;
import com.alipay.apmobilesecuritysdk.face.c;
import com.alipay.sdk.cons.b;
import java.util.HashMap;
import java.util.Map;

public final class a {
    public static synchronized String a(Context context, Map<String, String> map) {
        String a;
        synchronized (a.class) {
            Map hashMap = new HashMap();
            hashMap.put(b.g, com.alipay.security.mobile.module.commonutils.a.a(map, b.g, ""));
            hashMap.put(b.c, com.alipay.security.mobile.module.commonutils.a.a(map, b.c, ""));
            hashMap.put("userId", com.alipay.security.mobile.module.commonutils.a.a(map, "userId", ""));
            com.alipay.apmobilesecuritysdk.face.a a2 = com.alipay.apmobilesecuritysdk.face.a.a(context);
            String a3 = com.alipay.security.mobile.module.commonutils.a.a(hashMap, b.g, "");
            String a4 = com.alipay.security.mobile.module.commonutils.a.a(hashMap, b.c, "");
            a = com.alipay.security.mobile.module.commonutils.a.a(hashMap, "userId", "");
            com.alipay.security.mobile.module.a.a.a.a("https://mobilegw.alipay.com/mgw.htm");
            a2.b.addLast(new b(a2, a3, a4, a));
            if (a2.a == null) {
                a2.a = new Thread(new com.alipay.apmobilesecuritysdk.face.b(a2));
                a2.a.setUncaughtExceptionHandler(new c(a2));
                a2.a.start();
            }
            a = f.a();
            if (com.alipay.security.mobile.module.commonutils.a.a(a)) {
                com.alipay.apmobilesecuritysdk.e.b a5 = com.alipay.apmobilesecuritysdk.e.a.a(context);
                if (a5 == null || com.alipay.security.mobile.module.commonutils.a.a(a5.a)) {
                    a = com.alipay.apmobilesecuritysdk.a.a.a.a(context);
                    if (com.alipay.security.mobile.module.commonutils.a.a(a)) {
                        a = com.alipay.apmobilesecuritysdk.f.b.a(context);
                    }
                } else {
                    a = a5.a;
                }
            }
        }
        return a;
    }
}
