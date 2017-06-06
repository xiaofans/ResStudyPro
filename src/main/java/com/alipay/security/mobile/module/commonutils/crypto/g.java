package com.alipay.security.mobile.module.commonutils.crypto;

public final class g {
    public static final byte[] a = e.a("7B726A5DDD72CBF8D1700FB6EB278AFD7559C40A3761E5A71614D0AC9461ED8EE9F6AAEB443CD648");
    public static final byte[] b = e.a("C9582A82777392CAA65AD7F5228150E3F966C09D6A00288B5C6E0CFB441E111B713B4E0822A8C830");
    public static final int c = 8;
    public static final int d = 20;

    private g() {
    }

    private static byte[] a(byte[] bArr) {
        Object obj = new byte[20];
        b.a(obj);
        Object obj2 = new byte[20];
        b.a(obj2);
        Object a = c.a(bArr, a);
        System.arraycopy(a, 0, obj, 0, a.length);
        a = c.a(bArr, b);
        System.arraycopy(a, 0, obj2, 0, a.length);
        r2 = new byte[8];
        int i = obj[19] & 15;
        r2[3] = (byte) (obj[i] & 127);
        r2[2] = (byte) (obj[i + 1] & 255);
        r2[1] = (byte) (obj[i + 2] & 255);
        r2[0] = (byte) (obj[i + 3] & 255);
        int i2 = obj2[19] & 15;
        r2[4] = (byte) (obj2[i2] & 127);
        r2[5] = (byte) (obj2[i2 + 1] & 255);
        r2[6] = (byte) (obj2[i2 + 2] & 255);
        r2[7] = (byte) (obj2[i2 + 3] & 255);
        return r2;
    }

    private static byte[] a(byte[] bArr, int i) {
        int i2 = 0;
        Object obj = new byte[20];
        b.a(obj);
        Object obj2 = new byte[20];
        b.a(obj2);
        Object a = c.a(bArr, a);
        System.arraycopy(a, 0, obj, 0, a.length);
        a = c.a(bArr, b);
        System.arraycopy(a, 0, obj2, 0, a.length);
        r0 = new byte[8];
        int i3 = obj[19] & 15;
        r0[3] = (byte) (obj[i3] & 127);
        r0[2] = (byte) (obj[i3 + 1] & 255);
        r0[1] = (byte) (obj[i3 + 2] & 255);
        r0[0] = (byte) (obj[i3 + 3] & 255);
        int i4 = obj2[19] & 15;
        r0[4] = (byte) (obj2[i4] & 127);
        r0[5] = (byte) (obj2[i4 + 1] & 255);
        r0[6] = (byte) (obj2[i4 + 2] & 255);
        r0[7] = (byte) (obj2[i4 + 3] & 255);
        if (i <= 0) {
            return null;
        }
        if (i >= 8) {
            return r0;
        }
        byte[] bArr2 = new byte[i];
        while (i2 < i) {
            bArr2[i2] = r0[i2];
            i2++;
        }
        return bArr2;
    }
}
