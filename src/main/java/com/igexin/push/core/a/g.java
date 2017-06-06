package com.igexin.push.core.a;

import com.igexin.a.a.d.d;
import com.igexin.push.c.c.n;
import com.igexin.push.config.k;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class g extends a {
    private static final String a = k.a;
    private static Map b;

    public g() {
        b = new HashMap();
        b.put("redirect_server", new n());
        b.put("response_deviceid", new q());
        b.put("pushmessage", new l());
        b.put("response_ca_list", new p());
        b.put("received", new m());
        b.put("sendmessage_feedback", new r());
        b.put("block_client", new c());
    }

    public boolean a(d dVar) {
        return false;
    }

    public boolean a(Object obj) {
        if (obj instanceof n) {
            n nVar = (n) obj;
            if (nVar.a() && nVar.e != null) {
                try {
                    JSONObject jSONObject = new JSONObject((String) nVar.e);
                    if (jSONObject.has("action") && !jSONObject.getString("action").equals("received") && jSONObject.has("id")) {
                        e.a().a(jSONObject.getString("id"));
                    }
                    if (jSONObject != null && jSONObject.has("action")) {
                        b bVar = (b) b.get(jSONObject.getString("action"));
                        if (bVar != null) {
                            return bVar.a(obj, jSONObject);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return false;
    }
}
