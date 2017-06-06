package com.douban.book.reader.view.card;

import android.content.Context;
import com.douban.book.reader.content.paragraph.Paragraph;
import java.util.List;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class WorksTocCard_ extends WorksTocCard implements HasViews {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public WorksTocCard_(Context context) {
        super(context);
        init_();
    }

    public static WorksTocCard build(Context context) {
        WorksTocCard_ instance = new WorksTocCard_(context);
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
        OnViewChangedNotifier.replaceNotifier(OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_));
    }

    void updateContent(final List<Paragraph> paragraphList) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateContent(paragraphList);
            }
        }, 0);
    }

    void onLoadFailed() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.onLoadFailed();
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
