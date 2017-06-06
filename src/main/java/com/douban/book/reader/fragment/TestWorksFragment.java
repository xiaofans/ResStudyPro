package com.douban.book.reader.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.app.App;
import com.douban.book.reader.theme.ThemedAttrs;
import com.douban.book.reader.util.StringUtils;
import com.douban.book.reader.util.Utils;
import com.douban.book.reader.util.ViewUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(2130903110)
public class TestWorksFragment extends BaseFragment {
    @ViewById(2131558654)
    EditText mEditWorksId;
    @ViewById(2131558653)
    LinearLayout mLayoutBase;

    @AfterViews
    void init() {
        addWorks(130429, "测试文章", "各种case");
        addWorks(4420029, "放开那个Catalan数", "测试专栏");
        addWorks(237278, "思考的乐趣", "公式");
        addWorks(4101791, "帝都本格日本料理", "已完结专栏");
        addWorks(4134961, "旅行手绘小课堂", "未完结专栏");
        addWorks(14732532, "大明风月", "连载");
        addWorks(4356, "A Room of One's Own", "英文");
        addWorks(1239860, "爱德华三世", "英文");
        addWorks(4081549, "Java线程", "代码");
        addWorks(1499455, "Python源码剖析", "代码");
        addWorks(2118038, "前途", "画册模式1，无图注");
        addWorks(9540543, "焦虑的时候可以画画", "画册模式1，有图注");
        addWorks(1872918, "一个人", "画册模式2，有图注");
        addWorks(5243699, "Yoga", "画册模式2，部分页面无图注");
        addWorks(958945, "三体全集", "长文");
        addWorks(4929800, "安珀志全集", "长文");
    }

    @Click({2131558655})
    void onBtnOpenWorksClicked() {
        openWorks(StringUtils.toInt(this.mEditWorksId.getText().toString()));
    }

    @Click({2131558656})
    void onBtnOpenColumnClicked() {
        openColumn(StringUtils.toInt(this.mEditWorksId.getText().toString()));
    }

    private void addWorks(final int worksId, String title, String description) {
        TextView textView = new TextView(App.get());
        textView.setText(String.format("%s %s（%s）", new Object[]{Integer.valueOf(worksId), title, description}));
        textView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TestWorksFragment.this.openWorks(worksId);
            }
        });
        ThemedAttrs.ofView(textView).append(R.attr.textColorArray, Integer.valueOf(R.array.content_text_color)).updateView();
        ViewUtils.setVerticalPaddingResId(textView, R.dimen.general_subview_vertical_padding_normal);
        Utils.changeFonts(textView);
        this.mLayoutBase.addView(textView);
    }

    private void openWorks(int worksId) {
        WorksProfileFragment_.builder().worksId(worksId).build().showAsActivity((Fragment) this);
    }

    private void openColumn(int columnId) {
        WorksProfileFragment_.builder().legacyColumnId(columnId).build().showAsActivity((Fragment) this);
    }
}
