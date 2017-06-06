package com.sina.weibo.sdk.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ResizeableLayout extends RelativeLayout {
    private int mHeight = 0;
    private SizeChangeListener mSizeChangeListener = null;
    private int mWidth = 0;

    public interface SizeChangeListener {
        void onSizeChanged(int i, int i2, int i3, int i4);
    }

    public ResizeableLayout(Context context) {
        super(context);
    }

    public ResizeableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.mSizeChangeListener != null) {
            this.mSizeChangeListener.onSizeChanged(getWidth(), getHeight(), this.mWidth, this.mHeight);
        }
        this.mHeight = getHeight();
        this.mWidth = getWidth();
        super.onLayout(changed, l, t, r, b);
    }

    public void setSizeChangeListener(SizeChangeListener sizeChangeListener) {
        this.mSizeChangeListener = sizeChangeListener;
    }
}
