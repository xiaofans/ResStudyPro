package com.igexin.a.a.d;

import com.igexin.a.a.c.a;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class c {
    static final /* synthetic */ boolean h = (!c.class.desiredAssertionStatus());
    final transient ReentrantLock a = new ReentrantLock();
    final transient Condition b = this.a.newCondition();
    final TreeSet c;
    final AtomicInteger d = new AtomicInteger(0);
    int e;
    e f;
    public final AtomicLong g = new AtomicLong(-1);

    public c(Comparator comparator, e eVar) {
        this.c = new TreeSet(comparator);
        this.f = eVar;
    }

    private d e() {
        d a = a();
        return (a != null && this.c.remove(a)) ? a : null;
    }

    public final int a(d dVar, long j, TimeUnit timeUnit) {
        ReentrantLock reentrantLock = this.a;
        reentrantLock.lock();
        try {
            if (!this.c.contains(dVar)) {
                return -1;
            }
            this.c.remove(dVar);
            dVar.F = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(j, timeUnit);
            int i = a(dVar) ? 1 : -2;
            reentrantLock.unlock();
            return i;
        } finally {
            reentrantLock.unlock();
        }
    }

    d a() {
        try {
            return (d) this.c.first();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public final boolean a(d dVar) {
        if (dVar == null) {
            return false;
        }
        ReentrantLock reentrantLock = this.a;
        reentrantLock.lock();
        try {
            d a = a();
            int i = this.e + 1;
            this.e = i;
            dVar.G = i;
            if (this.c.add(dVar)) {
                dVar.m();
                if (a == null || this.c.comparator().compare(dVar, a) < 0) {
                    this.b.signalAll();
                }
                a.a("ScheduleQueue|offer|succeeded|" + dVar.getClass().getName() + "|" + dVar.hashCode() + "|" + dVar.a(TimeUnit.MILLISECONDS));
                reentrantLock.unlock();
                return true;
            }
            dVar.G--;
            a.a("ScheduleQueue|offer|failed|" + dVar.getClass().getName() + "|" + dVar.hashCode() + "|" + dVar.a(TimeUnit.MILLISECONDS));
            return false;
        } catch (Exception e) {
            a.a("ScheduleQueue|offer|exception|" + dVar.getClass().getName() + "|" + dVar.a(TimeUnit.MILLISECONDS));
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    public final boolean a(Class cls) {
        if (cls == null) {
            return false;
        }
        ReentrantLock reentrantLock = this.a;
        reentrantLock.lock();
        try {
            Collection arrayList = new ArrayList();
            a.a("ScheduleQueue|removeByType|" + cls.getName());
            Iterator it = this.c.iterator();
            while (it.hasNext()) {
                d dVar = (d) it.next();
                if (dVar.getClass() == cls) {
                    arrayList.add(dVar);
                }
            }
            a.a("ScheduleQueue|removeByType|" + cls.getName() + "|" + arrayList.size());
            this.c.removeAll(arrayList);
            return true;
        } finally {
            reentrantLock.unlock();
        }
    }

    final boolean b() {
        ReentrantLock reentrantLock = this.a;
        reentrantLock.lock();
        try {
            boolean isEmpty = this.c.isEmpty();
            return isEmpty;
        } finally {
            reentrantLock.unlock();
        }
    }

    public final d c() {
        d e;
        ReentrantLock reentrantLock = this.a;
        reentrantLock.lockInterruptibly();
        while (true) {
            d a = a();
            if (a == null) {
                this.d.set(1);
                this.e = 0;
                a.a("ScheduleQueue|take|forever");
                this.b.await();
            } else {
                try {
                    long a2 = a.a(TimeUnit.NANOSECONDS);
                    Object obj = (a.w || a.x) ? 1 : null;
                    if (a2 <= 0 || obj != null) {
                        e = e();
                    } else {
                        a.a("ScheduleQueue|take|" + a.getClass().getName() + "|" + a.hashCode() + "|" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(a.F)));
                        this.g.set(a.F);
                        if (this.f.y) {
                            this.f.a(a.F);
                        }
                        this.b.awaitNanos(a2);
                    }
                } finally {
                    reentrantLock.unlock();
                }
            }
        }
        e = e();
        if (h || e != null) {
            if (!b()) {
                this.b.signalAll();
            }
            this.g.set(-1);
            a.a("ScheduleQueue|take|" + a.getClass().getName() + "|" + a.hashCode() + "|imediate");
            return e;
        }
        throw new AssertionError();
    }

    public final void d() {
        this.c.clear();
    }
}
