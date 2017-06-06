package com.douban.book.reader.util;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import com.douban.book.reader.R;
import com.douban.book.reader.app.PageOpenHelper;
import com.douban.book.reader.fragment.LoginFragment_;
import com.douban.book.reader.manager.UserManager;
import com.douban.book.reader.util.ToastBuilder.OnCloseListener;

public class LoginPrompt {
    private static boolean sPromptShown = false;

    public static void reset() {
        sPromptShown = false;
    }

    public static void showIfNeeded(final Activity activity) {
        if (!sPromptShown && UserManager.getInstance().isAnonymousUser()) {
            new ToastBuilder().message((int) R.string.toast_login_recommended).attachTo(activity).autoClose(false).click(new OnClickListener() {
                public void onClick(View v) {
                    LoginFragment_.builder().build().showAsActivity(PageOpenHelper.from(activity));
                }
            }).onClose(new OnCloseListener() {
                public void onClose() {
                    LoginPrompt.sPromptShown = true;
                }
            }).show();
        }
    }
}
