package com.igexin.a.a.d;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

final class h implements ThreadFactory {
    final AtomicInteger a = new AtomicInteger(0);
    final /* synthetic */ f b;

    public h(f fVar) {
        this.b = fVar;
    }

    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, "TaskService-pool-" + this.a.incrementAndGet());
    }
}
