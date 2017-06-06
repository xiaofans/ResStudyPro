package com.alipay.sdk.protocol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.alipay.sdk.cons.c;
import com.alipay.sdk.data.f;
import com.alipay.sdk.exception.NetErrorException;
import com.alipay.sdk.sys.b;
import com.alipay.sdk.tid.a;
import com.douban.book.reader.entity.Annotation.Column;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.json.JSONException;
import org.json.JSONObject;

public final class e {
    public static g a(c cVar) throws NetErrorException {
        Throwable th;
        com.alipay.sdk.data.e eVar = cVar.a;
        f fVar = cVar.b;
        JSONObject jSONObject = cVar.c;
        if (jSONObject.has(c.c)) {
            g gVar = new g(eVar, fVar);
            gVar.a(cVar.c);
            return gVar;
        }
        if (jSONObject.has("status")) {
            switch (f.a(jSONObject.optString("status"))) {
                case SUCCESS:
                case NOT_POP_TYPE:
                case POP_TYPE:
                    gVar = new g(eVar, fVar);
                    gVar.a(jSONObject);
                    return gVar;
                case TID_REFRESH:
                    Context context = b.a().a;
                    String a = com.alipay.sdk.util.b.a(context).a();
                    String b = com.alipay.sdk.util.b.a(context).b();
                    a aVar = new a(context);
                    SQLiteDatabase writableDatabase;
                    try {
                        writableDatabase = aVar.getWritableDatabase();
                        try {
                            aVar.a(writableDatabase, a, b, "", "");
                            a.a(writableDatabase, a.c(a, b));
                            if (writableDatabase != null && writableDatabase.isOpen()) {
                                writableDatabase.close();
                            }
                        } catch (Exception e) {
                            writableDatabase.close();
                            aVar.close();
                            return null;
                        } catch (Throwable th2) {
                            th = th2;
                            writableDatabase.close();
                            throw th;
                        }
                    } catch (Exception e2) {
                        writableDatabase = null;
                        if (writableDatabase != null && writableDatabase.isOpen()) {
                            writableDatabase.close();
                        }
                        aVar.close();
                        return null;
                    } catch (Throwable th3) {
                        th = th3;
                        writableDatabase = null;
                        if (writableDatabase != null && writableDatabase.isOpen()) {
                            writableDatabase.close();
                        }
                        throw th;
                    }
                    aVar.close();
            }
        }
        return null;
    }

    private static void b(c cVar) throws NetErrorException {
        f fVar = cVar.b;
        JSONObject jSONObject = cVar.c;
        com.alipay.sdk.data.a aVar = cVar.a.a;
        com.alipay.sdk.data.a aVar2 = cVar.b.l;
        if (TextUtils.isEmpty(aVar2.c)) {
            aVar2.c = aVar.c;
        }
        if (TextUtils.isEmpty(aVar2.d)) {
            aVar2.d = aVar.d;
        }
        if (TextUtils.isEmpty(aVar2.b)) {
            aVar2.b = aVar.b;
        }
        if (TextUtils.isEmpty(aVar2.a)) {
            aVar2.a = aVar.a;
        }
        String str = SettingsJsonConstants.SESSION_KEY;
        JSONObject optJSONObject = jSONObject.optJSONObject("reflected_data");
        if (optJSONObject != null) {
            new StringBuilder("session = ").append(optJSONObject.optString(str, ""));
            cVar.b.i = optJSONObject;
        } else if (jSONObject.has(str)) {
            optJSONObject = new JSONObject();
            try {
                optJSONObject.put(str, jSONObject.optString(str));
                CharSequence charSequence = com.alipay.sdk.tid.b.a().a;
                if (!TextUtils.isEmpty(charSequence)) {
                    optJSONObject.put(com.alipay.sdk.cons.b.c, charSequence);
                }
                fVar.i = optJSONObject;
            } catch (JSONException e) {
            }
        }
        fVar.f = jSONObject.optString("end_code", "0");
        fVar.j = jSONObject.optString(Column.USER_ID, "");
        str = jSONObject.optString("result");
        try {
            str = URLDecoder.decode(jSONObject.optString("result"), "UTF-8");
        } catch (UnsupportedEncodingException e2) {
        }
        fVar.g = str;
        fVar.h = jSONObject.optString("memo", "");
    }
}
