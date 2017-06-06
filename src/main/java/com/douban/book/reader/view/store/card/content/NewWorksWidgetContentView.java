package com.douban.book.reader.view.store.card.content;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import com.douban.book.reader.app.App;
import com.douban.book.reader.entity.Works;
import com.douban.book.reader.fragment.WorksProfileFragment_;
import com.douban.book.reader.view.CoverLeftWorksView;
import com.douban.book.reader.view.CoverLeftWorksView_;
import com.douban.book.reader.view.store.IndicatedViewPager;
import java.util.List;

public class NewWorksWidgetContentView extends IndicatedViewPager {
    private List<Works> mWorksList;

    public NewWorksWidgetContentView(Context context) {
        super(context);
    }

    public NewWorksWidgetContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewWorksWidgetContentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setWorksList(List<Works> worksList) {
        if (worksList != null) {
            this.mWorksList = worksList;
            populateData();
        }
    }

    protected int getPageCount() {
        return this.mWorksList != null ? this.mWorksList.size() : 0;
    }

    protected View getPageView(int page) {
        CoverLeftWorksView worksView = CoverLeftWorksView_.build(App.get()).noVerticalPadding();
        worksView.showAbstract();
        final Works works = getWorks(page);
        if (works != null) {
            worksView.bindData(works);
            worksView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    WorksProfileFragment_.builder().worksId(works.id).build().showAsActivity(NewWorksWidgetContentView.this);
                }
            });
        }
        return worksView;
    }

    private Works getWorks(int page) {
        try {
            return (Works) this.mWorksList.get(page);
        } catch (Throwable th) {
            return null;
        }
    }
}
