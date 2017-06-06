package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.douban.book.reader.R;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ShareGiftPackInfoView_ extends ShareGiftPackInfoView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public ShareGiftPackInfoView_(Context context) {
        super(context);
        init_();
    }

    public ShareGiftPackInfoView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public ShareGiftPackInfoView_(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init_();
    }

    public static ShareGiftPackInfoView build(Context context) {
        ShareGiftPackInfoView_ instance = new ShareGiftPackInfoView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_share_gift_pack_info, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static ShareGiftPackInfoView build(Context context, AttributeSet attrs) {
        ShareGiftPackInfoView_ instance = new ShareGiftPackInfoView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static ShareGiftPackInfoView build(Context context, AttributeSet attrs, int defStyleAttr) {
        ShareGiftPackInfoView_ instance = new ShareGiftPackInfoView_(context, attrs, defStyleAttr);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mGiftBox = (BoxedWorksView) hasViews.findViewById(R.id.gift_box);
        this.mTitle = (TextView) hasViews.findViewById(R.id.title);
        this.mMessage = (TextView) hasViews.findViewById(R.id.message);
        init();
    }
}
