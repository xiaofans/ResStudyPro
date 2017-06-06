package com.igexin.push.core;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.igexin.a.a.c.a;
import com.igexin.push.config.k;
import com.igexin.push.config.l;
import com.igexin.push.core.b.c;
import com.igexin.push.core.bean.f;
import com.igexin.sdk.PushConsts;

public class e extends Handler {
    private static String a = k.a;

    public void handleMessage(Message message) {
        Bundle bundleExtra;
        if (message.what == a.c) {
            Intent intent = (Intent) message.obj;
            if (intent != null && intent.hasExtra("action")) {
                String stringExtra = intent.getStringExtra("action");
                if (stringExtra.equals(PushConsts.ACTION_SERVICE_INITIALIZE)) {
                    com.igexin.push.core.a.e.a().a(intent);
                } else if (stringExtra.equals(PushConsts.ACTION_SERVICE_INITIALIZE_SLAVE)) {
                    com.igexin.push.core.a.e.a().b(intent);
                } else if (stringExtra.equals(PushConsts.ACTION_BROADCAST_REFRESHLS)) {
                    if (l.n) {
                        c.a().b(intent);
                    }
                } else if (stringExtra.equals(PushConsts.ACTION_BROADCAST_PUSHMANAGER)) {
                    bundleExtra = intent.getBundleExtra("bundle");
                    a.b(a + " receive broadcast com.igexin.sdk.action.pushmanager");
                    com.igexin.push.core.a.e.a().a(bundleExtra);
                } else if (stringExtra.equals(PushConsts.ACTION_BROADCAST_USER_PRESENT)) {
                    g.J = System.currentTimeMillis();
                    Object obj = 1;
                    if (com.igexin.push.core.a.e.a().a(System.currentTimeMillis())) {
                        if ("1".equals(com.igexin.push.core.a.e.a().e("ccs"))) {
                            obj = null;
                        }
                    }
                    if (obj != null) {
                        com.igexin.push.core.a.e.a().y();
                    }
                } else if (stringExtra.equals("com.igexin.sdk.action.extdownloadsuccess")) {
                    com.igexin.push.core.a.e.a().d(intent);
                }
            }
        } else if (message.what == a.d) {
            c.a().a((Intent) message.obj);
        } else if (message.what == a.e) {
            com.igexin.push.core.a.e.a().c((Intent) message.obj);
        } else if (message.what == a.f) {
            com.igexin.push.core.a.e.a().c((Intent) message.obj);
        } else if (message.what == a.g) {
        } else {
            if (message.what == a.h) {
                bundleExtra = (Bundle) message.obj;
                com.igexin.push.core.a.e.a().b(bundleExtra.getString("taskid"), bundleExtra.getString("messageid"), bundleExtra.getString("actionid"));
            } else if (message.what == a.i) {
                com.igexin.push.f.a.a((f) message.obj);
            } else if (message.what == a.j) {
                com.igexin.push.core.a.e.a().t();
            }
        }
    }
}
