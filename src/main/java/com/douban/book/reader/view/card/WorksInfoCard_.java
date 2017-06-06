package com.douban.book.reader.view.card;

import android.content.Context;
import android.widget.RatingBar;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.entity.Works;
import com.douban.book.reader.manager.WorksManager_;
import com.douban.book.reader.view.ParagraphView;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class WorksInfoCard_ extends WorksInfoCard implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public WorksInfoCard_(Context context) {
        super(context);
        init_();
    }

    public static WorksInfoCard build(Context context) {
        WorksInfoCard_ instance = new WorksInfoCard_(context);
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
        this.mWorksManager = WorksManager_.getInstance_(getContext());
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public void onViewChanged(HasViews hasViews) {
        this.mTitle = (TextView) hasViews.findViewById(R.id.works_title);
        this.mSubTitle = (TextView) hasViews.findViewById(R.id.works_sub_title);
        this.mWorksBasicInfo = (TextView) hasViews.findViewById(R.id.works_info);
        this.mAbstract = (ParagraphView) hasViews.findViewById(R.id.abstract_text);
        this.mRatingBar = (RatingBar) hasViews.findViewById(R.id.rating_bar);
        this.mRatingInfo = (TextView) hasViews.findViewById(R.id.rating_info);
    }

    void updateViews(final Works works) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateViews(works);
            }
        }, 0);
    }

    void loadWorks(int worksId) {
        final int i = worksId;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadWorks(i);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
