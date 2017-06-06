package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.manager.GiftPackManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class GiftPackDetailBottomView_ extends GiftPackDetailBottomView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public GiftPackDetailBottomView_(Context context) {
        super(context);
        init_();
    }

    public GiftPackDetailBottomView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public GiftPackDetailBottomView_(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init_();
    }

    public static GiftPackDetailBottomView build(Context context) {
        GiftPackDetailBottomView_ instance = new GiftPackDetailBottomView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_gift_pack_detail_bottom_bar, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        this.mGiftPackManager = GiftPackManager_.getInstance_(getContext());
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static GiftPackDetailBottomView build(Context context, AttributeSet attrs) {
        GiftPackDetailBottomView_ instance = new GiftPackDetailBottomView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static GiftPackDetailBottomView build(Context context, AttributeSet attrs, int defStyleAttr) {
        GiftPackDetailBottomView_ instance = new GiftPackDetailBottomView_(context, attrs, defStyleAttr);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mTipsView = (TextView) hasViews.findViewById(R.id.tips);
        this.mBtnAction = (TextView) hasViews.findViewById(R.id.btn_action);
        if (this.mBtnAction != null) {
            this.mBtnAction.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    GiftPackDetailBottomView_.this.onBtnActionClicked();
                }
            });
        }
        init();
    }

    void updateViews() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateViews();
            }
        }, 0);
    }

    void loadData(int packId) {
        final int i = packId;
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

    void loadDataByHashCode(String hashCode) {
        final String str = hashCode;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadDataByHashCode(str);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    void receiveGift() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.receiveGift();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
