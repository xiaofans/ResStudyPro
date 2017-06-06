package com.igexin.push.core.a;

import com.igexin.a.a.c.a;
import org.json.JSONException;
import org.json.JSONObject;

public class r extends b {
    public boolean a(Object obj, JSONObject jSONObject) {
        try {
            if (jSONObject.has("action") && jSONObject.getString("action").equals("sendmessage_feedback")) {
                String string = jSONObject.getString("appid");
                String string2 = jSONObject.getString("taskid");
                String string3 = jSONObject.getString("actionid");
                String string4 = jSONObject.getString("result");
                long j = jSONObject.getLong("timestamp");
                a.b("SendMessageFeedbackAction|appid:" + string + "|taskid:" + string2 + "|actionid:" + string3);
                e.a().a(string, string2, string3, string4, j);
            }
        } catch (JSONException e) {
        }
        return true;
    }
}
