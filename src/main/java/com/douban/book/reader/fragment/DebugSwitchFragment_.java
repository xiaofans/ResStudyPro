package com.douban.book.reader.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.douban.book.reader.manager.SessionManager_;
import com.douban.book.reader.manager.UserManager_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.builder.FragmentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class DebugSwitchFragment_ extends DebugSwitchFragment implements HasViews {
    private View contentView_;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public static class FragmentBuilder_ extends FragmentBuilder<FragmentBuilder_, DebugSwitchFragment> {
        public DebugSwitchFragment build() {
            DebugSwitchFragment_ fragment_ = new DebugSwitchFragment_();
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
        return this.contentView_;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.contentView_ = null;
    }

    private void init_(Bundle savedInstanceState) {
        this.mUserManager = UserManager_.getInstance_(getActivity());
        this.mSessionManager = SessionManager_.getInstance_(getActivity());
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static FragmentBuilder_ builder() {
        return new FragmentBuilder_();
    }

    void updateUserInfoPref(final Preference preference) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateUserInfoPref(preference);
            }
        }, 0);
    }

    void refreshLogin(Preference userInfoPref) {
        final Preference preference = userInfoPref;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.refreshLogin(preference);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
