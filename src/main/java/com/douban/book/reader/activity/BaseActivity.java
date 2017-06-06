package com.douban.book.reader.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import com.douban.amonsul.MobileStat;
import com.douban.book.reader.R;
import com.douban.book.reader.app.App;
import com.douban.book.reader.app.ArkLayoutInflaterFactory;
import com.douban.book.reader.constant.Constants;
import com.douban.book.reader.constant.Dimen;
import com.douban.book.reader.controller.TaskController;
import com.douban.book.reader.event.ColorThemeChangedEvent;
import com.douban.book.reader.event.EventBusUtils;
import com.douban.book.reader.util.Analysis;
import com.douban.book.reader.util.AppInfo;
import com.douban.book.reader.util.Dumper;
import com.douban.book.reader.util.Logger;
import com.douban.book.reader.util.Res;
import com.douban.book.reader.util.Tag;
import com.douban.book.reader.util.ThemedUtils;
import com.douban.book.reader.util.ToastUtils;
import com.douban.book.reader.util.Utils;
import com.douban.book.reader.util.ViewUtils;
import com.douban.book.reader.util.WindowActivityCache;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    protected App mApp;
    BroadcastReceiver mFinishAllReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            BaseActivity.this.finish();
        }
    };
    private boolean mIsRestoredToTop = false;
    private Dialog mLoadingDialog = null;
    private int mVisibleTimes;
    private int mWindowTokenHashCode = 0;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        IBinder token = getWindow().getDecorView().getWindowToken();
        if (token != null) {
            this.mWindowTokenHashCode = token.hashCode();
            lifeCycleLog(String.format("onAttachedToWindow, token=%x", new Object[]{Integer.valueOf(this.mWindowTokenHashCode)}));
            WindowActivityCache.add(this.mWindowTokenHashCode, this);
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        lifeCycleLog("onDetachedFromWindow");
        WindowActivityCache.remove(this.mWindowTokenHashCode);
        this.mWindowTokenHashCode = 0;
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        lifeCycleLog("onNewIntent");
        Logger.d(Tag.LIFECYCLE, " ---- %s", Dumper.dump(getIntent()));
        if ((intent.getFlags() | 131072) > 0) {
            this.mIsRestoredToTop = true;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        if (AppInfo.isDebug()) {
            System.gc();
        }
        if (Utils.hasSmartBar()) {
            getWindow().setUiOptions(1);
        }
        supportRequestWindowFeature(5);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        AppCompatDelegate delegate = getDelegate();
        if (layoutInflater.getFactory() == null && (delegate instanceof LayoutInflaterFactory)) {
            LayoutInflaterCompat.setFactory(layoutInflater, new ArkLayoutInflaterFactory((LayoutInflaterFactory) delegate));
        }
        super.onCreate(savedInstanceState);
        lifeCycleLog("onCreate");
        Logger.d(Tag.LIFECYCLE, " ---- %s", Dumper.dump(getIntent()));
        this.mApp = (App) getApplicationContext();
        registerFinishAllReceiver();
        setSupportProgressBarIndeterminateVisibility(false);
        EventBusUtils.register(this);
    }

    protected void initToolbar() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setShowUpButtonOnActionBar(true);
            setShowLogoOnActionBar(false);
        }
    }

    protected void refreshActionBar() {
        runOnUiThread(new Runnable() {
            public void run() {
                BaseActivity.this.supportInvalidateOptionsMenu();
            }
        });
    }

    public View getActionBarView() {
        return getWindow().getDecorView().findViewById(getResources().getIdentifier("action_bar_container", "id", "android"));
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Utils.changeFonts(findViewById(16908290));
        initToolbar();
    }

    public void setContentView(View view) {
        Utils.changeFonts(view);
        super.setContentView(view);
    }

    public void setContentView(View view, LayoutParams params) {
        Utils.changeFonts(view);
        super.setContentView(view, params);
    }

    protected void onStart() {
        super.onStart();
        lifeCycleLog("onStart");
    }

    public void onResume() {
        super.onResume();
        lifeCycleLog("onResume");
        if (shouldBeConsideredAsAPage()) {
            this.mVisibleTimes++;
            Analysis.sendPageViewEvent(this.TAG, getIntent() != null ? getIntent().getExtras() : null, this.mVisibleTimes);
        }
        MobileStat.onResume(this);
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        lifeCycleLog("onPause");
        MobileStat.onPause(this);
        MobclickAgent.onPause(this);
    }

    protected void onStop() {
        super.onStop();
        lifeCycleLog("onStop");
    }

    public void onDestroy() {
        super.onDestroy();
        lifeCycleLog("onDestroy");
        unregisterFinishAllReceiver();
        EventBusUtils.unregister(this);
        TaskController.getInstance().cancelByCaller(this);
    }

    private void finishWithCheck() {
        if (shouldFinish()) {
            finish();
        }
    }

    protected boolean shouldFinish() {
        return true;
    }

    public void finish() {
        super.finish();
    }

    public void setTitle(final CharSequence title) {
        runOnUiThread(new Runnable() {
            public void run() {
                super.setTitle(title);
                ActionBar actionBar = BaseActivity.this.getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(title);
                }
            }
        });
    }

    public void setTitle(int titleId) {
        setTitle(Res.getString(titleId));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finishWithCheck();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setShowLogoOnActionBar(boolean showLogoOnActionBar) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(showLogoOnActionBar);
        }
    }

    public void setShowUpButtonOnActionBar(boolean showUpButtonOnActionBar) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(showUpButtonOnActionBar);
        }
    }

    public void onEventMainThread(ColorThemeChangedEvent event) {
        ThemedUtils.updateViewTree(getWindow().getDecorView().getRootView());
    }

    private void changeFontsForActionBar() {
        try {
            int actionViewResId = Resources.getSystem().getIdentifier("action_bar_container", "id", "android");
            if (actionViewResId > 0) {
                Utils.changeFonts(findViewById(actionViewResId));
            }
        } catch (Throwable e) {
            Logger.e(Tag.GENERAL, e);
        }
    }

    private void registerFinishAllReceiver() {
        registerReceiver(this.mFinishAllReceiver, new IntentFilter(Constants.INTENT_FINISH_ALL_ACTIVITIES));
    }

    private void unregisterFinishAllReceiver() {
        unregisterReceiver(this.mFinishAllReceiver);
    }

    private void lifeCycleLog(String event) {
        Logger.d(Tag.LIFECYCLE, "%n ---- %s@0x%x %s ----", getClass().getSimpleName(), Integer.valueOf(hashCode()), event);
    }

    public boolean shouldBeConsideredAsAPage() {
        return true;
    }

    protected void hideKeyBoard() {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    public void showLoadingDialog() {
        showLoadingDialog(R.string.text_book_loading, false);
    }

    @Deprecated
    public void showLoadingDialog(int resId) {
        showLoadingDialog(resId, false);
    }

    @Deprecated
    public void showCancelableLoadingDialog() {
        showCancelableLoadingDialog(R.string.text_book_loading);
    }

    @Deprecated
    public void showCancelableLoadingDialog(int resId) {
        showLoadingDialog(resId, true);
    }

    @Deprecated
    protected void showLoadingDialog(int resId, boolean cancelable) {
        runOnUiThread(new Runnable() {
            public void run() {
                BaseActivity.this.setSupportProgressBarIndeterminateVisibility(true);
            }
        });
    }

    public void dismissLoadingDialog() {
        runOnUiThread(new Runnable() {
            public void run() {
                BaseActivity.this.setSupportProgressBarIndeterminateVisibility(false);
            }
        });
    }

    @Deprecated
    protected void showToast(int resId) {
        ToastUtils.showToast(resId);
    }

    @Deprecated
    protected void showToast(CharSequence message) {
        ToastUtils.showToast(message);
    }

    public void leaveFullScreenMode() {
        showActionBar();
        showSystemNavigationBar();
    }

    public void enterFullScreenMode() {
        hideActionBar();
        hideSystemNavigationBar();
    }

    protected void setActionBarShowTitleEnabled(boolean showTitleEnabled) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(showTitleEnabled);
        }
    }

    private Toolbar getToolbar() {
        View view = findViewById(R.id.toolbar);
        if (view instanceof Toolbar) {
            return (Toolbar) view;
        }
        return null;
    }

    public void hideActionBar() {
        if (getToolbar() != null) {
            ViewUtils.goneWithAnim(R.anim.push_top_out, getToolbar());
        }
    }

    public void showActionBar() {
        if (getToolbar() != null) {
            ViewUtils.visibleWithAnim(R.anim.push_top_in, getToolbar());
        }
    }

    public boolean isActionBarShowing() {
        return ViewUtils.isVisible(getToolbar());
    }

    protected void setLayoutInFullScreen() {
        if (VERSION.SDK_INT < 16) {
            setLayoutInFullScreen14();
        } else {
            setLayoutInFullScreen16();
        }
    }

    @TargetApi(14)
    private void setLayoutInFullScreen14() {
        getWindow().addFlags(256);
        getWindow().addFlags(512);
        try {
            ViewUtils.setTopMargin(getActionBarView(), Dimen.STATUS_BAR_HEIGHT);
        } catch (Throwable th) {
        }
    }

    @TargetApi(16)
    private void setLayoutInFullScreen16() {
        int flag = 1280;
        if (VERSION.SDK_INT >= 19) {
            flag = 1280 | 512;
        }
        addSystemUiVisibilityFlag(flag);
    }

    private void hideSystemNavigationBar() {
        addSystemUiVisibilityFlag(getFullScreenSystemUiVisibilityFlags());
        if (VERSION.SDK_INT < 16) {
            getWindow().addFlags(1024);
        }
    }

    private void showSystemNavigationBar() {
        removeSystemUiVisibilityFlags(getFullScreenSystemUiVisibilityFlags());
        if (VERSION.SDK_INT < 16) {
            getWindow().clearFlags(1024);
        }
    }

    @TargetApi(19)
    private static int getFullScreenSystemUiVisibilityFlags() {
        int flags = 1;
        if (VERSION.SDK_INT >= 16) {
            flags = 1 | 4;
        }
        if (VERSION.SDK_INT >= 19) {
            return (flags | 2) | 4096;
        }
        return flags;
    }

    private void addSystemUiVisibilityFlag(int flags) {
        View decorView = getWindow().getDecorView();
        if (decorView != null) {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | flags);
        }
    }

    private void removeSystemUiVisibilityFlags(int flags) {
        View decorView = getWindow().getDecorView();
        if (decorView != null) {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & (flags ^ -1));
        }
    }
}
