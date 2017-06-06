package com.douban.book.reader.helper;

import com.douban.book.reader.R;
import com.douban.book.reader.activity.HomeActivity;
import com.douban.book.reader.activity.StoreSearchActivity_;
import com.douban.book.reader.app.App;
import com.douban.book.reader.app.PageOpenHelper;
import com.douban.book.reader.fragment.SettingFragment_;
import com.douban.book.reader.fragment.ShelfFragment_;
import com.douban.book.reader.fragment.StoreFragment_;
import com.douban.book.reader.fragment.TestFieldFragment_;
import com.douban.book.reader.fragment.TestWorksFragment_;

public class DrawerItemClickHelper {
    public static final String TAG = DrawerItemClickHelper.class.getSimpleName();

    public void performClick(int clickedItemId, PageOpenHelper helper) {
        switch (clickedItemId) {
            case R.id.drawer_menu_shelf:
                HomeActivity.showContent(helper, ShelfFragment_.class);
                return;
            case R.id.drawer_menu_store:
                HomeActivity.showContent(helper, StoreFragment_.class);
                return;
            case R.id.drawer_menu_search:
                helper.open(StoreSearchActivity_.intent(App.get()).get());
                return;
            case R.id.drawer_menu_test_field:
                HomeActivity.showContent(helper, TestFieldFragment_.class);
                return;
            case R.id.drawer_menu_test_works:
                HomeActivity.showContent(helper, TestWorksFragment_.class);
                return;
            case R.id.drawer_menu_settings:
                SettingFragment_.builder().build().showAsActivity(helper);
                return;
            default:
                return;
        }
    }
}
