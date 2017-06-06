package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.entity.Annotation;
import com.douban.book.reader.manager.AnnotationManager_;
import java.util.UUID;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ShareNoteInfoView_ extends ShareNoteInfoView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public ShareNoteInfoView_(Context context) {
        super(context);
        init_();
    }

    public ShareNoteInfoView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public ShareNoteInfoView_(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init_();
    }

    public static ShareNoteInfoView build(Context context) {
        ShareNoteInfoView_ instance = new ShareNoteInfoView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_share_note_info, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        this.mAnnotationManager = AnnotationManager_.getInstance_(getContext());
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static ShareNoteInfoView build(Context context, AttributeSet attrs) {
        ShareNoteInfoView_ instance = new ShareNoteInfoView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static ShareNoteInfoView build(Context context, AttributeSet attrs, int defStyleAttr) {
        ShareNoteInfoView_ instance = new ShareNoteInfoView_(context, attrs, defStyleAttr);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mNoteContent = (TextView) hasViews.findViewById(R.id.note_content);
        init();
    }

    void updateViews(final Annotation annotation) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateViews(annotation);
            }
        }, 0);
    }

    void loadData(UUID uuid) {
        final UUID uuid2 = uuid;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadData(uuid2);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
