package u.aly;

import android.content.Context;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.h;
import com.umeng.analytics.h.b;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import u.aly.cr.a;

/* compiled from: Sender */
public class z {
    private static final int a = 1;
    private static final int b = 2;
    private static final int c = 3;
    private e d;
    private g e;
    private final int f = 1;
    private Context g;
    private ab h;
    private u i;
    private bp j;
    private boolean k = false;
    private boolean l;

    public z(Context context, ab abVar) {
        this.d = e.a(context);
        this.e = g.a(context);
        this.g = context;
        this.h = abVar;
        this.i = new u(context);
        this.i.a(this.h);
    }

    public void a(bp bpVar) {
        this.j = bpVar;
    }

    public void a(boolean z) {
        this.k = z;
    }

    public void b(boolean z) {
        this.l = z;
    }

    public void a(x xVar) {
        this.e.a(xVar);
    }

    public void a() {
        if (this.j != null) {
            c();
        } else {
            b();
        }
    }

    private void b() {
        h.a(this.g).h().a(new b(this) {
            final /* synthetic */ z a;

            {
                this.a = r1;
            }

            public void a(File file) {
            }

            public boolean b(File file) {
                InputStream fileInputStream;
                Throwable th;
                try {
                    fileInputStream = new FileInputStream(file);
                    try {
                        byte[] b = bu.b(fileInputStream);
                        try {
                            int i;
                            bu.c(fileInputStream);
                            byte[] a = this.a.i.a(b);
                            if (a == null) {
                                i = 1;
                            } else {
                                i = this.a.a(a);
                            }
                            if (i == 2 && this.a.h.m()) {
                                this.a.h.l();
                            }
                            if (!this.a.l && i == 1) {
                                return false;
                            }
                            return true;
                        } catch (Exception e) {
                            return false;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        bu.c(fileInputStream);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileInputStream = null;
                    bu.c(fileInputStream);
                    throw th;
                }
            }

            public void c(File file) {
                this.a.h.k();
            }
        });
    }

    private void c() {
        this.d.a();
        bp bpVar = this.j;
        bpVar.a(this.d.b());
        byte[] b = b(bpVar);
        if (b == null) {
            bv.d("message is null");
            return;
        }
        c b2;
        int i;
        if (this.k) {
            b2 = c.b(this.g, AnalyticsConfig.getAppkey(this.g), b);
        } else {
            b2 = c.a(this.g, AnalyticsConfig.getAppkey(this.g), b);
        }
        byte[] c = b2.c();
        h.a(this.g).f();
        b = this.i.a(c);
        if (b == null) {
            i = 1;
        } else {
            i = a(b);
        }
        switch (i) {
            case 1:
                if (!this.l) {
                    h.a(this.g).b(c);
                }
                bv.e("connection error");
                return;
            case 2:
                if (this.h.m()) {
                    this.h.l();
                }
                this.d.d();
                this.h.k();
                return;
            case 3:
                this.h.k();
                return;
            default:
                return;
        }
    }

    private int a(byte[] bArr) {
        bz blVar = new bl();
        try {
            new cc(new a()).a(blVar, bArr);
            if (blVar.a == 1) {
                this.e.b(blVar.j());
                this.e.d();
            }
            bv.c("send log:" + blVar.f());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (blVar.a == 1) {
            return 2;
        }
        return 3;
    }

    private byte[] b(bp bpVar) {
        if (bpVar == null) {
            return null;
        }
        try {
            byte[] a = new ci().a(bpVar);
            if (bv.a) {
                bv.b(bpVar.toString());
            }
            return a;
        } catch (Throwable e) {
            bv.e("Fail to serialize log ...", e);
            return null;
        }
    }
}
