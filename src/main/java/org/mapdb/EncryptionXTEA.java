package org.mapdb;

import java.util.Arrays;

public final class EncryptionXTEA {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final int ALIGN = 16;
    private static final int DELTA = -1640531527;
    private static final int[] K = new int[]{1116352408, 1899447441, -1245643825, -373957723, 961987163, 1508970993, -1841331548, -1424204075, -670586216, 310598401, 607225278, 1426881987, 1925078388, -2132889090, -1680079193, -1046744716, -459576895, -272742522, 264347078, 604807628, 770255983, 1249150122, 1555081692, 1996064986, -1740746414, -1473132947, -1341970488, -1084653625, -958395405, -710438585, 113926993, 338241895, 666307205, 773529912, 1294757372, 1396182291, 1695183700, 1986661051, -2117940946, -1838011259, -1564481375, -1474664885, -1035236496, -949202525, -778901479, -694614492, -200395387, 275423344, 430227734, 506948616, 659060556, 883997877, 958139571, 1322822218, 1537002063, 1747873779, 1955562222, 2024104815, -2067236844, -1933114872, -1866530822, -1538233109, -1090935817, -965641998};
    private final int k0;
    private final int k1;
    private final int k10;
    private final int k11;
    private final int k12;
    private final int k13;
    private final int k14;
    private final int k15;
    private final int k16;
    private final int k17;
    private final int k18;
    private final int k19;
    private final int k2;
    private final int k20;
    private final int k21;
    private final int k22;
    private final int k23;
    private final int k24;
    private final int k25;
    private final int k26;
    private final int k27;
    private final int k28;
    private final int k29;
    private final int k3;
    private final int k30;
    private final int k31;
    private final int k4;
    private final int k5;
    private final int k6;
    private final int k7;
    private final int k8;
    private final int k9;

