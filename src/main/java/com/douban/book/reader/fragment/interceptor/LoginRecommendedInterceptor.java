package com.douban.book.reader.fragment.interceptor;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import com.douban.book.reader.R;
import com.douban.book.reader.app.PageOpenHelper;
import com.douban.book.reader.fragment.AlertDialogFragment.Builder;
import com.douban.book.reader.fragment.LoginFragment_;
import com.douban.book.reader.manager.UserManager;
import com.douban.book.reader.util.Res;

public class LoginRecommendedInterceptor implements Interceptor {
    private String mActionName;

    public LoginRecommendedInterceptor(String actionName) {
        this.mActionName = actionName;
    }

    public void performShowAsActivity(final PageOpenHelper helper, final Intent intent) {
        if (UserManager.getInstance().isAnonymousUser()) {
            new Builder().setMessage(Res.getString(R.string.dialog_message_login_suggested, this.mActionName)).setPositiveButton((int) R.string.dialog_button_login, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    LoginFragment_.builder().intentToStartAfterLogin(intent).build().showAsActivity(helper);
                }
            }).setNegativeButton(Res.getString(R.string.dialog_button_skip_login, this.mActionName), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    helper.open(intent);
                }
            }).create().show();
            return;
        }
        helper.open(intent);
    }
}
