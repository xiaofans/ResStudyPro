package com.douban.book.reader.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import com.douban.book.reader.R;
import com.douban.book.reader.util.CanvasUtils;
import com.douban.book.reader.util.Res;
import com.douban.book.reader.util.Utils;

public class PasswordView extends EditText {
    private final RectF mBounds = new RectF();
    private boolean mIsPasswordVisible;
    private boolean mTouchedDown;

    public PasswordView(Context context) {
        super(context);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & 255) {
            case 0:
                if (((float) getWidth()) - event.getX() < ((float) Utils.dp2pixel(44.0f))) {
                    this.mTouchedDown = true;
                    return true;
                }
                break;
            case 1:
                if (this.mTouchedDown) {
                    this.mTouchedDown = false;
                    togglePasswordVisibleStatus();
                    return true;
                }
                break;
            case 2:
                if (this.mTouchedDown) {
                    return true;
                }
                break;
            case 3:
                if (this.mTouchedDown) {
                    this.mTouchedDown = false;
                    break;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        CanvasUtils.drawDrawableCenteredInArea(canvas, Res.getDrawableWithTint((int) R.drawable.v_show_password, this.mIsPasswordVisible ? R.array.green : R.array.light_blue), this.mBounds);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int radius = getHeight() / 3;
        int centerY = getHeight() / 2;
        int centerX = getWidth() - centerY;
        if (this.mBounds != null) {
            this.mBounds.set((float) (centerX - radius), (float) (centerY - radius), (float) (centerX + radius), (float) (centerY + radius));
        }
    }

    private void togglePasswordVisibleStatus() {
        this.mIsPasswordVisible = !this.mIsPasswordVisible;
        setTransformationMethod(this.mIsPasswordVisible ? null : PasswordTransformationMethod.getInstance());
        setSelection(getText().length());
        invalidate();
    }
}
