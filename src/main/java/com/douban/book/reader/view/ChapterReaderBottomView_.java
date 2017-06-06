package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.manager.WorksManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ChapterReaderBottomView_ extends ChapterReaderBottomView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public ChapterReaderBottomView_(Context context) {
        super(context);
        init_();
    }

    public ChapterReaderBottomView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public ChapterReaderBottomView_(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init_();
    }

    public static ChapterReaderBottomView build(Context context) {
        ChapterReaderBottomView_ instance = new ChapterReaderBottomView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_chapter_reader_bottom_bar, this);
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

    public static ChapterReaderBottomView build(Context context, AttributeSet attrs) {
        ChapterReaderBottomView_ instance = new ChapterReaderBottomView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static ChapterReaderBottomView build(Context context, AttributeSet attrs, int defStyleAttr) {
        ChapterReaderBottomView_ instance = new ChapterReaderBottomView_(context, attrs, defStyleAttr);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mCoverView = (WorksCoverView) hasViews.findViewById(R.id.cover);
        this.mTvAction = (TextView) hasViews.findViewById(R.id.btn_action);
        this.mTvShare = (TextView) hasViews.findViewById(R.id.btn_share);
        if (this.mCoverView != null) {
            this.mCoverView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ChapterReaderBottomView_.this.openProfile();
                }
            });
        }
        if (this.mTvAction != null) {
            this.mTvAction.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ChapterReaderBottomView_.this.openReader();
                }
            });
        }
        if (this.mTvShare != null) {
            this.mTvShare.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ChapterReaderBottomView_.this.startShare();
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

    void loadData() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadData();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
