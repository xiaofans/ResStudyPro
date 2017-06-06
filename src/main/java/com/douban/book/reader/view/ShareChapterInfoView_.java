package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.douban.book.reader.R;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ShareChapterInfoView_ extends ShareChapterInfoView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public ShareChapterInfoView_(Context context) {
        super(context);
        init_();
    }

    public ShareChapterInfoView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public ShareChapterInfoView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static ShareChapterInfoView build(Context context) {
        ShareChapterInfoView_ instance = new ShareChapterInfoView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_share_chapter_info, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static ShareChapterInfoView build(Context context, AttributeSet attrs) {
        ShareChapterInfoView_ instance = new ShareChapterInfoView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static ShareChapterInfoView build(Context context, AttributeSet attrs, int defStyle) {
        ShareChapterInfoView_ instance = new ShareChapterInfoView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mTitle = (TextView) hasViews.findViewById(R.id.title);
        this.mAuthor = (TextView) hasViews.findViewById(R.id.author);
    }
}
