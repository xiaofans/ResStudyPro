package com.alipay.security.mobile.module.commonutils.crypto;

public final class b {
    private b() {
    }

    public static boolean a(byte[] bArr) {
        for (int i = 0; i < 20; i++) {
            bArr[i + 0] = (byte) 0;
        }
        return true;
    }

    private static boolean a(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        if (bArr == null || bArr2 == null || i3 <= 0 || bArr.length < i + i3 || bArr2.length < i2 + i3) {
            return false;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            if (bArr[i + i4] != bArr2[i2 + i4]) {
                return false;
            }
        }
        return true;
    }

    private static boolean a(byte[] bArr, byte[] bArr2, int i) {
        int i2 = 0;
        if (bArr == null || bArr2 == null) {
            return false;
        }
        if (i >= bArr.length) {
            return true;
        }
        while (i2 < bArr2.length && (i2 + i) + 1 <= bArr.length) {
            bArr[i2 + i] = bArr2[i2];
            i2++;
        }
        return true;
    }
}
