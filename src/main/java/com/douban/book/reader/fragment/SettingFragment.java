package com.douban.book.reader.fragment;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.support.v4.app.Fragment;
import android.view.View;
import com.douban.book.reader.R;
import com.douban.book.reader.activity.QQAuthActivity;
import com.douban.book.reader.activity.WeiboAuthActivity;
import com.douban.book.reader.app.App;
import com.douban.book.reader.app.PageOpenHelper;
import com.douban.book.reader.constant.Key;
import com.douban.book.reader.event.ArkEvent;
import com.douban.book.reader.event.LoggedInEvent;
import com.douban.book.reader.event.LoggedOutEvent;
import com.douban.book.reader.event.WeiboUserNameUpdatedEvent;
import com.douban.book.reader.fragment.AlertDialogFragment.Builder;
import com.douban.book.reader.fragment.share.FeedbackEditFragment_;
import com.douban.book.reader.helper.LogoutHelper;
import com.douban.book.reader.manager.UserManager;
import com.douban.book.reader.util.Analysis;
import com.douban.book.reader.util.DebugSwitch;
import com.douban.book.reader.util.ImageLoaderUtils;
import com.douban.book.reader.util.Pref;
import com.douban.book.reader.util.PushManager;
import com.douban.book.reader.util.Res;
import com.douban.book.reader.util.StringUtils;
import com.douban.book.reader.util.ToastUtils;
import com.douban.book.reader.util.Utils;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

