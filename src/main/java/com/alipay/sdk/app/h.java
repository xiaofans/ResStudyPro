package com.alipay.sdk.app;

import android.webkit.SslErrorHandler;
import com.alipay.sdk.widget.d;

final class h implements Runnable {
    final /* synthetic */ SslErrorHandler a;
    final /* synthetic */ a b;

    h(a aVar, SslErrorHandler sslErrorHandler) {
        this.b = aVar;
        this.a = sslErrorHandler;
    }

    public final void run() {
        d.a(this.b.a, "安全警告", "由于您的设备缺少根证书，将无法校验该访问站点的安全性，可能存在风险，请选择是否继续？", "继续", new i(this), "退出", new j(this));
    }
}
