package com.alipay.android.phone.mrpc.core;

import java.lang.reflect.Method;

public abstract class d implements ac {
    protected Method a;
    protected byte[] b;
    protected String c;
    protected int d;
    protected String e;
    protected boolean f;

    public d(Method method, int i, String str, byte[] bArr, String str2, boolean z) {
        this.a = method;
        this.d = i;
        this.c = str;
        this.b = bArr;
        this.e = str2;
        this.f = z;
    }
}
