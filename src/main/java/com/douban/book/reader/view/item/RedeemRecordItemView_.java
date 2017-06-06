package com.douban.book.reader.view.item;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.view.WorksCoverView;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class RedeemRecordItemView_ extends RedeemRecordItemView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public RedeemRecordItemView_(Context context) {
        super(context);
        init_();
    }

    public RedeemRecordItemView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public RedeemRecordItemView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static RedeemRecordItemView build(Context context) {
        RedeemRecordItemView_ instance = new RedeemRecordItemView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_general_record_item, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static RedeemRecordItemView build(Context context, AttributeSet attrs) {
        RedeemRecordItemView_ instance = new RedeemRecordItemView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static RedeemRecordItemView build(Context context, AttributeSet attrs, int defStyle) {
        RedeemRecordItemView_ instance = new RedeemRecordItemView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mCover = (WorksCoverView) hasViews.findViewById(R.id.cover);
        this.mWorksTitle = (TextView) hasViews.findViewById(R.id.works_title);
        this.mTime = (TextView) hasViews.findViewById(R.id.time);
        this.mCount = (TextView) hasViews.findViewById(R.id.count);
        init();
    }
}
