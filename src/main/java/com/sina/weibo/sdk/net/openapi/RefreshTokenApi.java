package com.sina.weibo.sdk.net.openapi;

import android.content.Context;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

public class RefreshTokenApi {
    private static final String REFRESH_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
    private Context mContext;

    private RefreshTokenApi(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static RefreshTokenApi create(Context context) {
        return new RefreshTokenApi(context);
    }

    public void refreshToken(String appKey, String refreshToken, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(appKey);
        params.put("client_id", appKey);
        params.put(WBConstants.AUTH_PARAMS_GRANT_TYPE, "refresh_token");
        params.put("refresh_token", refreshToken);
        new AsyncWeiboRunner(this.mContext).requestAsync(REFRESH_TOKEN_URL, params, "POST", listener);
    }
}
