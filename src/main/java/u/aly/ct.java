package u.aly;

import com.j256.ormlite.stmt.query.SimpleComparison;

/* compiled from: TField */
public class ct {
    public final String a;
    public final byte b;
    public final short c;

    public ct() {
        this("", (byte) 0, (short) 0);
    }

    public ct(String str, byte b, short s) {
        this.a = str;
        this.b = b;
        this.c = s;
    }

    public String toString() {
        return "<TField name:'" + this.a + "' type:" + this.b + " field-id:" + this.c + SimpleComparison.GREATER_THAN_OPERATION;
    }

    public boolean a(ct ctVar) {
        return this.b == ctVar.b && this.c == ctVar.c;
    }
}
