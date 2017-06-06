package com.douban.book.reader.view.page;

import android.content.Context;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.entity.Works;
import com.douban.book.reader.manager.WorksManager_;
import com.douban.book.reader.view.FlexibleScrollView;
import com.douban.book.reader.view.ParagraphView;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class GiftPageView_ extends GiftPageView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public GiftPageView_(Context context) {
        super(context);
        init_();
    }

    public static GiftPageView build(Context context) {
        GiftPageView_ instance = new GiftPageView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_page_gift, this);
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
        this.mGiveTime = (TextView) hasViews.findViewById(R.id.give_time);
        this.mGiftNote = (ParagraphView) hasViews.findViewById(R.id.gift_note);
        this.mGiver = (TextView) hasViews.findViewById(R.id.giver);
        this.mRecipient = (TextView) hasViews.findViewById(R.id.recipient);
        this.mGiftNoteContainer = (FlexibleScrollView) hasViews.findViewById(R.id.gift_note_container);
        init();
    }

    void updateView(final Works works) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateView(works);
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
