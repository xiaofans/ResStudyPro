package com.alipay.sdk.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import com.alipay.android.app.IAlixPay;
import com.alipay.android.app.IRemoteServiceCallback;
import com.alipay.sdk.util.k.a;

public class h {
    public static final String b = "failed";
    public Activity a;
    private IAlixPay c;
    private final Object d = IAlixPay.class;
    private boolean e = false;
    private boolean f;
    private String g;
    private String h;
    private String i = null;
    private ServiceConnection j = new i(this);
    private IRemoteServiceCallback k = new j(this);

    private java.lang.String a(java.lang.String r7, android.content.Intent r8) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x000b in list [B:46:0x016f]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r6 = this;
        r2 = 1;
        r5 = 0;
        r4 = 0;
        r6.i = r4;
        r0 = r6.e;
        if (r0 == 0) goto L_0x000c;
    L_0x0009:
        r0 = "";
    L_0x000b:
        return r0;
    L_0x000c:
        r6.e = r2;
        r0 = r6.a;
        r0 = com.alipay.sdk.util.k.f(r0);
        r6.g = r0;
        r0 = r6.a;
        r0 = r0.getApplicationContext();
        r1 = r6.j;
        r0.bindService(r8, r1, r2);
        r1 = r6.d;
        monitor-enter(r1);
        r0 = r6.c;
        if (r0 != 0) goto L_0x002f;
    L_0x0028:
        r0 = r6.d;	 Catch:{ InterruptedException -> 0x019d }
        r2 = 3500; // 0xdac float:4.905E-42 double:1.729E-320;	 Catch:{ InterruptedException -> 0x019d }
        r0.wait(r2);	 Catch:{ InterruptedException -> 0x019d }
    L_0x002f:
        monitor-exit(r1);
        r0 = r6.c;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        if (r0 != 0) goto L_0x00f6;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
    L_0x0034:
        r0 = r6.a;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = com.alipay.sdk.util.k.f(r0);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r6.h = r0;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = "b|";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0.<init>(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r6.g;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.append(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = "|a|";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.append(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r6.h;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.append(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = "|";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.append(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = android.os.Build.MANUFACTURER;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r2 = ";";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r3 = "1688";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r1.replace(r2, r3);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r2 = "#";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r3 = "2688";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r1.replace(r2, r3);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.append(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = "|";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.append(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = android.os.Build.MODEL;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r2 = ";";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r3 = "1688";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r1.replace(r2, r3);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r2 = "#";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r3 = "1688";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r1.replace(r2, r3);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.append(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = "|";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.append(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r6.a;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = com.alipay.sdk.util.b.a(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r1.b();	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.append(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.toString();	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r6.i = r0;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r6.g;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        if (r0 != 0) goto L_0x00af;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
    L_0x00ab:
        r0 = r6.h;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        if (r0 != 0) goto L_0x00d0;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
    L_0x00af:
        r0 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0.<init>();	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r6.i;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.append(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = "|";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.append(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r6.a;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = com.alipay.sdk.util.k.g(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.append(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.toString();	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r6.i = r0;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
    L_0x00d0:
        r0 = r6.i;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        com.alipay.sdk.util.d.a(r0);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = "failed";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r6.a;	 Catch:{ Throwable -> 0x019a }
        r2 = r6.j;	 Catch:{ Throwable -> 0x019a }
        r1.unbindService(r2);	 Catch:{ Throwable -> 0x019a }
    L_0x00de:
        r6.k = r4;
        r6.j = r4;
        r6.c = r4;
        r6.e = r5;
        r1 = r6.f;
        if (r1 == 0) goto L_0x000b;
    L_0x00ea:
        r1 = r6.a;
        r1.setRequestedOrientation(r5);
        r6.f = r5;
        goto L_0x000b;
    L_0x00f3:
        r0 = move-exception;
        monitor-exit(r1);
        throw r0;
    L_0x00f6:
        r0 = r6.a;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.getRequestedOrientation();	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        if (r0 != 0) goto L_0x0107;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
    L_0x00fe:
        r0 = r6.a;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = 1;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0.setRequestedOrientation(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = 1;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r6.f = r0;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
    L_0x0107:
        r0 = r6.c;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r6.k;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0.registerCallback(r1);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r6.c;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = r0.Pay(r7);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r6.c;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r2 = r6.k;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1.unregisterCallback(r2);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r6.a;	 Catch:{ Throwable -> 0x0198 }
        r2 = r6.j;	 Catch:{ Throwable -> 0x0198 }
        r1.unbindService(r2);	 Catch:{ Throwable -> 0x0198 }
    L_0x0122:
        r6.k = r4;
        r6.j = r4;
        r6.c = r4;
        r6.e = r5;
        r1 = r6.f;
        if (r1 == 0) goto L_0x000b;
    L_0x012e:
        r1 = r6.a;
        r1.setRequestedOrientation(r5);
        r6.f = r5;
        goto L_0x000b;
    L_0x0137:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1.<init>();	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r2 = r6.i;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r1.append(r2);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r2 = "|e|";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r1.append(r2);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r2 = r0.getMessage();	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r1.append(r2);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r1.toString();	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r6.i = r1;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0.printStackTrace();	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r0 = "failed";	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1 = r6.a;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r2 = r6.j;	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
        r1.unbindService(r2);	 Catch:{ Throwable -> 0x0137, all -> 0x0178, Throwable -> 0x0196 }
    L_0x0163:
        r6.k = r4;
        r6.j = r4;
        r6.c = r4;
        r6.e = r5;
        r1 = r6.f;
        if (r1 == 0) goto L_0x000b;
    L_0x016f:
        r1 = r6.a;
        r1.setRequestedOrientation(r5);
        r6.f = r5;
        goto L_0x000b;
    L_0x0178:
        r0 = move-exception;
        r1 = r6.a;	 Catch:{ Throwable -> 0x0194 }
        r2 = r6.j;	 Catch:{ Throwable -> 0x0194 }
        r1.unbindService(r2);	 Catch:{ Throwable -> 0x0194 }
    L_0x0180:
        r6.k = r4;
        r6.j = r4;
        r6.c = r4;
        r6.e = r5;
        r1 = r6.f;
        if (r1 == 0) goto L_0x0193;
    L_0x018c:
        r1 = r6.a;
        r1.setRequestedOrientation(r5);
        r6.f = r5;
    L_0x0193:
        throw r0;
    L_0x0194:
        r1 = move-exception;
        goto L_0x0180;
    L_0x0196:
        r1 = move-exception;
        goto L_0x0163;
    L_0x0198:
        r1 = move-exception;
        goto L_0x0122;
    L_0x019a:
        r1 = move-exception;
        goto L_0x00de;
    L_0x019d:
        r0 = move-exception;
        goto L_0x002f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.util.h.a(java.lang.String, android.content.Intent):java.lang.String");
    }

    public h(Activity activity) {
        this.a = activity;
    }

    public final String a(String str) {
        Intent intent;
        try {
            a aVar;
            Context context = this.a;
            String str2 = k.b;
            for (PackageInfo packageInfo : context.getPackageManager().getInstalledPackages(64)) {
                if (packageInfo.packageName.equals(str2)) {
                    a aVar2 = new a();
                    aVar2.a = packageInfo.signatures[0].toByteArray();
                    aVar2.b = packageInfo.versionCode;
                    aVar = aVar2;
                    break;
                }
            }
            aVar = null;
            if (aVar != null) {
                CharSequence a = k.a(aVar.a);
                if (!(a == null || TextUtils.equals(a, com.alipay.sdk.cons.a.i))) {
                    d.a("fake#" + b.a(this.a).b());
                    return b;
                }
            }
            if (aVar.b > 78) {
                intent = new Intent();
                intent.setClassName(k.b, "com.alipay.android.app.TransProcessPayActivity");
                this.a.startActivity(intent);
                Thread.sleep(150);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        intent = new Intent();
        intent.setPackage(k.b);
        intent.setAction("com.eg.android.AlipayGphone.IAlixPay");
        return a(str, intent);
    }

    private void a() {
        this.a = null;
    }
}
