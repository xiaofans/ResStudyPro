package com.douban.book.reader.content.paragraph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import com.douban.book.reader.app.App;
import com.douban.book.reader.constant.Char;
import com.douban.book.reader.util.Logger;
import com.douban.book.reader.util.PaintUtils;
import com.douban.book.reader.util.StringUtils;
import com.douban.book.reader.util.Utils;
import java.util.ArrayList;

public class Code {
    public static final int DEFAULT_BACKGROUND_COLOR = Color.argb(48, 128, 128, 128);
    public static final float DEFAULT_PADDING = ((float) Utils.dp2pixel(3.0f));
    private static final int LINEFEED_WIDTH = 8;
    private static final float LINE_SPACING_RATIO = 0.2f;
    private static final int TAB_WIDTH = 4;
    private static final String TAG = "Code";
    private static Paint sBorderPaint = new Paint();
    private App mApp = App.get();
    private boolean mChanged = false;
    private float mCharWidth;
    private ArrayList<Line> mCodeLineArray = new ArrayList();
    private int mGeneratedScale = -1;
    private boolean mInlineMode = false;
    private float mLeftMargin;
    private float mLineHeight;
    private float mPadding = DEFAULT_PADDING;
    private Paint mPaint = new Paint();
    private String mText;
    private float mTextAreaWidth;
    private float mTextSize;
    private float mTopMargin;
    private float mWidth;

    private static class Line {
        int end;
        int indent;
        int start;

        private Line() {
        }
    }

    static {
        sBorderPaint.setColor(DEFAULT_BACKGROUND_COLOR);
    }

    public Code() {
        this.mPaint.setTypeface(Typeface.MONOSPACE);
        this.mPaint.setSubpixelText(true);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setHinting(1);
    }

    public void setLeftMargin(float leftMargin) {
        if (this.mLeftMargin != leftMargin) {
            this.mLeftMargin = leftMargin;
            this.mChanged = true;
        }
    }

    public void setWidth(float width) {
        if (this.mWidth != width) {
            this.mWidth = width;
            this.mTextAreaWidth = this.mWidth - (this.mPadding * 2.0f);
            this.mChanged = true;
        }
    }

    public void setTextColor(int color) {
        this.mPaint.setColor(color);
    }

    public int getStretchPointCount() {
        return 0;
    }

    public void setTextSize(float textSize) {
        if (this.mTextSize != textSize) {
            this.mTextSize = textSize;
            this.mLineHeight = this.mTextSize * 1.2f;
            this.mTopMargin = this.mTextSize * LINE_SPACING_RATIO;
            this.mTextAreaWidth = this.mWidth - (this.mPadding * 2.0f);
            this.mPaint.setTextSize(textSize);
            this.mCharWidth = this.mPaint.measureText(" ");
            this.mChanged = true;
        }
    }

    public void setText(String text) {
        if (this.mText == null || !this.mText.equals(text)) {
            this.mText = text;
            this.mChanged = true;
        }
    }

    public void setInlineMode(boolean inlineMode) {
        this.mInlineMode = inlineMode;
    }

    public float getWidth() {
        if (this.mInlineMode) {
            return this.mPaint.measureText(this.mText) + (this.mPadding * 2.0f);
        }
        return this.mWidth;
    }

    public float getHeight() {
        return getHeight(0);
    }

    public float getHeight(int startLine) {
        if (needRegenerate()) {
            generate();
        }
        return (this.mLineHeight * ((float) (this.mCodeLineArray.size() - startLine))) + ((this.mTopMargin + this.mPadding) * 2.0f);
    }

    public int getPagableLineNum(int startLine, float desiredOffset) {
        float space = this.mTopMargin + this.mPadding;
        if (desiredOffset < this.mLineHeight + (2.0f * space)) {
            return 0;
        }
        int lineNum = startLine;
        float height = space;
        while (this.mLineHeight + height < desiredOffset) {
            height += this.mLineHeight;
            lineNum++;
        }
        if (lineNum >= this.mCodeLineArray.size()) {
            return this.mCodeLineArray.size() - 1;
        }
        return lineNum;
    }

    public int getCharOffsetByLineNum(int lineNum) {
        try {
            return ((Line) this.mCodeLineArray.get(lineNum)).start;
        } catch (Exception e) {
            Logger.e(TAG, e);
            return 0;
        }
    }

    private boolean needRegenerate() {
        if (!this.mChanged && this.mGeneratedScale == this.mApp.getScale()) {
            return false;
        }
        return true;
    }

    public void draw(Canvas canvas, float offsetX, float offsetY, int startLine, int endLine) {
        if (needRegenerate()) {
            generate();
        }
        if (endLine <= 0 || endLine > this.mCodeLineArray.size()) {
            endLine = this.mCodeLineArray.size();
        }
        float shadowLeft = offsetX + this.mLeftMargin;
        float shadowTop = offsetY + this.mTopMargin;
        Canvas canvas2 = canvas;
        canvas2.drawRoundRect(new RectF(shadowLeft, shadowTop, shadowLeft + this.mWidth, ((((this.mTopMargin + this.mPadding) * 2.0f) + offsetY) + (this.mLineHeight * ((float) (endLine - startLine)))) - this.mTopMargin), 2.0f, 2.0f, sBorderPaint);
        float x = (this.mLeftMargin + offsetX) + this.mPadding;
        float y = ((this.mPadding + shadowTop) + this.mLineHeight) - this.mPaint.descent();
        for (int i = startLine; i < endLine; i++) {
            Line line = (Line) this.mCodeLineArray.get(i);
            canvas.drawText(this.mText.substring(line.start, line.end), (((float) line.indent) * this.mCharWidth) + x, y, this.mPaint);
            y += this.mLineHeight;
        }
    }

