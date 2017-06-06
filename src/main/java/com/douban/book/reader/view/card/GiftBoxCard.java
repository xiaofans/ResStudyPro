package com.douban.book.reader.view.card;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import com.douban.book.reader.R;
import com.douban.book.reader.entity.Gift;
import com.douban.book.reader.entity.GiftPack;
import com.douban.book.reader.fragment.WorksProfileFragment_;
import com.douban.book.reader.util.Res;
import com.douban.book.reader.util.ViewUtils;
import com.douban.book.reader.view.BoxedWorksView;
import com.douban.book.reader.view.BoxedWorksView_;
import org.androidannotations.annotations.EViewGroup;

@EViewGroup
public class GiftBoxCard extends Card<GiftBoxCard> {
    private BoxedWorksView mBoxedWorksView;

    public GiftBoxCard(Context context) {
        super(context);
    }

    private void addBoxView(int height) {
        this.mBoxedWorksView = BoxedWorksView_.build(getContext());
        ViewUtils.of(this.mBoxedWorksView).widthMatchParent().height(height).commit();
        content(this.mBoxedWorksView);
    }

    public GiftBoxCard gift(final Gift gift) {
        addBoxView(Res.getDimensionPixelSize(R.dimen.boxed_gift_view_height));
        this.mBoxedWorksView.worksId(gift.works.id).isOpened(true).showBoxCover(true);
        this.mBoxedWorksView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                WorksProfileFragment_.builder().worksId(gift.works.id).build().showAsActivity(GiftBoxCard.this);
            }
        });
        return this;
    }

    public GiftBoxCard giftPack(final GiftPack giftPack) {
        addBoxView(Res.getDimensionPixelSize(R.dimen.boxed_gift_view_height_large));
        ViewUtils.of(this.mBoxedWorksView).bottomMargin(-Res.getDimensionPixelSize(R.dimen.general_subview_vertical_padding_medium)).commit();
        this.mBoxedWorksView.worksId(giftPack.works.id).isOpened(false);
        this.mBoxedWorksView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                WorksProfileFragment_.builder().worksId(giftPack.works.id).build().showAsActivity(GiftBoxCard.this);
            }
        });
        return this;
    }
}
