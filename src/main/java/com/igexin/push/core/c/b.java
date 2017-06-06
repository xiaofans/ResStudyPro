package com.igexin.push.core.c;

import android.database.sqlite.SQLiteDatabase;
import com.igexin.push.f.c;
import java.util.HashMap;
import java.util.Map;

public class b implements a {
    private static b c;
    c a = new c();
    private Map b = new HashMap();

    private b() {
    }

    public static b a() {
        if (c == null) {
            c = new b();
        }
        return c;
    }

    public void a(SQLiteDatabase sQLiteDatabase) {
    }

    public void b(SQLiteDatabase sQLiteDatabase) {
    }

    public void c(SQLiteDatabase sQLiteDatabase) {
    }
}
