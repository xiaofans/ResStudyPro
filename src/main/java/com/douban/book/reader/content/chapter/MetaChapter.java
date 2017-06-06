package com.douban.book.reader.content.chapter;

import android.content.Context;
import com.douban.book.reader.view.page.AbsPageView;
import com.douban.book.reader.view.page.MetaPageView_;

public class MetaChapter extends PseudoChapter {
    public MetaChapter(int worksId) {
        super(worksId);
    }

    public int getPackageId() {
        return -103;
    }

    public AbsPageView getPageView(Context context, int pageNum) {
        return MetaPageView_.build(context);
    }
}
