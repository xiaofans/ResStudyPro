package com.mcxiaoke.next.ui.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

public class CheckableImageView extends ImageView implements Checkable {
    private static final int[] CheckedStateSet = new int[]{16842912};
    private boolean mChecked = false;

    public CheckableImageView(Context context) {
        super(context);
    }

    public CheckableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isChecked() {
        return this.mChecked;
    }

    public void setChecked(boolean b) {
        if (b != this.mChecked) {
            this.mChecked = b;
            refreshDrawableState();
        }
    }

    public void toggle() {
        setChecked(!this.mChecked);
    }

    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CheckedStateSet);
        }
        return drawableState;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }
}
