package com.douban.book.reader.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.douban.book.reader.R;
import com.douban.book.reader.constant.Constants;
import com.douban.book.reader.event.EventBusUtils;
import com.douban.book.reader.event.OpenIdAuthenticatedEvent;
import com.douban.book.reader.network.client.JsonClient;
import com.douban.book.reader.network.param.QueryString;
import com.douban.book.reader.network.param.RequestParam;
import com.douban.book.reader.util.AnalysisUtils;
import com.douban.book.reader.util.ExceptionUtils;
import com.douban.book.reader.util.Logger;
import com.douban.book.reader.util.ToastUtils;
import com.douban.book.reader.util.WXUtils;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.json.JSONObject;

@EActivity
public class WeixinAuthActivity extends BaseBlankActivity implements IWXAPIEventHandler {
    private static final int WX_RESP_CODE_AUTH_DENIED = 2;
    private static final int WX_RESP_CODE_OK = 0;
    private static final int WX_RESP_CODE_UNKNOWN = 3;
    private static final int WX_RESP_CODE_USER_CANCEL = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.act_wx_entry);
        WXUtils.handleIntent(getIntent(), this);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        WXUtils.handleIntent(getIntent(), this);
    }

    public void onReq(BaseReq baseReq) {
        WelcomeActivity_.intent((Context) this).start();
        finish();
    }

    public void onResp(BaseResp resp) {
        if (resp instanceof Resp) {
            handleAuthResp((Resp) resp);
        } else {
            handleShareResp(resp);
        }
        finish();
    }

    private void handleAuthResp(Resp resp) {
        if (resp.errCode == 0) {
            getOpenId(resp.code);
        } else {
            ToastUtils.showToast((int) R.string.error_login_not_completed);
        }
    }

    @Background
    void getOpenId(String code) {
        try {
            JSONObject json = (JSONObject) new JsonClient("https://api.weixin.qq.com/sns/oauth2/access_token").get(((QueryString) ((QueryString) ((QueryString) RequestParam.queryString().append("appid", "wx72811b1bff66105e")).append("secret", Constants.WX_APP_KEY)).append("code", code)).append(WBConstants.AUTH_PARAMS_GRANT_TYPE, "authorization_code"));
            EventBusUtils.post(new OpenIdAuthenticatedEvent(110, json.optString("openid"), json.optString("access_token")));
        } catch (Throwable e) {
            Logger.e(this.TAG, e);
            ToastUtils.showToast(ExceptionUtils.getHumanReadableMessage(e, (int) R.string.error_login_fail));
        }
    }

    private void handleShareResp(BaseResp resp) {
        int toastRes;
        int respCode;
        switch (resp.errCode) {
            case -4:
                toastRes = R.string.toast_weixin_share_result_denied;
                respCode = 2;
                break;
            case -2:
                toastRes = R.string.toast_weixin_share_result_cancel;
                respCode = 1;
                break;
            case 0:
                toastRes = R.string.toast_weixin_share_result_ok;
                respCode = 0;
                break;
            default:
                toastRes = R.string.toast_weixin_share_result_unknown;
                respCode = 3;
                break;
        }
        ToastUtils.showToast(toastRes);
        AnalysisUtils.sendGetShareRespFromWXEvent(resp.transaction, (long) respCode);
    }
}
