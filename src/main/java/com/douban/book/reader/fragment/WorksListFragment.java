package com.douban.book.reader.fragment;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import com.douban.book.reader.R;
import com.douban.book.reader.activity.StoreSearchActivity_;
import com.douban.book.reader.adapter.BaseArrayAdapter;
import com.douban.book.reader.adapter.ViewBinderAdapter;
import com.douban.book.reader.app.App;
import com.douban.book.reader.constant.Key;
import com.douban.book.reader.entity.Works;
import com.douban.book.reader.helper.WorksListUri;
import com.douban.book.reader.helper.WorksListUri.Type;
import com.douban.book.reader.manager.Lister;
import com.douban.book.reader.manager.WorksManager;
import com.douban.book.reader.theme.ThemedAttrs;
import com.douban.book.reader.util.DebugSwitch;
import com.douban.book.reader.util.Logger;
import com.douban.book.reader.util.StringUtils;
import com.douban.book.reader.util.ViewUtils;
import com.douban.book.reader.view.ChartWorksItemView;
import com.douban.book.reader.view.ChartWorksItemView_;
import com.douban.book.reader.view.CoverLeftWorksView;
import com.douban.book.reader.view.CoverLeftWorksView_;
import com.douban.book.reader.view.ParagraphView;
import com.douban.book.reader.view.ParagraphView.Indent;
import com.mcxiaoke.next.ui.endless.EndlessListView;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;

@EFragment
@OptionsMenu({2131623950})
public class WorksListFragment extends BaseEndlessListFragment<Works> {
    private FrameLayout mListHeaderView;
    private ParagraphView mListMetaView;
    private boolean mShouldShowPrice;
    @Bean
    WorksManager mWorksManager;
    @FragmentArg
    Uri uri;

    public static class WorksListMeta {
        public String description;
        public String icon;
        public String title;
    }

    @AfterViews
    void init() {
        this.mShouldShowPrice = WorksListUri.shouldShowPrice(this.uri);
    }

    public Lister<Works> onCreateLister() {
        if (this.uri != null) {
            return this.mWorksManager.worksLister(this.uri);
        }
        return null;
    }

    public BaseArrayAdapter<Works> onCreateAdapter() {
        if (WorksListUri.getType(this.uri) == Type.RANK) {
            return new ViewBinderAdapter<Works>(ChartWorksItemView_.class) {
                protected void bindView(View itemView, Works data) {
                    ((ChartWorksItemView) itemView).setRankNum(getPosition(data) + 1);
                    super.bindView(itemView, data);
                }
            };
        }
        return new ViewBinderAdapter<Works>(CoverLeftWorksView_.class) {
            protected void bindView(View itemView, Works data) {
                ((CoverLeftWorksView) itemView).showAbstract();
                ((CoverLeftWorksView) itemView).showRatingInfo(true);
                ((CoverLeftWorksView) itemView).showPrice(WorksListFragment.this.mShouldShowPrice);
                super.bindView(itemView, data);
            }
        };
    }

    protected void onListViewCreated(EndlessListView listView) {
        this.mListMetaView = new ParagraphView(App.get());
        ThemedAttrs.ofView(this.mListMetaView).append(R.attr.textColorArray, Integer.valueOf(R.array.content_text_color)).updateView();
        ViewUtils.setTextAppearance(App.get(), this.mListMetaView, R.style.AppWidget.Text.Content.Block);
        this.mListMetaView.setFirstLineIndent(Indent.AUTO);
        ViewUtils.setVerticalPaddingResId(this.mListMetaView, R.dimen.general_subview_vertical_padding_medium);
        ViewUtils.setHorizontalPaddingResId(this.mListMetaView, R.dimen.page_horizontal_padding);
        this.mListHeaderView = new FrameLayout(getActivity());
        addHeaderView(this.mListHeaderView);
        switch (WorksListUri.getType(this.uri)) {
            case TOPIC:
                setTitle((int) R.string.title_works_list_topic);
                break;
            case TAG:
                setTitle((int) R.string.title_works_list_tag);
                break;
            case RECOMMENDATION:
                setTitle((int) R.string.title_works_list_recommendation);
                break;
            case RANK:
                setTitle((int) R.string.title_works_list_chart);
                break;
            default:
                setTitle((int) R.string.title_works_list);
                break;
        }
        loadMeta();
    }

    @OptionsItem({2131558990})
    void onMenuItemSearchClicked() {
        StoreSearchActivity_.intent((Fragment) this).start();
    }

    @Background
    void loadMeta() {
        try {
            updateMetaView(this.mWorksManager.worksListMeta(this.uri));
        } catch (Exception e) {
            Logger.e(this.TAG, e);
        }
    }

    @UiThread
    void updateMetaView(WorksListMeta meta) {
        if (meta != null) {
            setTitle(meta.title);
            String metaText = meta.description;
            if (DebugSwitch.on(Key.APP_DEBUG_SHOW_WEBVIEW_URL)) {
                metaText = String.format("%s%n%s", new Object[]{this.uri, metaText});
            }
            if (StringUtils.isNotEmpty(metaText)) {
                this.mListMetaView.setParagraphText(metaText);
                this.mListView.setHeaderDividersEnabled(true);
                this.mListHeaderView.addView(this.mListMetaView);
                return;
            }
            this.mListHeaderView.removeView(this.mListMetaView);
        }
    }

    @ItemClick({2131558592})
    void onWorksItemClicked(Works works) {
        if (works != null) {
            WorksProfileFragment_.builder().worksId(works.id).build().showAsActivity((Fragment) this);
        }
    }
}
