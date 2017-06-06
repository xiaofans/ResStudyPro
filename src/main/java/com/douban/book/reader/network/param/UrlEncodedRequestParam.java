package com.douban.book.reader.network.param;

import com.douban.book.reader.util.KeyValuePair;
import com.douban.book.reader.util.Logger;
import com.douban.book.reader.util.MapIterator;
import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class UrlEncodedRequestParam<T extends UrlEncodedRequestParam<T>> extends RequestParam<T> {
    private static final String TAG = UrlEncodedRequestParam.class.getSimpleName();
    private Map<String, Object> mMap = new LinkedHashMap();

    protected UrlEncodedRequestParam() {
    }

    public boolean isEmpty() {
        return this.mMap.isEmpty();
    }

    protected void doAppend(String key, Object value) {
        if (value instanceof UrlEncodedRequestParam) {
            throw new UnsupportedOperationException(String.format("%s cannot be nested.", new Object[]{getClass().getSimpleName()}));
        } else {
            this.mMap.put(key, value);
        }
    }

    public Iterator<KeyValuePair> iterator() {
        return new MapIterator(this.mMap);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Entry<String, Object> entry : this.mMap.entrySet()) {
            try {
                String encodedValue = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
                if (builder.length() > 0) {
                    builder.append("&");
                }
                builder.append((String) entry.getKey()).append(SimpleComparison.EQUAL_TO_OPERATION).append(encodedValue);
            } catch (UnsupportedEncodingException e) {
                Logger.e(TAG, e);
            }
        }
        return builder.toString();
    }
}
