package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.content.page.Range;
import com.douban.book.reader.manager.WorksManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ShareSelectionInfoView_ extends ShareSelectionInfoView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public ShareSelectionInfoView_(Context context) {
        super(context);
        init_();
    }

    public ShareSelectionInfoView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public ShareSelectionInfoView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static ShareSelectionInfoView build(Context context) {
        ShareSelectionInfoView_ instance = new ShareSelectionInfoView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_share_selection_info, this);
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

    public static ShareSelectionInfoView build(Context context, AttributeSet attrs) {
        ShareSelectionInfoView_ instance = new ShareSelectionInfoView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static ShareSelectionInfoView build(Context context, AttributeSet attrs, int defStyle) {
        ShareSelectionInfoView_ instance = new ShareSelectionInfoView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mImageIllus = (ImageView) hasViews.findViewById(R.id.image_illus);
        this.mSelection = (ParagraphView) hasViews.findViewById(R.id.selection);
        this.mTitle = (TextView) hasViews.findViewById(R.id.title);
    }

    void setSelection(final CharSequence selection) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.setSelection(selection);
            }
        }, 0);
    }

    void setIllusUrl(final String url) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.setIllusUrl(url);
            }
        }, 0);
    }

    void loadData(int worksId, Range range) {
        final int i = worksId;
        final Range range2 = range;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadData(i, range2);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
