package com.douban.book.reader.view.item;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.adapter.ViewBinder;
import com.douban.book.reader.entity.Notification;
import com.douban.book.reader.theme.ThemedAttrs;
import com.douban.book.reader.util.ThemedUtils;
import com.douban.book.reader.util.ViewUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(2130903171)
public class NotificationItemView extends RelativeLayout implements ViewBinder<Notification> {
    @ViewById(2131558769)
    TextView mContent;
    @ViewById(2131558884)
    ImageView mIconUnRead;

    public NotificationItemView(Context context) {
        super(context);
    }

    public NotificationItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotificationItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @AfterViews
    void init() {
        ViewUtils.setVerticalPaddingResId(this, R.dimen.general_subview_vertical_padding_medium);
        ViewUtils.setHorizontalPaddingResId(this, R.dimen.general_subview_horizontal_padding_small);
        ThemedAttrs.ofView(this).append(R.attr.backgroundDrawableArray, Integer.valueOf(R.array.bg_list_item));
        ThemedUtils.updateView(this);
        setGravity(16);
        setDescendantFocusability(393216);
        ViewUtils.of(this).width(-1).height(-2).commit();
    }

    public void bindData(Notification notification) {
        boolean z;
        boolean z2 = true;
        this.mContent.setText(notification.content);
        ThemedAttrs.ofView(this.mContent).append(R.attr.textColorArray, Integer.valueOf(notification.hasRead ? R.array.secondary_text_color : R.array.content_text_color)).updateView();
        if (notification.hasRead) {
            z = false;
        } else {
            z = true;
        }
        ViewUtils.visibleIf(z, this.mIconUnRead);
        if (notification.hasRead) {
            z2 = false;
        }
        setActivated(z2);
    }
}
