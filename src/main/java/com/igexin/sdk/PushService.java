package com.igexin.sdk;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import com.igexin.a.a.c.a;
import com.igexin.push.config.m;
import com.igexin.push.core.g;
import com.igexin.sdk.a.d;

public class PushService extends Service {
    private static String a = "PushSdk";
    private IPushCore b;
    private boolean c = false;
    private boolean d;
    private boolean e;
    private ServiceConnection f = new a(this);

    private void b() {
        try {
            a.b(a + " bind user process service");
            bindService(new Intent(this, PushServiceUser.class), this.f, 1);
        } catch (Exception e) {
            a.b(a + e.toString());
        }
    }

    private void c() {
        try {
            a.b(a + " stop user process service by getui");
            this.d = true;
            unbindService(this.f);
        } catch (Exception e) {
            a.b(a + e.toString());
        }
    }

    public IBinder onBind(Intent intent) {
        return this.b != null ? this.b.onServiceBind(intent) : null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        if (this.b != null) {
            this.b.onServiceDestroy();
        }
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }

    public void onLowMemory() {
        a.b(a + " PushService Low Memory!");
        super.onLowMemory();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        String stringExtra;
        super.onStartCommand(intent, i, i2);
        if (intent != null) {
            stringExtra = intent.getStringExtra("action");
            if (stringExtra != null && stringExtra.equals("stopUserService") && this.e) {
                c();
                return 1;
            }
        }
        if (!this.c) {
            this.c = true;
            b();
            if (intent != null) {
                stringExtra = intent.getStringExtra("action");
                if (!(PushConsts.ACTION_SERVICE_INITIALIZE.equals(stringExtra) || PushConsts.ACTION_SERVICE_INITIALIZE_SLAVE.equals(stringExtra))) {
                    m.a((Context) this);
                    if ("1".equals(g.c().get("ss")) && !new d(this).c()) {
                        c();
                        stopSelf();
                        return 1;
                    }
                }
            }
            com.igexin.sdk.a.a.a().a((Context) this);
            this.b = com.igexin.sdk.a.a.a().b();
            this.b.start(this);
        }
        return this.b != null ? this.b.onServiceStartCommand(intent, i, i2) : 1;
    }
}
