package org.json.alipay;

import com.douban.book.reader.constant.Char;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class c {
    public static final Object b = new a();
    public Map a;

    private static final class a {
        private a() {
        }

        protected final Object clone() {
            return this;
        }

        public final boolean equals(Object obj) {
            return obj == null || obj == this;
        }

        public final String toString() {
            return "null";
        }
    }

    public c() {
        this.a = new HashMap();
    }

    public c(String str) {
        this(new d(str));
    }

    public c(Map map) {
        if (map == null) {
            map = new HashMap();
        }
        this.a = map;
    }

    public c(d dVar) {
        this();
        if (dVar.c() != '{') {
            throw dVar.a("A JSONObject text must begin with '{'");
        }
        while (true) {
            switch (dVar.c()) {
                case '\u0000':
                    throw dVar.a("A JSONObject text must end with '}'");
                case Header.STRING_0 /*125*/:
                    return;
                default:
                    dVar.a();
                    String obj = dVar.d().toString();
                    char c = dVar.c();
                    if (c == '=') {
                        if (dVar.b() != '>') {
                            dVar.a();
                        }
                    } else if (c != ':') {
                        throw dVar.a("Expected a ':' after a key");
                    }
                    Object d = dVar.d();
                    if (obj == null) {
                        throw new a("Null key.");
                    }
                    if (d != null) {
                        b(d);
                        this.a.put(obj, d);
                    } else {
                        this.a.remove(obj);
                    }
                    switch (dVar.c()) {
                        case ',':
                        case ';':
                            if (dVar.c() != '}') {
                                dVar.a();
                            } else {
                                return;
                            }
                        case Header.STRING_0 /*125*/:
                            return;
                        default:
                            throw dVar.a("Expected a ',' or '}'");
                    }
            }
        }
    }

    static String a(Object obj) {
        if (obj == null || obj.equals(null)) {
            return "null";
        }
        if (!(obj instanceof Number)) {
            return ((obj instanceof Boolean) || (obj instanceof c) || (obj instanceof b)) ? obj.toString() : obj instanceof Map ? new c((Map) obj).toString() : obj instanceof Collection ? new b((Collection) obj).toString() : obj.getClass().isArray() ? new b(obj).toString() : b(obj.toString());
        } else {
            obj = (Number) obj;
            if (obj == null) {
                throw new a("Null pointer");
            }
            b(obj);
            String obj2 = obj.toString();
            if (obj2.indexOf(46) <= 0 || obj2.indexOf(101) >= 0 || obj2.indexOf(69) >= 0) {
                return obj2;
            }
            while (obj2.endsWith("0")) {
                obj2 = obj2.substring(0, obj2.length() - 1);
            }
            return obj2.endsWith(".") ? obj2.substring(0, obj2.length() - 1) : obj2;
        }
    }

    public static String b(String str) {
        int i = 0;
        if (str == null || str.length() == 0) {
            return "\"\"";
        }
        int length = str.length();
        StringBuffer stringBuffer = new StringBuffer(length + 4);
        stringBuffer.append('\"');
        int i2 = 0;
        while (i < length) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case '\b':
                    stringBuffer.append("\\b");
                    break;
                case '\t':
                    stringBuffer.append("\\t");
                    break;
                case '\n':
                    stringBuffer.append("\\n");
                    break;
                case '\f':
                    stringBuffer.append("\\f");
                    break;
                case '\r':
                    stringBuffer.append("\\r");
                    break;
                case '\"':
                case '\\':
                    stringBuffer.append('\\');
                    stringBuffer.append(charAt);
                    break;
                case '/':
                    if (i2 == 60) {
                        stringBuffer.append('\\');
                    }
                    stringBuffer.append(charAt);
                    break;
                default:
                    if (charAt >= Char.SPACE && ((charAt < '' || charAt >= ' ') && (charAt < ' ' || charAt >= '℀'))) {
                        stringBuffer.append(charAt);
                        break;
                    }
                    String str2 = "000" + Integer.toHexString(charAt);
                    stringBuffer.append("\\u" + str2.substring(str2.length() - 4));
                    break;
                    break;
            }
            i++;
            char c = charAt;
        }
        stringBuffer.append('\"');
        return stringBuffer.toString();
    }

    private static void b(Object obj) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Double) {
            if (((Double) obj).isInfinite() || ((Double) obj).isNaN()) {
                throw new a("JSON does not allow non-finite numbers.");
            }
        } else if (!(obj instanceof Float)) {
        } else {
            if (((Float) obj).isInfinite() || ((Float) obj).isNaN()) {
                throw new a("JSON does not allow non-finite numbers.");
            }
        }
    }

    private boolean c(String str) {
        return this.a.containsKey(str);
    }

    public final Object a(String str) {
        Object obj = str == null ? null : this.a.get(str);
        if (obj != null) {
            return obj;
        }
        throw new a("JSONObject[" + b(str) + "] not found.");
    }

    public final Iterator a() {
        return this.a.keySet().iterator();
    }

    public String toString() {
        try {
            Iterator a = a();
            StringBuffer stringBuffer = new StringBuffer("{");
            while (a.hasNext()) {
                if (stringBuffer.length() > 1) {
                    stringBuffer.append(',');
                }
                Object next = a.next();
                stringBuffer.append(b(next.toString()));
                stringBuffer.append(':');
                stringBuffer.append(a(this.a.get(next)));
            }
            stringBuffer.append('}');
            return stringBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