    static {
        boolean z;
        if (EncryptionXTEA.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public EncryptionXTEA(byte[] password) {
        byte[] b = getHash(password);
        int[] key = new int[4];
        int i = 0;
        while (i < 16) {
            int i2 = i / 4;
            int i3 = i + 1;
            i = i3 + 1;
            i3 = i + 1;
            i = i3 + 1;
            key[i2] = (((b[i] << 24) + ((b[i3] & 255) << 16)) + ((b[i] & 255) << 8)) + (b[i3] & 255);
        }
        int[] r = new int[32];
        int sum = 0;
        i = 0;
        while (i < 32) {
            i3 = i + 1;
            r[i] = key[sum & 3] + sum;
            sum -= 1640531527;
            i = i3 + 1;
            r[i3] = key[(sum >>> 11) & 3] + sum;
        }
        this.k0 = r[0];
        this.k1 = r[1];
        this.k2 = r[2];
        this.k3 = r[3];
        this.k4 = r[4];
        this.k5 = r[5];
        this.k6 = r[6];
        this.k7 = r[7];
        this.k8 = r[8];
        this.k9 = r[9];
        this.k10 = r[10];
        this.k11 = r[11];
        this.k12 = r[12];
        this.k13 = r[13];
        this.k14 = r[14];
        this.k15 = r[15];
        this.k16 = r[16];
        this.k17 = r[17];
        this.k18 = r[18];
        this.k19 = r[19];
        this.k20 = r[20];
        this.k21 = r[21];
        this.k22 = r[22];
        this.k23 = r[23];
        this.k24 = r[24];
        this.k25 = r[25];
        this.k26 = r[26];
        this.k27 = r[27];
        this.k28 = r[28];
        this.k29 = r[29];
        this.k30 = r[30];
        this.k31 = r[31];
    }

    public void encrypt(byte[] bytes, int off, int len) {
        if ($assertionsDisabled || len % 16 == 0) {
            for (int i = off; i < off + len; i += 8) {
                encryptBlock(bytes, bytes, i);
            }
            return;
        }
        throw new AssertionError("unaligned len " + len);
    }

    public void decrypt(byte[] bytes, int off, int len) {
        if ($assertionsDisabled || len % 16 == 0) {
            for (int i = off; i < off + len; i += 8) {
                decryptBlock(bytes, bytes, i);
            }
            return;
        }
        throw new AssertionError("unaligned len " + len);
    }

    private void encryptBlock(byte[] in, byte[] out, int off) {
        int z = (((in[off + 4] << 24) | ((in[off + 5] & 255) << 16)) | ((in[off + 6] & 255) << 8)) | (in[off + 7] & 255);
        int y = ((((in[off] << 24) | ((in[off + 1] & 255) << 16)) | ((in[off + 2] & 255) << 8)) | (in[off + 3] & 255)) + ((((z << 4) ^ (z >>> 5)) + z) ^ this.k0);
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k1;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k2;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k3;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k4;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k5;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k6;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k7;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k8;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k9;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k10;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k11;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k12;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k13;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k14;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k15;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k16;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k17;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k18;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k19;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k20;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k21;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k22;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k23;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k24;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k25;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k26;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k27;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k28;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k29;
        y += (((z << 4) ^ (z >>> 5)) + z) ^ this.k30;
        z += (((y >>> 5) ^ (y << 4)) + y) ^ this.k31;
        out[off] = (byte) (y >> 24);
        out[off + 1] = (byte) (y >> 16);
        out[off + 2] = (byte) (y >> 8);
        out[off + 3] = (byte) y;
        out[off + 4] = (byte) (z >> 24);
        out[off + 5] = (byte) (z >> 16);
        out[off + 6] = (byte) (z >> 8);
        out[off + 7] = (byte) z;
    }

    private void decryptBlock(byte[] in, byte[] out, int off) {
        int y = (((in[off] << 24) | ((in[off + 1] & 255) << 16)) | ((in[off + 2] & 255) << 8)) | (in[off + 3] & 255);
        int z = ((((in[off + 4] << 24) | ((in[off + 5] & 255) << 16)) | ((in[off + 6] & 255) << 8)) | (in[off + 7] & 255)) - ((((y >>> 5) ^ (y << 4)) + y) ^ this.k31);
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k30;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k29;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k28;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k27;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k26;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k25;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k24;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k23;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k22;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k21;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k20;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k19;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k18;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k17;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k16;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k15;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k14;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k13;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k12;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k11;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k10;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k9;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k8;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k7;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k6;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k5;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k4;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k3;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k2;
        z -= (((y >>> 5) ^ (y << 4)) + y) ^ this.k1;
        y -= (((z << 4) ^ (z >>> 5)) + z) ^ this.k0;
        out[off] = (byte) (y >> 24);
        out[off + 1] = (byte) (y >> 16);
        out[off + 2] = (byte) (y >> 8);
        out[off + 3] = (byte) y;
        out[off + 4] = (byte) (z >> 24);
        out[off + 5] = (byte) (z >> 16);
        out[off + 6] = (byte) (z >> 8);
        out[off + 7] = (byte) z;
    }

    public static byte[] getHash(byte[] data) {
        int byteLen = data.length;
        int intLen = (((byteLen + 9) + 63) / 64) * 16;
        byte[] bytes = new byte[(intLen * 4)];
        System.arraycopy(data, 0, bytes, 0, byteLen);
        bytes[byteLen] = Byte.MIN_VALUE;
        int[] buff = new int[intLen];
        int i = 0;
        for (int j = 0; j < intLen; j++) {
            buff[j] = readInt(bytes, i);
            i += 4;
        }
        buff[intLen - 2] = byteLen >>> 29;
        buff[intLen - 1] = byteLen << 3;
        Object w = new int[64];
        int[] iArr = new int[8];
        iArr = new int[]{1779033703, -1150833019, 1013904242, -1521486534, 1359893119, -1694144372, 528734635, 1541459225};
        for (int block = 0; block < intLen; block += 16) {
            System.arraycopy(buff, block + 0, w, 0, 16);
            for (i = 16; i < 64; i++) {
                int x = w[i - 2];
                int theta1 = (rot(x, 17) ^ rot(x, 19)) ^ (x >>> 10);
                x = w[i - 15];
                w[i] = ((w[i - 7] + theta1) + ((rot(x, 7) ^ rot(x, 18)) ^ (x >>> 3))) + w[i - 16];
            }
            int a = iArr[0];
            int b = iArr[1];
            int c = iArr[2];
            int d = iArr[3];
            int e = iArr[4];
            int f = iArr[5];
            int g = iArr[6];
            int h = iArr[7];
            for (i = 0; i < 64; i++) {
                int t1 = (((((rot(e, 6) ^ rot(e, 11)) ^ rot(e, 25)) + h) + ((e & f) ^ ((e ^ -1) & g))) + K[i]) + w[i];
                h = g;
                g = f;
                f = e;
                e = d + t1;
                d = c;
                c = b;
                b = a;
                a = t1 + (((rot(a, 2) ^ rot(a, 13)) ^ rot(a, 22)) + (((a & b) ^ (a & c)) ^ (b & c)));
            }
            iArr[0] = iArr[0] + a;
            iArr[1] = iArr[1] + b;
            iArr[2] = iArr[2] + c;
            iArr[3] = iArr[3] + d;
            iArr[4] = iArr[4] + e;
            iArr[5] = iArr[5] + f;
            iArr[6] = iArr[6] + g;
            iArr[7] = iArr[7] + h;
        }
        byte[] result = new byte[32];
        for (i = 0; i < 8; i++) {
            writeInt(result, i * 4, iArr[i]);
        }
        Arrays.fill(w, 0);
        Arrays.fill(buff, 0);
        Arrays.fill(iArr, 0);
        Arrays.fill(bytes, (byte) 0);
        return result;
    }

    private static int rot(int i, int count) {
        return (i << (32 - count)) | (i >>> count);
    }

    private static int readInt(byte[] b, int i) {
        return ((((b[i] & 255) << 24) + ((b[i + 1] & 255) << 16)) + ((b[i + 2] & 255) << 8)) + (b[i + 3] & 255);
    }

    private static void writeInt(byte[] b, int i, int value) {
        b[i] = (byte) (value >> 24);
        b[i + 1] = (byte) (value >> 16);
        b[i + 2] = (byte) (value >> 8);
        b[i + 3] = (byte) value;
    }
}
