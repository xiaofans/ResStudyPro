package com.douban.book.reader.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.douban.book.reader.R;
import com.douban.book.reader.activity.BaseActivity;
import com.douban.book.reader.activity.GeneralFragmentActivity;
import com.douban.book.reader.activity.GeneralFragmentDrawerActivity;
import com.douban.book.reader.app.App;
import com.douban.book.reader.app.PageOpenHelper;
import com.douban.book.reader.constant.ViewTagKey;
import com.douban.book.reader.controller.TaskController;
import com.douban.book.reader.event.ColorThemeChangedEvent;
import com.douban.book.reader.event.EventBusUtils;
import com.douban.book.reader.fragment.interceptor.Interceptor;
import com.douban.book.reader.manager.exception.DataLoadException;
import com.douban.book.reader.util.Analysis;
import com.douban.book.reader.util.BundleUtils;
import com.douban.book.reader.util.FragmentInstanceCache;
import com.douban.book.reader.util.Logger;
import com.douban.book.reader.util.Res;
import com.douban.book.reader.util.StringUtils;
import com.douban.book.reader.util.Tag;
import com.douban.book.reader.util.Utils;
import com.douban.book.reader.util.ViewUtils;
import com.douban.book.reader.view.LoadErrorPageView.RefreshClickListener;
import com.douban.book.reader.view.LoadErrorPageView_;
import com.douban.book.reader.view.LoadingView;
import com.douban.book.reader.view.LoadingView_;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseFragment extends Fragment {
    private static final String KEY_DRAWER_ENABLED = "drawer_enabled";
    private static final String KEY_ICON_RES_ID = "icon_res_id";
    private static final String KEY_ICON_UPDATED_TO_ACTIVITY = "icon_updated_to_activity";
    private static final String KEY_IS_VISIBLE_LIFE_CYCLE_MANUALLY_CONTROLLED = "is_visible_life_cycle_manually_controlled";
    private static final String KEY_SHOULD_BE_CONSIDERED_AS_A_PAGE = "should_be_considered_as_a_page";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TITLE_UPDATED_TO_ACTIVITY = "title_updated_to_activity";
    private static final int SHOW_BLOCKING_LOADING_DIALOG = 2;
    private static final int SHOW_LOADING_DIALOG = 1;
    protected final String TAG = getClass().getSimpleName();
    private Map<Integer, OnActivityResultHandler> mActivityResultHandlerMap;
    private boolean mDrawerEnabled = true;
    private Drawable mIconDrawable = null;
    private int mIconResId = 0;
    private boolean mIconUpdatedToActivity = false;
    private Interceptor mInterceptor = null;
    private boolean mIsVisibleLifeCycleManuallyControlled = false;
    private Menu mMenu;
    private OnFinishListener mOnFinishListener;
    private boolean mOnVisibleCalled = false;
    private boolean mShouldBeConsideredAsAPage = false;
    private boolean mShouldShowActionBar = true;
    private Handler mShowLoadingDialogHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    BaseFragment.this.performShowLoadingDialog(false);
                    return;
                case 2:
                    BaseFragment.this.performShowLoadingDialog(true);
                    return;
                default:
                    return;
            }
        }
    };
    private HashMap<String, Object> mTagMap = new HashMap();
    private CharSequence mTitle;
    private boolean mTitleUpdatedToActivity = false;
    private int mVisibleTimes;

    public interface OnActivityResultHandler {
        void onActivityResultedOk(Intent intent);
    }

    public interface OnFinishListener {
        boolean beforeFinish();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        lifeCycleLog("onAttach", new Object[0]);
        EventBusUtils.register(this);
        if (this.mTitleUpdatedToActivity) {
            updateActivityTitle();
        }
        if (this.mIconUpdatedToActivity) {
            updateActivityIcon();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mTitle = savedInstanceState.getCharSequence("title");
            this.mTitleUpdatedToActivity = savedInstanceState.getBoolean(KEY_TITLE_UPDATED_TO_ACTIVITY);
            this.mIconResId = savedInstanceState.getInt(KEY_ICON_RES_ID, 0);
            this.mIconUpdatedToActivity = savedInstanceState.getBoolean(KEY_ICON_UPDATED_TO_ACTIVITY);
            this.mDrawerEnabled = savedInstanceState.getBoolean(KEY_DRAWER_ENABLED);
            this.mShouldBeConsideredAsAPage = savedInstanceState.getBoolean(KEY_SHOULD_BE_CONSIDERED_AS_A_PAGE);
            this.mIsVisibleLifeCycleManuallyControlled = savedInstanceState.getBoolean(KEY_IS_VISIBLE_LIFE_CYCLE_MANUALLY_CONTROLLED);
            if (this.mTitleUpdatedToActivity) {
                updateActivityTitle();
            }
            if (this.mIconUpdatedToActivity) {
                updateActivityIcon();
            }
        }
        lifeCycleLog("onCreate", savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lifeCycleLog("onCreateView", new Object[0]);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifeCycleLog("onViewCreated", new Object[0]);
        Utils.changeFonts(view);
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            if (this.mShouldBeConsideredAsAPage) {
                getActionBar().hide();
            }
            setHasOptionsMenu(false);
            onCreateOptionsMenu(toolbar.getMenu(), new SupportMenuInflater(getActivity()));
            toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    return BaseFragment.this.onOptionsItemSelected(item);
                }
            });
            toolbar.setTitle(getTitle());
            toolbar.setNavigationIcon((int) R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            toolbar.setNavigationOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    BaseFragment.this.finish();
                }
            });
        }
        if (this.mShouldBeConsideredAsAPage) {
            setAsRootOfContentView(view);
        }
    }

    protected void setAsRootOfContentView(View view) {
        view.setTag(ViewTagKey.ATTACHED_FRAGMENT, this);
    }

    protected void updateThemedViews() {
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lifeCycleLog("onActivityCreated", new Object[0]);
    }

    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        lifeCycleLog("onViewStateRestored", new Object[0]);
        updateThemedViews();
    }

    public void onStart() {
        super.onStart();
        lifeCycleLog("onStart", new Object[0]);
        if (!this.mIsVisibleLifeCycleManuallyControlled) {
            onVisible();
        }
    }

    public void onResume() {
        super.onResume();
        lifeCycleLog("onResume", new Object[0]);
        if (hasToolbar()) {
            invalidateOptionsMenu();
        }
    }

    public Context getContext() {
        Context context = super.getContext();
        if (context == null) {
            return App.get();
        }
        return context;
    }

    public void onVisible() {
        lifeCycleLog("onVisible", new Object[0]);
        this.mVisibleTimes++;
        if (!this.mOnVisibleCalled) {
            onFirstVisible();
        }
        this.mOnVisibleCalled = true;
        if (this.mShouldBeConsideredAsAPage) {
            Analysis.sendPageViewEvent(this.TAG, getArguments(), this.mVisibleTimes);
        }
    }

    protected void onFirstVisible() {
        lifeCycleLog("onFirstVisible", new Object[0]);
    }

    public void onInvisible() {
        lifeCycleLog("onInvisible", new Object[0]);
    }

    public void onPause() {
        super.onPause();
        lifeCycleLog("onPause", new Object[0]);
    }

    public void onStop() {
        super.onStop();
        lifeCycleLog("onStop", new Object[0]);
        if (!this.mIsVisibleLifeCycleManuallyControlled) {
            onInvisible();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("title", this.mTitle);
        outState.putBoolean(KEY_TITLE_UPDATED_TO_ACTIVITY, this.mTitleUpdatedToActivity);
        outState.putInt(KEY_ICON_RES_ID, this.mIconResId);
        outState.putBoolean(KEY_ICON_UPDATED_TO_ACTIVITY, this.mIconUpdatedToActivity);
        outState.putBoolean(KEY_DRAWER_ENABLED, this.mDrawerEnabled);
        outState.putBoolean(KEY_SHOULD_BE_CONSIDERED_AS_A_PAGE, this.mShouldBeConsideredAsAPage);
        outState.putBoolean(KEY_IS_VISIBLE_LIFE_CYCLE_MANUALLY_CONTROLLED, this.mIsVisibleLifeCycleManuallyControlled);
        lifeCycleLog("onSaveInstanceState", outState);
    }

    public void onDestroyView() {
        super.onDestroyView();
        lifeCycleLog("onDestroyView", new Object[0]);
    }

    public void onDestroy() {
        super.onDestroy();
        lifeCycleLog("onDestroy", new Object[0]);
        FragmentInstanceCache.remove(this);
        TaskController.getInstance().cancelByCaller(this);
    }

    public void onDetach() {
        super.onDetach();
        lifeCycleLog("onDetach", new Object[0]);
        EventBusUtils.unregister(this);
        try {
            Field field = Fragment.class.getDeclaredField("mChildFragmentManager");
            field.setAccessible(true);
            field.set(this, null);
        } catch (Exception e) {
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        lifeCycleLog(String.format("onActivityResult requestCode=%s", new Object[]{Integer.valueOf(requestCode)}), new Object[0]);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
        if (resultCode == -1) {
            OnActivityResultHandler handler = getActivityResultHandler(requestCode);
            if (handler != null) {
                handler.onActivityResultedOk(data);
            }
        }
    }

    public void appendArgument(String key, Object value) {
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            try {
                setArguments(bundle);
            } catch (Exception e) {
                Logger.e(this.TAG, e);
            }
        }
        BundleUtils.put(bundle, key, value);
    }

    public void onEventMainThread(ColorThemeChangedEvent event) {
        updateThemedViews();
    }

    private Toolbar getToolbar() {
        View contentView = getView();
        if (contentView != null) {
            View toolbar = contentView.findViewById(R.id.toolbar);
            if (toolbar instanceof Toolbar) {
                return (Toolbar) toolbar;
            }
        }
        return null;
    }

    public boolean hasToolbar() {
        return getToolbar() != null;
    }

    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return getActivity().getLayoutInflater();
    }

    public BaseFragment setIcon(Drawable iconDrawable) {
        this.mIconDrawable = iconDrawable;
        this.mIconResId = 0;
        if (this.mIconUpdatedToActivity) {
            updateActivityIcon();
        }
        return this;
    }

    public BaseFragment setIcon(int iconResId) {
        this.mIconResId = iconResId;
        this.mIconDrawable = null;
        if (this.mIconUpdatedToActivity) {
            updateActivityIcon();
        }
        return this;
    }

    public BaseFragment setIconUpdatedToActivity(boolean updatedToActivity) {
        this.mIconUpdatedToActivity = updatedToActivity;
        if (this.mIconUpdatedToActivity) {
            updateActivityIcon();
        }
        return this;
    }

    public int getIcon() {
        return this.mIconResId;
    }

    public BaseFragment setTitle(CharSequence title) {
        if (title == null) {
            title = "";
        }
        this.mTitle = title;
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setTitle(this.mTitle);
        } else if (this.mTitleUpdatedToActivity) {
            updateActivityTitle();
        }
        return this;
    }

    public BaseFragment setTitle(int resId) {
        return setTitle(Res.getString(resId));
    }

    public BaseFragment setTitleUpdatedToActivity(boolean titleUpdatedToActivity) {
        this.mTitleUpdatedToActivity = titleUpdatedToActivity;
        if (this.mTitleUpdatedToActivity) {
            updateActivityTitle();
        }
        return this;
    }

    public BaseFragment setShowInterceptor(Interceptor interceptor) {
        this.mInterceptor = interceptor;
        return this;
    }

    public BaseFragment setActionBarVisible(boolean visible) {
        this.mShouldShowActionBar = visible;
        return this;
    }

    public boolean shouldShowActionBar() {
        return this.mShouldShowActionBar;
    }

    public void onBackPressed() {
        finish();
    }

    public BaseFragment setDrawerEnabled(boolean drawerEnabled) {
        this.mDrawerEnabled = drawerEnabled;
        return this;
    }

    private void updateActivityIcon() {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            return;
        }
        if (this.mIconResId <= 0 && this.mIconDrawable == null) {
            return;
        }
        if (this.mIconDrawable != null) {
            actionBar.setIcon(this.mIconDrawable);
        } else if (this.mIconResId > 0) {
            actionBar.setIcon(this.mIconResId);
        }
    }

    private void updateActivityTitle() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.setTitle(this.mTitle);
        }
    }

    protected void setActionBarNavigationMode(int mode) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.getActionBar().setNavigationMode(mode);
        }
    }

    protected void showLoadErrorPage(DataLoadException dataLoadException, RefreshClickListener listener) {
        showLoadErrorPage((ViewGroup) getView(), dataLoadException, listener);
    }

    protected void showLoadErrorPage(@IdRes int attachToResId, DataLoadException dataLoadException, RefreshClickListener listener) {
        View view = getView();
        if (view != null) {
            showLoadErrorPage((ViewGroup) view.findViewById(attachToResId), dataLoadException, listener);
        }
    }

    protected void showLoadErrorPage(final ViewGroup attachTo, final DataLoadException dataLoadException, final RefreshClickListener listener) {
        if (attachTo != null) {
            App.get().runOnUiThread(new Runnable() {
                public void run() {
                    if (BaseFragment.this.getView() instanceof ViewGroup) {
                        LoadErrorPageView_.build(BaseFragment.this.getActivity()).refreshClickListener(listener).attachTo(attachTo).exception(dataLoadException).show();
                    }
                }
            });
        }
    }

    protected ActionBar getActionBar() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            return activity.getSupportActionBar();
        }
        return null;
    }

    public void setTag(String key, Object tag) {
        this.mTagMap.put(key, tag);
    }

    public Object getTag(String key) {
        return this.mTagMap.get(key);
    }

    public void setShouldBeConsideredAsAPage(boolean shouldBeConsideredAsAPage) {
        this.mShouldBeConsideredAsAPage = shouldBeConsideredAsAPage;
    }

    public void setVisibleLifeCycleManuallyControlled(boolean isVisibleLifeCycleManuallyControlled) {
        this.mIsVisibleLifeCycleManuallyControlled = isVisibleLifeCycleManuallyControlled;
    }

    public void showAsActivity(Fragment fromFragment) {
        showAsActivity(PageOpenHelper.from(fromFragment));
    }

    public void showAsActivity(BaseActivity fromActivity) {
        showAsActivity(PageOpenHelper.from((Activity) fromActivity));
    }

    public void showAsActivity(View fromView) {
        showAsActivity(PageOpenHelper.from(fromView));
    }

    public void showAsActivity(View fromView, int flags) {
        showAsActivity(PageOpenHelper.from(fromView).flags(flags));
    }

    public void showAsActivity(PageOpenHelper helper) {
        setTitleUpdatedToActivity(true);
        setIconUpdatedToActivity(true);
        setShouldBeConsideredAsAPage(true);
        Intent intent = new Intent(App.get(), this.mDrawerEnabled ? GeneralFragmentDrawerActivity.class : GeneralFragmentActivity.class);
        intent.putExtra(GeneralFragmentActivity.KEY_FRAGMENT, FragmentInstanceCache.push(this));
        intent.putExtra(GeneralFragmentActivity.KEY_SHOW_ACTION_BAR, shouldShowActionBar());
        if (this.mInterceptor != null) {
            this.mInterceptor.performShowAsActivity(helper, intent);
        } else {
            helper.open(intent);
        }
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public void showLoadingDialog() {
        showLoadingDialog(false);
    }

    public void showBlockingLoadingDialog() {
        showLoadingDialog(true);
    }

    public void showLoadingDialog(boolean blockPage) {
        showLoadingDialog(blockPage, 1000);
    }

    public void showLoadingDialogImmediately() {
        showLoadingDialog(false, 0);
    }

    public void showLoadingDialog(boolean blockPage, int delay) {
        Message msg = Message.obtain();
        msg.what = blockPage ? 2 : 1;
        if (delay > 0) {
            this.mShowLoadingDialogHandler.sendMessageDelayed(msg, (long) delay);
        } else {
            this.mShowLoadingDialogHandler.sendMessage(msg);
        }
    }

    protected void performShowLoadingDialog(boolean blockPage) {
        View contentView = getView();
        if (contentView != null) {
            View attachTo = contentView.findViewById(R.id.loading_view_base);
            if (attachTo == null) {
                attachTo = contentView;
            }
            LoadingView loadingView = (LoadingView) getView().findViewById(R.id.view_loading);
            if (loadingView == null) {
                loadingView = LoadingView_.build(App.get());
                ((ViewGroup) attachTo).addView(loadingView);
            } else {
                ViewUtils.visible(loadingView);
            }
            loadingView.blockPage(blockPage);
        }
    }

    public void dismissLoadingDialog() {
        this.mShowLoadingDialogHandler.removeMessages(1);
        this.mShowLoadingDialogHandler.removeMessages(2);
        App.get().runOnUiThread(new Runnable() {
            public void run() {
                BaseFragment.this.performDismissLoadingDialog();
            }
        });
    }

    protected void performDismissLoadingDialog() {
        if (getView() != null && getView().findViewById(R.id.view_loading) != null) {
            ViewUtils.gone(getView().findViewById(R.id.view_loading));
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.mMenu = menu;
    }

    protected Menu getMenu() {
        return this.mMenu;
    }

    protected void invalidateOptionsMenu() {
        App.get().runOnUiThread(new Runnable() {
            public void run() {
                Toolbar toolbar = BaseFragment.this.getToolbar();
                if (toolbar != null) {
                    BaseFragment.this.onPrepareOptionsMenu(toolbar.getMenu());
                    return;
                }
                Activity activity = BaseFragment.this.getActivity();
                if (activity != null) {
                    ActivityCompat.invalidateOptionsMenu(activity);
                }
            }
        });
    }

    protected void enableMenuItems(MenuItem... menuItems) {
        enableMenuItemsIf(true, menuItems);
    }

    protected void disableMenuItems(MenuItem... menuItems) {
        enableMenuItemsIf(false, menuItems);
    }

    private void enableMenuItemsIf(final boolean condition, final MenuItem... menuItems) {
        if (menuItems != null && menuItems.length != 0) {
            App.get().runOnUiThread(new Runnable() {
                public void run() {
                    for (MenuItem menuItem : menuItems) {
                        if (menuItem != null) {
                            menuItem.setEnabled(condition);
                        }
                    }
                }
            });
        }
    }

    protected void showMenuItems(MenuItem... menuItems) {
        showMenuItemsIf(true, menuItems);
    }

    protected void hideMenuItems(MenuItem... menuItems) {
        showMenuItemsIf(false, menuItems);
    }

    protected void showMenuItemsIf(final boolean condition, final MenuItem... menuItems) {
        if (menuItems != null && menuItems.length != 0) {
            App.get().runOnUiThread(new Runnable() {
                public void run() {
                    for (MenuItem menuItem : menuItems) {
                        if (menuItem != null) {
                            menuItem.setVisible(condition);
                        }
                    }
                }
            });
        }
    }

    protected void showMenuItems(@IdRes int... menuItems) {
        showMenuItemsIf(true, menuItems);
    }

    protected void hideMenuItems(@IdRes int... menuItems) {
        showMenuItemsIf(false, menuItems);
    }

    protected void showMenuItemsIf(final boolean condition, @IdRes final int... menuItems) {
        if (menuItems != null && menuItems.length != 0 && getMenu() != null) {
            App.get().runOnUiThread(new Runnable() {
                public void run() {
                    for (int resId : menuItems) {
                        MenuItem menuItem = BaseFragment.this.getMenu().findItem(resId);
                        if (menuItem != null) {
                            menuItem.setVisible(condition);
                        }
                    }
                }
            });
        }
    }

    protected void showAllMenuItems() {
        showAllMenuItemsIf(true);
    }

    protected void hideAllMenuItems() {
        showAllMenuItemsIf(false);
    }

    protected void showAllMenuItemsIf(final boolean condition) {
        if (getMenu() != null) {
            App.get().runOnUiThread(new Runnable() {
                public void run() {
                    Menu menu = BaseFragment.this.getMenu();
                    for (int i = 0; i < menu.size(); i++) {
                        MenuItem menuItem = menu.getItem(i);
                        if (menuItem != null) {
                            menuItem.setVisible(condition);
                        }
                    }
                }
            });
        }
    }

    protected void post(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    protected void postDelayed(Runnable runnable, int delay) {
        new Handler(Looper.getMainLooper()).postDelayed(runnable, (long) delay);
    }

    public void setOnFinishListener(OnFinishListener listener) {
        this.mOnFinishListener = listener;
    }

    public boolean shouldFinish() {
        if (this.mOnFinishListener != null) {
            return this.mOnFinishListener.beforeFinish();
        }
        return true;
    }

    public void finish() {
        App.get().runOnUiThread(new Runnable() {
            public void run() {
                if (BaseFragment.this.shouldFinish()) {
                    BaseFragment.this.finishSkippingCheck();
                }
            }
        });
    }

    protected void finishSkippingCheck() {
        App.get().runOnUiThread(new Runnable() {
            public void run() {
                FragmentActivity activity = BaseFragment.this.getActivity();
                if (activity != null) {
                    Fragment parentFragment = BaseFragment.this.getParentFragment();
                    if (parentFragment == null) {
                        activity.finish();
                    } else if (parentFragment instanceof BaseTabFragment) {
                        ((BaseFragment) parentFragment).finish();
                    } else {
                        activity.getSupportFragmentManager().beginTransaction().remove(BaseFragment.this).commitAllowingStateLoss();
                    }
                }
            }
        });
    }

    public synchronized void addActivityResultHandler(int requestCode, OnActivityResultHandler handler) {
        if (this.mActivityResultHandlerMap == null) {
            this.mActivityResultHandlerMap = new HashMap();
        }
        this.mActivityResultHandlerMap.put(Integer.valueOf(requestCode), handler);
    }

    private synchronized OnActivityResultHandler getActivityResultHandler(int requestCode) {
        OnActivityResultHandler onActivityResultHandler;
        if (this.mActivityResultHandlerMap == null) {
            onActivityResultHandler = null;
        } else {
            onActivityResultHandler = (OnActivityResultHandler) this.mActivityResultHandlerMap.get(Integer.valueOf(requestCode));
        }
        return onActivityResultHandler;
    }

    private void lifeCycleLog(String event, Object... extras) {
        CharSequence formattedExtras = null;
        if (extras.length > 0) {
            formattedExtras = StringUtils.joinSkippingNull((CharSequence) ", ", extras);
        }
        String str = Tag.LIFECYCLE;
        String str2 = "%n     ---- %s@0x%x%s %s %s----";
        Object[] objArr = new Object[5];
        objArr[0] = getClass().getSimpleName();
        objArr[1] = Integer.valueOf(hashCode());
        objArr[2] = getArguments() == null ? "" : " (" + getArguments() + ")";
        objArr[3] = event;
        objArr[4] = StringUtils.isEmpty(formattedExtras) ? "" : "(extras: " + formattedExtras + ") ";
        Logger.d(str, str2, objArr);
    }
}
