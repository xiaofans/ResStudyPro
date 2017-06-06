package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

class ValueEscapeUtil {
    private ValueEscapeUtil() {
    }

    static ObjectAndStatic<Value> applyEscapings(ObjectAndStatic<Value> value, int... escapings) {
        ObjectAndStatic<Value> escapedValue = value;
        for (int escaping : escapings) {
            escapedValue = applyEscaping(escapedValue, escaping);
        }
        return escapedValue;
    }

    static String urlEncode(String string) throws UnsupportedEncodingException {
        return URLEncoder.encode(string, "UTF-8").replaceAll("\\+", "%20");
    }

    private static ObjectAndStatic<Value> applyEscaping(ObjectAndStatic<Value> value, int escaping) {
        if (isValidStringType((Value) value.getObject())) {
            switch (escaping) {
                case 12:
                    return escapeUri(value);
                default:
                    Log.e("Unsupported Value Escaping: " + escaping);
                    return value;
            }
        }
        Log.e("Escaping can only be applied to strings.");
        return value;
    }

    private static ObjectAndStatic<Value> escapeUri(ObjectAndStatic<Value> value) {
        try {
            return new ObjectAndStatic(Types.objectToValue(urlEncode(Types.valueToString((Value) value.getObject()))), value.isStatic());
        } catch (UnsupportedEncodingException e) {
            Log.e("Escape URI: unsupported encoding", e);
            return value;
        }
    }

    private static boolean isValidStringType(Value value) {
        return Types.valueToObject(value) instanceof String;
    }
}
