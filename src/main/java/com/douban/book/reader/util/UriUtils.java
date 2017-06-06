package com.douban.book.reader.util;

import android.net.Uri;
import android.net.Uri.Builder;
import com.douban.book.reader.constant.Key;
import java.util.List;

public class UriUtils {
    private static final String ARK_API_V2_BASE_URI = "/api/v2/";
    private static final String TAG = UriUtils.class.getSimpleName();

    public static String getFilenameSuffix(Uri uri) {
        if (uri == null) {
            return "";
        }
        String fileName = uri.getLastPathSegment();
        if (fileName == null) {
            return "";
        }
        int index = fileName.lastIndexOf(".");
        if (index < 0 || index >= fileName.length() - 1) {
            return "";
        }
        return fileName.substring(index + 1);
    }

    public static String getFirstPathSegment(Uri uri) {
        try {
            return (String) uri.getPathSegments().get(0);
        } catch (Exception e) {
            Logger.e(TAG, e);
            return "";
        }
    }

    public static String getLastPathSegment(Uri uri) {
        if (uri == null) {
            return "";
        }
        String lastSegment = uri.getLastPathSegment();
        if (lastSegment == null) {
            return "";
        }
        return lastSegment;
    }

    public static String getPathSegmentNextTo(Uri uri, String segment) {
        if (uri == null) {
            return "";
        }
        List<String> pathSegments = uri.getPathSegments();
        int index = pathSegments.indexOf(segment);
        if (index >= 0) {
            index++;
            if (index < pathSegments.size()) {
                return (String) pathSegments.get(index);
            }
        }
        return "";
    }

    public static int getIntPathSegmentNextTo(Uri uri, String segment) {
        return StringUtils.toInt(getPathSegmentNextTo(uri, segment));
    }

    public static int getIntQueryParameter(Uri uri, String name) {
        return StringUtils.toInt(uri.getQueryParameter(name));
    }

    public static String figureRelativeUri(Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("uri cannot be null");
        } else if (!StringUtils.equalsIgnoreCase(uri.getAuthority(), getDefaultAuthority())) {
            return uri.toString();
        } else {
            String path = uri.getPath();
            if (path == null || !path.startsWith(ARK_API_V2_BASE_URI)) {
                return path;
            }
            return path.substring(ARK_API_V2_BASE_URI.length());
        }
    }

    public static Uri resolveRelativeUri(String uriStr) {
        Uri uri = Uri.parse(uriStr);
        if (StringUtils.isEmpty(uri.getAuthority())) {
            return new Builder().scheme(Pref.ofApp().getString(Key.APP_API_SCHEME, "https")).encodedAuthority(getDefaultAuthority()).path(ARK_API_V2_BASE_URI).appendEncodedPath(uriStr).build();
        }
        return uri;
    }

    public static boolean isArkApiV2Uri(Uri uri) {
        return (uri == null || uri.getPath() == null || !uri.getPath().startsWith(ARK_API_V2_BASE_URI)) ? false : true;
    }

    public static boolean isInArkDomain(Uri uri) {
        if (uri == null) {
            return false;
        }
        return StringUtils.equalsIgnoreCase(uri.getHost(), "read.douban.com");
    }

    public static boolean isInDoubanDomain(Uri uri) {
        if (uri == null) {
            return false;
        }
        String host = uri.getHost();
        if (StringUtils.equalsIgnoreCase(StringUtils.getSegment(host, -2), "douban") && StringUtils.equalsIgnoreCase(StringUtils.getSegment(host, -1), "com")) {
            return true;
        }
        return false;
    }

    public static boolean isInDoubanDomain(String uri) {
        return StringUtils.isNotEmpty(uri) && isInDoubanDomain(Uri.parse(uri));
    }

    public static boolean isPublicUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        return StringUtils.inListIgnoreCase(uri.getScheme(), "http", "https");
    }

    public static boolean isPublicUri(String uri) {
        return StringUtils.isNotEmpty(uri) && isPublicUri(Uri.parse(uri));
    }

    public static boolean isAppUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        return StringUtils.equalsIgnoreCase(uri.getScheme(), "ark");
    }

    private static String getDefaultAuthority() {
        return Pref.ofApp().getString(Key.APP_API_HOST, "www.douban.com");
    }

    public static CharSequence toStringRemovingScheme(Uri uri) {
        if (uri == null || uri.isRelative()) {
            return StringUtils.toStr(uri);
        }
        CharSequence uriStr = uri.toString().replaceFirst(String.format("%s:", new Object[]{uri.getScheme()}), "");
        if (uriStr.startsWith("//")) {
            return uriStr.substring(2);
        }
        return uriStr;
    }
}
