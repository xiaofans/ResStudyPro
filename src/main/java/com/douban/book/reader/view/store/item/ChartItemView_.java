package com.douban.book.reader.view.store.item;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;
import com.douban.book.reader.R;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ChartItemView_ extends ChartItemView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public ChartItemView_(Context context) {
        super(context);
        init_();
    }

    public ChartItemView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public ChartItemView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static ChartItemView build(Context context) {
        ChartItemView_ instance = new ChartItemView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_store_widget_chart_item, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static ChartItemView build(Context context, AttributeSet attrs) {
        ChartItemView_ instance = new ChartItemView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static ChartItemView build(Context context, AttributeSet attrs, int defStyle) {
        ChartItemView_ instance = new ChartItemView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mTopDivider = hasViews.findViewById(R.id.top_divider);
        this.mTitle = (TextView) hasViews.findViewById(R.id.title);
        this.mWorksView = (StoreWorksItemView) hasViews.findViewById(R.id.works);
        this.mBtnChart = (Button) hasViews.findViewById(R.id.btn_chart);
    }
}
