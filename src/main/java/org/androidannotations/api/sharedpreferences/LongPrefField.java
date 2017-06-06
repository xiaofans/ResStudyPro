package org.androidannotations.api.sharedpreferences;

import android.content.SharedPreferences;

public final class LongPrefField extends AbstractPrefField<Long> {
    LongPrefField(SharedPreferences sharedPreferences, String key, Long defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    public Long getOr(Long defaultValue) {
        Long valueOf;
        try {
            valueOf = Long.valueOf(this.sharedPreferences.getLong(this.key, defaultValue.longValue()));
        } catch (ClassCastException e) {
            try {
                valueOf = Long.valueOf(Long.parseLong(this.sharedPreferences.getString(this.key, "" + defaultValue)));
            } catch (Exception e2) {
                throw e;
            }
        }
        return valueOf;
    }

    protected void putInternal(Long value) {
        apply(edit().putLong(this.key, value.longValue()));
    }
}
