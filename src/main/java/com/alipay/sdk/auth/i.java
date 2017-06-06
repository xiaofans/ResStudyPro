package com.alipay.sdk.auth;

import android.app.Activity;
import com.alipay.sdk.data.e;
import com.alipay.sdk.net.d;

final class i implements Runnable {
    final /* synthetic */ d a;
    final /* synthetic */ Activity b;
    final /* synthetic */ e c;
    final /* synthetic */ APAuthInfo d;

    public final void run() {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r6 = this;
        r0 = 0;
        r1 = 0;
        r2 = r6.a;	 Catch:{ NetErrorException -> 0x0109 }
        r3 = r6.b;	 Catch:{ NetErrorException -> 0x0109 }
        r4 = r6.c;	 Catch:{ NetErrorException -> 0x0109 }
        r5 = 0;	 Catch:{ NetErrorException -> 0x0109 }
        r1 = r2.a(r3, r4, r5);	 Catch:{ NetErrorException -> 0x0109 }
    L_0x000d:
        r2 = com.alipay.sdk.auth.h.c;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        if (r2 == 0) goto L_0x001d;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
    L_0x0013:
        r2 = com.alipay.sdk.auth.h.c;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r2.c();	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        com.alipay.sdk.auth.h.c = null;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
    L_0x001d:
        if (r1 != 0) goto L_0x0052;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
    L_0x001f:
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0.<init>();	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = r6.d;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = r1.getRedirectUri();	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = "?resultCode=202";	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0 = r0.toString();	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        com.alipay.sdk.auth.h.d = r0;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0 = r6.b;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = com.alipay.sdk.auth.h.d;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        com.alipay.sdk.auth.h.a(r0, r1);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0 = com.alipay.sdk.auth.h.c;
        if (r0 == 0) goto L_0x0051;
    L_0x004a:
        r0 = com.alipay.sdk.auth.h.c;
        r0.c();
    L_0x0051:
        return;
    L_0x0052:
        r1 = r1.c;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r2 = "form";	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = r1.optJSONObject(r2);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r2 = "onload";	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = com.alipay.sdk.protocol.b.a(r1, r2);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = com.alipay.sdk.protocol.a.a(r1);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r2 = r1.length;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
    L_0x0065:
        if (r0 >= r2) goto L_0x0079;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
    L_0x0067:
        r3 = r1[r0];	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r4 = com.alipay.sdk.protocol.a.WapPay;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        if (r3 != r4) goto L_0x00b6;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
    L_0x006d:
        r0 = r3.h;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0 = com.alipay.sdk.util.a.a(r0);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = 0;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0 = r0[r1];	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        com.alipay.sdk.auth.h.d = r0;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
    L_0x0079:
        r0 = com.alipay.sdk.auth.h.d;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0 = android.text.TextUtils.isEmpty(r0);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        if (r0 == 0) goto L_0x00b9;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
    L_0x0083:
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0.<init>();	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = r6.d;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = r1.getRedirectUri();	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = "?resultCode=202";	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0 = r0.toString();	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        com.alipay.sdk.auth.h.d = r0;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0 = r6.b;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = com.alipay.sdk.auth.h.d;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        com.alipay.sdk.auth.h.a(r0, r1);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0 = com.alipay.sdk.auth.h.c;
        if (r0 == 0) goto L_0x0051;
    L_0x00ae:
        r0 = com.alipay.sdk.auth.h.c;
        r0.c();
        goto L_0x0051;
    L_0x00b6:
        r0 = r0 + 1;
        goto L_0x0065;
    L_0x00b9:
        r0 = new android.content.Intent;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = r6.b;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r2 = com.alipay.sdk.auth.AuthActivity.class;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0.<init>(r1, r2);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = "params";	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r2 = com.alipay.sdk.auth.h.d;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0.putExtra(r1, r2);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = "redirectUri";	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r2 = r6.d;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r2 = r2.getRedirectUri();	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0.putExtra(r1, r2);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1 = r6.b;	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r1.startActivity(r0);	 Catch:{ Exception -> 0x00ea, all -> 0x00fa }
        r0 = com.alipay.sdk.auth.h.c;
        if (r0 == 0) goto L_0x0051;
    L_0x00e1:
        r0 = com.alipay.sdk.auth.h.c;
        r0.c();
        goto L_0x0051;
    L_0x00ea:
        r0 = move-exception;
        r0 = com.alipay.sdk.auth.h.c;
        if (r0 == 0) goto L_0x0051;
    L_0x00f1:
        r0 = com.alipay.sdk.auth.h.c;
        r0.c();
        goto L_0x0051;
    L_0x00fa:
        r0 = move-exception;
        r1 = com.alipay.sdk.auth.h.c;
        if (r1 == 0) goto L_0x0108;
    L_0x0101:
        r1 = com.alipay.sdk.auth.h.c;
        r1.c();
    L_0x0108:
        throw r0;
    L_0x0109:
        r2 = move-exception;
        goto L_0x000d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.auth.i.run():void");
    }

    i(d dVar, Activity activity, e eVar, APAuthInfo aPAuthInfo) {
        this.a = dVar;
        this.b = activity;
        this.c = eVar;
        this.d = aPAuthInfo;
    }
}
