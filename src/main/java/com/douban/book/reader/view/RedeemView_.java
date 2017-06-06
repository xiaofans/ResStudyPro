package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import com.douban.book.reader.R;
import com.douban.book.reader.entity.RedeemRecord;
import com.douban.book.reader.manager.UserManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class RedeemView_ extends RedeemView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public RedeemView_(Context context) {
        super(context);
        init_();
    }

    public RedeemView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public RedeemView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static RedeemView build(Context context) {
        RedeemView_ instance = new RedeemView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_redeem, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        this.mUserManager = UserManager_.getInstance_(getContext());
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static RedeemView build(Context context, AttributeSet attrs) {
        RedeemView_ instance = new RedeemView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static RedeemView build(Context context, AttributeSet attrs, int defStyle) {
        RedeemView_ instance = new RedeemView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mBtnConfirm = (Button) hasViews.findViewById(R.id.btn_confirm);
        this.mInput = (EditText) hasViews.findViewById(R.id.input);
        init();
    }

    void onRedeemStarted() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.onRedeemStarted();
            }
        }, 0);
    }

    void onRedeemSucceed(final RedeemRecord redeemRecord) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.onRedeemSucceed(redeemRecord);
            }
        }, 0);
    }

    void onRedeemFailed(final Exception e) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.onRedeemFailed(e);
            }
        }, 0);
    }

    void exchange(String code) {
        final String str = code;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.exchange(str);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
