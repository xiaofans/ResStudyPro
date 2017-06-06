package com.douban.book.reader.view.store.card;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import com.douban.book.reader.app.PageOpenHelper;
import com.douban.book.reader.entity.store.NewWorksStoreWidgetEntity;
import com.douban.book.reader.view.store.card.content.NewWorksWidgetContentView;
import org.androidannotations.annotations.EViewGroup;

@EViewGroup
public class NewWorksWidgetCard extends BaseWidgetCard<NewWorksStoreWidgetEntity> {
    public NewWorksWidgetCard(Context context) {
        super(context);
    }

    protected void onEntityBound(final NewWorksStoreWidgetEntity entity) {
        if (entity.payload != null) {
            moreBtnVisible(true);
            clickListener(new OnClickListener() {
                public void onClick(View v) {
                    PageOpenHelper.from(NewWorksWidgetCard.this).open(entity.payload.uri);
                }
            });
            if (entity.payload.worksList == null || entity.payload.worksList.isEmpty()) {
                hide();
                return;
            }
            setVisibility(0);
            ((NewWorksWidgetContentView) getOrCreateContentView(NewWorksWidgetContentView.class)).setWorksList(entity.payload.worksList);
            return;
        }
        hide();
    }
}
