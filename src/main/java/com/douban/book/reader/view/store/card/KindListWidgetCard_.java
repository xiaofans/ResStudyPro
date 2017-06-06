package com.douban.book.reader.view.store.card;

import android.content.Context;
import com.douban.book.reader.entity.WorksKind;
import com.douban.book.reader.entity.store.KindListStoreWidgetEntity;
import com.douban.book.reader.manager.WorksKindManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class KindListWidgetCard_ extends KindListWidgetCard implements HasViews {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public KindListWidgetCard_(Context context) {
        super(context);
        init_();
    }

    public static KindListWidgetCard build(Context context) {
        KindListWidgetCard_ instance = new KindListWidgetCard_(context);
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
        this.mWorksKindManager = WorksKindManager_.getInstance_(getContext());
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    void updateViews(final KindListStoreWidgetEntity entity, final WorksKind worksRootKind) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateViews(entity, worksRootKind);
            }
        }, 0);
    }

    void loadRootKind(KindListStoreWidgetEntity entity) {
        final KindListStoreWidgetEntity kindListStoreWidgetEntity = entity;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadRootKind(kindListStoreWidgetEntity);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
