package com.douban.book.reader.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.entity.Tag;
import com.douban.book.reader.lib.view.FlowLayout;
import com.douban.book.reader.view.BoxedWorksView;
import java.util.List;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.builder.FragmentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class TestFieldFragment_ extends TestFieldFragment implements HasViews, OnViewChangedListener {
    private View contentView_;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public static class FragmentBuilder_ extends FragmentBuilder<FragmentBuilder_, TestFieldFragment> {
        public TestFieldFragment build() {
            TestFieldFragment_ fragment_ = new TestFieldFragment_();
            fragment_.setArguments(this.args);
            return fragment_;
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
        if (this.contentView_ == null) {
            this.contentView_ = inflater.inflate(R.layout.frag_test_field, container, false);
        }
        return this.contentView_;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.contentView_ = null;
        this.mIconFontShowCase = null;
        this.mIconFontShowCaseLarge = null;
        this.mTagsLayout = null;
        this.mBoxedWorksView = null;
        this.mBoxedWorksView2 = null;
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static FragmentBuilder_ builder() {
        return new FragmentBuilder_();
    }

    public void onViewChanged(HasViews hasViews) {
        this.mIconFontShowCase = (TextView) hasViews.findViewById(R.id.icon_font_showcase);
        this.mIconFontShowCaseLarge = (TextView) hasViews.findViewById(R.id.icon_font_showcase_large);
        this.mTagsLayout = (FlowLayout) hasViews.findViewById(R.id.tags_layout);
        this.mBoxedWorksView = (BoxedWorksView) hasViews.findViewById(R.id.boxed_works);
        this.mBoxedWorksView2 = (BoxedWorksView) hasViews.findViewById(R.id.boxed_works_2);
        init();
    }

    void updateTagsView(final List<Tag> list) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateTagsView(list);
            }
        }, 0);
    }
}
