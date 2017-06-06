package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import com.douban.book.reader.R;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ChartWorksItemView_ extends ChartWorksItemView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public ChartWorksItemView_(Context context) {
        super(context);
        init_();
    }

    public ChartWorksItemView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public ChartWorksItemView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static ChartWorksItemView build(Context context) {
        ChartWorksItemView_ instance = new ChartWorksItemView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_chart_works_item_view, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static ChartWorksItemView build(Context context, AttributeSet attrs) {
        ChartWorksItemView_ instance = new ChartWorksItemView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static ChartWorksItemView build(Context context, AttributeSet attrs, int defStyle) {
        ChartWorksItemView_ instance = new ChartWorksItemView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mRankNum = (RoundTipView) hasViews.findViewById(R.id.rank_num);
        this.mWorksView = (CoverLeftWorksView_) hasViews.findViewById(R.id.works_view);
        init();
    }
}
