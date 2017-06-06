package com.sina.weibo.sdk.api.share;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.Utility;

public class WeiboDownloader {
    private static final String CANCEL_CHINESS = "以后再说";
    private static final String CANCEL_ENGLISH = "Download Later";
    private static final String OK_CHINESS = "现在下载";
    private static final String OK_ENGLISH = "Download Now";
    private static final String PROMPT_CHINESS = "未安装微博客户端，是否现在去下载？";
    private static final String PROMPT_ENGLISH = "Sina Weibo client is not installed, download now?";
    private static final String TITLE_CHINESS = "提示";
    private static final String TITLE_ENGLISH = "Notice";

    class AnonymousClass1 implements OnClickListener {
        private final /* synthetic */ Context val$context;

        AnonymousClass1(Context context) {
            this.val$context = context;
        }

        public void onClick(DialogInterface dialog, int which) {
            WeiboDownloader.downloadWeibo(this.val$context);
        }
    }

    class AnonymousClass2 implements OnClickListener {
        private final /* synthetic */ IWeiboDownloadListener val$listener;

        AnonymousClass2(IWeiboDownloadListener iWeiboDownloadListener) {
            this.val$listener = iWeiboDownloadListener;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (this.val$listener != null) {
                this.val$listener.onCancel();
            }
        }
    }

    public static Dialog createDownloadConfirmDialog(Context context, IWeiboDownloadListener listener) {
        String title = TITLE_CHINESS;
        String prompt = PROMPT_CHINESS;
        String ok = OK_CHINESS;
        String cancel = CANCEL_CHINESS;
        if (!Utility.isChineseLocale(context.getApplicationContext())) {
            title = TITLE_ENGLISH;
            prompt = PROMPT_ENGLISH;
            ok = OK_ENGLISH;
            cancel = CANCEL_ENGLISH;
        }
        return new Builder(context).setMessage(prompt).setTitle(title).setPositiveButton(ok, new AnonymousClass1(context)).setNegativeButton(cancel, new AnonymousClass2(listener)).create();
    }

    private static void downloadWeibo(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setData(Uri.parse(WBConstants.WEIBO_DOWNLOAD_URL));
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
