package com.igexin.push.a.a;

import com.igexin.push.core.a.e;
import com.igexin.push.core.g;
import com.igexin.push.e.b.d;
import com.umeng.analytics.a;

public class b implements d {
    private long a = 0;

    public void a() {
        e.a().z();
        e.a().r();
        e.a().s();
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - g.J > a.i) {
            Object obj = 1;
            if (e.a().a(currentTimeMillis)) {
                if ("1".equals(e.a().e("ccs"))) {
                    obj = null;
                }
            }
            if (obj != null) {
                g.J = currentTimeMillis;
                e.a().y();
            }
        }
        e.a().A();
    }

    public void a(long j) {
        this.a = j;
    }

    public boolean b() {
        return System.currentTimeMillis() - this.a > a.i;
    }
}
