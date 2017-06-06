package com.sina.weibo.sdk.net.openapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import com.douban.amonsul.StatConstant;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;

public class ShareWeiboApi {
    private static final String REPOST_URL = "https://api.weibo.com/2/statuses/repost.json";
    private static final String TAG = ShareWeiboApi.class.getName();
    private static final String UPDATE_URL = "https://api.weibo.com/2/statuses/update.json";
    private static final String UPLOAD_URL = "https://api.weibo.com/2/statuses/upload.json";
    private String mAccessToken;
    private String mAppKey;
    private Context mContext;

    private ShareWeiboApi(Context context, String appKey, String token) {
        this.mContext = context.getApplicationContext();
        this.mAppKey = appKey;
        this.mAccessToken = token;
    }

    public static ShareWeiboApi create(Context context, String appKey, String token) {
        return new ShareWeiboApi(context, appKey, token);
    }

    public void update(String content, String lat, String lon, RequestListener listener) {
        requestAsync(UPDATE_URL, buildUpdateParams(content, lat, lon), "POST", listener);
    }

    public void upload(String content, Bitmap bitmap, String lat, String lon, RequestListener listener) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        params.put("pic", bitmap);
        requestAsync(UPLOAD_URL, params, "POST", listener);
    }

    public void repost(String repostBlogId, String repostContent, int comment, RequestListener listener) {
        WeiboParameters params = buildUpdateParams(repostContent, null, null);
        params.put("id", repostBlogId);
        params.put("is_comment", String.valueOf(comment));
        requestAsync(REPOST_URL, params, "POST", listener);
    }

    private void requestAsync(String url, WeiboParameters params, String httpMethod, RequestListener listener) {
        if (TextUtils.isEmpty(this.mAccessToken) || TextUtils.isEmpty(url) || params == null || TextUtils.isEmpty(httpMethod) || listener == null) {
            LogUtil.e(TAG, "Argument error!");
            return;
        }
        params.put("access_token", this.mAccessToken);
        new AsyncWeiboRunner(this.mContext).requestAsync(url, params, httpMethod, listener);
    }

    private WeiboParameters buildUpdateParams(String content, String lat, String lon) {
        WeiboParameters params = new WeiboParameters(this.mAppKey);
        params.put("status", content);
        if (!TextUtils.isEmpty(lon)) {
            params.put("long", lon);
        }
        if (!TextUtils.isEmpty(lat)) {
            params.put(StatConstant.JSON_KEY_LAC, lat);
        }
        if (!TextUtils.isEmpty(this.mAppKey)) {
            params.put("source", this.mAppKey);
        }
        return params;
    }
}
