package com.igexin.a.a.a;

public class a {
    public static void a(int[] iArr, int i, int i2) {
        int i3 = iArr[i];
        iArr[i] = iArr[i2];
        iArr[i2] = i3;
    }

    public static boolean a(byte[] bArr) {
        if (r3 <= 0 || r3 > 256) {
            return false;
        }
        int i = 0;
        for (byte b : bArr) {
            if ((b & 255) == 14) {
                i++;
                if (i > 3) {
                    return false;
                }
            }
        }
        return true;
    }

    public static byte[] a(byte[] bArr, String str) {
        return a(bArr, str.getBytes());
    }

    public static byte[] a(byte[] bArr, byte[] bArr2) {
        int i = 0;
        if (!a(bArr2)) {
            throw new IllegalArgumentException("key is fail!");
        } else if (bArr.length < 1) {
            throw new IllegalArgumentException("data is fail!");
        } else {
            int i2;
            int[] iArr = new int[256];
            for (i2 = 0; i2 < iArr.length; i2++) {
                iArr[i2] = i2;
            }
            int i3 = 0;
            for (i2 = 0; i2 < iArr.length; i2++) {
                i3 = ((i3 + iArr[i2]) + (bArr2[i2 % bArr2.length] & 255)) % 256;
                a(iArr, i2, i3);
            }
            byte[] bArr3 = new byte[bArr.length];
            i2 = 0;
            i3 = 0;
            while (i < bArr3.length) {
                i2 = (i2 + 1) % 256;
                i3 = (i3 + iArr[i2]) % 256;
                a(iArr, i2, i3);
                bArr3[i] = (byte) (iArr[(iArr[i2] + iArr[i3]) % 256] ^ bArr[i]);
                i++;
            }
            return bArr3;
        }
    }

    public static byte[] b(byte[] bArr, String str) {
        return a(bArr, str.getBytes());
    }
}
