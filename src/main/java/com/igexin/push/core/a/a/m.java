package com.igexin.push.core.a.a;

import com.igexin.a.a.c.a;
import com.igexin.push.core.g;
import com.igexin.push.e.b.f;
import java.util.Map;

class m extends f {
    final /* synthetic */ Map a;
    final /* synthetic */ l b;

    m(l lVar, long j, Map map) {
        this.b = lVar;
        this.a = map;
        super(j);
    }

    protected void a() {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(g.g.getPackageName());
            stringBuffer.append("#");
            stringBuffer.append(this.b.d((String) this.a.get("pkgName")));
            stringBuffer.append("#");
            stringBuffer.append((String) this.a.get("pkgName"));
            stringBuffer.append("/");
            stringBuffer.append((String) this.a.get("serviceName"));
            stringBuffer.append("#");
            if (l.a((String) this.a.get("pkgName"), (String) this.a.get("serviceName"))) {
                stringBuffer.append("1");
            } else {
                stringBuffer.append("0");
            }
            this.b.b("30026", stringBuffer.toString(), (String) this.a.get("messageId"), (String) this.a.get("taskId"), (String) this.a.get("id"));
            a.b("feedback actionId=30026 result=" + stringBuffer.toString());
        } catch (Exception e) {
        }
    }

    public int b() {
        return 0;
    }
}
