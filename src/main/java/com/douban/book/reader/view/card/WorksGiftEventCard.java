package com.douban.book.reader.view.card;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.douban.book.reader.R;
import com.douban.book.reader.app.PageOpenHelper;
import com.douban.book.reader.entity.GiftEvent;
import com.douban.book.reader.event.ColorThemeChangedEvent;
import com.douban.book.reader.fragment.GiftPackCreateFragment_;
import com.douban.book.reader.lib.view.CropImageView;
import com.douban.book.reader.theme.Theme;
import com.douban.book.reader.theme.ThemedAttrs;
import com.douban.book.reader.util.ImageLoaderUtils;
import com.douban.book.reader.util.RichText;
import com.douban.book.reader.util.ViewUtils;
import org.androidannotations.annotations.EViewGroup;

@EViewGroup
public class WorksGiftEventCard extends Card<WorksGiftEventCard> {
    private Button mButton = ((Button) findViewById(R.id.button));
    private CropImageView mImage = ((CropImageView) findViewById(R.id.image));

    public WorksGiftEventCard(Context context) {
        super(context);
        content((int) R.layout.card_works_gift_event);
        noContentPadding();
        this.mImage.setCropAlign(3);
        this.mImage.setCropType(2);
        this.mButton.setText(RichText.textWithIcon((int) R.drawable.v_gift, (int) R.string.select_this_book_to_present));
        ViewUtils.setEventAware(this);
        updateButton();
    }

    public void onEventMainThread(ColorThemeChangedEvent event) {
        updateButton();
    }

    private void updateButton() {
        if (Theme.isNight()) {
            ThemedAttrs.ofView(this.mButton).append(R.attr.backgroundColorArray, Integer.valueOf(R.array.red)).append(R.attr.textColorArray, Integer.valueOf(R.array.invert_text_color)).updateView();
        } else {
            ThemedAttrs.ofView(this.mButton).append(R.attr.backgroundColorArray, Integer.valueOf(R.array.btn_bg_color)).append(R.attr.textColorArray, Integer.valueOf(R.array.red)).updateView();
        }
    }

    public WorksGiftEventCard event(final GiftEvent event, final int worksId) {
        ImageLoaderUtils.displayImage(event.profileImg, this.mImage);
        this.mImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PageOpenHelper.from(WorksGiftEventCard.this).preferInternalWebView().open(event.url);
            }
        });
        this.mButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                GiftPackCreateFragment_.builder().worksId(worksId).eventId(event.id).build().showAsActivity(WorksGiftEventCard.this);
            }
        });
        return this;
    }
}
