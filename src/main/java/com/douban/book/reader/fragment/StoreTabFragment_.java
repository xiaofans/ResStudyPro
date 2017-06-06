package com.douban.book.reader.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.douban.book.reader.manager.StoreManager_;
import com.douban.book.reader.manager.exception.DataLoadException;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.builder.FragmentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class StoreTabFragment_ extends StoreTabFragment implements HasViews, OnViewChangedListener {
    public static final String TAB_NAME_ARG = "tabName";
    private View contentView_;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public static class FragmentBuilder_ extends FragmentBuilder<FragmentBuilder_, StoreTabFragment> {
        public StoreTabFragment build() {
            StoreTabFragment_ fragment_ = new StoreTabFragment_();
            fragment_.setArguments(this.args);
            return fragment_;
        }

        public FragmentBuilder_ tabName(String tabName) {
            this.args.putString(StoreTabFragment_.TAB_NAME_ARG, tabName);
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
        this.mStoreManager = StoreManager_.getInstance_(getActivity());
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
        if (args_ != null && args_.containsKey(TAB_NAME_ARG)) {
            this.tabName = args_.getString(TAB_NAME_ARG);
        }
    }

    public void onViewChanged(HasViews hasViews) {
        init();
    }

    void updateCards() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateCards();
            }
        }, 0);
    }

    void onLoadDataSucceed() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.onLoadDataSucceed();
            }
        }, 0);
    }

    void onLoadDataFailed(final DataLoadException e) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.onLoadDataFailed(e);
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
