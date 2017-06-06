package com.douban.book.reader.view.store;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.douban.book.reader.R;
import com.douban.book.reader.app.PageOpenHelper;
import com.douban.book.reader.entity.store.LinksStoreWidgetEntity.Link;
import com.douban.book.reader.theme.ThemedAttrs;
import com.douban.book.reader.util.Analysis;
import com.douban.book.reader.util.ImageLoaderUtils;
import com.douban.book.reader.util.ViewUtils;

public class LinkImageView extends ImageView {
    public LinkImageView(Context context) {
        super(context);
        init();
    }

    public LinkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinkImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setAdjustViewBounds(true);
        ViewUtils.of(this).width(-1).height(-2).commit();
        ThemedAttrs.ofView(this).append(R.attr.autoDimInNightMode, Boolean.valueOf(true)).updateView();
        setScaleType(ScaleType.CENTER_CROP);
    }

    public void setData(Link link) {
        setImage(link.img);
        setLinkUri(link.uri);
    }

    public void setImage(String image) {
        ImageLoaderUtils.displayImage(image, this);
    }

    public void setLinkUri(final String uri) {
        setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PageOpenHelper.from(LinkImageView.this).open(uri);
                Analysis.sendEventWithExtra("banner", "click", uri);
            }
        });
    }

    public void setLinkUri(final Uri uri) {
        setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PageOpenHelper.from(LinkImageView.this).open(uri);
                Analysis.sendEventWithExtra("banner", "click", String.valueOf(uri));
            }
        });
    }
}
