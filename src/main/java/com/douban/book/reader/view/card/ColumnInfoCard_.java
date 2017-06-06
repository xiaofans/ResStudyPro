package com.douban.book.reader.view.card;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.entity.Works;
import com.douban.book.reader.manager.WorksManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ColumnInfoCard_ extends ColumnInfoCard implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public ColumnInfoCard_(Context context) {
        super(context);
        init_();
    }

    public static ColumnInfoCard build(Context context) {
        ColumnInfoCard_ instance = new ColumnInfoCard_(context);
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
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        this.mWorksManager = WorksManager_.getInstance_(getContext());
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public void onViewChanged(HasViews hasViews) {
        this.mColumnPlanInfo = (TextView) hasViews.findViewById(R.id.column_info);
        this.mBtnSubscribe = (TextView) hasViews.findViewById(R.id.btn_subscribe);
        this.mProgressBar = (ProgressBar) hasViews.findViewById(R.id.progress_bar);
        if (this.mBtnSubscribe != null) {
            this.mBtnSubscribe.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ColumnInfoCard_.this.onBtnSubscribeClicked();
                }
            });
        }
    }

    void onWorksSubscribedOpStart() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.onWorksSubscribedOpStart();
            }
        }, 0);
    }

    void onWorksSubscribedOpFinish() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.onWorksSubscribedOpFinish();
            }
        }, 0);
    }

    void updateViews(final Works works) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateViews(works);
            }
        }, 0);
    }

    void toggleSubscribeStatus() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.toggleSubscribeStatus();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    void loadData(int worksId) {
        final int i = worksId;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadData(i);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
