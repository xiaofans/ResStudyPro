package com.douban.book.reader.controller;

import android.os.Bundle;
import com.douban.book.reader.util.AppInfo;
import com.douban.book.reader.util.AssertUtils;
import com.douban.book.reader.util.Logger;
import java.util.concurrent.Callable;
import natalya.os.TaskExecutor;
import natalya.os.TaskExecutor.TaskCallback;

public abstract class AbsTaskController {
    private static final boolean DEBUG = AppInfo.isDebug();
    private static final String TAG = AbsTaskController.class.getSimpleName();

    public <T> String execute(final Callable<T> callable, final TaskCallback<T> callback, Object caller) {
        if (!DEBUG) {
            return TaskExecutor.getInstance().execute(callable, callback, caller);
        }
        Logger.v(TAG, "execute() caller=" + caller, new Object[0]);
        return TaskExecutor.getInstance().execute(new Callable<T>() {
            public T call() throws Exception {
                return callable.call();
            }
        }, new TaskCallback<T>() {
            public void onTaskSuccess(T result, Bundle extras, Object object) {
                if (callback != null) {
                    callback.onTaskSuccess(result, extras, object);
                }
            }

            public void onTaskFailure(Throwable e, Bundle extras) {
                Logger.e(AbsTaskController.TAG, e);
                if (callback != null) {
                    callback.onTaskFailure(e, extras);
                }
            }
        }, caller);
    }

    public String execute(final Runnable runnable, Object caller) {
        AssertUtils.notNull("caller and runnable cann't be null", runnable, caller);
        return TaskExecutor.getInstance().execute(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                runnable.run();
                return Boolean.valueOf(true);
            }
        }, null, caller);
    }

    public void destroy() {
        TaskExecutor.getInstance().destroy();
    }

    public void cancelByCaller(Object caller) {
        TaskExecutor.getInstance().cancelByCaller(caller);
    }

    public void cancelByTag(String tag) {
        TaskExecutor.getInstance().cancelByTag(tag);
    }

    public void cancelAll() {
        TaskExecutor.getInstance().cancelAll();
    }

    public void onLogout() {
        cancelAll();
    }
}
