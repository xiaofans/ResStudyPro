package com.alipay.sdk.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

public final class b {
    private static final String b = "00:00:00:00:00:00";
    private static b e = null;
    public String a;
    private String c;
    private String d;

    private b(android.content.Context r4) {
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
        r3 = this;
        r3.<init>();
        r0 = "phone";	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r0 = r4.getSystemService(r0);	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r0 = (android.telephony.TelephonyManager) r0;	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r1 = r0.getDeviceId();	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r3.b(r1);	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r0 = r0.getSubscriberId();	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        if (r0 == 0) goto L_0x0032;	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
    L_0x0018:
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r1.<init>();	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r0 = r1.append(r0);	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r1 = "000000000000000";	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r1 = 0;	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r2 = 15;	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r0 = r0.substring(r1, r2);	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
    L_0x0032:
        r3.c = r0;	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r0 = "wifi";	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r0 = r4.getSystemService(r0);	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r0 = (android.net.wifi.WifiManager) r0;	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r0 = r0.getConnectionInfo();	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r0 = r0.getMacAddress();	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r3.a = r0;	 Catch:{ Exception -> 0x0053, all -> 0x0061 }
        r0 = r3.a;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 == 0) goto L_0x0052;
    L_0x004e:
        r0 = "00:00:00:00:00:00";
        r3.a = r0;
    L_0x0052:
        return;
    L_0x0053:
        r0 = move-exception;
        r0 = r3.a;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 == 0) goto L_0x0052;
    L_0x005c:
        r0 = "00:00:00:00:00:00";
        r3.a = r0;
        goto L_0x0052;
    L_0x0061:
        r0 = move-exception;
        r1 = r3.a;
        r1 = android.text.TextUtils.isEmpty(r1);
        if (r1 == 0) goto L_0x006e;
    L_0x006a:
        r1 = "00:00:00:00:00:00";
        r3.a = r1;
    L_0x006e:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.util.b.<init>(android.content.Context):void");
    }

    public static b a(Context context) {
        if (e == null) {
            e = new b(context);
        }
        return e;
    }

    public final String a() {
        if (TextUtils.isEmpty(this.c)) {
            this.c = "000000000000000";
        }
        return this.c;
    }

    public final String b() {
        if (TextUtils.isEmpty(this.d)) {
            this.d = "000000000000000";
        }
        return this.d;
    }

    private void a(String str) {
        if (str != null) {
            str = (str + "000000000000000").substring(0, 15);
        }
        this.c = str;
    }

    private void b(String str) {
        if (str != null) {
            byte[] bytes = str.getBytes();
            int i = 0;
            while (i < bytes.length) {
                if (bytes[i] < (byte) 48 || bytes[i] > (byte) 57) {
                    bytes[i] = (byte) 48;
                }
                i++;
            }
            str = (new String(bytes) + "000000000000000").substring(0, 15);
        }
        this.d = str;
    }

    private String c() {
        String str = b() + "|";
        Object a = a();
        if (TextUtils.isEmpty(a)) {
            return str + "000000000000000";
        }
        return str + a;
    }

    private String d() {
        return this.a;
    }

    public static g b(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.getType() == 0) {
                return g.a(activeNetworkInfo.getSubtype());
            }
            if (activeNetworkInfo == null || activeNetworkInfo.getType() != 1) {
                return g.NONE;
            }
            return g.WIFI;
        } catch (Exception e) {
            return g.NONE;
        }
    }

    public static String c(Context context) {
        b a = a(context);
        String str = a.b() + "|";
        Object a2 = a.a();
        return (TextUtils.isEmpty(a2) ? str + "000000000000000" : str + a2).substring(0, 8);
    }
}
