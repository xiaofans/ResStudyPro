package com.alipay.android.phone.mrpc.core;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public final class q extends d {
    private n g;

    public q(n nVar, Method method, int i, String str, byte[] bArr, boolean z) {
        super(method, i, str, bArr, HttpRequest.CONTENT_TYPE_FORM, z);
        this.g = nVar;
    }

    public final Object a() {
        Throwable e;
        aa vVar = new v(this.g.a());
        vVar.b = this.b;
        vVar.c = this.e;
        vVar.e = this.f;
        vVar.a("id", String.valueOf(this.d));
        vVar.a("operationType", this.c);
        vVar.a(HttpRequest.ENCODING_GZIP, String.valueOf(this.g.d()));
        vVar.a(new BasicHeader("uuid", UUID.randomUUID().toString()));
        List<Header> list = this.g.c().b;
        if (!(list == null || list.isEmpty())) {
            for (Header a : list) {
                vVar.a(a);
            }
        }
        new StringBuilder("threadid = ").append(Thread.currentThread().getId()).append("; ").append(vVar.toString());
        try {
            ab abVar = (ab) this.g.b().a(vVar).get();
            if (abVar != null) {
                return abVar.a();
            }
            throw new c(Integer.valueOf(9), "response is null");
        } catch (Throwable e2) {
            throw new c(Integer.valueOf(13), "", e2);
        } catch (Throwable e22) {
            Throwable th = e22;
            e22 = th.getCause();
            if (e22 == null || !(e22 instanceof a)) {
                throw new c(Integer.valueOf(9), "", th);
            }
            a aVar = (a) e22;
            int i = aVar.k;
            switch (i) {
                case 1:
                    i = 2;
                    break;
                case 2:
                    i = 3;
                    break;
                case 3:
                    i = 4;
                    break;
                case 4:
                    i = 5;
                    break;
                case 5:
                    i = 6;
                    break;
                case 6:
                    i = 7;
                    break;
                case 7:
                    i = 8;
                    break;
                case 8:
                    i = 15;
                    break;
                case 9:
                    i = 16;
                    break;
            }
            throw new c(Integer.valueOf(i), aVar.l);
        } catch (Throwable e222) {
            throw new c(Integer.valueOf(13), "", e222);
        }
    }
}
