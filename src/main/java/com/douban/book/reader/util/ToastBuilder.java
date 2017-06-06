package com.douban.book.reader.util;

import android.app.Activity;
import android.os.Build;
import android.os.Build.VERSION;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.app.App;
import com.douban.book.reader.constant.Dimen;
import com.douban.book.reader.theme.ThemedAttrs;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ToastBuilder {
    private static final int TOAST_SHOW_TIME = 2000;
    private static Queue<View> sToastQueue = new ConcurrentLinkedQueue();
    private Activity mActivity = null;
    private boolean mAutoClose = true;
    private CharSequence mMessage = null;
    private OnClickListener mOnClickListener = null;
    private OnCloseListener mOnCloseListener = null;
    private WindowManager mWindowManager = ((WindowManager) App.get().getSystemService("window"));

    public interface OnCloseListener {
        void onClose();
    }

    public ToastBuilder message(CharSequence message) {
        this.mMessage = message;
        return this;
    }

    public ToastBuilder message(int resId) {
        this.mMessage = Res.getString(resId);
        return this;
    }

    public ToastBuilder autoClose(boolean autoClose) {
        this.mAutoClose = autoClose;
        return this;
    }

    public ToastBuilder attachTo(Activity activity) {
        if (activity != null) {
            this.mActivity = activity;
            this.mWindowManager = activity.getWindowManager();
        }
        return this;
    }

    public ToastBuilder click(OnClickListener listener) {
        this.mOnClickListener = listener;
        autoClose(false);
        return this;
    }

    public ToastBuilder onClose(OnCloseListener listener) {
        this.mOnCloseListener = listener;
        autoClose(false);
        return this;
    }

    public void show() {
        App.get().runOnUiThread(new Runnable() {
            public void run() {
                if (VERSION.SDK_INT < 19 && ToastBuilder.this.mActivity == null && !ToastBuilder.this.mAutoClose && StringUtils.equalsIgnoreCase(Build.MANUFACTURER, "xiaomi")) {
                    ToastBuilder.this.mAutoClose = true;
                }
                final View toast = View.inflate(App.get(), R.layout.toast_layout, null);
                TextView textView = (TextView) toast.findViewById(R.id.text_toast);
                textView.setText(ToastBuilder.this.mMessage);
                toast.setBackgroundColor(Res.getColorOverridingAlpha(ToastBuilder.this.mAutoClose ? R.color.toast_normal_bg : R.array.green, 0.95f));
                ThemedAttrs.ofView(textView).append(R.attr.textColorArray, Integer.valueOf(ToastBuilder.this.mAutoClose ? R.array.toast_text_color : R.array.invert_text_color)).updateView();
                if (ToastBuilder.this.mAutoClose) {
                    toast.postDelayed(new Runnable() {
                        public void run() {
                            ToastBuilder.dismissToast(toast, ToastBuilder.this.mWindowManager);
                        }
                    }, 2000);
                } else {
                    toast.findViewById(R.id.divider).setVisibility(0);
                    View closeBtn = toast.findViewById(R.id.btn_close);
                    closeBtn.setVisibility(0);
                    closeBtn.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            ToastBuilder.dismissToast(toast, ToastBuilder.this.mWindowManager);
                            if (ToastBuilder.this.mOnCloseListener != null) {
                                ToastBuilder.this.mOnCloseListener.onClose();
                            }
                        }
                    });
                }
                textView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        ToastBuilder.dismissToast(toast, ToastBuilder.this.mWindowManager);
                        if (ToastBuilder.this.mOnClickListener != null) {
                            ToastBuilder.this.mOnClickListener.onClick(v);
                        }
                    }
                });
                ToastBuilder.this.showToast(toast, ToastBuilder.this.mWindowManager);
            }
        });
    }

    private void showToast(View toast, WindowManager windowManager) {
        if (windowManager != null) {
            dismissQueuedToast(windowManager);
            try {
                LayoutParams layoutParams = new LayoutParams();
                layoutParams.height = -2;
                layoutParams.width = -1;
                layoutParams.y = Dimen.STATUS_BAR_HEIGHT + Dimen.getActionBarHeight();
                layoutParams.gravity = 48;
                layoutParams.type = getLayerType();
                layoutParams.flags = 296;
                layoutParams.format = -3;
                layoutParams.windowAnimations = R.style.AppAnimation.Toast;
                windowManager.addView(toast, layoutParams);
                sToastQueue.add(toast);
            } catch (Exception e) {
            }
        }
    }

    private static void dismissToast(View toast, WindowManager windowManager) {
        if (toast != null && windowManager != null) {
            try {
                windowManager.removeView(toast);
                sToastQueue.remove(toast);
            } catch (Exception e) {
            }
        }
    }

    private static void pushOutToast(View toast, WindowManager windowManager) {
        if (toast != null && windowManager != null) {
            try {
                LayoutParams params = (LayoutParams) toast.getLayoutParams();
                params.windowAnimations = R.style.AppAnimation.Toast.PushOut;
                windowManager.updateViewLayout(toast, params);
                dismissToast(toast, windowManager);
            } catch (Exception e) {
            }
        }
    }

    private static void dismissQueuedToast(WindowManager windowManager) {
        while (true) {
            View toast = (View) sToastQueue.poll();
            if (toast != null) {
                pushOutToast(toast, windowManager);
            } else {
                return;
            }
        }
    }

    private int getLayerType() {
        if (this.mActivity != null) {
            return 2;
        }
        if (this.mAutoClose || VERSION.SDK_INT >= 19) {
            return 2005;
        }
        return 2003;
    }
}