    private void generate() {
        this.mCodeLineArray.clear();
        int maxCharInLine = (int) (this.mTextAreaWidth / this.mCharWidth);
        int maxIndent = maxCharInLine / 2;
        int indentReduceRatio = 1;
        if (maxCharInLine < 50) {
            indentReduceRatio = 2;
        }
        int start = 0;
        while (start < this.mText.length()) {
            int end = findLineEnd(this.mText, start);
            String strLine = this.mText.substring(start, end);
            int indent = getLeadingSpaceCount(strLine);
            if (indent > maxIndent) {
                indent = maxIndent;
            }
            start += getLeadingSpaceCharCount(strLine);
            strLine = StringUtils.trimWhitespace(strLine).toString();
            int reducedIndent = reduceIndent(indent, indentReduceRatio);
            int pos = getBreakUpPositionByWidth(strLine, this.mTextAreaWidth - (((float) reducedIndent) * this.mCharWidth));
            Line line = new Line();
            line.indent = reducedIndent;
            line.start = start;
            line.end = start + pos;
            this.mCodeLineArray.add(line);
            while (strLine.length() > pos) {
                start += pos;
                strLine = strLine.substring(pos);
                start += getLeadingSpaceCharCount(strLine);
                strLine = StringUtils.trimWhitespace(strLine).toString();
                line = new Line();
                line.indent = reduceIndent(indent + 8, indentReduceRatio);
                pos = getBreakUpPositionByWidth(strLine, this.mTextAreaWidth - (((float) line.indent) * this.mCharWidth));
                if (pos == 0) {
                    line.indent = 8;
                    float maxWidth = this.mTextAreaWidth - (((float) line.indent) * this.mCharWidth);
                    pos = getBreakUpPositionByWidth(strLine, maxWidth);
                    if (pos == 0) {
                        pos = getBreakUpPositionByWidth(strLine, maxWidth, false);
                    }
                }
                line.start = start;
                line.end = start + pos;
                this.mCodeLineArray.add(line);
            }
            start = end;
        }
        this.mGeneratedScale = this.mApp.getScale();
        this.mChanged = false;
    }

    private int reduceIndent(int indent, int reduceBy) {
        if (reduceBy == 1) {
            return indent;
        }
        int i = Math.round(((float) indent) / ((float) reduceBy));
        if (i % 2 != 0) {
            i++;
        }
        return i;
    }

    private int findLineEnd(String str, int start) {
        while (start < str.length()) {
            int start2 = start + 1;
            char c = str.charAt(start);
            if (c == Char.CARRIAGE_RETURN || c == '\n') {
                if (c == Char.CARRIAGE_RETURN && start2 < str.length() - 1 && str.charAt(start2 + 1) == '\n') {
                    start = start2 + 1;
                } else {
                    start = start2;
                }
                if (start <= str.length()) {
                    return str.length();
                }
                return start;
            }
            start = start2;
        }
        if (start <= str.length()) {
            return start;
        }
        return str.length();
    }

    private int getLeadingSpaceCount(String str) {
        int count = 0;
        int index = 0;
        while (index < str.length()) {
            int index2 = index + 1;
            char c = str.charAt(index);
            if (!Character.isWhitespace(c) || c == Char.CARRIAGE_RETURN) {
                index = index2;
                break;
            } else if (c == '\n') {
                index = index2;
                break;
            } else {
                if (c == '\t') {
                    count += 4;
                } else {
                    count++;
                }
                index = index2;
            }
        }
        return count;
    }

    private int getLeadingSpaceCharCount(String str) {
        int count = 0;
        int index = 0;
        while (index < str.length()) {
            int index2 = index + 1;
            char c = str.charAt(index);
            if (!Character.isWhitespace(c) || c == Char.CARRIAGE_RETURN) {
                break;
            } else if (c == '\n') {
                index = index2;
                break;
            } else {
                count++;
                index = index2;
            }
        }
        return count;
    }

    public int getBreakUpPositionByWidth(String str, float width) {
        return getBreakUpPositionByWidth(str, width, true);
    }

    public int getBreakUpPositionByWidth(String str, float width, boolean dontBreakWord) {
        int pos = PaintUtils.breakText(this.mPaint, str, width);
        if (pos == str.length()) {
            return pos;
        }
        if (dontBreakWord) {
            int i = pos;
            while (i > 0) {
                char ch = str.charAt(i - 1);
                if (!Character.isLetterOrDigit(ch) && ch != Char.UNDERLINE) {
                    pos = i;
                    break;
                }
                i--;
            }
            if (i == 0 && this.mInlineMode) {
                pos = i;
            }
        }
        return pos;
    }
}
