package com.douban.book.reader.fragment;

import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.JavascriptInterface;
import com.douban.book.reader.constant.Constants;
import com.douban.book.reader.entity.Session;
import com.douban.book.reader.event.ArkRequest;
import com.douban.book.reader.event.EventBusUtils;
import com.douban.book.reader.event.NewUserRegisteredEvent;
import com.douban.book.reader.helper.AppUri;
import com.douban.book.reader.network.param.JsonRequestParam;
import com.douban.book.reader.network.param.RequestParam;
import com.douban.book.reader.util.JsonUtils;
import com.douban.book.reader.util.ToastUtils;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.json.JSONObject;

@EFragment
public class DoubanAccountOperationFragment extends BaseWebFragment {
    private static final String ACCOUNTS_AUTHORITY = "accounts.douban.com";
    private static final int PAGE_LOGIN = 2;
    private static final int REGISTER_SUCCEED = 1;
    private static final UriMatcher sUriMatcher = new UriMatcher(-1);
    @FragmentArg
    Action action;
    @FragmentArg
    Intent intentToStartAfterLogin;
    @FragmentArg
    ArkRequest requestToSendAfterLogin;

    public enum Action {
        REGISTER,
        RESET_PASSWORD
    }

    static {
        sUriMatcher.addURI("p", "register_succeed", 1);
        sUriMatcher.addURI(ACCOUNTS_AUTHORITY, "app/login", 2);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enableJavascript("current_app");
        loadUrl(this.action == Action.RESET_PASSWORD ? resetPasswordUri() : registerUri());
        enablePullToRefresh(false);
    }

    protected boolean shouldOverrideUrlLoading(String url) {
        Uri uri = Uri.parse(url);
        switch (sUriMatcher.match(uri)) {
            case 1:
                try {
                    JSONObject data = new JSONObject(uri.getQueryParameter("oauth"));
                    EventBusUtils.post(new NewUserRegisteredEvent(data.optString("douban_user_name"), (Session) JsonUtils.fromJsonObj(data, Session.class)));
                    finish();
                    return true;
                } catch (Throwable e) {
                    ToastUtils.showToast(e);
                    break;
                }
            case 2:
                DoubanLoginFragment_.builder().intentToStartAfterLogin(this.intentToStartAfterLogin).requestToSendAfterLogin(this.requestToSendAfterLogin).build().showAsActivity((Fragment) this);
                finish();
                return true;
        }
        return super.shouldOverrideUrlLoading(url);
    }

    protected void onPageFinished(String url) {
        addClass("ua-android");
        super.onPageFinished(url);
    }

    @JavascriptInterface
    public String getClientVariables() {
        return String.valueOf(((JsonRequestParam) RequestParam.json().append("apiKey", Constants.APP_KEY)).append("uri", ((JsonRequestParam) ((JsonRequestParam) RequestParam.json().append("openPage", AppUri.withPath(AppUri.PATH_OPEN_URL) + "?url=")).append("loginSuccess", AppUri.withPath("register_succeed") + "?oauth=")).append("registerSuccess", AppUri.withPath("register_succeed") + "?oauth=")));
    }

    private static Builder baseUri() {
        return new Builder().scheme("https").authority(ACCOUNTS_AUTHORITY).appendEncodedPath("app/register").appendQueryParameter("appid", "ark");
    }

    protected static Uri registerUri() {
        return baseUri().build();
    }

    protected static Uri resetPasswordUri() {
        return baseUri().fragment("forget").build();
    }
}
