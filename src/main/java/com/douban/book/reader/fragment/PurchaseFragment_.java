package com.douban.book.reader.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.douban.book.reader.manager.GiftPackManager_;
import com.douban.book.reader.manager.WorksManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.builder.FragmentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class PurchaseFragment_ extends PurchaseFragment implements HasViews, OnViewChangedListener {
    public static final String CHAPTER_ID_ARG = "chapterId";
    public static final String GIFT_PACK_ID_ARG = "giftPackId";
    public static final String PROMPT_DOWNLOAD_ARG = "promptDownload";
    public static final String URI_ARG = "uri";
    public static final String WORKS_ID_ARG = "worksId";
    private View contentView_;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public static class FragmentBuilder_ extends FragmentBuilder<FragmentBuilder_, PurchaseFragment> {
        public PurchaseFragment build() {
            PurchaseFragment_ fragment_ = new PurchaseFragment_();
            fragment_.setArguments(this.args);
            return fragment_;
        }

        public FragmentBuilder_ uri(Uri uri) {
            this.args.putParcelable("uri", uri);
            return this;
        }

        public FragmentBuilder_ worksId(int worksId) {
            this.args.putInt("worksId", worksId);
            return this;
        }

        public FragmentBuilder_ chapterId(int chapterId) {
            this.args.putInt("chapterId", chapterId);
            return this;
        }

        public FragmentBuilder_ giftPackId(int giftPackId) {
            this.args.putInt(PurchaseFragment_.GIFT_PACK_ID_ARG, giftPackId);
            return this;
        }

        public FragmentBuilder_ promptDownload(boolean promptDownload) {
            this.args.putBoolean(PurchaseFragment_.PROMPT_DOWNLOAD_ARG, promptDownload);
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
        this.mWorksManager = WorksManager_.getInstance_(getActivity());
        this.mGiftPackManager = GiftPackManager_.getInstance_(getActivity());
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
        if (args_ != null) {
            if (args_.containsKey("uri")) {
                this.uri = (Uri) args_.getParcelable("uri");
            }
            if (args_.containsKey("worksId")) {
                this.worksId = args_.getInt("worksId");
            }
            if (args_.containsKey("chapterId")) {
                this.chapterId = args_.getInt("chapterId");
            }
            if (args_.containsKey(GIFT_PACK_ID_ARG)) {
                this.giftPackId = args_.getInt(GIFT_PACK_ID_ARG);
            }
            if (args_.containsKey(PROMPT_DOWNLOAD_ARG)) {
                this.promptDownload = args_.getBoolean(PROMPT_DOWNLOAD_ARG);
            }
        }
    }

    public void onViewChanged(HasViews hasViews) {
        init();
    }

    void updateViews() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateViews();
            }
        }, 0);
    }

    void onPurchaseSucceed() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.onPurchaseSucceed();
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

    void doPurchase(boolean secretly) {
        final boolean z = secretly;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.doPurchase(z);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    void refreshUserInfo() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.refreshUserInfo();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    void updateWorks() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.updateWorks();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    void updateGiftPack() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.updateGiftPack();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
