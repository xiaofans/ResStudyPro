package com.igexin.push.core.d;

import com.douban.amonsul.StatConstant;
import com.igexin.a.b.a;
import com.igexin.push.core.g;
import com.igexin.push.e.a.b;
import org.json.JSONObject;

public class k extends b {
    public k(String str, byte[] bArr, int i) {
        super(str);
        a(bArr, i);
    }

    private void a(byte[] bArr, int i) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("action", "upload_BI");
            jSONObject.put("BIType", String.valueOf(i));
            jSONObject.put(StatConstant.JSON_KEY_CELLID, g.s);
            jSONObject.put("BIData", new String(com.igexin.a.a.b.g.e(bArr, 0), "UTF-8"));
            b(a.b(jSONObject.toString().getBytes()));
        } catch (Exception e) {
        }
    }

    public void a(byte[] bArr) {
        String str = new String(bArr);
    }

    public int b() {
        return 0;
    }
}
