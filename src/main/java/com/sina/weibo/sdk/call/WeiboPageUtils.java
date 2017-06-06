package com.sina.weibo.sdk.call;

import android.content.Context;
import android.text.TextUtils;
import com.sina.weibo.sdk.constant.WBPageConstants.ExceptionMsg;
import com.sina.weibo.sdk.constant.WBPageConstants.ParamKey;
import com.sina.weibo.sdk.constant.WBPageConstants.Scheme;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public final class WeiboPageUtils {
    private WeiboPageUtils() {
    }

    public static void postNewWeibo(Context context, String content, String poiId, String poiName, Position position, String pageId, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        }
        StringBuilder uri = new StringBuilder(Scheme.SENDWEIBO);
        HashMap<String, String> paramMap = new HashMap();
        try {
            paramMap.put("content", URLEncoder.encode(content, "UTF-8").replaceAll("\\+", "%20"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        paramMap.put(ParamKey.POIID, poiId);
        paramMap.put(ParamKey.POINAME, poiName);
        if (position != null) {
            paramMap.put(ParamKey.LONGITUDE, position.getStrLongitude());
            paramMap.put(ParamKey.LATITUDE, position.getStrLatitude());
        }
        paramMap.put(ParamKey.PAGEID, pageId);
        paramMap.put(ParamKey.EXTPARAM, extParam);
        uri.append(CommonUtils.buildUriQuery(paramMap));
        CommonUtils.openWeiboActivity(context, "android.intent.action.VIEW", uri.toString());
    }

    public static void viewNearbyPeople(Context context, Position position, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        }
        StringBuilder uri = new StringBuilder(Scheme.NEARBYPEOPLE);
        HashMap<String, String> paramMap = new HashMap();
        if (position != null) {
            paramMap.put(ParamKey.LONGITUDE, position.getStrLongitude());
            paramMap.put(ParamKey.LATITUDE, position.getStrLatitude());
            paramMap.put(ParamKey.OFFSET, position.getStrOffset());
        }
        paramMap.put(ParamKey.EXTPARAM, extParam);
        uri.append(CommonUtils.buildUriQuery(paramMap));
        CommonUtils.openWeiboActivity(context, "android.intent.action.VIEW", uri.toString());
    }

    public static void viewNearbyWeibo(Context context, Position position, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        }
        StringBuilder uri = new StringBuilder(Scheme.NEARBYWEIBO);
        HashMap<String, String> paramMap = new HashMap();
        if (position != null) {
            paramMap.put(ParamKey.LONGITUDE, position.getStrLongitude());
            paramMap.put(ParamKey.LATITUDE, position.getStrLatitude());
            paramMap.put(ParamKey.OFFSET, position.getStrOffset());
        }
        paramMap.put(ParamKey.EXTPARAM, extParam);
        uri.append(CommonUtils.buildUriQuery(paramMap));
        CommonUtils.openWeiboActivity(context, "android.intent.action.VIEW", uri.toString());
    }

    public static void viewUserInfo(Context context, String uid, String nick, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        } else if (TextUtils.isEmpty(uid) && TextUtils.isEmpty(nick)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.UID_NICK_ERROR);
        } else {
            StringBuilder uri = new StringBuilder(Scheme.USERINFO);
            HashMap<String, String> paramMap = new HashMap();
            paramMap.put("uid", uid);
            paramMap.put(ParamKey.NICK, nick);
            paramMap.put(ParamKey.EXTPARAM, extParam);
            uri.append(CommonUtils.buildUriQuery(paramMap));
            CommonUtils.openWeiboActivity(context, "android.intent.action.VIEW", uri.toString());
        }
    }

    public static void viewUsertrends(Context context, String uid, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        } else if (TextUtils.isEmpty(uid)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.UID_NICK_ERROR);
        } else {
            StringBuilder uri = new StringBuilder(Scheme.USERTRENDS);
            HashMap<String, String> paramMap = new HashMap();
            paramMap.put("uid", uid);
            paramMap.put(ParamKey.EXTPARAM, extParam);
            uri.append(CommonUtils.buildUriQuery(paramMap));
            CommonUtils.openWeiboActivity(context, "android.intent.action.VIEW", uri.toString());
        }
    }

    public static void viewPageInfo(Context context, String pageId, String title, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        } else if (TextUtils.isEmpty(pageId)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.PAGEID_ERROR);
        } else {
            StringBuilder uri = new StringBuilder(Scheme.PAGEINFO);
            HashMap<String, String> paramMap = new HashMap();
            paramMap.put(ParamKey.PAGEID, pageId);
            paramMap.put("title", title);
            paramMap.put(ParamKey.EXTPARAM, extParam);
            uri.append(CommonUtils.buildUriQuery(paramMap));
            CommonUtils.openWeiboActivity(context, "android.intent.action.VIEW", uri.toString());
        }
    }

    public static void viewPageProductList(Context context, String pageId, String cardId, String title, Integer count, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        } else if (TextUtils.isEmpty(pageId)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.PAGEID_ERROR);
        } else if (TextUtils.isEmpty(cardId)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CARDID_ERROR);
        } else if (count == null || count.intValue() >= 0) {
            StringBuilder uri = new StringBuilder(Scheme.PAGEPRODUCTLIST);
            HashMap<String, String> paramMap = new HashMap();
            paramMap.put(ParamKey.PAGEID, pageId);
            paramMap.put(ParamKey.CARDID, cardId);
            paramMap.put("title", title);
            paramMap.put("page", "1");
            paramMap.put(ParamKey.COUNT, String.valueOf(count));
            paramMap.put(ParamKey.EXTPARAM, extParam);
            uri.append(CommonUtils.buildUriQuery(paramMap));
            CommonUtils.openWeiboActivity(context, "android.intent.action.VIEW", uri.toString());
        } else {
            throw new WeiboIllegalParameterException(ExceptionMsg.COUNT_ERROR);
        }
    }

    public static void viewPageUserList(Context context, String pageId, String cardId, String title, Integer count, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        } else if (TextUtils.isEmpty(pageId)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.PAGEID_ERROR);
        } else if (TextUtils.isEmpty(cardId)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CARDID_ERROR);
        } else if (count == null || count.intValue() >= 0) {
            StringBuilder uri = new StringBuilder(Scheme.PAGEUSERLIST);
            HashMap<String, String> paramMap = new HashMap();
            paramMap.put(ParamKey.PAGEID, pageId);
            paramMap.put(ParamKey.CARDID, cardId);
            paramMap.put("title", title);
            paramMap.put("page", "1");
            paramMap.put(ParamKey.COUNT, String.valueOf(count));
            paramMap.put(ParamKey.EXTPARAM, extParam);
            uri.append(CommonUtils.buildUriQuery(paramMap));
            CommonUtils.openWeiboActivity(context, "android.intent.action.VIEW", uri.toString());
        } else {
            throw new WeiboIllegalParameterException(ExceptionMsg.COUNT_ERROR);
        }
    }

    public static void viewPageWeiboList(Context context, String pageId, String cardId, String title, Integer count, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        } else if (TextUtils.isEmpty(pageId)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.PAGEID_ERROR);
        } else if (TextUtils.isEmpty(cardId)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CARDID_ERROR);
        } else if (count == null || count.intValue() >= 0) {
            StringBuilder uri = new StringBuilder(Scheme.PAGEWEIBOLIST);
            HashMap<String, String> paramMap = new HashMap();
            paramMap.put(ParamKey.PAGEID, pageId);
            paramMap.put(ParamKey.CARDID, cardId);
            paramMap.put("title", title);
            paramMap.put("page", "1");
            paramMap.put(ParamKey.COUNT, String.valueOf(count));
            paramMap.put(ParamKey.EXTPARAM, extParam);
            uri.append(CommonUtils.buildUriQuery(paramMap));
            CommonUtils.openWeiboActivity(context, "android.intent.action.VIEW", uri.toString());
        } else {
            throw new WeiboIllegalParameterException(ExceptionMsg.COUNT_ERROR);
        }
    }

    public static void viewPagePhotoList(Context context, String pageId, String cardId, String title, Integer count, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        } else if (TextUtils.isEmpty(pageId)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.PAGEID_ERROR);
        } else if (TextUtils.isEmpty(cardId)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CARDID_ERROR);
        } else if (count == null || count.intValue() >= 0) {
            StringBuilder uri = new StringBuilder(Scheme.PAGEPHOTOLIST);
            HashMap<String, String> paramMap = new HashMap();
            paramMap.put(ParamKey.PAGEID, pageId);
            paramMap.put(ParamKey.CARDID, cardId);
            paramMap.put("title", title);
            paramMap.put("page", "1");
            paramMap.put(ParamKey.COUNT, String.valueOf(count));
            paramMap.put(ParamKey.EXTPARAM, extParam);
            uri.append(CommonUtils.buildUriQuery(paramMap));
            CommonUtils.openWeiboActivity(context, "android.intent.action.VIEW", uri.toString());
        } else {
            throw new WeiboIllegalParameterException(ExceptionMsg.COUNT_ERROR);
        }
    }

    public static void viewPageDetailInfo(Context context, String pageId, String cardId, String title, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        } else if (TextUtils.isEmpty(pageId)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.PAGEID_ERROR);
        } else if (TextUtils.isEmpty(cardId)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CARDID_ERROR);
        } else {
            StringBuilder uri = new StringBuilder(Scheme.PAGEDETAILINFO);
            HashMap<String, String> paramMap = new HashMap();
            paramMap.put(ParamKey.PAGEID, pageId);
            paramMap.put(ParamKey.CARDID, cardId);
            paramMap.put("title", title);
            paramMap.put(ParamKey.EXTPARAM, extParam);
            uri.append(CommonUtils.buildUriQuery(paramMap));
            CommonUtils.openWeiboActivity(context, "android.intent.action.VIEW", uri.toString());
        }
    }

    public static void openInWeiboBrowser(Context context, String url, String sinainternalbrowser, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        } else if (TextUtils.isEmpty(url)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.URL_ERROR);
        } else if (TextUtils.isEmpty(sinainternalbrowser) || "topnav".equals(sinainternalbrowser) || "default".equals(sinainternalbrowser) || "fullscreen".equals(sinainternalbrowser)) {
            StringBuilder uri = new StringBuilder(Scheme.BROWSER);
            HashMap<String, String> paramMap = new HashMap();
            paramMap.put("url", url);
            paramMap.put(ParamKey.SINAINTERNALBROWSER, sinainternalbrowser);
            paramMap.put(ParamKey.EXTPARAM, extParam);
            uri.append(CommonUtils.buildUriQuery(paramMap));
            CommonUtils.openWeiboActivity(context, "android.intent.action.VIEW", uri.toString());
        } else {
            throw new WeiboIllegalParameterException(ExceptionMsg.SINAINTERNALBROWSER);
        }
    }

    public static void displayInWeiboMap(Context context, Position position, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        }
        String mapUrl = "http://weibo.cn/dpool/ttt/maps.php?xy=%s,%s&amp;size=320x320&amp;offset=%s";
        String lon = "";
        String lat = "";
        String offset = "";
        if (position != null) {
            lon = position.getStrLongitude();
            lat = position.getStrLatitude();
            offset = position.getStrOffset();
        }
        openInWeiboBrowser(context, String.format(mapUrl, new Object[]{lon, lat, offset}), "default", extParam);
    }

    public static void openQrcodeScanner(Context context, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        }
        StringBuilder uri = new StringBuilder(Scheme.QRCODE);
        HashMap<String, String> paramMap = new HashMap();
        paramMap.put(ParamKey.EXTPARAM, extParam);
        uri.append(CommonUtils.buildUriQuery(paramMap));
        CommonUtils.openWeiboActivity(context, "android.intent.action.VIEW", uri.toString());
    }

    public static void viewNearPhotoList(Context context, String longitude_X, String latitude_Y, Integer count, String extParam) throws WeiboNotInstalledException {
        viewPagePhotoList(context, "100101" + longitude_X + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + latitude_Y, "nearphoto", "周边热图", count, extParam);
    }

    public static void viewPoiPhotoList(Context context, String poiid, Integer count, String extParam) throws WeiboNotInstalledException {
        viewPagePhotoList(context, "100101" + poiid, "nearphoto", "周边热图", count, extParam);
    }

    public static void viewPoiPage(Context context, String longitude_X, String latitude_Y, String title, String extParam) throws WeiboNotInstalledException {
        viewPageInfo(context, "100101" + longitude_X + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + latitude_Y, title, extParam);
    }

    public static void weiboDetail(Context context, String mblogid, String extParam) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(ExceptionMsg.CONTEXT_ERROR);
        } else if (TextUtils.isEmpty(mblogid)) {
            throw new WeiboIllegalParameterException(ExceptionMsg.PAGEID_ERROR);
        } else {
            StringBuilder uri = new StringBuilder(Scheme.MBLOGDETAIL);
            HashMap<String, String> paramMap = new HashMap();
            paramMap.put(ParamKey.MBLOGID, mblogid);
            paramMap.put(ParamKey.EXTPARAM, extParam);
            uri.append(CommonUtils.buildUriQuery(paramMap));
            CommonUtils.openWeiboActivity(context, "android.intent.action.VIEW", uri.toString());
        }
    }
}
