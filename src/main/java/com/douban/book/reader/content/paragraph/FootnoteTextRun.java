package com.douban.book.reader.content.paragraph;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import com.douban.book.reader.R;
import com.douban.book.reader.content.paragraph.Paragraph.FootnoteSpan;
import com.douban.book.reader.content.touchable.FootnoteTouchable;
import com.douban.book.reader.content.touchable.Touchable;
import com.douban.book.reader.util.CanvasUtils;
import com.douban.book.reader.util.Logger;
import com.douban.book.reader.util.Res;
import com.douban.book.reader.util.SpanUtils;
import com.douban.book.reader.util.Utils;

public class FootnoteTextRun extends TextRun {
    private static final String TAG = FootnoteTextRun.class.getSimpleName();
    private static final int TOUCH_AREA_SIZE = Utils.dp2pixel(40.0f);
    private final FootnoteTouchable mFootnoteTouchable = new FootnoteTouchable();
    private final float mHorizontalPadding = (this.mMarkSize / 3.0f);
    private final float mMarkSize = (Res.getScaledDimension(R.array.font_size_content) / 2.0f);

    public int getStretchPointCount() {
        return 0;
    }

    public Touchable getTouchable() {
        return this.mFootnoteTouchable;
    }

    public float getWidth() {
        return this.mMarkSize + (this.mHorizontalPadding * 2.0f);
    }

    public void draw(Canvas canvas, float x, float y, Paint paint) {
        CharSequence str;
        try {
            str = this.mText.subSequence(this.start, this.start + this.len);
        } catch (Exception e) {
            str = "";
            Logger.e(TAG, e);
        }
        RectF dispArea = new RectF(this.mHorizontalPadding + x, paint.ascent() + y, (this.mMarkSize + x) + this.mHorizontalPadding, paint.descent() + y);
        RectF markArea = new RectF(dispArea.left, dispArea.top, dispArea.left + this.mMarkSize, dispArea.top + this.mMarkSize);
        RectF touchArea = CanvasUtils.enlargeRectTo(dispArea, (float) TOUCH_AREA_SIZE);
        Drawable drawable = Res.getDrawableWithTint((int) R.drawable.v_footnote, (int) R.array.reader_footnote_mark_color);
        drawable.setBounds(CanvasUtils.rectFToRect(markArea));
        drawable.draw(canvas);
        this.mFootnoteTouchable.hotArea.add(touchArea);
        this.mFootnoteTouchable.dispArea = dispArea;
        SpannableString footnoteStr = new SpannableString(str);
        SpanUtils.removeSpan(footnoteStr, FootnoteSpan.class);
        this.mFootnoteTouchable.str = footnoteStr;
    }

    public boolean canPinStopAfter() {
        return false;
    }
}
