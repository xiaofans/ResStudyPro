package com.sina.weibo.sdk.auth.sso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.component.AuthRequestParam;
import com.sina.weibo.sdk.component.WeiboSdkBrowser;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.NetworkHelper;
import com.sina.weibo.sdk.utils.UIUtils;
import com.sina.weibo.sdk.utils.Utility;

class WebAuthHandler {
    private static final String NETWORK_NOT_AVAILABLE_EN = "Network is not available";
    private static final String NETWORK_NOT_AVAILABLE_ZH_CN = "无法连接到网络，请检查网络配置";
    private static final String NETWORK_NOT_AVAILABLE_ZH_TW = "無法連接到網络，請檢查網络配置";
    private static final String OAUTH2_BASE_URL = "https://open.weibo.cn/oauth2/authorize?";
    private static final int OBTAIN_AUTH_CODE = 0;
    private static final int OBTAIN_AUTH_TOKEN = 1;
    private static final String TAG = WebAuthHandler.class.getName();
    private AuthInfo mAuthInfo;
    private Context mContext;

    public WebAuthHandler(Context context, AuthInfo authInfo) {
        this.mContext = context;
        this.mAuthInfo = authInfo;
    }

    public AuthInfo getAuthInfo() {
        return this.mAuthInfo;
    }

    public void anthorize(WeiboAuthListener listener) {
        authorize(listener, 1);
    }

    public void authorize(WeiboAuthListener listener, int type) {
        startDialog(listener, type);
    }

    private void startDialog(WeiboAuthListener listener, int type) {
        if (listener != null) {
            WeiboParameters requestParams = new WeiboParameters(this.mAuthInfo.getAppKey());
            requestParams.put("client_id", this.mAuthInfo.getAppKey());
            requestParams.put(WBConstants.AUTH_PARAMS_REDIRECT_URL, this.mAuthInfo.getRedirectUrl());
            requestParams.put("scope", this.mAuthInfo.getScope());
            requestParams.put(WBConstants.AUTH_PARAMS_RESPONSE_TYPE, "code");
            requestParams.put("version", WBConstants.WEIBO_SDK_VERSION_CODE);
            String aid = Utility.getAid(this.mContext, this.mAuthInfo.getAppKey());
            if (!TextUtils.isEmpty(aid)) {
                requestParams.put("aid", aid);
            }
            if (1 == type) {
                requestParams.put("packagename", this.mAuthInfo.getPackageName());
                requestParams.put("key_hash", this.mAuthInfo.getKeyHash());
            }
            String url = new StringBuilder(OAUTH2_BASE_URL).append(requestParams.encodeUrl()).toString();
            if (NetworkHelper.hasInternetPermission(this.mContext)) {
                AuthRequestParam req = new AuthRequestParam(this.mContext);
                req.setAuthInfo(this.mAuthInfo);
                req.setAuthListener(listener);
                req.setUrl(url);
                req.setSpecifyTitle("微博登录");
                Bundle data = req.createRequestParamBundle();
                Intent intent = new Intent(this.mContext, WeiboSdkBrowser.class);
                intent.putExtras(data);
                this.mContext.startActivity(intent);
                return;
            }
            UIUtils.showAlert(this.mContext, "Error", "Application requires permission to access the Internet");
        }
    }
}
