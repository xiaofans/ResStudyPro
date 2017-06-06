package com.tencent.wxop.stat.a;

import com.sina.weibo.sdk.utils.AidTask;

public enum e {
    PAGE_VIEW(1),
    SESSION_ENV(2),
    ERROR(3),
    CUSTOM(1000),
    ADDITION(1001),
    MONITOR_STAT(AidTask.WHAT_LOAD_AID_ERR),
    MTA_GAME_USER(1003),
    NETWORK_MONITOR(1004),
    NETWORK_DETECTOR(1005);
    
    private int bG;

    private e(int i) {
        this.bG = i;
    }

    public final int r() {
        return this.bG;
    }
}
