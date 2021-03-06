package com.douban.book.reader.view.store.card;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import com.douban.book.reader.R;
import com.douban.book.reader.app.PageOpenHelper;
import com.douban.book.reader.entity.Works;
import com.douban.book.reader.entity.store.PromotionStoreWidgetEntity;
import com.douban.book.reader.helper.WorksListUri.Display;
import com.douban.book.reader.util.IterableUtils;
import com.douban.book.reader.util.StringUtils;
import com.douban.book.reader.view.store.WorksGridView;
import java.util.List;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;

@EViewGroup
public class PromotionWidgetCard extends BaseWidgetCard<PromotionStoreWidgetEntity> {
    WorksGridView mWorksGridView;

    public PromotionWidgetCard(Context context) {
        super(context);
    }

    @AfterViews
    void init() {
        content((int) R.layout.card_content_promotion);
        noContentPadding();
        this.mWorksGridView = (WorksGridView) findViewById(R.id.top_works);
    }

    protected void onEntityBound(final PromotionStoreWidgetEntity entity) {
        if (entity == null || entity.payload == null || entity.payload.worksList == null || entity.payload.worksList.size() <= 0) {
            hide();
            return;
        }
        setVisibility(0);
        List<Works> worksList = entity.payload.worksList;
        if (StringUtils.isNotEmpty(entity.payload.uri)) {
            moreBtnVisible(true);
            clickListener(new OnClickListener() {
                public void onClick(View v) {
                    PageOpenHelper.from(PromotionWidgetCard.this).open(entity.payload.uri);
                }
            });
        }
        this.mWorksGridView.showPrice(IterableUtils.containsAny(entity.payload.display, String.valueOf(Display.PRICE)));
        this.mWorksGridView.setWorksList(worksList);
    }
}
