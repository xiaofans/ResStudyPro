package com.douban.book.reader.view.page;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.manager.WorksManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class LastPageView_ extends LastPageView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public LastPageView_(Context context) {
        super(context);
        init_();
    }

    public static LastPageView build(Context context) {
        LastPageView_ instance = new LastPageView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_page_last, this);
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
        this.mTitle = (TextView) hasViews.findViewById(R.id.title);
        this.mColumnPlanInfo = (TextView) hasViews.findViewById(R.id.column_plan_info);
        this.mTextSubscriptionInfo = (TextView) hasViews.findViewById(R.id.text_subscription_info);
        this.mBtnSubscribe = (Button) hasViews.findViewById(R.id.btn_subscribe);
        this.mSubscribeProgressBar = (ProgressBar) hasViews.findViewById(R.id.subscribe_progress_bar);
        this.mTextPurchaseTip = (TextView) hasViews.findViewById(R.id.text_purchase_tip);
        this.mBtnPurchase = (Button) hasViews.findViewById(R.id.btn_purchase);
        this.mRatingLayout = hasViews.findViewById(R.id.rating_layout);
        this.mTextRatingTip = (TextView) hasViews.findViewById(R.id.text_rating_tip);
        this.mRatingBar = (RatingBar) hasViews.findViewById(R.id.rating_bar);
        this.mBtnDownload = (Button) hasViews.findViewById(R.id.btn_download);
        this.mDownloadInfo = (TextView) hasViews.findViewById(R.id.text_download_info);
        if (this.mBtnSubscribe != null) {
            this.mBtnSubscribe.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    LastPageView_.this.onBtnSubscribeClicked();
                }
            });
        }
        if (this.mBtnPurchase != null) {
            this.mBtnPurchase.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    LastPageView_.this.onBtnPurchaseClicked();
                }
            });
        }
        if (this.mRatingLayout != null) {
            this.mRatingLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    LastPageView_.this.onRatingLayoutClicked();
                }
            });
        }
        if (this.mBtnDownload != null) {
            this.mBtnDownload.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    LastPageView_.this.onDownloadClicked();
                }
            });
        }
        init();
    }

    void setIsSubscribing(final boolean isSubscribing) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.setIsSubscribing(isSubscribing);
            }
        }, 0);
    }

    void updateViews() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateViews();
            }
        }, 0);
    }

    void onBtnSubscribeClicked() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.onBtnSubscribeClicked();
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
