package com.igexin.push.c.c;

import com.igexin.a.a.d.a;

public abstract class e extends a {
    public int i;
    public byte j;

    protected int a(String str) {
        return str.equals("UTF-8") ? 1 : str.equals("UTF-16") ? 2 : str.equals("UTF-16BE") ? 16 : str.equals("UTF-16LE") ? 17 : str.equals("GBK") ? 25 : str.equals("GB2312") ? 26 : str.equals("GB18030") ? 27 : str.equals("ISO-8859-1") ? 33 : 1;
    }

    protected String a(byte b) {
        switch (b & 63) {
            case 1:
                return "UTF-8";
            case 2:
                return "UTF-16";
            case 16:
                return "UTF-16BE";
            case 17:
                return "UTF-16LE";
            case 25:
                return "GBK";
            case 26:
                return "GB2312";
            case 27:
                return "GB18030";
            case 33:
                return "ISO-8859-1";
            default:
                return "UTF-8";
        }
    }

    public abstract void a(byte[] bArr);

    public int b() {
        return this.i;
    }

    public abstract byte[] d();
}
