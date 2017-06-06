package com.douban.book.reader.view.card;

import android.content.Context;
import com.douban.book.reader.entity.Works;
import com.douban.book.reader.manager.WorksManager_;
import java.util.List;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class SimilarWorksCard_ extends SimilarWorksCard implements HasViews {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public SimilarWorksCard_(Context context) {
        super(context);
        init_();
    }

    public static SimilarWorksCard build(Context context) {
        SimilarWorksCard_ instance = new SimilarWorksCard_(context);
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
        this.mWorksManager = WorksManager_.getInstance_(getContext());
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    void onLoadFailed() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.onLoadFailed();
            }
        }, 0);
    }

    void updateViews(final List<Works> worksList) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateViews(worksList);
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
