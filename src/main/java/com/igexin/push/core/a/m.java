package com.igexin.push.core.a;

import com.igexin.push.config.k;
import com.igexin.push.core.c.c;
import org.json.JSONException;
import org.json.JSONObject;

public class m extends b {
    private static final String a = k.a;

    public boolean a(Object obj, JSONObject jSONObject) {
        try {
            if (jSONObject.has("action") && jSONObject.getString("action").equals("received")) {
                try {
                    if (c.a().a(Long.parseLong(jSONObject.getString("id")))) {
                        e.a().g();
                    }
                } catch (NumberFormatException e) {
                }
            }
        } catch (JSONException e2) {
        }
        return true;
    }
}
