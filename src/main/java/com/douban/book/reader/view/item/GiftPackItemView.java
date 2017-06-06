package com.douban.book.reader.view.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import com.douban.book.reader.R;
import com.douban.book.reader.adapter.ViewBinder;
import com.douban.book.reader.entity.GiftPack;
import com.douban.book.reader.fragment.GiftPackDetailFragment_;
import com.douban.book.reader.util.Res;
import com.douban.book.reader.util.ViewUtils;
import com.douban.book.reader.view.BoxedWorksView;
import com.douban.book.reader.view.BoxedWorksView_;

public class GiftPackItemView extends FrameLayout implements ViewBinder<GiftPack> {
    private BoxedWorksView mBoxedWorksView;

    public GiftPackItemView(Context context) {
        super(context);
        init(context);
    }

    public GiftPackItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GiftPackItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        this.mBoxedWorksView = BoxedWorksView_.build(context);
        ViewUtils.of(this.mBoxedWorksView).widthWrapContent().height(Res.getDimensionPixelSize(R.dimen.boxed_gift_view_height)).commit();
        this.mBoxedWorksView.isOpened(false);
        addView(this.mBoxedWorksView);
    }

    public void bindData(final GiftPack data) {
        if (this.mBoxedWorksView != null) {
            this.mBoxedWorksView.worksId(data.works.id).showQuantity(data.quantity).isDepleted(data.isDepleted()).invalidate();
            this.mBoxedWorksView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    GiftPackDetailFragment_.builder().packId(data.id).build().showAsActivity(GiftPackItemView.this);
                }
            });
        }
    }
}
