package com.igexin.push.c.c;

import com.igexin.a.a.b.g;
import com.igexin.download.Downloads;

public class n extends e {
    public int a;
    public int b;
    public long c;
    public String d;
    public Object e;
    public Object f;
    public String g;
    public String h;

    public n() {
        this.h = "UTF-8";
        this.i = 26;
    }

    public void a(byte[] bArr) {
        this.a = g.b(bArr, 0);
        this.b = bArr[2] & Downloads.STATUS_RUNNING;
        this.h = a(bArr[2]);
        this.c = g.d(bArr, 3);
        int i = bArr[11] & 255;
        try {
            this.d = new String(bArr, 12, i, this.h);
        } catch (Exception e) {
            this.d = "";
        }
        int i2 = i + 12;
        i = 0;
        while (true) {
            i |= bArr[i2] & 127;
            if ((bArr[i2] & 128) == 0) {
                break;
            }
            i <<= 7;
            i2++;
        }
        i2++;
        if (i > 0) {
            if (this.b == Downloads.STATUS_RUNNING) {
                this.e = new byte[i];
                System.arraycopy(bArr, i2, this.e, 0, i);
            } else {
                try {
                    this.e = new String(bArr, i2, i, this.h);
                } catch (Exception e2) {
                }
            }
        }
        i2 = i + i2;
        i = 0;
        while (true) {
            i |= bArr[i2] & 127;
            if ((bArr[i2] & 128) == 0) {
                break;
            }
            i <<= 7;
            i2++;
        }
        i2++;
        if (i > 0) {
            this.f = new byte[i];
            System.arraycopy(bArr, i2, this.f, 0, i);
        }
        i += i2;
        if (bArr.length > i) {
            int i3 = i + 1;
            i = bArr[i] & 255;
            try {
                this.g = new String(bArr, i3, i, this.h);
            } catch (Exception e3) {
            }
            i += i3;
        }
    }

    public final boolean a() {
        return this.b == 128;
    }

    public byte[] d() {
        int i = 0;
        try {
            byte[] bytes = this.d.getBytes(this.h);
            byte[] bytes2 = this.g.getBytes(this.h);
            r5 = !"".equals(this.e) ? this.b == Downloads.STATUS_RUNNING ? (byte[]) this.e : ((String) this.e).getBytes(this.h) : null;
            byte[] bArr = this.f != null ? (byte[]) this.f : null;
            int length = r5 == null ? 0 : r5.length;
            if (bArr != null) {
                i = bArr.length;
            }
            byte[] a = g.a(length);
            byte[] a2 = g.a(i);
            byte[] bArr2 = new byte[((((((bytes.length + 13) + a.length) + length) + a2.length) + i) + bytes2.length)];
            try {
                int b = g.b(this.a, bArr2, 0);
                b += g.c(this.b | a(this.h), bArr2, b);
                b += g.a(this.c, bArr2, b);
                b += g.c(bytes.length, bArr2, b);
                b += g.a(bytes, 0, bArr2, b, bytes.length);
                b += g.a(a, 0, bArr2, b, a.length);
                if (length > 0) {
                    b += g.a(r5, 0, bArr2, b, length);
                }
                b += g.a(a2, 0, bArr2, b, a2.length);
                if (i > 0) {
                    b += g.a(bArr, 0, bArr2, b, i);
                }
                b += g.c(bytes2.length, bArr2, b);
                b += g.a(bytes2, 0, bArr2, b, bytes2.length);
                return bArr2;
            } catch (Exception e) {
                return bArr2;
            }
        } catch (Exception e2) {
            return null;
        }
    }
}
