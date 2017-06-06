package com.douban.book.reader.view.page;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.content.pack.Package;
import com.douban.book.reader.content.pack.WorksData;
import com.douban.book.reader.entity.Works;
import com.douban.book.reader.event.DownloadProgressChangedEvent;
import com.douban.book.reader.event.DownloadStatusChangedEvent;
import com.douban.book.reader.event.ManifestUpdatedEvent;
import com.douban.book.reader.event.WorksUpdatedEvent;
import com.douban.book.reader.fragment.PurchaseFragment_;
import com.douban.book.reader.manager.WorksManager;
import com.douban.book.reader.manager.exception.DataLoadException;
import com.douban.book.reader.task.DownloadManager;
import com.douban.book.reader.util.DateUtils;
import com.douban.book.reader.util.ReaderUri;
import com.douban.book.reader.util.Res;
import com.douban.book.reader.util.RichText;
import com.douban.book.reader.util.Utils;
import com.douban.book.reader.util.ViewUtils;
import com.douban.book.reader.view.FlexibleScrollView;
import com.douban.book.reader.view.ParagraphView;
import com.douban.book.reader.view.ParagraphView.Indent;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(2130903174)
public class ChapterPreviewPageView extends AbsPageView {
    @ViewById(2131558897)
    Button mBtnDownload;
    @ViewById(2131558898)
    Button mBtnPurchaseChapter;
    @ViewById(2131558896)
    Button mBtnPurchaseWorks;
    private int mPackageId;
    @ViewById(2131558893)
    TextView mPublishDate;
    @ViewById(2131558899)
    TextView mPurchaseNeeded;
    @ViewById(2131558901)
    FlexibleScrollView mScrollView;
    @ViewById(2131558902)
    ParagraphView mSummary;
    @ViewById(2131558462)
    TextView mTitle;
    private Uri mUri;
    private int mWorksId;
    @Bean
    WorksManager mWorksManager;

    public ChapterPreviewPageView(Context context) {
        super(context);
    }

    @AfterViews
    void init() {
        this.mScrollView.setMaxHeight((float) (((this.mSummary.getLineHeight() * 5) + this.mScrollView.getPaddingTop()) + this.mScrollView.getPaddingBottom()));
        this.mBtnDownload.setText(RichText.textWithIcon((int) R.drawable.v_download, (int) R.string.download));
        setGeneralTouchListener(this);
        ViewUtils.setEventAware(this);
    }

    public void onEventMainThread(DownloadProgressChangedEvent event) {
        if (event.isValidFor(this.mUri)) {
            updateDownloadProgress(event.getProgress());
        }
    }

    public void onEventMainThread(DownloadStatusChangedEvent event) {
        if (event.isValidFor(this.mUri)) {
            updateDownloadButton();
        }
    }

    public void onEventMainThread(WorksUpdatedEvent event) {
        if (event.isValidFor(this.mWorksId)) {
            setData(this.mWorksId, this.mPackageId);
        }
    }

    public void onEventMainThread(ManifestUpdatedEvent event) {
        if (event.isValidFor(this.mWorksId)) {
            setData(this.mWorksId, this.mPackageId);
        }
    }

    public boolean isDraggable() {
        return false;
    }

    @Click({2131558896})
    void onBtnPurchaseWorksClicked() {
        if (this.mWorksId > 0) {
            PurchaseFragment_.builder().uri(ReaderUri.works(this.mWorksId)).build().showAsActivity((View) this);
        }
    }

    @Click({2131558898})
    void onBtnPurchaseChapterClicked() {
        if (this.mWorksId > 0 && this.mPackageId > 0) {
            PurchaseFragment_.builder().uri(ReaderUri.pack(this.mWorksId, this.mPackageId)).build().showAsActivity((View) this);
        }
    }

    @Click({2131558897})
    void onBtnDownloadClicked() {
        DownloadManager.scheduleDownload(ReaderUri.works(this.mWorksId));
    }

    public void setData(int worksId, int packageId) {
        this.mWorksId = worksId;
        this.mPackageId = packageId;
        this.mUri = ReaderUri.pack(worksId, packageId);
        Package pack = Package.get(worksId, packageId);
        this.mTitle.setText(pack.getTitle());
        this.mSummary.setParagraphText(pack.getAbstractText());
        this.mSummary.setFirstLineIndent(Indent.ALL);
        if (pack.getPublishDate() != null) {
            this.mPublishDate.setText(Res.getString(R.string.msg_published_at, DateUtils.formatDate(publishDate)));
        }
        if (pack.isPurchaseNeeded()) {
            ViewUtils.gone(this.mBtnDownload);
            ViewUtils.visible(this.mPurchaseNeeded);
            ViewUtils.showTextIf(shouldShowPurchaseWholeWorks(worksId), this.mBtnPurchaseWorks, RichText.textWithIcon((int) R.drawable.v_purchase, Res.getString(R.string.btn_purchase_whole_works, Utils.formatPriceWithSymbol(getWorksPrice(worksId)))));
            ViewUtils.showText(this.mBtnPurchaseChapter, RichText.textWithIcon((int) R.drawable.v_purchase, Res.getString(R.string.btn_purchase_chapter, Utils.formatPriceWithSymbol(pack.getPrice()))));
            return;
        }
        ViewUtils.visible(this.mBtnDownload);
        updateDownloadButton();
        ViewUtils.gone(this.mPurchaseNeeded, this.mBtnPurchaseChapter, this.mBtnPurchaseWorks);
    }

    private void updateDownloadButton() {
        switch (WorksData.get(this.mWorksId).getPackage(this.mPackageId).getStatus()) {
            case READY:
                this.mBtnDownload.setText(RichText.textWithIcon((int) R.drawable.v_read, (int) R.string.read));
                return;
            case PENDING:
                this.mBtnDownload.setText(RichText.textWithIcon((int) R.drawable.ic_download_pending, (int) R.string.download_pending));
                return;
            case DOWNLOADING:
                updateDownloadProgress(0);
                return;
            default:
                this.mBtnDownload.setText(RichText.textWithIcon((int) R.drawable.v_download, (int) R.string.download));
                return;
        }
    }

    private void updateDownloadProgress(int progress) {
        CharSequence string;
        Button button = this.mBtnDownload;
        if (progress < 0) {
            string = Res.getString(R.string.downloading);
        } else {
            string = String.format("%d%%", new Object[]{Integer.valueOf(progress)});
        }
        button.setText(string);
    }

    private boolean shouldShowPurchaseWholeWorks(int worksId) {
        try {
            Works works = this.mWorksManager.getWorks(worksId);
            if (!works.isSalable || works.hasOwned || works.price <= 0) {
                return false;
            }
            return true;
        } catch (DataLoadException e) {
            return false;
        }
    }

    private int getWorksPrice(int worksId) {
        try {
            return this.mWorksManager.getWorks(worksId).price;
        } catch (DataLoadException e) {
            return 0;
        }
    }
}
