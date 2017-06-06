package com.douban.book.reader.view.card;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import com.douban.book.reader.R;
import com.douban.book.reader.location.TocItem;
import java.util.List;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ColumnTocCard_ extends ColumnTocCard implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public ColumnTocCard_(Context context) {
        super(context);
        init_();
    }

    public static ColumnTocCard build(Context context) {
        ColumnTocCard_ instance = new ColumnTocCard_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public void onViewChanged(HasViews hasViews) {
        View view_btn_bottom = hasViews.findViewById(R.id.btn_bottom);
        if (view_btn_bottom != null) {
            view_btn_bottom.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ColumnTocCard_.this.onBottomButtonClicked();
                }
            });
        }
        init();
    }

    void updateContent(final List<TocItem> tocItemList) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateContent(tocItemList);
            }
        }, 0);
    }

    void onLoadFailed() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.onLoadFailed();
            }
        }, 0);
    }

    void loadData() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadData();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
