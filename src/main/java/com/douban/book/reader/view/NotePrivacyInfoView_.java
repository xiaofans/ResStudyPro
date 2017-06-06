package com.douban.book.reader.view;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.lib.view.LockView;
import com.douban.book.reader.manager.AnnotationManager_;
import com.douban.book.reader.manager.UserManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class NotePrivacyInfoView_ extends NotePrivacyInfoView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public NotePrivacyInfoView_(Context context) {
        super(context);
        init_();
    }

    public NotePrivacyInfoView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public NotePrivacyInfoView_(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init_();
    }

    public static NotePrivacyInfoView build(Context context) {
        NotePrivacyInfoView_ instance = new NotePrivacyInfoView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_note_privacy_info, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        this.mAnnotationManager = AnnotationManager_.getInstance_(getContext());
        this.mUserManager = UserManager_.getInstance_(getContext());
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static NotePrivacyInfoView build(Context context, AttributeSet attrs) {
        NotePrivacyInfoView_ instance = new NotePrivacyInfoView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static NotePrivacyInfoView build(Context context, AttributeSet attrs, int defStyleAttr) {
        NotePrivacyInfoView_ instance = new NotePrivacyInfoView_(context, attrs, defStyleAttr);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mPrivacyInfo = (TextView) hasViews.findViewById(R.id.text_privacy_info);
        this.mShareTarget = hasViews.findViewById(R.id.share_target);
        this.mLockView = (LockView) hasViews.findViewById(R.id.btn_privacy);
        if (this.mLockView != null) {
            this.mLockView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    NotePrivacyInfoView_.this.onLockClicked();
                }
            });
            this.mLockView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    NotePrivacyInfoView_.this.onLockChanged(isChecked);
                }
            });
        }
        init();
    }

    void updateView() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateView();
            }
        }, 0);
    }

    void setLockEnabled(final boolean enabled) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.setLockEnabled(enabled);
            }
        }, 0);
    }

    void updatePrivateInfoText(@StringRes final int resId) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updatePrivateInfoText(resId);
            }
        }, 0);
    }

    void updatePrivacyToServer() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.updatePrivacyToServer();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
