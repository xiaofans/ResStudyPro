package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import com.douban.book.reader.R;
import com.douban.book.reader.entity.Works;
import com.douban.book.reader.manager.WorksManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class MyReviewView_ extends MyReviewView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public MyReviewView_(Context context) {
        super(context);
        init_();
    }

    public MyReviewView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public MyReviewView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static MyReviewView build(Context context) {
        MyReviewView_ instance = new MyReviewView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_my_review, this);
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

    public static MyReviewView build(Context context, AttributeSet attrs) {
        MyReviewView_ instance = new MyReviewView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static MyReviewView build(Context context, AttributeSet attrs, int defStyle) {
        MyReviewView_ instance = new MyReviewView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mReviewItemView = (ReviewItemView) hasViews.findViewById(R.id.review_item);
        this.mProgressBar = (ProgressBar) hasViews.findViewById(R.id.progress);
        this.mRatingBar = (RatingBar) hasViews.findViewById(R.id.rating_bar);
        this.mReviewLayout = hasViews.findViewById(R.id.review_layout);
        this.mRootView = hasViews.findViewById(R.id.root);
        if (this.mReviewItemView != null) {
            this.mReviewItemView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MyReviewView_.this.onReviewItemClicked();
                }
            });
        }
        init();
    }

    public void startLoading() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.startLoading();
            }
        }, 0);
    }

    public void finishLoading() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.finishLoading();
            }
        }, 0);
    }

    public void updateViews(final Works works) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateViews(works);
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
