package org.mapdb;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.mapdb.Fun.Tuple2;
import org.mapdb.LongMap.LongMapIterator;

public class AsyncWriteEngine extends EngineWrapper implements Engine {
    static final /* synthetic */ boolean $assertionsDisabled = (!AsyncWriteEngine.class.desiredAssertionStatus());
    protected static final Object TOMBSTONE = new Object();
    protected static final AtomicLong threadCounter = new AtomicLong();
    protected final AtomicReference<CountDownLatch> action;
    protected final CountDownLatch activeThreadsCount;
    protected final int asyncFlushDelay;
    protected volatile boolean closeInProgress;
    protected final ReentrantReadWriteLock commitLock;
    protected final int maxSize;
    protected final AtomicInteger size;
    protected volatile Throwable threadFailedException;
    protected final LongConcurrentHashMap<Tuple2<Object, Serializer>> writeCache;

    protected static final class WriterRunnable implements Runnable {
        protected final long asyncFlushDelay;
        private final ReentrantReadWriteLock commitLock;
        protected final WeakReference<AsyncWriteEngine> engineRef;
        protected final int maxParkSize;
        protected final AtomicInteger size;

        public void run() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x003c in list [B:25:0x0061]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
            /*
            r6 = this;
        L_0x0000:
            r2 = r6.asyncFlushDelay;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            r4 = 0;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            if (r2 == 0) goto L_0x0023;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
        L_0x0008:
            r2 = r6.commitLock;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            r2 = r2.isWriteLocked();	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            if (r2 != 0) goto L_0x0023;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
        L_0x0010:
            r2 = r6.size;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            r2 = r2.get();	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            r3 = r6.maxParkSize;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            if (r2 >= r3) goto L_0x0023;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
        L_0x001a:
            r2 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            r4 = r6.asyncFlushDelay;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            r2 = r2 * r4;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            java.util.concurrent.locks.LockSupport.parkNanos(r2);	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
        L_0x0023:
            r2 = r6.engineRef;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            r1 = r2.get();	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            r1 = (org.mapdb.AsyncWriteEngine) r1;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            if (r1 != 0) goto L_0x003d;
        L_0x002d:
            r2 = r6.engineRef;
            r1 = r2.get();
            r1 = (org.mapdb.AsyncWriteEngine) r1;
            if (r1 == 0) goto L_0x003c;
        L_0x0037:
            r2 = r1.activeThreadsCount;
            r2.countDown();
        L_0x003c:
            return;
        L_0x003d:
            r2 = r1.threadFailedException;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            if (r2 == 0) goto L_0x0051;
        L_0x0041:
            r2 = r6.engineRef;
            r1 = r2.get();
            r1 = (org.mapdb.AsyncWriteEngine) r1;
            if (r1 == 0) goto L_0x003c;
        L_0x004b:
            r2 = r1.activeThreadsCount;
            r2.countDown();
            goto L_0x003c;
        L_0x0051:
            r2 = r1.runWriter();	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            if (r2 != 0) goto L_0x0000;
        L_0x0057:
            r2 = r6.engineRef;
            r1 = r2.get();
            r1 = (org.mapdb.AsyncWriteEngine) r1;
            if (r1 == 0) goto L_0x003c;
        L_0x0061:
            r2 = r1.activeThreadsCount;
            r2.countDown();
            goto L_0x003c;
        L_0x0067:
            r0 = move-exception;
            r2 = r6.engineRef;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            r1 = r2.get();	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            r1 = (org.mapdb.AsyncWriteEngine) r1;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
            if (r1 == 0) goto L_0x0074;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
        L_0x0072:
            r1.threadFailedException = r0;	 Catch:{ Throwable -> 0x0067, all -> 0x0084 }
        L_0x0074:
            r2 = r6.engineRef;
            r1 = r2.get();
            r1 = (org.mapdb.AsyncWriteEngine) r1;
            if (r1 == 0) goto L_0x003c;
        L_0x007e:
            r2 = r1.activeThreadsCount;
            r2.countDown();
            goto L_0x003c;
        L_0x0084:
            r2 = move-exception;
            r3 = r6.engineRef;
            r1 = r3.get();
            r1 = (org.mapdb.AsyncWriteEngine) r1;
            if (r1 == 0) goto L_0x0094;
        L_0x008f:
            r3 = r1.activeThreadsCount;
            r3.countDown();
        L_0x0094:
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapdb.AsyncWriteEngine.WriterRunnable.run():void");
        }

        public WriterRunnable(AsyncWriteEngine engine) {
            this.engineRef = new WeakReference(engine);
            this.asyncFlushDelay = (long) engine.asyncFlushDelay;
            this.commitLock = engine.commitLock;
            this.size = engine.size;
            this.maxParkSize = engine.maxSize / 4;
        }
    }

    public AsyncWriteEngine(Engine engine, int _asyncFlushDelay, int queueSize, Executor executor) {
        super(engine);
        this.size = new AtomicInteger();
        this.writeCache = new LongConcurrentHashMap();
        this.commitLock = new ReentrantReadWriteLock(false);
        this.activeThreadsCount = new CountDownLatch(1);
        this.threadFailedException = null;
        this.closeInProgress = false;
        this.action = new AtomicReference(null);
        this.asyncFlushDelay = _asyncFlushDelay;
        this.maxSize = queueSize;
        startThreads(executor);
    }

    public AsyncWriteEngine(Engine engine) {
        this(engine, 100, CC.ASYNC_WRITE_QUEUE_SIZE, null);
    }

    protected void startThreads(Executor executor) {
        Runnable writerRun = new WriterRunnable(this);
        if (executor != null) {
            executor.execute(writerRun);
            return;
        }
        Thread writerThread = new Thread(writerRun, "MapDB writer #" + threadCounter.incrementAndGet());
        writerThread.setDaemon(true);
        writerThread.start();
    }

    protected boolean runWriter() throws InterruptedException {
        CountDownLatch latch = (CountDownLatch) this.action.getAndSet(null);
        do {
            LongMapIterator<Tuple2<Object, Serializer>> iter = this.writeCache.longMapIterator();
            while (iter.moveToNext()) {
                long recid = iter.key();
                Tuple2<Object, Serializer> item = (Tuple2) iter.value();
                if (item != null) {
                    if (item.a == TOMBSTONE) {
                        super.delete(recid, (Serializer) item.b);
                    } else {
                        super.update(recid, item.a, (Serializer) item.b);
                    }
                    if (this.writeCache.remove(recid, item)) {
                        this.size.decrementAndGet();
                    }
                }
            }
            if (latch == null) {
                break;
            }
        } while (!this.writeCache.isEmpty());
        if (latch != null) {
            if ($assertionsDisabled || this.writeCache.isEmpty()) {
                long count = latch.getCount();
                if (count == 0) {
                    return false;
                }
                if (count == 1) {
                    super.commit();
                    latch.countDown();
                } else if (count == 2) {
                    super.rollback();
                    latch.countDown();
                    latch.countDown();
                } else if (count == 3) {
                    super.compact();
                    latch.countDown();
                    latch.countDown();
                    latch.countDown();
                } else {
                    throw new AssertionError();
                }
            }
            throw new AssertionError();
        }
        return true;
    }

    protected void checkState() {
        if (this.closeInProgress) {
            throw new IllegalAccessError("db has been closed");
        } else if (this.threadFailedException != null) {
            throw new RuntimeException("Writer thread failed", this.threadFailedException);
        }
    }

    public <A> long put(A value, Serializer<A> serializer) {
        int size2 = 0;
        this.commitLock.readLock().lock();
        try {
            checkState();
            long recid = preallocate();
            if (this.writeCache.put(recid, new Tuple2(value, serializer)) == null) {
                size2 = this.size.incrementAndGet();
            }
            this.commitLock.readLock().unlock();
            if (size2 > this.maxSize) {
                clearCache();
            }
            return recid;
        } catch (Throwable th) {
            this.commitLock.readLock().unlock();
        }
    }

    public <A> A get(long recid, Serializer<A> serializer) {
        this.commitLock.readLock().lock();
        try {
            checkState();
            Tuple2<Object, Serializer> item = (Tuple2) this.writeCache.get(recid);
            A a;
            if (item == null) {
                a = super.get(recid, serializer);
                this.commitLock.readLock().unlock();
                return a;
            } else if (item.a == TOMBSTONE) {
                return null;
            } else {
                a = item.a;
                this.commitLock.readLock().unlock();
                return a;
            }
        } finally {
            this.commitLock.readLock().unlock();
        }
    }

    public <A> void update(long recid, A value, Serializer<A> serializer) {
        int size2 = 0;
        this.commitLock.readLock().lock();
        try {
            checkState();
            if (this.writeCache.put(recid, new Tuple2(value, serializer)) == null) {
                size2 = this.size.incrementAndGet();
            }
            this.commitLock.readLock().unlock();
            if (size2 > this.maxSize) {
                clearCache();
            }
        } catch (Throwable th) {
            this.commitLock.readLock().unlock();
        }
    }

    public <A> boolean compareAndSwap(long recid, A expectedOldValue, A newValue, Serializer<A> serializer) {
        int size2 = 0;
        this.commitLock.writeLock().lock();
        try {
            boolean ret;
            checkState();
            Tuple2<Object, Serializer> existing = (Tuple2) this.writeCache.get(recid);
            A oldValue = existing != null ? existing.a : super.get(recid, serializer);
            if (oldValue == expectedOldValue || (oldValue != null && oldValue.equals(expectedOldValue))) {
                if (this.writeCache.put(recid, new Tuple2(newValue, serializer)) == null) {
                    size2 = this.size.incrementAndGet();
                }
                ret = true;
            } else {
                ret = false;
            }
            this.commitLock.writeLock().unlock();
            if (size2 > this.maxSize) {
                clearCache();
            }
            return ret;
        } catch (Throwable th) {
            this.commitLock.writeLock().unlock();
        }
    }

    public <A> void delete(long recid, Serializer<A> serializer) {
        update(recid, TOMBSTONE, serializer);
    }

    public void close() {
        this.commitLock.writeLock().lock();
        try {
            if (this.closeInProgress) {
                this.commitLock.writeLock().unlock();
                return;
            }
            checkState();
            this.closeInProgress = true;
            if (this.action.compareAndSet(null, new CountDownLatch(0))) {
                do {
                } while (!this.activeThreadsCount.await(1000, TimeUnit.MILLISECONDS));
                super.close();
                this.commitLock.writeLock().unlock();
                return;
            }
            throw new AssertionError();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Throwable th) {
            this.commitLock.writeLock().unlock();
        }
    }

    protected void waitForAction(int actionNumber) {
        this.commitLock.writeLock().lock();
        try {
            checkState();
            CountDownLatch msg = new CountDownLatch(actionNumber);
            if (this.action.compareAndSet(null, msg)) {
                while (!msg.await(100, TimeUnit.MILLISECONDS)) {
                    checkState();
                }
                this.commitLock.writeLock().unlock();
                return;
            }
            throw new AssertionError();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Throwable th) {
            this.commitLock.writeLock().unlock();
        }
    }

    public void commit() {
        waitForAction(1);
    }

    public void rollback() {
        waitForAction(2);
    }

    public void compact() {
        waitForAction(3);
    }

    public void clearCache() {
        this.commitLock.writeLock().lock();
        try {
            checkState();
            while (!this.writeCache.isEmpty()) {
                checkState();
                Thread.sleep(100);
            }
            this.commitLock.writeLock().unlock();
            super.clearCache();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Throwable th) {
            this.commitLock.writeLock().unlock();
        }
    }
}
