package com.douban.book.reader.view.item;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.douban.book.reader.R;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class DepositRecordItemView_ extends DepositRecordItemView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public DepositRecordItemView_(Context context) {
        super(context);
        init_();
    }

    public DepositRecordItemView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public DepositRecordItemView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static DepositRecordItemView build(Context context) {
        DepositRecordItemView_ instance = new DepositRecordItemView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_deposit_record_item, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static DepositRecordItemView build(Context context, AttributeSet attrs) {
        DepositRecordItemView_ instance = new DepositRecordItemView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static DepositRecordItemView build(Context context, AttributeSet attrs, int defStyle) {
        DepositRecordItemView_ instance = new DepositRecordItemView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mTime = (TextView) hasViews.findViewById(R.id.time);
        this.mType = (TextView) hasViews.findViewById(R.id.type);
        this.mCount = (TextView) hasViews.findViewById(R.id.count);
        init();
    }
}