package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.entity.Review;
import com.douban.book.reader.entity.Works;
import com.douban.book.reader.manager.ReviewManager_;
import com.douban.book.reader.manager.WorksManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ReviewSummaryView_ extends ReviewSummaryView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public ReviewSummaryView_(Context context) {
        super(context);
        init_();
    }

    public ReviewSummaryView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public ReviewSummaryView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static ReviewSummaryView build(Context context) {
        ReviewSummaryView_ instance = new ReviewSummaryView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_review_summary, this);
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

    public static ReviewSummaryView build(Context context, AttributeSet attrs) {
        ReviewSummaryView_ instance = new ReviewSummaryView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static ReviewSummaryView build(Context context, AttributeSet attrs, int defStyle) {
        ReviewSummaryView_ instance = new ReviewSummaryView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mCreatorAvatar = (UserAvatarView) hasViews.findViewById(R.id.creator_avatar);
        this.mCreator = (TextView) hasViews.findViewById(R.id.creator);
        this.mCreatedDate = (TextView) hasViews.findViewById(R.id.created_date);
        this.mBtnWorksProfile = (TextView) hasViews.findViewById(R.id.btn_works_profile);
        this.mRateBar = (RatingBar) hasViews.findViewById(R.id.rate);
        this.mContent = (ParagraphView) hasViews.findViewById(R.id.content);
        this.mProgressBar = (ProgressBar) hasViews.findViewById(R.id.progress);
    }

    public void setProgressBarVisible(final boolean visible) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.setProgressBarVisible(visible);
            }
        }, 0);
    }

    public void updateWorksViews(final Works works) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateWorksViews(works);
            }
        }, 0);
    }

    public void updateViews(final Review review) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateViews(review);
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
