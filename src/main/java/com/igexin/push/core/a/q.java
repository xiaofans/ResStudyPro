package com.igexin.push.core.a;

import com.igexin.a.a.c.a;
import com.igexin.push.config.k;
import com.igexin.push.core.c.f;
import com.igexin.push.core.g;
import org.json.JSONObject;

public class q extends b {
    private static final String a = k.a;

    public boolean a(Object obj, JSONObject jSONObject) {
        try {
            if (jSONObject.has("action") && jSONObject.getString("action").equals("response_deviceid")) {
                String string = jSONObject.getString("deviceid");
                a.b(a + " get devid resp, devid : " + string + ", save 2db and file");
                f.a().b(string);
                if (g.av != null) {
                    a.b(a + " deviceid arrived cancel addPhoneInfoTimerTask...");
                    g.av.t();
                    g.av = null;
                }
                if (g.z != null) {
                    com.igexin.push.core.f.a().i().i();
                }
                a.b("deviceidRsp|" + g.z);
            }
        } catch (Exception e) {
        }
        return true;
    }
}