@EFragment
public class SettingFragment extends BasePreferenceFragment {
    @Bean
    UserManager mUserManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_pref);
        setTitle((int) R.string.settings);
        PreferenceCategory categorySnsShare = (PreferenceCategory) findPreference("setting_category_sns_share");
        Preference connectWeiboPreference = findPreference("weibo_bind");
        connectWeiboPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                SettingFragment.this.toggleWeiboBind(preference);
                return true;
            }
        });
        Preference connectQQPreference = findPreference("qq_bind");
        connectQQPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                SettingFragment.this.toggleQQBind(preference);
                return true;
            }
        });
        if (this.mUserManager.isWeiboUser()) {
            categorySnsShare.removePreference(connectWeiboPreference);
        }
        if (this.mUserManager.isQQUser() || !DebugSwitch.on(Key.APP_DEBUG_ENABLE_QQ_LOGIN_AND_BIND)) {
            categorySnsShare.removePreference(connectQQPreference);
        }
        if (categorySnsShare.getPreferenceCount() <= 0) {
            getPreferenceScreen().removePreference(categorySnsShare);
        }
        performAnalysis(Key.SETTING_PREVENET_READING_SCREENSAVE, "prevent_screen_save_while_reading");
        performAnalysis(Key.SETTING_PAGETURN_WITH_VOLUME, "page_turn_with_volume_key");
        findPreference(Key.SETTING_ENABLE_PUSH_SERVICE).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (Boolean.TRUE.equals(newValue)) {
                    PushManager.start();
                } else {
                    PushManager.stop();
                }
                Analysis.sendPrefChangedEvent("enable_push", newValue);
                return true;
            }
        });
        PreferenceCategory categoryOthers = (PreferenceCategory) findPreference("setting_category_other");
        Preference diagnosticsFeedbackPreference = findPreference("setting_send_diagnostics_feedback");
        diagnosticsFeedbackPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                FeedbackEditFragment_.builder().isReport(true).build().showAsActivity(SettingFragment.this);
                return true;
            }
        });
        if (!DebugSwitch.on(Key.APP_DEBUG_SEND_DIAGNOSTIC_REPORT)) {
            categoryOthers.removePreference(diagnosticsFeedbackPreference);
        }
        findPreference("setting_feedback").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                FeedbackEditFragment_.builder().build().showAsActivity(SettingFragment.this);
                return true;
            }
        });
        findPreference("setting_rating").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse("market://details?id=" + App.get().getPackageName()));
                    SettingFragment.this.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    try {
                        SettingFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://market.android.com/details?id=" + App.get().getPackageName())));
                    } catch (Exception e2) {
                        ToastUtils.showToast((int) R.string.toast_no_market_app);
                    }
                }
                return true;
            }
        });
        findPreference("setting_about").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                AboutFragment_.builder().build().showAsActivity(SettingFragment.this);
                return true;
            }
        });
        findPreference("setting_other_apps").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                AppSuggestFragment_.builder().build().setDrawerEnabled(false).showAsActivity(SettingFragment.this);
                return true;
            }
        });
        findPreference("setting_logout").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                new LogoutHelper(SettingFragment.this).logout();
                return true;
            }
        });
        updateAccountCategory();
        updateWeiboStatus(connectWeiboPreference);
        updateQQBindStatus(connectQQPreference);
    }

    private void performAnalysis(String prefKey, final String eventKey) {
        findPreference(prefKey).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Analysis.sendPrefChangedEvent(eventKey, newValue);
                return true;
            }
        });
    }

    private void updateAccountCategory() {
        PreferenceCategory categoryOther = (PreferenceCategory) findPreference("setting_category_other");
        Preference logoutPref = findPreference("setting_logout");
        if (this.mUserManager.isNormalUser()) {
            categoryOther.addPreference(logoutPref);
        } else {
            categoryOther.removePreference(logoutPref);
        }
    }

    private void toggleQQBind(Preference preference) {
        if (QQAuthActivity.isAuthenticated()) {
            unBindQQ(preference);
        } else {
            bindQQ();
        }
    }

    private void bindQQ() {
        QQAuthActivity.startAuth(PageOpenHelper.from((Fragment) this));
    }

    private void unBindQQ(final Preference preference) {
        new Builder().setMessage((int) R.string.dialog_message_unbind_qq_confirm).setPositiveButton((int) R.string.dialog_button_ok, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                QQAuthActivity.clearToken();
                SettingFragment.this.updateQQBindStatus(preference);
                ToastUtils.showToast((int) R.string.toast_unbind_qq_success);
            }
        }).setNegativeButton((int) R.string.dialog_button_cancel, null).create().show();
    }

    private void toggleWeiboBind(Preference preference) {
        if (WeiboAuthActivity.isAuthenticated()) {
            unbindWeibo(preference);
        } else {
            bindWeibo(preference);
        }
    }

    public void onEventMainThread(ArkEvent event) {
        switch (event) {
            case WEIBO_AUTHENTICATED:
                updateWeiboStatus(findPreference("weibo_bind"));
                ToastUtils.showToast((int) R.string.toast_share_bind_sina_success);
                return;
            case QQ_AUTHENTICATED:
                updateQQBindStatus(findPreference("qq_bind"));
                ToastUtils.showToast((int) R.string.toast_bind_qq_success);
                return;
            default:
                return;
        }
    }

    public void onEventMainThread(WeiboUserNameUpdatedEvent event) {
        updateWeiboStatus(findPreference("weibo_bind"));
    }

    public void onEventMainThread(LoggedInEvent event) {
        updateAccountCategory();
    }

    public void onEventMainThread(LoggedOutEvent event) {
        updateAccountCategory();
    }

    private void bindWeibo(Preference preference) {
        WeiboAuthActivity.startAuth(PageOpenHelper.from((Fragment) this));
    }

    private void unbindWeibo(final Preference preference) {
        new Builder().setMessage((int) R.string.dialog_message_unbind_weibo_confirm).setPositiveButton((int) R.string.dialog_button_ok, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                WeiboAuthActivity.clearToken();
                SettingFragment.this.updateWeiboStatus(preference);
                ToastUtils.showToast((int) R.string.toast_share_unbind_sina_success);
            }
        }).setNegativeButton((int) R.string.dialog_button_cancel, null).create().show();
    }

    private void updateQQBindStatus(Preference preference) {
        if (QQAuthActivity.isAuthenticated()) {
            preference.setTitle(R.string.setting_item_qq_bound);
            preference.setSummary(R.string.setting_summary_weibo_bound);
            return;
        }
        preference.setTitle(R.string.setting_item_qq);
        preference.setSummary("");
    }

    private void updateWeiboStatus(Preference preference) {
        if (WeiboAuthActivity.isAuthenticated()) {
            preference.setTitle(R.string.setting_item_weibo_bound);
            String userName = Pref.ofApp().getString(Key.APP_WEIBO_USER_NAME);
            if (StringUtils.isEmpty(userName)) {
                preference.setSummary(R.string.setting_summary_weibo_bound);
                WeiboAuthActivity.retrieveUserName();
                return;
            }
            preference.setSummary(userName);
            return;
        }
        preference.setTitle(R.string.setting_item_weibo);
        preference.setSummary("");
    }

    private void setIconUrl(final Preference preference, String iconUrl) {
        ImageLoaderUtils.loadImage(iconUrl, new ImageSize(Utils.dp2pixel(60.0f), Utils.dp2pixel(80.0f)), new SimpleImageLoadingListener() {
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                preference.setIcon(new BitmapDrawable(Res.get(), loadedImage));
            }
        });
    }
}
