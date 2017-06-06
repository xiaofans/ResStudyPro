package com.mcxiaoke.next.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.mcxiaoke.next.ui.R;

public class FixedRatioImageView extends ImageView {
    private static final float INVALID_RATIO = -1.0f;
    private static final int STRETCH_HORIZONTAL = 0;
    private static final int STRETCH_VERTICAL = 1;
    private int mOrientation = 0;
    private float mRatio = INVALID_RATIO;

    public FixedRatioImageView(Context context) {
        super(context);
        setUp(context, null);
    }

    public FixedRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp(context, attrs);
    }

    public FixedRatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUp(context, attrs);
    }

    private void setUp(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FixedRatioImageView);
            this.mRatio = a.getDimension(R.styleable.FixedRatioImageView_ratio, INVALID_RATIO);
            this.mOrientation = a.getInt(R.styleable.FixedRatioImageView_fri_orientation, 0);
            a.recycle();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mRatio <= 0.0f) {
            return;
        }
        if (this.mOrientation == 1) {
            int height = getMeasuredHeight();
            setMeasuredDimension((int) (((float) height) * this.mRatio), height);
            return;
        }
        int width = getMeasuredWidth();
        setMeasuredDimension(width, (int) (((float) width) * this.mRatio));
    }

    public void setRatio(float ratio) {
        this.mRatio = ratio;
        requestLayout();
    }
}
