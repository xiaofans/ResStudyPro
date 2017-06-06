package com.douban.book.reader.fragment.interceptor;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import com.douban.book.reader.R;
import com.douban.book.reader.activity.GeneralFragmentActivity;
import com.douban.book.reader.activity.WeiboAuthActivity;
import com.douban.book.reader.app.PageOpenHelper;
import com.douban.book.reader.constant.ShareTo;
import com.douban.book.reader.fragment.AlertDialogFragment.Builder;
import com.douban.book.reader.fragment.BaseShareEditFragment;
import com.douban.book.reader.fragment.LoginFragment_;
import com.douban.book.reader.manager.UserManager;
import java.util.ArrayList;
import java.util.List;

public class SelectShareTargetInterceptor implements Interceptor {
    private static final ShareTo[] SHARE_TARGETS = new ShareTo[]{ShareTo.DOUBAN, ShareTo.WEIXIN, ShareTo.MOMENTS, ShareTo.WEIBO, ShareTo.OTHER_APPS};

    private CharSequence[] getDisplayTitleList() {
        List<CharSequence> result = new ArrayList();
        for (ShareTo shareTo : SHARE_TARGETS) {
            result.add(shareTo.getDisplayText());
        }
        return (CharSequence[]) result.toArray(new CharSequence[0]);
    }

    public void performShowAsActivity(final PageOpenHelper helper, final Intent intent) {
        new Builder().setTitle((int) R.string.share_to).setItems(getDisplayTitleList(), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ShareTo shareTo = SelectShareTargetInterceptor.SHARE_TARGETS[which];
                if (intent != null) {
                    GeneralFragmentActivity.putAdditionalArgs(intent, BaseShareEditFragment.KEY_SHARE_TO, shareTo);
                    if (shareTo == ShareTo.WEIBO && !WeiboAuthActivity.isAuthenticated()) {
                        new Builder().setMessage((int) R.string.dialog_message_bind_weibo_confirm).setPositiveButton((int) R.string.dialog_button_bind, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                WeiboAuthActivity.startAuth(helper, intent);
                            }
                        }).setNegativeButton((int) R.string.dialog_button_cancel, null).create().show();
                    } else if (shareTo == ShareTo.DOUBAN && UserManager.getInstance().isAnonymousUser()) {
                        LoginFragment_.builder().intentToStartAfterLogin(intent).build().showAsActivity(helper);
                    } else {
                        if (shareTo == ShareTo.OTHER_APPS || shareTo == ShareTo.WEIXIN || shareTo == ShareTo.MOMENTS) {
                            intent.putExtra(GeneralFragmentActivity.KEY_SHOW_ACTION_BAR, false);
                        }
                        helper.open(intent);
                    }
                }
            }
        }).setCanceledOnTouchOutside(true).setCancelable(true).create().show(helper);
    }
}
