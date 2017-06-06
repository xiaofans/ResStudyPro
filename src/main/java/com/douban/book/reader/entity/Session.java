package com.douban.book.reader.entity;

import com.tencent.connect.common.Constants;

public class Session {
    public String accessToken;
    public String deviceId;
    public int doubanUserId;
    public int expiresIn;
    public int openIdType;
    public String refreshToken;

    public boolean isWeiboUser() {
        return this.openIdType == 104;
    }

    public boolean isQQUser() {
        return this.openIdType == 103;
    }

    public boolean isOpenIdLogin() {
        return this.openIdType > 0;
    }

    public String getOpenIdTypeName() {
        switch (this.openIdType) {
            case 103:
                return Constants.SOURCE_QQ;
            case 104:
                return "Weibo";
            case 110:
                return "Weixin";
            default:
                return "<Unknown>";
        }
    }
}
