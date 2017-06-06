package com.igexin.push.core.c;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.douban.book.reader.entity.DbCacheEntity.Column;
import com.igexin.a.a.a.a;
import com.igexin.a.a.b.d;
import com.igexin.push.config.k;
import com.igexin.push.core.g;
import com.igexin.push.f.b;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class f implements a {
    private static final String a = (k.a + "_RuntimeDataManager");
    private static f b;
    private Map c = new HashMap();

    private f() {
    }

    public static f a() {
        if (b == null) {
            b = new f();
        }
        return b;
    }

    private void a(SQLiteDatabase sQLiteDatabase, int i, String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", Integer.valueOf(i));
        contentValues.put(Column.VALUE, str);
        sQLiteDatabase.replace("runtime", null, contentValues);
    }

    private void a(SQLiteDatabase sQLiteDatabase, int i, byte[] bArr) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", Integer.valueOf(i));
        contentValues.put(Column.VALUE, bArr);
        sQLiteDatabase.replace("runtime", null, contentValues);
    }

    private boolean e() {
        return d.c().a(new o(this), false, true);
    }

    private byte[] e(String str) {
        return com.igexin.push.f.d.a(str.getBytes());
    }

    private void f() {
        b.a();
        String c = b.c();
        if (c == null || c.length() <= 5) {
            b.e();
        }
    }

    private String g() {
        String str = "";
        Random random = new Random(Math.abs(new Random().nextLong()));
        for (int i = 0; i < 15; i++) {
            str = str + random.nextInt(10);
        }
        return str;
    }

    public void a(SQLiteDatabase sQLiteDatabase) {
    }

    public boolean a(int i) {
        g.U = i;
        d.c().a(new n(this), false, true);
        return true;
    }

    public boolean a(long j) {
        g.a(j);
        d.c().a(new q(this), false, true);
        return true;
    }

    public boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        g.x = str;
        return d.c().a(new p(this), false, true);
    }

    public boolean a(String str, String str2, long j) {
        g.r = j;
        if (TextUtils.isEmpty(g.z)) {
            g.z = str2;
        }
        g.s = str;
        return e();
    }

    public boolean a(boolean z) {
        if (g.N == z) {
            return false;
        }
        g.N = z;
        d.c().a(new k(this), false, true);
        return true;
    }

    public void b() {
        d.c().a(new g(this), false, true);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void b(android.database.sqlite.SQLiteDatabase r12) {
        /*
        r11 = this;
        r6 = 0;
        r9 = 20;
        r7 = 1;
        r1 = 0;
        r4 = 0;
        r0 = "select id, value from runtime order by id";
        r2 = 0;
        r0 = r12.rawQuery(r0, r2);	 Catch:{ Exception -> 0x02e8, all -> 0x02e5 }
        if (r0 == 0) goto L_0x02d6;
    L_0x0010:
        r2 = r0.moveToNext();	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r2 == 0) goto L_0x02d6;
    L_0x0016:
        r2 = 0;
        r3 = 1;
        r8 = r0.getInt(r2);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r8 == r7) goto L_0x0028;
    L_0x001e:
        r2 = 14;
        if (r8 == r2) goto L_0x0028;
    L_0x0022:
        r2 = 19;
        if (r8 == r2) goto L_0x0028;
    L_0x0026:
        if (r8 != r9) goto L_0x0174;
    L_0x0028:
        r2 = r0.getBlob(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r3 = r2;
        r2 = r1;
    L_0x002e:
        switch(r8) {
            case 1: goto L_0x0032;
            case 2: goto L_0x019d;
            case 3: goto L_0x0204;
            case 4: goto L_0x0181;
            case 5: goto L_0x0031;
            case 6: goto L_0x01ce;
            case 7: goto L_0x01bc;
            case 8: goto L_0x01aa;
            case 9: goto L_0x01e0;
            case 10: goto L_0x01f2;
            case 11: goto L_0x0211;
            case 12: goto L_0x0223;
            case 13: goto L_0x0235;
            case 14: goto L_0x0242;
            case 15: goto L_0x0283;
            case 16: goto L_0x0293;
            case 17: goto L_0x02a5;
            case 18: goto L_0x02b2;
            case 19: goto L_0x0251;
            case 20: goto L_0x0269;
            case 21: goto L_0x02c4;
            default: goto L_0x0031;
        };	 Catch:{ Exception -> 0x0051, all -> 0x018e }
    L_0x0031:
        goto L_0x0010;
    L_0x0032:
        r2 = new java.lang.String;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r8 = com.igexin.push.core.g.B;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r3 = com.igexin.a.a.a.a.a(r3, r8);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r2.<init>(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r2 == 0) goto L_0x0047;
    L_0x003f:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x004b, all -> 0x018e }
        if (r3 == 0) goto L_0x017b;
    L_0x0047:
        r2 = r4;
    L_0x0048:
        com.igexin.push.core.g.r = r2;	 Catch:{ Exception -> 0x004b, all -> 0x018e }
        goto L_0x0010;
    L_0x004b:
        r2 = move-exception;
        r2 = 0;
        com.igexin.push.core.g.r = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x0051:
        r2 = move-exception;
    L_0x0052:
        if (r0 == 0) goto L_0x0057;
    L_0x0054:
        r0.close();
    L_0x0057:
        r2 = com.igexin.push.core.g.r;
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r0 != 0) goto L_0x0076;
    L_0x005d:
        r2 = com.igexin.push.f.b.d();
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r0 == 0) goto L_0x0076;
    L_0x0065:
        com.igexin.push.core.g.r = r2;
        r0 = java.lang.String.valueOf(r2);
        r0 = r0.getBytes();
        r0 = com.igexin.push.f.d.a(r0);
        r11.a(r12, r7, r0);
    L_0x0076:
        r0 = com.igexin.push.core.g.s;
        if (r0 != 0) goto L_0x0095;
    L_0x007a:
        r0 = com.igexin.push.f.b.b();
        if (r0 == 0) goto L_0x0095;
    L_0x0080:
        com.igexin.push.core.g.t = r0;
        com.igexin.push.core.g.s = r0;
        r0 = com.igexin.push.core.g.s;
        r0 = java.lang.String.valueOf(r0);
        r0 = r0.getBytes();
        r0 = com.igexin.push.f.d.a(r0);
        r11.a(r12, r9, r0);
    L_0x0095:
        r0 = com.igexin.push.core.g.s;
        if (r0 != 0) goto L_0x00c1;
    L_0x0099:
        r2 = com.igexin.push.core.g.r;
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r0 == 0) goto L_0x00c1;
    L_0x009f:
        r2 = com.igexin.push.core.g.r;
        r0 = java.lang.String.valueOf(r2);
        r0 = com.igexin.a.b.a.a(r0);
        com.igexin.push.core.g.t = r0;
        r2 = com.igexin.push.core.g.r;
        com.igexin.push.core.g.a(r2);
        r0 = com.igexin.push.core.g.s;
        r0 = java.lang.String.valueOf(r0);
        r0 = r0.getBytes();
        r0 = com.igexin.push.f.d.a(r0);
        r11.a(r12, r9, r0);
    L_0x00c1:
        r0 = "cfcd208495d565ef66e7dff9f98764da";
        r2 = com.igexin.push.core.g.s;
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x00e1;
    L_0x00cb:
        r2 = com.igexin.push.core.g.r;
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r0 == 0) goto L_0x02dd;
    L_0x00d1:
        r0 = a();
        r2 = com.igexin.push.core.g.r;
        r0.a(r2);
        r0 = com.igexin.push.core.g.s;
        com.igexin.push.core.g.t = r0;
        com.igexin.push.f.b.f();
    L_0x00e1:
        r0 = com.igexin.push.core.g.au;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x00f3;
    L_0x00e9:
        r0 = "null";
        r1 = com.igexin.push.core.g.au;
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x010a;
    L_0x00f3:
        r0 = 32;
        r0 = com.igexin.a.b.a.a(r0);
        com.igexin.push.core.g.au = r0;
        r0 = 14;
        r1 = com.igexin.push.core.g.au;
        r1 = r1.getBytes();
        r1 = com.igexin.push.f.d.a(r1);
        r11.a(r12, r0, r1);
    L_0x010a:
        r0 = com.igexin.push.f.b.c();
        r1 = com.igexin.push.core.g.z;
        if (r1 != 0) goto L_0x0127;
    L_0x0112:
        if (r0 == 0) goto L_0x0127;
    L_0x0114:
        r1 = r0.length();
        r2 = 5;
        if (r1 <= r2) goto L_0x0127;
    L_0x011b:
        com.igexin.push.core.g.z = r0;
        r0 = 2;
        r1 = com.igexin.push.core.g.z;
        r1 = java.lang.String.valueOf(r1);
        r11.a(r12, r0, r1);
    L_0x0127:
        r0 = com.igexin.push.core.g.A;
        if (r0 != 0) goto L_0x0173;
    L_0x012b:
        r0 = com.igexin.push.core.g.u;
        if (r0 != 0) goto L_0x0146;
    L_0x012f:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "V";
        r0 = r0.append(r1);
        r1 = r11.g();
        r0 = r0.append(r1);
        r0 = r0.toString();
    L_0x0146:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "A-";
        r1 = r1.append(r2);
        r0 = r1.append(r0);
        r1 = "-";
        r0 = r0.append(r1);
        r2 = java.lang.System.currentTimeMillis();
        r0 = r0.append(r2);
        r0 = r0.toString();
        com.igexin.push.core.g.A = r0;
        r0 = 3;
        r1 = com.igexin.push.core.g.A;
        r1 = java.lang.String.valueOf(r1);
        r11.a(r12, r0, r1);
    L_0x0173:
        return;
    L_0x0174:
        r2 = r0.getString(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r3 = r1;
        goto L_0x002e;
    L_0x017b:
        r2 = java.lang.Long.parseLong(r2);	 Catch:{ Exception -> 0x004b, all -> 0x018e }
        goto L_0x0048;
    L_0x0181:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x0198;
    L_0x0189:
        r2 = r7;
    L_0x018a:
        com.igexin.push.core.g.l = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x018e:
        r1 = move-exception;
        r10 = r1;
        r1 = r0;
        r0 = r10;
    L_0x0192:
        if (r1 == 0) goto L_0x0197;
    L_0x0194:
        r1.close();
    L_0x0197:
        throw r0;
    L_0x0198:
        r2 = java.lang.Boolean.parseBoolean(r2);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x018a;
    L_0x019d:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x01a6;
    L_0x01a5:
        r2 = r1;
    L_0x01a6:
        com.igexin.push.core.g.z = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x01aa:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x01b7;
    L_0x01b2:
        r2 = r4;
    L_0x01b3:
        com.igexin.push.core.g.H = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x01b7:
        r2 = java.lang.Long.parseLong(r2);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x01b3;
    L_0x01bc:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x01c9;
    L_0x01c4:
        r2 = r4;
    L_0x01c5:
        com.igexin.push.core.g.G = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x01c9:
        r2 = java.lang.Long.parseLong(r2);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x01c5;
    L_0x01ce:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x01db;
    L_0x01d6:
        r2 = r4;
    L_0x01d7:
        com.igexin.push.core.g.F = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x01db:
        r2 = java.lang.Long.parseLong(r2);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x01d7;
    L_0x01e0:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x01ed;
    L_0x01e8:
        r2 = r4;
    L_0x01e9:
        com.igexin.push.core.g.P = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x01ed:
        r2 = java.lang.Long.parseLong(r2);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x01e9;
    L_0x01f2:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x01ff;
    L_0x01fa:
        r2 = r4;
    L_0x01fb:
        com.igexin.push.core.g.Q = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x01ff:
        r2 = java.lang.Long.parseLong(r2);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x01fb;
    L_0x0204:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x020d;
    L_0x020c:
        r2 = r1;
    L_0x020d:
        com.igexin.push.core.g.A = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x0211:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x021e;
    L_0x0219:
        r2 = r4;
    L_0x021a:
        com.igexin.push.core.g.K = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x021e:
        r2 = java.lang.Long.parseLong(r2);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x021a;
    L_0x0223:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x0230;
    L_0x022b:
        r2 = r4;
    L_0x022c:
        com.igexin.push.core.g.L = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x0230:
        r2 = java.lang.Long.parseLong(r2);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x022c;
    L_0x0235:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x023e;
    L_0x023d:
        r2 = r1;
    L_0x023e:
        com.igexin.push.core.g.M = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x0242:
        r2 = new java.lang.String;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r8 = com.igexin.push.core.g.B;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r3 = com.igexin.a.a.a.a.a(r3, r8);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r2.<init>(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        com.igexin.push.core.g.au = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x0251:
        r2 = new java.lang.String;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r8 = com.igexin.push.core.g.B;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r3 = com.igexin.a.a.a.a.a(r3, r8);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r2.<init>(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x0265;
    L_0x0264:
        r2 = r1;
    L_0x0265:
        com.igexin.push.core.g.x = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x0269:
        r2 = new java.lang.String;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r8 = com.igexin.push.core.g.B;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r3 = com.igexin.a.a.a.a.a(r3, r8);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r2.<init>(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x027d;
    L_0x027c:
        r2 = r1;
    L_0x027d:
        com.igexin.push.core.g.t = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        com.igexin.push.core.g.s = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x0283:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 != 0) goto L_0x0010;
    L_0x028b:
        r2 = java.lang.Boolean.parseBoolean(r2);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        com.igexin.push.core.g.N = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x0293:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x02a0;
    L_0x029b:
        r2 = r4;
    L_0x029c:
        com.igexin.push.core.g.O = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x02a0:
        r2 = java.lang.Long.parseLong(r2);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x029c;
    L_0x02a5:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x02ae;
    L_0x02ad:
        r2 = r1;
    L_0x02ae:
        com.igexin.push.core.g.S = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x02b2:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x02bf;
    L_0x02ba:
        r2 = r6;
    L_0x02bb:
        com.igexin.push.core.g.U = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x02bf:
        r2 = java.lang.Integer.parseInt(r2);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x02bb;
    L_0x02c4:
        r3 = "null";
        r3 = r2.equals(r3);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        if (r3 == 0) goto L_0x02d1;
    L_0x02cc:
        r2 = r4;
    L_0x02cd:
        com.igexin.push.core.g.aw = r2;	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x0010;
    L_0x02d1:
        r2 = java.lang.Long.parseLong(r2);	 Catch:{ Exception -> 0x0051, all -> 0x018e }
        goto L_0x02cd;
    L_0x02d6:
        if (r0 == 0) goto L_0x0057;
    L_0x02d8:
        r0.close();
        goto L_0x0057;
    L_0x02dd:
        com.igexin.push.core.g.t = r1;
        com.igexin.push.core.g.s = r1;
        com.igexin.push.core.g.r = r4;
        goto L_0x00e1;
    L_0x02e5:
        r0 = move-exception;
        goto L_0x0192;
    L_0x02e8:
        r0 = move-exception;
        r0 = r1;
        goto L_0x0052;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.igexin.push.core.c.f.b(android.database.sqlite.SQLiteDatabase):void");
    }

    public boolean b(long j) {
        if (g.P == j) {
            return false;
        }
        g.P = j;
        d.c().a(new s(this), false, true);
        return true;
    }

    public boolean b(String str) {
        g.z = str;
        d.c().a(new r(this), false, true);
        return true;
    }

    public void c(SQLiteDatabase sQLiteDatabase) {
        a(sQLiteDatabase, 1, a.b(String.valueOf(g.r).getBytes(), g.B));
        a(sQLiteDatabase, 4, String.valueOf(g.l));
        a(sQLiteDatabase, 8, String.valueOf(g.H));
        a(sQLiteDatabase, 7, String.valueOf(g.G));
        a(sQLiteDatabase, 6, String.valueOf(g.F));
        a(sQLiteDatabase, 9, String.valueOf(g.P));
        a(sQLiteDatabase, 10, String.valueOf(g.Q));
        a(sQLiteDatabase, 3, String.valueOf(g.A));
        a(sQLiteDatabase, 11, String.valueOf(g.K));
        a(sQLiteDatabase, 12, String.valueOf(g.L));
        a(sQLiteDatabase, 20, a.b(String.valueOf(g.s).getBytes(), g.B));
        a(sQLiteDatabase, 2, String.valueOf(g.z));
    }

    public boolean c() {
        g.r = 0;
        g.s = null;
        return e();
    }

    public boolean c(long j) {
        if (g.L == j) {
            return false;
        }
        g.L = j;
        d.c().a(new t(this), false, true);
        return true;
    }

    public boolean c(String str) {
        if (str == null || str.equals(g.M)) {
            return false;
        }
        g.M = str;
        d.c().a(new j(this), false, true);
        return true;
    }

    public Map d() {
        return this.c;
    }

    public boolean d(long j) {
        g.aw = j;
        com.igexin.a.a.c.a.b(a + " save idc config failed time : " + j);
        d.c().a(new u(this, j), false, true);
        return true;
    }

    public boolean d(String str) {
        if (g.S == str) {
            return false;
        }
        g.S = str;
        d.c().a(new m(this), false, true);
        return true;
    }

    public boolean e(long j) {
        if (g.Q == j) {
            return false;
        }
        g.Q = j;
        d.c().a(new v(this), false, true);
        return true;
    }

    public boolean f(long j) {
        if (g.G == j) {
            return false;
        }
        g.G = j;
        d.c().a(new h(this), false, true);
        return true;
    }

    public boolean g(long j) {
        if (g.K == j) {
            return false;
        }
        g.K = j;
        d.c().a(new i(this), false, true);
        return true;
    }

    public boolean h(long j) {
        if (g.O == j) {
            return false;
        }
        g.O = j;
        d.c().a(new l(this), false, true);
        return true;
    }
}
