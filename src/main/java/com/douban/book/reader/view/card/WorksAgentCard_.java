package com.douban.book.reader.view.card;

import android.content.Context;
import com.douban.book.reader.entity.WorksAgent;
import com.douban.book.reader.manager.WorksAgentManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class WorksAgentCard_ extends WorksAgentCard implements HasViews {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public WorksAgentCard_(Context context) {
        super(context);
        init_();
    }

    public static WorksAgentCard build(Context context) {
        WorksAgentCard_ instance = new WorksAgentCard_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        this.mWorksAgentManager = WorksAgentManager_.getInstance_(getContext());
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    void onLoadFailed() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.onLoadFailed();
            }
        }, 0);
    }

    void refreshViews(final WorksAgent worksAgent) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.refreshViews(worksAgent);
            }
        }, 0);
    }

    void loadData() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadData();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
