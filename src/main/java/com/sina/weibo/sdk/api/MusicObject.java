package com.sina.weibo.sdk.api;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.sina.weibo.sdk.utils.LogUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class MusicObject extends BaseMediaObject {
    public static final Creator<MusicObject> CREATOR = new Creator<MusicObject>() {
        public MusicObject createFromParcel(Parcel in) {
            return new MusicObject(in);
        }

        public MusicObject[] newArray(int size) {
            return new MusicObject[size];
        }
    };
    public static final String EXTRA_KEY_DEFAULTTEXT = "extra_key_defaulttext";
    public String dataHdUrl;
    public String dataUrl;
    public String defaultText;
    public int duration;
    public String h5Url;

    public MusicObject(Parcel in) {
        super(in);
        this.h5Url = in.readString();
        this.dataUrl = in.readString();
        this.dataHdUrl = in.readString();
        this.duration = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.h5Url);
        dest.writeString(this.dataUrl);
        dest.writeString(this.dataHdUrl);
        dest.writeInt(this.duration);
    }

    public boolean checkArgs() {
        if (!super.checkArgs()) {
            return false;
        }
        if (this.dataUrl != null && this.dataUrl.length() > 512) {
            LogUtil.e("Weibo.MusicObject", "checkArgs fail, dataUrl is invalid");
            return false;
        } else if (this.dataHdUrl != null && this.dataHdUrl.length() > 512) {
            LogUtil.e("Weibo.MusicObject", "checkArgs fail, dataHdUrl is invalid");
            return false;
        } else if (this.duration > 0) {
            return true;
        } else {
            LogUtil.e("Weibo.MusicObject", "checkArgs fail, duration is invalid");
            return false;
        }
    }

    public int getObjType() {
        return 3;
    }

    protected BaseMediaObject toExtraMediaObject(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                this.defaultText = new JSONObject(str).optString("extra_key_defaulttext");
            } catch (JSONException e) {
            }
        }
        return this;
    }

    protected String toExtraMediaString() {
        try {
            JSONObject json = new JSONObject();
            if (!TextUtils.isEmpty(this.defaultText)) {
                json.put("extra_key_defaulttext", this.defaultText);
            }
            return json.toString();
        } catch (JSONException e) {
            return "";
        }
    }
}
