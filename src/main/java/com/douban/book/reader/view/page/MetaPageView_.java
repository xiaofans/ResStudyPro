package com.douban.book.reader.view.page;

import android.content.Context;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.entity.Manifest;
import com.douban.book.reader.entity.Works;
import com.douban.book.reader.manager.WorksManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class MetaPageView_ extends MetaPageView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public MetaPageView_(Context context) {
        super(context);
        init_();
    }

    public static MetaPageView build(Context context) {
        MetaPageView_ instance = new MetaPageView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_page_meta, this);
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
        this.mTitle = (TextView) hasViews.findViewById(R.id.title);
        this.mSubTitle = (TextView) hasViews.findViewById(R.id.subtitle);
        this.mAuthor = (TextView) hasViews.findViewById(R.id.author);
        this.mTranslator = (TextView) hasViews.findViewById(R.id.translator);
        this.mPublisher = (TextView) hasViews.findViewById(R.id.publisher);
        this.mPublishTime = (TextView) hasViews.findViewById(R.id.publish_time);
        this.mOnSaleTime = (TextView) hasViews.findViewById(R.id.on_sale_time);
        this.mISBN = (TextView) hasViews.findViewById(R.id.isbn);
        this.mWorksAcknowledgements = (TextView) hasViews.findViewById(R.id.works_acknowledgements);
    }

    void setWorks(final Works works, final Manifest manifest) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.setWorks(works, manifest);
            }
        }, 0);
    }

    void loadData(int worksId) {
        final int i = worksId;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadData(i);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
