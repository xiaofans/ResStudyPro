package com.douban.book.reader.fragment;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.douban.book.reader.R;
import com.douban.book.reader.theme.Theme;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BasePreferenceFragment extends BaseFragment implements OnPreferenceTreeClickListener {
    private static final int FIRST_REQUEST_CODE = 100;
    private static final int MSG_BIND_PREFERENCES = 1;
    private static final String PREFERENCES_TAG = "android:preferences";
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    BasePreferenceFragment.this.bindPreferences();
                    return;
                default:
                    return;
            }
        }
    };
    private boolean mHavePrefs;
    private boolean mInitDone;
    private ListView mList;
    private OnKeyListener mListOnKeyListener = new OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (BasePreferenceFragment.this.mList.getSelectedItem() instanceof Preference) {
                View selectedView = BasePreferenceFragment.this.mList.getSelectedView();
            }
            return false;
        }
    };
    private PreferenceManager mPreferenceManager;
    private final Runnable mRequestFocus = new Runnable() {
        public void run() {
            BasePreferenceFragment.this.mList.focusableViewAvailable(BasePreferenceFragment.this.mList);
        }
    };

    public interface OnPreferenceStartFragmentCallback {
        boolean onPreferenceStartFragment(BasePreferenceFragment basePreferenceFragment, Preference preference);
    }

    public BasePreferenceFragment() {
        setDrawerEnabled(false);
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.mPreferenceManager = PreferenceManagerCompat.newInstance(getActivity(), 100);
        PreferenceManagerCompat.setFragment(this.mPreferenceManager, this);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        getActivity().setTheme(Theme.isNight() ? R.style.AppBaseTheme : R.style.AppBaseTheme.Light);
        return paramLayoutInflater.cloneInContext(getActivity()).inflate(R.layout.frag_base_preference, paramViewGroup, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.mHavePrefs) {
            bindPreferences();
        }
        this.mInitDone = true;
        if (savedInstanceState != null) {
            Bundle container = savedInstanceState.getBundle(PREFERENCES_TAG);
            if (container != null) {
                PreferenceScreen preferenceScreen = getPreferenceScreen();
                if (preferenceScreen != null) {
                    preferenceScreen.restoreHierarchyState(container);
                }
            }
        }
    }

    public void onStart() {
        super.onStart();
        PreferenceManagerCompat.setOnPreferenceTreeClickListener(this.mPreferenceManager, this);
    }

    public void onStop() {
        super.onStop();
        PreferenceManagerCompat.dispatchActivityStop(this.mPreferenceManager);
        PreferenceManagerCompat.setOnPreferenceTreeClickListener(this.mPreferenceManager, null);
    }

    public void onDestroyView() {
        this.mList = null;
        this.mHandler.removeCallbacks(this.mRequestFocus);
        this.mHandler.removeMessages(1);
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
        PreferenceManagerCompat.dispatchActivityDestroy(this.mPreferenceManager);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            Bundle container = new Bundle();
            preferenceScreen.saveHierarchyState(container);
            outState.putBundle(PREFERENCES_TAG, container);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PreferenceManagerCompat.dispatchActivityResult(this.mPreferenceManager, requestCode, resultCode, data);
    }

    public PreferenceManager getPreferenceManager() {
        return this.mPreferenceManager;
    }

    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        if (PreferenceManagerCompat.setPreferences(this.mPreferenceManager, preferenceScreen) && preferenceScreen != null) {
            this.mHavePrefs = true;
            if (this.mInitDone) {
                postBindPreferences();
            }
        }
    }

    public PreferenceScreen getPreferenceScreen() {
        return PreferenceManagerCompat.getPreferenceScreen(this.mPreferenceManager);
    }

    public void addPreferencesFromIntent(Intent intent) {
        requirePreferenceManager();
        setPreferenceScreen(PreferenceManagerCompat.inflateFromIntent(this.mPreferenceManager, intent, getPreferenceScreen()));
    }

    public void addPreferencesFromResource(int preferencesResId) {
        requirePreferenceManager();
        setPreferenceScreen(PreferenceManagerCompat.inflateFromResource(this.mPreferenceManager, getActivity(), preferencesResId, getPreferenceScreen()));
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (getActivity() instanceof OnPreferenceStartFragmentCallback) {
            return ((OnPreferenceStartFragmentCallback) getActivity()).onPreferenceStartFragment(this, preference);
        }
        return false;
    }

    public Preference findPreference(CharSequence key) {
        if (this.mPreferenceManager == null) {
            return null;
        }
        return this.mPreferenceManager.findPreference(key);
    }

    private void requirePreferenceManager() {
        if (this.mPreferenceManager == null) {
            throw new RuntimeException("This should be called after super.onCreate.");
        }
    }

    private void postBindPreferences() {
        if (!this.mHandler.hasMessages(1)) {
            this.mHandler.obtainMessage(1).sendToTarget();
        }
    }

    private void bindPreferences() {
        final PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.bind(getListView());
        }
        if (VERSION.SDK_INT <= 10) {
            getListView().setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (parent instanceof ListView) {
                        position -= ((ListView) parent).getHeaderViewsCount();
                    }
                    Preference item = preferenceScreen.getRootAdapter().getItem(position);
                    if (item instanceof Preference) {
                        Preference preference = item;
                        try {
                            Method performClick = Preference.class.getDeclaredMethod("performClick", new Class[]{PreferenceScreen.class});
                            performClick.setAccessible(true);
                            performClick.invoke(preference, new Object[]{preferenceScreen});
                        } catch (InvocationTargetException e) {
                        } catch (IllegalAccessException e2) {
                        } catch (NoSuchMethodException e3) {
                        }
                    }
                }
            });
        }
    }

    public ListView getListView() {
        ensureList();
        return this.mList;
    }

    private void ensureList() {
        if (this.mList == null) {
            View root = getView();
            if (root == null) {
                throw new IllegalStateException("Content view not yet created");
            }
            View rawListView = root.findViewById(16908298);
            if (rawListView instanceof ListView) {
                this.mList = (ListView) rawListView;
                if (this.mList == null) {
                    throw new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
                }
                this.mList.setOnKeyListener(this.mListOnKeyListener);
                this.mHandler.post(this.mRequestFocus);
                return;
            }
            throw new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ListView class");
        }
    }
}
