package com.douban.book.reader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.douban.book.reader.R;
import com.douban.book.reader.app.App;
import com.douban.book.reader.event.ColorThemeChangedEvent;
import com.douban.book.reader.util.CanvasUtils;
import com.douban.book.reader.util.Font;
import com.douban.book.reader.util.PaintUtils;
import com.douban.book.reader.util.Res;
import com.douban.book.reader.util.StringUtils;
import com.douban.book.reader.util.Utils;
import com.douban.book.reader.util.ViewUtils;
import java.util.ArrayList;
import java.util.List;

public class IndexedSeekBar extends WrapSeekBar {
    private static final int MAX_LEVEL = 10000;
    private static final int SCALED_TOUCH_SLOP = ViewConfiguration.get(App.get()).getScaledTouchSlop();
    private static final int SCROLL_BY_CHAPTER_SWITCH_DISTANCE = -45;
    private List<Integer> mChapterHeadPages = new ArrayList();
    private boolean mIsDragging = false;
    private boolean mIsEnabled = true;
    private boolean mIsScrollByChapter = false;
    private int mMax;
    private OnReadSeekBarChangeListener mOnReadSeekBarChangeListener;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mProgress;
    private SliderDrawable mSlider = null;
    private int mSliderBgHeight = Utils.dp2pixel(7.0f);
    private int mThumbSize = Utils.dp2pixel(30.0f);
    private boolean mThumbTextVisible = true;
    private int mTouchDownX;
    private int mWidth = 0;

    private static class BackgroundDrawable extends Drawable {
        private int mBgColor;
        private int mSliderBgHeight;

        private BackgroundDrawable() {
        }

        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            int height = bounds.height();
            int width = bounds.width();
            int padding = (height - this.mSliderBgHeight) / 2;
            Paint paint = PaintUtils.obtainPaint();
            PaintUtils.applyNightModeMaskIfNeeded(paint);
            paint.setColor(this.mBgColor);
            canvas.drawRect((float) bounds.left, (float) (bounds.top + padding), (float) (bounds.left + ((getLevel() * width) / 10000)), (float) ((bounds.top + padding) + this.mSliderBgHeight), paint);
            PaintUtils.recyclePaint(paint);
        }

        public void setSliderBgHeight(int height) {
            this.mSliderBgHeight = height;
        }

        public void setBgColor(int color) {
            this.mBgColor = color;
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter cf) {
        }

