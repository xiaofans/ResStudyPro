package com.douban.book.reader.view.item;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.view.WorksCoverView;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class FeedItemView_ extends FeedItemView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public FeedItemView_(Context context) {
        super(context);
        init_();
    }

    public FeedItemView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public FeedItemView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static FeedItemView build(Context context) {
        FeedItemView_ instance = new FeedItemView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_feed_item, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static FeedItemView build(Context context, AttributeSet attrs) {
        FeedItemView_ instance = new FeedItemView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static FeedItemView build(Context context, AttributeSet attrs, int defStyle) {
        FeedItemView_ instance = new FeedItemView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mPublishInfo = (TextView) hasViews.findViewById(R.id.publish_info);
        this.mTitle = (TextView) hasViews.findViewById(R.id.title);
        this.mCover = (WorksCoverView) hasViews.findViewById(R.id.cover);
        this.mContent = (TextView) hasViews.findViewById(R.id.content);
        init();
    }
}
