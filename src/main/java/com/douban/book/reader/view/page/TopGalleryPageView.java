package com.douban.book.reader.view.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.douban.book.reader.R;
import com.douban.book.reader.content.paragraph.IllusParagraph;
import com.douban.book.reader.content.paragraph.IllusParagraph.ClipMode;
import com.douban.book.reader.content.touchable.Touchable;
import com.douban.book.reader.util.PaintUtils;
import com.douban.book.reader.util.Res;
import com.douban.book.reader.util.Utils;
import com.douban.book.reader.util.ViewUtils;
import java.util.List;

public class TopGalleryPageView extends AbsGalleryPageView {
    private static final int BUTTON_HEIGHT = 42;
    private Rect mImageAreaRect;
    private RectF mImageLayoutRect;

    public TopGalleryPageView(Context context) {
        super(context);
    }

    protected void initView() {
        inflate(getContext(), R.layout.top_page_view_gallery, this);
        super.initView();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int legendHeight = getPageHeight() / 4;
        final int btnTopMargin = (getPageHeight() - legendHeight) - Utils.dp2pixel(42.0f);
        post(new Runnable() {
            public void run() {
                ViewUtils.setTopMargin(TopGalleryPageView.this.mBtnMore, btnTopMargin);
                ViewUtils.setTopMargin(TopGalleryPageView.this.mBtnShare, btnTopMargin);
            }
        });
        this.mImageAreaRect = new Rect(0, 0, getPageWidth(), getPageHeight() - legendHeight);
        this.mImageLayoutRect = new RectF(0.0f, getHeaderHeight(), (float) getPageWidth(), (float) ((getPageHeight() - legendHeight) - Utils.dp2pixel(42.0f)));
    }

    public List<Touchable> getTouchableArray() {
        if (this.mParagraph == null) {
            return EMPTY_TOUCHABLE_LIST;
        }
        return this.mParagraph.getTouchableArray();
    }

    protected int getHeaderBgColor() {
        return Res.getColor(R.array.reader_gallery_photo_bg_color);
    }

    protected void drawPage(Canvas canvas) {
        super.drawPage(canvas);
        if (((float) canvas.getClipBounds().bottom) > this.mMarginTop) {
            Paint paint = PaintUtils.obtainPaint();
            paint.setColor(Res.getColor(R.array.reader_gallery_photo_bg_color));
            canvas.drawRect(this.mImageAreaRect, paint);
            PaintUtils.recyclePaint(paint);
            if (this.mParagraph instanceof IllusParagraph) {
                IllusParagraph paragraph = this.mParagraph;
                paragraph.setOnGetDrawableListener(this.mOnGetDrawableListener);
                paragraph.setClipMode(ClipMode.FIT_WIDTH);
                paragraph.setLayoutRect(this.mImageLayoutRect);
                Drawable illusDrawable = this.mOnGetDrawableListener.getDrawable(paragraph.getIllusSeq());
                if (illusDrawable != null) {
                    Bitmap bitmap = ((BitmapDrawable) illusDrawable).getBitmap();
                    if ((bitmap.getHeight() > 2048 || bitmap.getWidth() > 2048) && ViewUtils.isSoftLayerType(this)) {
                        ViewUtils.setSoftLayerType(this);
                        invalidate();
                        return;
                    }
                }
                paragraph.drawIllus(canvas);
            }
        }
    }
}
