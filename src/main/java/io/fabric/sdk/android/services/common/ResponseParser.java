package io.fabric.sdk.android.services.common;

import com.douban.amonsul.StatConstant;

public class ResponseParser {
    public static final int ResponseActionDiscard = 0;
    public static final int ResponseActionRetry = 1;

    public static int parse(int statusCode) {
        if (statusCode >= 200 && statusCode <= 299) {
            return 0;
        }
        if (statusCode >= 300 && statusCode <= 399) {
            return 1;
        }
        if (statusCode >= 400 && statusCode <= 499) {
            return 0;
        }
        if (statusCode >= StatConstant.DEFAULT_MAX_EVENT_COUNT) {
            return 1;
        }
        return 1;
    }
}
