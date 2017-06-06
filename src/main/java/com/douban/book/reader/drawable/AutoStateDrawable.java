package com.douban.book.reader.drawable;

import android.graphics.drawable.Drawable;

public class AutoStateDrawable extends DrawableWrapper {
    public AutoStateDrawable(Drawable drawable) {
        super(drawable);
    }

    public boolean isStateful() {
        return true;
    }

    protected boolean onStateChange(int[] states) {
        for (int state : states) {
            if (state == 16842908 || state == 16842919) {
                setAlpha(Header.MA_VAR);
                return true;
            }
        }
        setAlpha(255);
        return super.onStateChange(states);
    }
}
