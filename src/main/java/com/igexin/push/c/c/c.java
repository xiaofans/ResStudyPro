package com.igexin.push.c.c;

import com.igexin.a.a.b.g;
import com.igexin.download.Downloads;
import com.igexin.push.e.b.b;

public class c extends e {
    public int a;
    public int b;
    public Object c;
    public String d;
    public String e;
    public int f;
    public b g;

    public c() {
        this.e = "UTF-8";
        this.f = 1;
        this.i = 27;
        this.j = (byte) 52;
    }

    public final void a() {
        this.b = 64;
    }

    public void a(int i) {
        this.f = i;
    }

    public void a(b bVar) {
        this.g = bVar;
    }

    public void a(byte[] bArr) {
        this.a = g.b(bArr, 0);
        this.b = bArr[2] & Downloads.STATUS_RUNNING;
        this.e = a(bArr[2]);
        int i = 3;
        int i2 = 0;
        while (true) {
            i2 |= bArr[i] & 127;
            if ((bArr[i] & 128) == 0) {
                break;
            }
            i2 <<= 7;
            i++;
        }
        i++;
        if (i2 > 0) {
            if (this.b == Downloads.STATUS_RUNNING) {
                this.c = new byte[i2];
                System.arraycopy(bArr, i, this.c, 0, i2);
            } else {
                try {
                    this.c = new String(bArr, i, i2, this.e);
                } catch (Exception e) {
                }
            }
        }
        i2 += i;
        int i3 = bArr[i2] & 255;
        i2++;
        if (bArr.length > i2) {
            try {
                this.d = new String(bArr, i2, i3, this.e);
            } catch (Exception e2) {
            }
            i2 += i3;
        }
    }

    public int c() {
        return this.f;
    }

    public byte[] d() {
        byte[] bArr;
        int i = 0;
        try {
            byte[] bytes = this.d.getBytes(this.e);
            r3 = !"".equals(this.c) ? this.b == Downloads.STATUS_RUNNING ? (byte[]) this.c : ((String) this.c).getBytes(this.e) : null;
            if (r3 != null) {
                i = r3.length;
            }
            byte[] a = g.a(i);
            bArr = new byte[(((a.length + 4) + i) + bytes.length)];
            try {
                int b = g.b(this.a, bArr, 0);
                b += g.c(this.b | a(this.e), bArr, b);
                b += g.a(a, 0, bArr, b, a.length);
                if (i > 0) {
                    b += g.a(r3, 0, bArr, b, i);
                }
                b += g.c(bytes.length, bArr, b);
                b += g.a(bytes, 0, bArr, b, bytes.length);
            } catch (Exception e) {
            }
        } catch (Exception e2) {
            bArr = null;
        }
        if (bArr != null && bArr.length >= 512) {
            this.j = (byte) (this.j | 128);
        }
        return bArr;
    }

    public b e() {
        return this.g;
    }
}
