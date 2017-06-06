package com.douban.book.reader.helper;

import android.content.Context;
import com.douban.book.reader.manager.SessionManager.SessionRetriever;
import com.douban.book.reader.manager.SessionManager_;
import com.douban.book.reader.manager.ShelfManager_;
import com.douban.book.reader.manager.UserManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;

public final class LoginHelper_ extends LoginHelper {
    private Context context_;

    private LoginHelper_(Context context) {
        this.context_ = context;
        init_();
    }

    public static LoginHelper_ getInstance_(Context context) {
        return new LoginHelper_(context);
    }

    private void init_() {
        this.mUserManager = UserManager_.getInstance_(this.context_);
        this.mSessionManager = SessionManager_.getInstance_(this.context_);
        this.mShelfManager = ShelfManager_.getInstance_(this.context_);
    }

    public void rebind(Context context) {
        this.context_ = context;
        init_();
    }

    void confirmMigrate(final String lastAccessToken) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.confirmMigrate(lastAccessToken);
            }
        }, 0);
    }

    void postLogin() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.postLogin();
            }
        }, 0);
    }

    void performLogin(SessionRetriever sessionRetriever) {
        final SessionRetriever sessionRetriever2 = sessionRetriever;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.performLogin(sessionRetriever2);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    void migrateLastUserDataAndRedirect(String lastAccessToken) {
        final String str = lastAccessToken;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.migrateLastUserDataAndRedirect(str);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    void clearLastUserDataAndRedirect() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.clearLastUserDataAndRedirect();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    void refreshShelfItems() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.refreshShelfItems();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
