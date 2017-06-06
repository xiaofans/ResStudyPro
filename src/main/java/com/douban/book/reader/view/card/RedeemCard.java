package com.douban.book.reader.view.card;

import android.content.Context;
import com.douban.book.reader.view.RedeemView;
import com.douban.book.reader.view.RedeemView.RedeemViewListener;
import com.douban.book.reader.view.RedeemView_;

public class RedeemCard extends Card<RedeemCard> {
    private RedeemView mRedeemView = RedeemView_.build(getContext());

    public RedeemCard(Context context) {
        super(context);
        noDivider();
        content(this.mRedeemView);
    }

    public RedeemCard setRedeemViewListener(RedeemViewListener redeemViewListener) {
        this.mRedeemView.setListener(redeemViewListener);
        return this;
    }
}
