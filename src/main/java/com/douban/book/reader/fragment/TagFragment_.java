package com.douban.book.reader.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.douban.book.reader.entity.Tag;
import java.util.List;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.builder.FragmentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class TagFragment_ extends TagFragment implements HasViews, OnViewChangedListener {
    public static final String TAG_TITLE_ARG = "tagTitle";
    private View contentView_;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public static class FragmentBuilder_ extends FragmentBuilder<FragmentBuilder_, TagFragment> {
        public TagFragment build() {
            TagFragment_ fragment_ = new TagFragment_();
            fragment_.setArguments(this.args);
            return fragment_;
        }

        public FragmentBuilder_ tagTitle(String tagTitle) {
            this.args.putString(TagFragment_.TAG_TITLE_ARG, tagTitle);
            return this;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public View findViewById(int id) {
        if (this.contentView_ == null) {
            return null;
        }
        return this.contentView_.findViewById(id);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        return this.contentView_;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.contentView_ = null;
    }

    private void init_(Bundle savedInstanceState) {
        injectFragmentArguments_();
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static FragmentBuilder_ builder() {
        return new FragmentBuilder_();
    }

    private void injectFragmentArguments_() {
        Bundle args_ = getArguments();
        if (args_ != null && args_.containsKey(TAG_TITLE_ARG)) {
            this.tagTitle = args_.getString(TAG_TITLE_ARG);
        }
    }

    public void onViewChanged(HasViews hasViews) {
        init();
    }

    void updateTagView(final List<Tag> tags) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateTagView(tags);
            }
        }, 0);
    }

    void refreshTag() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.refreshTag();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
