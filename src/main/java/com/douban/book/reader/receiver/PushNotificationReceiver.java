package com.douban.book.reader.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.douban.book.reader.app.App;
import com.douban.book.reader.manager.UserManager;
import com.douban.book.reader.network.param.JsonRequestParam;
import com.douban.book.reader.network.param.RequestParam;
import com.douban.book.reader.util.Analysis;
import com.douban.book.reader.util.Dumper;
import com.douban.book.reader.util.Logger;
import com.douban.book.reader.util.NotificationUtils;
import com.douban.book.reader.util.PushManager;
import com.douban.book.reader.util.StringUtils;
import com.igexin.sdk.PushConsts;
import java.io.UnsupportedEncodingException;
import org.json.JSONObject;

public class PushNotificationReceiver extends BroadcastReceiver {
    private static final String PUSH_KEY_MESSAGE = "message";
    private static final String PUSH_KEY_NID = "nid";
    private static final String PUSH_KEY_TYPE = "type";
    private static final String PUSH_KEY_UID = "uid";
    private static final String PUSH_KEY_VALUE = "value";
    private static final String PUSH_TYPE_OPEN_PAGE = "open_page";
    private static final String TAG = PushNotificationReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        Logger.d(TAG, "=== Received: %s", Dumper.dump(intent));
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            switch (bundle.getInt("action")) {
                case PushConsts.GET_MSG_DATA /*10001*/:
                    byte[] payload = bundle.getByteArray("payload");
                    if (payload != null) {
                        try {
                            Logger.d(TAG, "payload: %s", new String(payload, "UTF-8"));
                            handlePushNotification(data);
                            return;
                        } catch (UnsupportedEncodingException e) {
                            Logger.e(TAG, e);
                            return;
                        }
                    }
                    return;
                case PushConsts.GET_CLIENTID /*10002*/:
                    PushManager.registerDevice(bundle.getString("clientid"));
                    return;
                default:
                    return;
            }
        }
    }

    private void handlePushNotification(String payload) {
        try {
            if (PushManager.isPushEnabled()) {
                JSONObject payloadJson = new JSONObject(payload);
                int uid = payloadJson.optInt("uid", 0);
                int currentUid = UserManager.getInstance().getUserId();
                if (uid <= 0 || currentUid == uid) {
                    String msg = payloadJson.optString("message");
                    CharSequence type = payloadJson.optString("type");
                    String value = payloadJson.optString("value");
                    long nid = payloadJson.optLong(PUSH_KEY_NID);
                    JsonRequestParam extra = (JsonRequestParam) ((JsonRequestParam) RequestParam.json().append("type", type)).append(PUSH_KEY_NID, Long.valueOf(nid));
                    if (StringUtils.equals((CharSequence) PUSH_TYPE_OPEN_PAGE, type)) {
                        JSONObject jsonValue = new JSONObject(value);
                        String title = jsonValue.optString("title");
                        String url = jsonValue.optString("url");
                        extra.appendIfNotEmpty("uri", url);
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                        intent.putExtra("com.android.browser.application_id", App.get().getPackageName());
                        NotificationUtils.showMessage(type, nid, title, msg, intent);
                        Analysis.sendEventWithExtra("Push", "RECEIVE_MSG", extra.getJsonObject());
                        return;
                    }
                    throw new Exception(String.format("Unknown type %s", new Object[]{type}));
                }
                throw new Exception(String.format("Wrong uid. currentUid=%s, targetUid=%s", new Object[]{Integer.valueOf(currentUid), Integer.valueOf(uid)}));
            }
            throw new Exception("Push is disabled by user.");
        } catch (Exception e) {
            Logger.ec(TAG, e, "Push message not shown.", new Object[0]);
            Analysis.sendEventWithExtra("Push", "IGNORED_MSG", String.valueOf(e));
        }
    }
}
