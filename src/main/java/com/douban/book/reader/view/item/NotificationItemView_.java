package com.douban.book.reader.view.item;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.douban.book.reader.R;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class NotificationItemView_ extends NotificationItemView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public NotificationItemView_(Context context) {
        super(context);
        init_();
    }

    public NotificationItemView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public NotificationItemView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static NotificationItemView build(Context context) {
        NotificationItemView_ instance = new NotificationItemView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_notification_item, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static NotificationItemView build(Context context, AttributeSet attrs) {
        NotificationItemView_ instance = new NotificationItemView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static NotificationItemView build(Context context, AttributeSet attrs, int defStyle) {
        NotificationItemView_ instance = new NotificationItemView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mContent = (TextView) hasViews.findViewById(R.id.content);
        this.mIconUnRead = (ImageView) hasViews.findViewById(R.id.ic_unread);
        init();
    }
}
