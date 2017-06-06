package com.douban.book.reader.network.param;

import android.net.Uri;
import com.douban.book.reader.constant.Key;
import com.douban.book.reader.constant.ShareTo;
import com.douban.book.reader.content.page.Range;
import com.douban.book.reader.entity.Annotation.Column;
import com.douban.book.reader.network.param.MultiPartRequestParam.Entry;
import com.douban.book.reader.util.DateUtils;
import com.douban.book.reader.util.KeyValuePair;
import com.douban.book.reader.util.Pref;
import com.douban.book.reader.util.StringUtils;
import java.util.Date;
import java.util.Iterator;
import org.json.JSONException;

public abstract class RequestParam<T extends RequestParam<T>> implements Iterable<KeyValuePair> {

    public enum Type {
        JSON,
        QUERY_STRING,
        FORM,
        MULTI_PART
    }

    protected abstract void doAppend(String str, Object obj);

    public abstract Type getType();

    public abstract boolean isEmpty();

    public abstract Iterator<KeyValuePair> iterator();

    public static RequestParam<JsonRequestParam> json() {
        return new JsonRequestParam();
    }

    public static RequestParam<JsonRequestParam> json(Object object) throws JSONException {
        return new JsonRequestParam(object);
    }

    public static RequestParam<FormRequestParam> form() {
        return new FormRequestParam();
    }

    public static RequestParam<QueryString> queryString() {
        return new QueryString();
    }

    public static RequestParam<MultiPartRequestParam> multiPart() {
        return new MultiPartRequestParam();
    }

    public static RequestParam<?> ofType(Type type) {
        switch (type) {
            case JSON:
                return json();
            case QUERY_STRING:
                return queryString();
            case FORM:
                return form();
            case MULTI_PART:
                return multiPart();
            default:
                return form();
        }
    }

    public T appendUriQuery(Uri uri) {
        if (uri != null) {
            for (String key : uri.getQueryParameterNames()) {
                append(key, uri.getQueryParameter(key));
            }
        }
        return self();
    }

    public T appendIf(boolean condition, String key, Object value) {
        if (condition) {
            return append(key, value);
        }
        return self();
    }

    public T appendIfNotEmpty(String key, Object value) {
        if (value != null) {
            if (StringUtils.isNotEmpty(value.toString())) {
                return append(key, value);
            }
        }
        return self();
    }

    public T append(String key, Object value) {
        if (!(value instanceof byte[]) || getType() == Type.MULTI_PART) {
            doAppend(key, formatValue(value));
            return self();
        }
        throw new IllegalArgumentException("Type must be MULTI_PART to append data of bytes[] type.");
    }

    public T appendRange(Range range) {
        if (range.isValid()) {
            append(Column.START_PARAGRAPH_ID, Integer.valueOf(range.startPosition.paragraphId));
            append(Column.START_OFFSET, Integer.valueOf(range.startPosition.paragraphOffset));
            append(Column.END_PARAGRAPH_ID, Integer.valueOf(range.endPosition.paragraphId));
            append(Column.END_OFFSET, Integer.valueOf(range.endPosition.paragraphOffset));
        }
        return self();
    }

    public T appendShareTo(ShareTo shareTo) {
        if (shareTo == ShareTo.DOUBAN) {
            append("tweet_to_broadcast", Boolean.valueOf(true));
        } else if (shareTo == ShareTo.WEIBO) {
            append("tweet_to_weibo", Boolean.valueOf(true));
            append("weibo_access_token", Pref.ofApp().getString(Key.APP_WEIBO_ACCESS_TOKEN));
        }
        return self();
    }

    public T appendShareToIf(boolean condition, ShareTo shareTo) {
        if (condition) {
            appendShareTo(shareTo);
        }
        return self();
    }

    public T append(RequestParam<?> param) {
        if (param != null) {
            Iterator<KeyValuePair> iterator = param.iterator();
            while (iterator.hasNext()) {
                KeyValuePair keyValuePair = (KeyValuePair) iterator.next();
                Object key = keyValuePair.getKey();
                if (key != null) {
                    append(String.valueOf(key), keyValuePair.getValue());
                }
            }
        }
        return self();
    }

    private static Object formatValue(Object value) {
        if (value instanceof Date) {
            return DateUtils.formatIso8601((Date) value);
        }
        if (value instanceof byte[]) {
            return new Entry((byte[]) value);
        }
        return value;
    }

    private T self() {
        return this;
    }
}
