package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.douban.book.reader.R;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class VoteView_ extends VoteView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public VoteView_(Context context) {
        super(context);
        init_();
    }

    public VoteView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public VoteView_(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init_();
    }

    public static VoteView build(Context context) {
        VoteView_ instance = new VoteView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_vote, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static VoteView build(Context context, AttributeSet attrs) {
        VoteView_ instance = new VoteView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static VoteView build(Context context, AttributeSet attrs, int defStyleAttr) {
        VoteView_ instance = new VoteView_(context, attrs, defStyleAttr);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mBtnVoteUp = (TextView) hasViews.findViewById(R.id.vote_up);
        this.mBtnVoteDown = (TextView) hasViews.findViewById(R.id.vote_down);
        if (this.mBtnVoteUp != null) {
            this.mBtnVoteUp.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    VoteView_.this.onVoteClicked(view);
                }
            });
        }
        if (this.mBtnVoteDown != null) {
            this.mBtnVoteDown.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    VoteView_.this.onVoteClicked(view);
                }
            });
        }
        init();
    }

    void disableVoteButtons() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.disableVoteButtons();
            }
        }, 0);
    }

    void enableVoteButtons() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.enableVoteButtons();
            }
        }, 0);
    }

    void performVote(boolean isVoteUp) {
        final boolean z = isVoteUp;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.performVote(z);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
