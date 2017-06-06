package com.douban.book.reader.util;

import android.os.Handler;
import android.os.Message;
import com.douban.book.reader.app.App;

public class WatchDogTimer {
    private static final int MSG_TIMED_OUT = 1;
    private static final String TAG = WatchDogTimer.class.getSimpleName();
    private Callback mCallback = null;
    private Object mExtra = null;
    private Handler mHandler = null;
    private Thread mThreadToWatch = null;

    public interface Callback {
        void onTimedOut(Object obj);
    }

    public static class TimedOutException extends Exception {
        public TimedOutException(String detailMessage) {
            super(detailMessage);
        }
    }

    public WatchDogTimer(Callback callback) {
        this.mCallback = callback;
        App.get().runOnUiThread(new Runnable() {
            public void run() {
                WatchDogTimer.this.initHandler();
            }
        });
    }

    public void startWatching(int timeoutInMillis, Object extra) {
        if (this.mThreadToWatch != null) {
            throw new IllegalStateException("Another startWatching has already been called.");
        }
        this.mThreadToWatch = Thread.currentThread();
        this.mExtra = extra;
        if (this.mHandler != null) {
            this.mHandler.sendEmptyMessageDelayed(1, (long) timeoutInMillis);
        }
    }

    public void stopWatching() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(1);
        }
        this.mThreadToWatch = null;
    }

    private void initHandler() {
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (WatchDogTimer.this.mThreadToWatch != null) {
                    Logger.d(WatchDogTimer.TAG, "TIMED OUT. Interrupting thread %s (%s) ...", Long.valueOf(WatchDogTimer.this.mThreadToWatch.getId()), WatchDogTimer.this.mThreadToWatch.getName());
                    WatchDogTimer.this.mThreadToWatch.interrupt();
                    WatchDogTimer.this.mThreadToWatch = null;
                    if (WatchDogTimer.this.mCallback != null) {
                        WatchDogTimer.this.mCallback.onTimedOut(WatchDogTimer.this.mExtra);
                    }
                }
            }
        };
    }
}
