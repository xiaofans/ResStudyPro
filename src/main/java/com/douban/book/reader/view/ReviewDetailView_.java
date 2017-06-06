package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import com.douban.book.reader.R;
import com.douban.book.reader.entity.Review;
import com.douban.book.reader.manager.ReviewManager_;
import com.douban.book.reader.manager.WorksManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ReviewDetailView_ extends ReviewDetailView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public ReviewDetailView_(Context context) {
        super(context);
        init_();
    }

    public ReviewDetailView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public ReviewDetailView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static ReviewDetailView build(Context context) {
        ReviewDetailView_ instance = new ReviewDetailView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_review_detail, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        this.mReviewManager = ReviewManager_.getInstance_(getContext());
        this.mWorksManager = WorksManager_.getInstance_(getContext());
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static ReviewDetailView build(Context context, AttributeSet attrs) {
        ReviewDetailView_ instance = new ReviewDetailView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static ReviewDetailView build(Context context, AttributeSet attrs, int defStyle) {
        ReviewDetailView_ instance = new ReviewDetailView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mReviewSummary = (ReviewSummaryView) hasViews.findViewById(R.id.review_summary);
        this.mDividerAboveVote = hasViews.findViewById(R.id.divider_above_vote);
        this.mVoteView = (VoteView) hasViews.findViewById(R.id.vote_view);
        init();
    }

    public void refreshViews(final Review review) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.refreshViews(review);
            }
        }, 0);
    }

    void loadReview() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadReview();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
