package u.aly;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: ActiveUser */
public class aq implements Serializable, Cloneable, bz<aq, e> {
    public static final Map<e, cl> c;
    private static final dd d = new dd("ActiveUser");
    private static final ct e = new ct("provider", (byte) 11, (short) 1);
    private static final ct f = new ct("puid", (byte) 11, (short) 2);
    private static final Map<Class<? extends dg>, dh> g = new HashMap();
    public String a;
    public String b;

    /* compiled from: ActiveUser */
    private static class b implements dh {
        private b() {
        }

        public /* synthetic */ dg b() {
            return a();
        }

        public a a() {
            return new a();
        }
    }

    /* compiled from: ActiveUser */
    private static class d implements dh {
        private d() {
        }

        public /* synthetic */ dg b() {
            return a();
        }

        public c a() {
            return new c();
        }
    }

    /* compiled from: ActiveUser */
    public enum e implements cg {
        PROVIDER((short) 1, "provider"),
        PUID((short) 2, "puid");
        
        private static final Map<String, e> c = null;
        private final short d;
        private final String e;

        static {
            c = new HashMap();
            Iterator it = EnumSet.allOf(e.class).iterator();
            while (it.hasNext()) {
                e eVar = (e) it.next();
                c.put(eVar.b(), eVar);
            }
        }

        public static e a(int i) {
            switch (i) {
                case 1:
                    return PROVIDER;
                case 2:
                    return PUID;
                default:
                    return null;
            }
        }

        public static e b(int i) {
            e a = a(i);
            if (a != null) {
                return a;
            }
            throw new IllegalArgumentException("Field " + i + " doesn't exist!");
        }

        public static e a(String str) {
            return (e) c.get(str);
        }

        private e(short s, String str) {
            this.d = s;
            this.e = str;
        }

        public short a() {
            return this.d;
        }

        public String b() {
            return this.e;
        }
    }

    /* compiled from: ActiveUser */
    private static class a extends di<aq> {
        private a() {
        }

        public /* synthetic */ void a(cy cyVar, bz bzVar) throws cf {
            b(cyVar, (aq) bzVar);
        }

        public /* synthetic */ void b(cy cyVar, bz bzVar) throws cf {
            a(cyVar, (aq) bzVar);
        }

        public void a(cy cyVar, aq aqVar) throws cf {
            cyVar.j();
            while (true) {
                ct l = cyVar.l();
                if (l.b == (byte) 0) {
                    cyVar.k();
                    aqVar.j();
                    return;
                }
                switch (l.c) {
                    case (short) 1:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        aqVar.a = cyVar.z();
                        aqVar.a(true);
                        break;
                    case (short) 2:
                        if (l.b != (byte) 11) {
                            db.a(cyVar, l.b);
                            break;
                        }
                        aqVar.b = cyVar.z();
                        aqVar.b(true);
                        break;
                    default:
                        db.a(cyVar, l.b);
                        break;
                }
                cyVar.m();
            }
        }

        public void b(cy cyVar, aq aqVar) throws cf {
            aqVar.j();
            cyVar.a(aq.d);
            if (aqVar.a != null) {
                cyVar.a(aq.e);
                cyVar.a(aqVar.a);
                cyVar.c();
            }
            if (aqVar.b != null) {
                cyVar.a(aq.f);
                cyVar.a(aqVar.b);
                cyVar.c();
            }
            cyVar.d();
            cyVar.b();
        }
    }

    /* compiled from: ActiveUser */
    private static class c extends dj<aq> {
        private c() {
        }

        public void a(cy cyVar, aq aqVar) throws cf {
            de deVar = (de) cyVar;
            deVar.a(aqVar.a);
            deVar.a(aqVar.b);
        }

        public void b(cy cyVar, aq aqVar) throws cf {
            de deVar = (de) cyVar;
            aqVar.a = deVar.z();
            aqVar.a(true);
            aqVar.b = deVar.z();
            aqVar.b(true);
        }
    }

    public /* synthetic */ cg b(int i) {
        return a(i);
    }

    public /* synthetic */ bz g() {
        return a();
    }

    static {
        g.put(di.class, new b());
        g.put(dj.class, new d());
        Map enumMap = new EnumMap(e.class);
        enumMap.put(e.PROVIDER, new cl("provider", (byte) 1, new cm((byte) 11)));
        enumMap.put(e.PUID, new cl("puid", (byte) 1, new cm((byte) 11)));
        c = Collections.unmodifiableMap(enumMap);
        cl.a(aq.class, c);
    }

    public aq(String str, String str2) {
        this();
        this.a = str;
        this.b = str2;
    }

    public aq(aq aqVar) {
        if (aqVar.e()) {
            this.a = aqVar.a;
        }
        if (aqVar.i()) {
            this.b = aqVar.b;
        }
    }

    public aq a() {
        return new aq(this);
    }

    public void b() {
        this.a = null;
        this.b = null;
    }

    public String c() {
        return this.a;
    }

    public aq a(String str) {
        this.a = str;
        return this;
    }

    public void d() {
        this.a = null;
    }

    public boolean e() {
        return this.a != null;
    }

    public void a(boolean z) {
        if (!z) {
            this.a = null;
        }
    }

    public String f() {
        return this.b;
    }

    public aq b(String str) {
        this.b = str;
        return this;
    }

    public void h() {
        this.b = null;
    }

    public boolean i() {
        return this.b != null;
    }

    public void b(boolean z) {
        if (!z) {
            this.b = null;
        }
    }

    public e a(int i) {
        return e.a(i);
    }

    public void a(cy cyVar) throws cf {
        ((dh) g.get(cyVar.D())).b().b(cyVar, this);
    }

    public void b(cy cyVar) throws cf {
        ((dh) g.get(cyVar.D())).b().a(cyVar, this);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("ActiveUser(");
        stringBuilder.append("provider:");
        if (this.a == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.a);
        }
        stringBuilder.append(", ");
        stringBuilder.append("puid:");
        if (this.b == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.b);
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public void j() throws cf {
        if (this.a == null) {
            throw new cz("Required field 'provider' was not present! Struct: " + toString());
        } else if (this.b == null) {
            throw new cz("Required field 'puid' was not present! Struct: " + toString());
        }
    }

    private void a(ObjectOutputStream objectOutputStream) throws IOException {
        try {
            b(new cs(new dk((OutputStream) objectOutputStream)));
        } catch (cf e) {
            throw new IOException(e.getMessage());
        }
    }

    private void a(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            a(new cs(new dk((InputStream) objectInputStream)));
        } catch (cf e) {
            throw new IOException(e.getMessage());
        }
    }
}
