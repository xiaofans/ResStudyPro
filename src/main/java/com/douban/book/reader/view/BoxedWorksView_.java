package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import com.douban.book.reader.entity.Works;
import com.douban.book.reader.manager.WorksManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class BoxedWorksView_ extends BoxedWorksView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public BoxedWorksView_(Context context) {
        super(context);
        init_();
    }

    public BoxedWorksView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public BoxedWorksView_(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init_();
    }

    public static BoxedWorksView build(Context context) {
        BoxedWorksView_ instance = new BoxedWorksView_(context);
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
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static BoxedWorksView build(Context context, AttributeSet attrs) {
        BoxedWorksView_ instance = new BoxedWorksView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static BoxedWorksView build(Context context, AttributeSet attrs, int defStyleAttr) {
        BoxedWorksView_ instance = new BoxedWorksView_(context, attrs, defStyleAttr);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        init();
    }

    void updateWorksCover(final Works works) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateWorksCover(works);
            }
        }, 0);
    }

    void loadWorks(int worksId) {
        final int i = worksId;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadWorks(i);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
