package com.douban.book.reader.view.store;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.douban.book.reader.R;
import com.douban.book.reader.adapter.ViewBinderAdapter;
import com.douban.book.reader.entity.Works;
import com.douban.book.reader.util.Res;
import com.douban.book.reader.util.ViewUtils;
import com.douban.book.reader.view.ExpandableHeightGridView;
import com.douban.book.reader.view.store.item.StoreWorksItemView;
import com.douban.book.reader.view.store.item.StoreWorksItemView_;
import java.util.List;
import org.androidannotations.annotations.EViewGroup;

@EViewGroup
public class WorksGridView extends ExpandableHeightGridView {
    private static final int COLUMN_COUNT = 3;
    private boolean mShowFirstRowOnly = true;
    private boolean mShowPrice;
    private ViewBinderAdapter<Works> mWorksAdapter = null;
    private List<Works> mWorksList = null;

    public WorksGridView(Context context) {
        super(context);
        init();
    }

    public WorksGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WorksGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        ViewUtils.of(this).width(-1).height(-2).commit();
        setNumColumns(3);
        setColumnWidth(-1);
        setVerticalSpacing(Res.getDimensionPixelSize(R.dimen.general_subview_vertical_padding_medium));
        setGravity(17);
        setExpanded(true);
        this.mWorksAdapter = new ViewBinderAdapter<Works>(StoreWorksItemView_.class) {
            protected void bindView(View itemView, Works data) {
                ((StoreWorksItemView) itemView).showPrice(WorksGridView.this.mShowPrice);
                super.bindView(itemView, data);
            }
        };
        setAdapter(this.mWorksAdapter);
    }

    public void setWorksList(List<Works> worksList) {
        this.mWorksList = worksList;
        updateView();
    }

    public void showFirstRowOnly(boolean showFirstRowOnly) {
        this.mShowFirstRowOnly = showFirstRowOnly;
        updateView();
    }

    public void showPrice(boolean showPrice) {
        this.mShowPrice = showPrice;
    }

    private void updateView() {
        if (this.mWorksList == null) {
            this.mWorksAdapter.clear();
            return;
        }
        if (this.mShowFirstRowOnly && this.mWorksList.size() > 3) {
            this.mWorksList = this.mWorksList.subList(0, 3);
        }
        this.mWorksAdapter.clear();
        this.mWorksAdapter.addAll(this.mWorksList);
    }
}
