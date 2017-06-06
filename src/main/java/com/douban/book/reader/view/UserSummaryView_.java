package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.entity.WorksAgent;
import com.douban.book.reader.lib.view.BadgeTextView;
import com.douban.book.reader.manager.FeedManager_;
import com.douban.book.reader.manager.NotificationManager_;
import com.douban.book.reader.manager.UserManager_;
import com.douban.book.reader.manager.WorksAgentManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class UserSummaryView_ extends UserSummaryView implements HasViews, OnViewChangedListener {
    private boolean alreadyInflated_ = false;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public UserSummaryView_(Context context) {
        super(context);
        init_();
    }

    public UserSummaryView_(Context context, AttributeSet attrs) {
        super(context, attrs);
        init_();
    }

    public UserSummaryView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init_();
    }

    public static UserSummaryView build(Context context) {
        UserSummaryView_ instance = new UserSummaryView_(context);
        instance.onFinishInflate();
        return instance;
    }

    public void onFinishInflate() {
        if (!this.alreadyInflated_) {
            this.alreadyInflated_ = true;
            inflate(getContext(), R.layout.view_user_summary, this);
            this.onViewChangedNotifier_.notifyViewChanged(this);
        }
        super.onFinishInflate();
    }

    private void init_() {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        this.mUserManager = UserManager_.getInstance_(getContext());
        this.mFeedManager = FeedManager_.getInstance_(getContext());
        this.mNotificationManager = NotificationManager_.getInstance_(getContext());
        this.mWorksAgentManager = WorksAgentManager_.getInstance_(getContext());
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public static UserSummaryView build(Context context, AttributeSet attrs) {
        UserSummaryView_ instance = new UserSummaryView_(context, attrs);
        instance.onFinishInflate();
        return instance;
    }

    public static UserSummaryView build(Context context, AttributeSet attrs, int defStyle) {
        UserSummaryView_ instance = new UserSummaryView_(context, attrs, defStyle);
        instance.onFinishInflate();
        return instance;
    }

    public void onViewChanged(HasViews hasViews) {
        this.mUserAvatar = (UserAvatarView) hasViews.findViewById(R.id.user_avatar);
        this.mUserName = (TextView) hasViews.findViewById(R.id.user_name);
        this.mBtnLogin = (TextView) hasViews.findViewById(R.id.btn_login);
        this.mFeedsNumber = (TextView) hasViews.findViewById(R.id.feeds_number);
        this.mOwnNumber = (TextView) hasViews.findViewById(R.id.own_number);
        this.mComposedNumber = (TextView) hasViews.findViewById(R.id.composed_number);
        this.mAuthorBlock = hasViews.findViewById(R.id.author_block);
        this.mBtnNotification = (BadgeTextView) hasViews.findViewById(R.id.btn_notification);
        this.mBtnGift = (TextView) hasViews.findViewById(R.id.btn_gift);
        this.mBtnAccount = (TextView) hasViews.findViewById(R.id.btn_account);
        View view_btn_feeds = hasViews.findViewById(R.id.btn_feeds);
        View view_btn_composed = hasViews.findViewById(R.id.btn_composed);
        View view_btn_own = hasViews.findViewById(R.id.btn_own);
        if (this.mUserAvatar != null) {
            this.mUserAvatar.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    UserSummaryView_.this.onUserNameClicked();
                }
            });
        }
        if (this.mUserName != null) {
            this.mUserName.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    UserSummaryView_.this.onUserNameClicked();
                }
            });
        }
        if (this.mBtnLogin != null) {
            this.mBtnLogin.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    UserSummaryView_.this.onUserNameClicked();
                }
            });
        }
        if (view_btn_feeds != null) {
            view_btn_feeds.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    UserSummaryView_.this.onBtnFeedsClicked();
                }
            });
        }
        if (this.mBtnNotification != null) {
            this.mBtnNotification.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    UserSummaryView_.this.onBtnNotificationClicked();
                }
            });
        }
        if (this.mBtnGift != null) {
            this.mBtnGift.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    UserSummaryView_.this.onBtnGiftClicked();
                }
            });
        }
        if (view_btn_composed != null) {
            view_btn_composed.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    UserSummaryView_.this.onBtnComposedClicked();
                }
            });
        }
        if (view_btn_own != null) {
            view_btn_own.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    UserSummaryView_.this.onBtnOwn();
                }
            });
        }
        if (this.mBtnAccount != null) {
            this.mBtnAccount.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    UserSummaryView_.this.onBtnAmountLeftClicked();
                }
            });
        }
        init();
    }

    void refreshUserAgentView(final WorksAgent worksAgent) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.refreshUserAgentView(worksAgent);
            }
        }, 0);
    }

    void hideAuthorBlock() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.hideAuthorBlock();
            }
        }, 0);
    }

    void updateNotificationView(final boolean hasUnread) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateNotificationView(hasUnread);
            }
        }, 0);
    }

    void updateUnReadView(final TextView textView, final int num) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateUnReadView(textView, num);
            }
        }, 0);
    }

    void refreshUserAgent() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.refreshUserAgent();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    void refreshNotificationStatus() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.refreshNotificationStatus();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    void reloadFeedsNumber() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.reloadFeedsNumber();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
