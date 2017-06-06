package com.douban.book.reader.content.paragraph.decorator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import com.douban.book.reader.util.PaintUtils;
import com.douban.book.reader.util.Res;
import com.douban.book.reader.util.Utils;

public class LineQuoteDecorator extends Decorator {
    private static final int LINE_WIDTH = Utils.dp2pixel(2.0f);
    public static final int USE_TEXT_COLOR = -1;
    private int mQuoteLineColorRes = -1;

    public LineQuoteDecorator(@ArrayRes @ColorRes int quoteLineColorRes) {
        this.mQuoteLineColorRes = quoteLineColorRes;
    }

    public float getInsetLeft() {
        return getParagraph().getTextSize() / 2.0f;
    }

    public void draw(Canvas canvas, int startLine, int endLine) {
        Paint paint = PaintUtils.obtainPaint();
        if (this.mQuoteLineColorRes == -1) {
            paint.setColor(getParagraph().getTextColor());
        } else {
            paint.setColor(Res.getColor(this.mQuoteLineColorRes));
        }
        canvas.drawRect(0.0f, 0.0f, (float) LINE_WIDTH, getParagraph().getHeight(startLine), paint);
        PaintUtils.recyclePaint(paint);
    }
}
