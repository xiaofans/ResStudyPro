package com.igexin.push.core.bean;

import android.os.Build.VERSION;
import com.douban.amonsul.StatConstant;
import com.douban.amonsul.network.NetWorker;
import com.igexin.push.core.g;
import com.igexin.sdk.PushBuildConfig;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import org.json.JSONObject;

public class a {
    public String a;
    public String b;
    public String c;
    public String d;
    public String e;
    public String f = "open";
    public String g;
    public String h;
    public String i;
    public String j;
    public String k;
    public long l;

    public a() {
        if (g.e != null) {
            this.f += ":" + g.e;
        }
        this.e = PushBuildConfig.sdk_conf_version;
        this.b = g.v;
        this.c = g.u;
        this.d = g.x;
        this.i = g.y;
        this.a = g.w;
        this.h = "ANDROID";
        this.j = "android" + VERSION.RELEASE;
        this.k = "MDP";
        this.g = g.z;
        this.l = System.currentTimeMillis();
    }

    public static String a(a aVar) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("model", aVar.a == null ? "" : aVar.a);
        jSONObject.put("sim", aVar.b == null ? "" : aVar.b);
        jSONObject.put("imei", aVar.c == null ? "" : aVar.c);
        jSONObject.put(StatConstant.JSON_KEY_MAC, aVar.d == null ? "" : aVar.d);
        jSONObject.put("version", aVar.e == null ? "" : aVar.e);
        jSONObject.put("channelid", aVar.f == null ? "" : aVar.f);
        jSONObject.put("type", "ANDROID");
        jSONObject.put(SettingsJsonConstants.APP_KEY, aVar.k == null ? "" : aVar.k);
        jSONObject.put("deviceid", "ANDROID-" + (aVar.g == null ? "" : aVar.g));
        jSONObject.put("system_version", aVar.j == null ? "" : aVar.j);
        jSONObject.put("cell", aVar.i == null ? "" : aVar.i);
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("action", "addphoneinfo");
        jSONObject2.put("id", String.valueOf(aVar.l));
        jSONObject2.put(NetWorker.PARAM_KEY_APP_INFO, jSONObject);
        return jSONObject2.toString();
    }
}
