package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.douban.book.reader.R;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class WorksUGCView_ extends WorksUGCView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public WorksUGCView_(Context context) {
        super(context);
        init_();
    }

    public WorksUGCView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public WorksUGCView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static WorksUGCView build(Context context) {
        WorksUGCView_ instance = new WorksUGCView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_works_ugc, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static WorksUGCView build(Context context, AttributeSet attrs) {
        WorksUGCView_ instance = new WorksUGCView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static WorksUGCView build(Context context, AttributeSet attrs, int defStyle) {
        WorksUGCView_ instance = new WorksUGCView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mNoteLayout = (LinearLayout) hasViews.findViewById(R.id.note_layout);
        this.mNoteCount = (TextView) hasViews.findViewById(R.id.note_count);
        this.mRatingBar = (RatingBar) hasViews.findViewById(R.id.works_rating);
        this.mRateLayout = (LinearLayout) hasViews.findViewById(R.id.rate_layout);
        this.mWorksStatusView = (WorksStatusView) hasViews.findViewById(R.id.works_status);
        if (this.mNoteLayout != null) {
            this.mNoteLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    WorksUGCView_.this.onNoteClicked();
                }
            });
        }
        if (this.mRateLayout != null) {
            this.mRateLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    WorksUGCView_.this.onReviewClicked();
                }
            });
        }
        init();
    }

    void refreshViews() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.refreshViews();
            }
        }, 0);
    }

    void loadWorks() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadWorks();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
