package com.douban.book.reader.view.store.card;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import com.douban.book.reader.app.PageOpenHelper;
import com.douban.book.reader.entity.store.MoreWorksStoreWidgetEntity;
import com.douban.book.reader.view.store.card.content.MoreWorksWidgetContentView_;
import org.androidannotations.annotations.EViewGroup;

@EViewGroup
public class MoreWorksWidgetCard extends BaseWidgetCard<MoreWorksStoreWidgetEntity> {
    public MoreWorksWidgetCard(Context context) {
        super(context);
    }

    protected void onEntityBound(final MoreWorksStoreWidgetEntity entity) {
        if (!(entity.payload == null || entity.payload.moreBtn == null)) {
            titleClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PageOpenHelper.from(MoreWorksWidgetCard.this).open(entity.payload.moreBtn.uri);
                }
            });
        }
        if (entity.payload == null || entity.payload.worksList == null || entity.payload.worksList.size() <= 0) {
            hide();
            return;
        }
        setVisibility(0);
        ((MoreWorksWidgetContentView_) getOrCreateContentView(MoreWorksWidgetContentView_.class)).setStoreWidgetEntity(entity);
    }
}