        public int getOpacity() {
            return 0;
        }
    }

    public interface OnReadSeekBarChangeListener {
        void onProgressChanged(IndexedSeekBar indexedSeekBar, int i);

        void onScrollStatusChanged(IndexedSeekBar indexedSeekBar, boolean z);

        void onStartTrackingTouch(IndexedSeekBar indexedSeekBar);

        void onStopTrackingTouch(IndexedSeekBar indexedSeekBar);
    }

    private static class SliderDrawable extends Drawable {
        private static final int BORDER_WIDTH = Utils.dp2pixel(1.0f);
        private String mText;
        private int mThumbSize;

        private SliderDrawable() {
        }

        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            float x = (float) bounds.centerX();
            float y = (float) bounds.centerY();
            float radius = (float) (this.mThumbSize / 2);
            Paint paint = PaintUtils.obtainPaint();
            PaintUtils.applyNightModeMaskIfNeeded(paint);
            paint.setShadowLayer(5.0f, 0.0f, 0.0f, Res.getColorOverridingAlpha(R.color.black, 0.5f));
            paint.setColor(Res.getColor(R.color.white));
            canvas.drawCircle(x, y, radius, paint);
            paint.setColor(Res.getColor(R.color.green));
            paint.clearShadowLayer();
            canvas.drawCircle(x, y, radius - ((float) BORDER_WIDTH), paint);
            if (StringUtils.isNotEmpty(this.mText)) {
                paint.setColor(-1);
                paint.setTypeface(Font.SANS_SERIF_BOLD);
                paint.setTextSize((float) ((this.mThumbSize - (BORDER_WIDTH * 4)) / 3));
                CanvasUtils.drawTextCenteredOnPoint(canvas, paint, x, y, this.mText);
            }
            PaintUtils.recyclePaint(paint);
        }

        public void setAlpha(int alpha) {
        }

        public void setText(String text) {
            this.mText = text;
        }

        public void setThumbSize(int size) {
            this.mThumbSize = size;
        }

        public void setColorFilter(ColorFilter cf) {
        }

        public int getOpacity() {
            return 0;
        }

        public int getIntrinsicWidth() {
            return this.mThumbSize;
        }

        public int getIntrinsicHeight() {
            return this.mThumbSize;
        }
    }

    public IndexedSeekBar(Context context) {
        super(context);
        initView(context);
    }

    public IndexedSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public IndexedSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        updateProgressDrawable();
        initThumb();
        updateThumb();
        ViewUtils.setEventAware(this);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mWidth = getWidth();
        this.mPaddingLeft = getPaddingLeft();
        this.mPaddingRight = getPaddingRight();
    }

    public void onEventMainThread(ColorThemeChangedEvent event) {
        invalidate();
    }

    public void setOnReadSeekBarChangeListener(OnReadSeekBarChangeListener l) {
        this.mOnReadSeekBarChangeListener = l;
    }

    public void setOnSeekBarChangeListener(final OnSeekBarChangeListener listener) {
        setOnReadSeekBarChangeListener(new OnReadSeekBarChangeListener() {
            public void onProgressChanged(IndexedSeekBar seekBar, int progress) {
                listener.onProgressChanged(seekBar, progress, true);
            }

            public void onStartTrackingTouch(IndexedSeekBar seekBar) {
                listener.onStartTrackingTouch(seekBar);
            }

            public void onStopTrackingTouch(IndexedSeekBar seekBar) {
                listener.onStopTrackingTouch(seekBar);
            }

            public void onScrollStatusChanged(IndexedSeekBar seekBar, boolean isScrollByChapter) {
            }
        });
    }

    private void onStartTrackingTouch() {
        this.mIsDragging = true;
        if (this.mOnReadSeekBarChangeListener != null) {
            this.mOnReadSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    private void onStopTrackingTouch() {
        this.mIsDragging = false;
        if (this.mOnReadSeekBarChangeListener != null) {
            this.mOnReadSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            switch (event.getAction()) {
                case 0:
                    this.mTouchDownX = (int) event.getX();
                    setPressed(true);
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    attemptClaimDrag();
                    break;
                case 1:
                    if (this.mIsDragging) {
                        trackTouchEvent(event);
                        onStopTrackingTouch();
                        setPressed(false);
                    } else {
                        onStartTrackingTouch();
                        trackTouchEvent(event);
                        onStopTrackingTouch();
                    }
                    invalidate();
                    break;
                case 2:
                    if (!this.mIsDragging) {
                        if (Math.abs(event.getX() - ((float) this.mTouchDownX)) > ((float) SCALED_TOUCH_SLOP)) {
                            setPressed(true);
                            onStartTrackingTouch();
                            trackTouchEvent(event);
                            break;
                        }
                    }
                    trackTouchEvent(event);
                    break;
                    break;
                case 3:
                    if (this.mIsDragging) {
                        onStopTrackingTouch();
                        setPressed(false);
                    }
                    invalidate();
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private void attemptClaimDrag() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    private void trackTouchEvent(MotionEvent event) {
        float scale;
        int available = (this.mWidth - this.mPaddingLeft) - this.mPaddingRight;
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (x < this.mPaddingLeft) {
            scale = 0.0f;
        } else if (x > this.mWidth - this.mPaddingRight) {
            scale = 1.0f;
        } else {
            scale = ((float) (x - this.mPaddingLeft)) / ((float) available);
        }
        int progress = Math.round(((float) super.getMax()) * scale);
        if (y >= SCROLL_BY_CHAPTER_SWITCH_DISTANCE || this.mChapterHeadPages.isEmpty()) {
            setScrollByChapter(false);
        } else {
            setScrollByChapter(true);
            progress = nearestPage(progress);
        }
        setProgress(progress);
        if (this.mOnReadSeekBarChangeListener != null) {
            this.mOnReadSeekBarChangeListener.onProgressChanged(this, progress);
        }
    }

    private void setScrollByChapter(boolean scrollByChapter) {
        if (this.mIsScrollByChapter != scrollByChapter) {
            this.mIsScrollByChapter = scrollByChapter;
            if (this.mOnReadSeekBarChangeListener != null) {
                this.mOnReadSeekBarChangeListener.onScrollStatusChanged(this, scrollByChapter);
            }
        }
    }

    private int nearestPage(int progress) {
        int before = ((Integer) this.mChapterHeadPages.get(0)).intValue();
        int length = this.mChapterHeadPages.size();
        int next = ((Integer) this.mChapterHeadPages.get(length - 1)).intValue();
        for (int i = 1; i < length; i++) {
            if (progress <= ((Integer) this.mChapterHeadPages.get(i)).intValue()) {
                before = ((Integer) this.mChapterHeadPages.get(i - 1)).intValue();
                next = ((Integer) this.mChapterHeadPages.get(i)).intValue();
                break;
            }
        }
        if (Math.abs(before - progress) < Math.abs(next - progress)) {
            return before;
        }
        return next;
    }

    public int getMax() {
        return this.mMax;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public void setMax(int max) {
        this.mMax = max;
        super.setMax(max);
        updateThumb();
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        super.setProgress(progress);
        updateThumb();
    }

    public void setEnabled(boolean enabled) {
        this.mIsEnabled = enabled;
        updateThumb();
    }

    public void setSliderBgHeight(int height) {
        this.mSliderBgHeight = height;
        updateProgressDrawable();
    }

    public void setThumbSize(int size) {
        this.mThumbSize = size;
        initThumb();
        updateThumb();
    }

    public void setThumbTextVisible(boolean visible) {
        this.mThumbTextVisible = visible;
    }

    private void updateProgressDrawable() {
        BackgroundDrawable backgroundDrawable = new BackgroundDrawable();
        backgroundDrawable.setSliderBgHeight(this.mSliderBgHeight);
        backgroundDrawable.setBgColor(Res.getColor(R.color.bg_progress_bar));
        backgroundDrawable.setLevel(10000);
        BackgroundDrawable progressDrawable = new BackgroundDrawable();
        progressDrawable.setSliderBgHeight(this.mSliderBgHeight);
        progressDrawable.setBgColor(Res.getColorOverridingAlpha(R.color.green, 0.9f));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{backgroundDrawable, progressDrawable});
        layerDrawable.setId(0, 16908288);
        layerDrawable.setId(1, 16908301);
        setProgressDrawable(layerDrawable);
    }

    private void initThumb() {
        this.mSlider = new SliderDrawable();
        this.mSlider.setThumbSize(this.mThumbSize);
        setThumb(this.mSlider);
        setThumbOffset(0);
    }

    private void updateThumb() {
        if (this.mSlider == null) {
            initThumb();
        }
        if (this.mThumbTextVisible) {
            int percent;
            String text = "...";
            if (this.mMax > 0) {
                percent = (this.mProgress * 100) / this.mMax;
            } else {
                percent = 0;
            }
            if (isEnabled() && percent >= 0) {
                text = String.format("%2d%%", new Object[]{Integer.valueOf(percent)});
            }
            this.mSlider.setText(text);
            return;
        }
        this.mSlider.setText(null);
    }

    public void setIndexes(List<Integer> indexes) {
        if (indexes != null) {
            this.mChapterHeadPages = indexes;
        }
    }
}
