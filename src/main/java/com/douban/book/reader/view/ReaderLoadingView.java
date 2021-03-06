package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.constant.Key;
import com.douban.book.reader.content.pack.WorksData;
import com.douban.book.reader.event.ArkEvent;
import com.douban.book.reader.event.DownloadProgressChangedEvent;
import com.douban.book.reader.event.DownloadStatusChangedEvent;
import com.douban.book.reader.event.PagingProgressUpdatedEvent;
import com.douban.book.reader.fragment.share.FeedbackEditFragment_;
import com.douban.book.reader.task.DownloadManager;
import com.douban.book.reader.task.PagingTaskManager;
import com.douban.book.reader.theme.ThemedAttrs;
import com.douban.book.reader.util.DebugSwitch;
import com.douban.book.reader.util.ReaderUri;
import com.douban.book.reader.util.Res;
import com.douban.book.reader.util.ThemedUtils;
import com.douban.book.reader.util.Utils;
import com.douban.book.reader.util.ViewUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(2130903165)
public class ReaderLoadingView extends LinearLayout {
    @ViewById(2131558873)
    Button mBtnReport;
    @ViewById(2131558519)
    Button mBtnRetry;
    @ViewById(2131558872)
    TextView mGuideForShelf;
    @ViewById(2131558871)
    ProgressBar mProgressBar;
    @ViewById(2131558870)
    TextView mTextStatus;
    private int mWorksId;

    public ReaderLoadingView(Context context) {
        super(context);
    }

    public ReaderLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReaderLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setWorksId(int worksId) {
        this.mWorksId = worksId;
        updateStatus();
    }

    @AfterViews
    void init() {
        ViewUtils.setEventAware(this);
        ThemedAttrs.ofView(this).append(R.attr.backgroundColorArray, Integer.valueOf(R.array.page_bg_color));
        ThemedUtils.updateView(this);
    }

    public void onEventMainThread(DownloadStatusChangedEvent event) {
        if (event.isValidFor(this.mWorksId)) {
            updateStatus();
        }
    }

    public void onEventMainThread(DownloadProgressChangedEvent event) {
        if (event.isValidFor(this.mWorksId)) {
            updateDownloadProgress();
        }
    }

    public void onEventMainThread(PagingProgressUpdatedEvent event) {
        if (event.isValidFor(this.mWorksId)) {
            updatePagingStatus();
        }
    }

    public void onEventMainThread(ArkEvent event) {
        switch (event) {
            case SLIDING_UP_PANEL_EXPANDED:
                ViewUtils.setBottomPadding(this, Utils.dp2pixel(220.0f));
                return;
            case SLIDING_UP_PANEL_COLLAPSED:
                ViewUtils.setBottomPadding(this, 0);
                return;
            default:
                return;
        }
    }

    private void updateStatus() {
        ViewUtils.invisible(this.mBtnRetry, this.mBtnReport, this.mProgressBar, this.mGuideForShelf);
        ThemedAttrs.ofView(this.mTextStatus).append(R.attr.textColorArray, Integer.valueOf(R.array.green)).updateView();
        switch (WorksData.get(this.mWorksId).getStatus()) {
            case PENDING:
                this.mTextStatus.setText(R.string.reader_pending);
                ViewUtils.visible(this.mGuideForShelf);
                return;
            case DOWNLOADING:
                updateDownloadProgress();
                return;
            case FAILED:
            case PAUSED:
                showFailed();
                return;
            default:
                if (PagingTaskManager.isPaging(this.mWorksId)) {
                    updatePagingStatus();
                    return;
                } else {
                    this.mTextStatus.setText(R.string.reader_processing);
                    return;
                }
        }
    }

    private void updateDownloadProgress() {
        CharSequence string;
        ViewUtils.invisible(this.mBtnRetry, this.mBtnReport, this.mProgressBar);
        ThemedAttrs.ofView(this.mTextStatus).append(R.attr.textColorArray, Integer.valueOf(R.array.green)).updateView();
        int progress = WorksData.get(this.mWorksId).getDownloadProgress();
        TextView textView = this.mTextStatus;
        if (progress < 0) {
            string = Res.getString(R.string.reader_downloading);
        } else {
            string = Res.getString(R.string.reader_downloading_with_progress, Integer.valueOf(progress));
        }
        textView.setText(string);
        this.mProgressBar.setProgress(progress);
        this.mProgressBar.setEnabled(true);
        ViewUtils.visible(this.mProgressBar, this.mGuideForShelf);
    }

    private void updatePagingStatus() {
        ViewUtils.invisible(this.mBtnRetry, this.mBtnReport, this.mProgressBar, this.mGuideForShelf);
        ThemedAttrs.ofView(this.mTextStatus).append(R.attr.textColorArray, Integer.valueOf(R.array.green)).updateView();
        this.mTextStatus.setText(R.string.reader_paging);
    }

    private void showFailed() {
        this.mTextStatus.setText(R.string.reader_failed_to_load);
        ThemedAttrs.ofView(this.mTextStatus).append(R.attr.textColorArray, Integer.valueOf(R.array.red)).updateView();
        this.mProgressBar.setEnabled(false);
        ViewUtils.visible(this.mBtnRetry, this.mProgressBar, this.mGuideForShelf);
        ViewUtils.visibleIf(DebugSwitch.on(Key.APP_DEBUG_SEND_DIAGNOSTIC_REPORT), this.mBtnReport);
    }

    @Click({2131558519})
    void onBtnRetryClicked() {
        DownloadManager.scheduleDownload(ReaderUri.works(this.mWorksId));
    }

    @Click({2131558873})
    void onBtnReportClicked() {
        FeedbackEditFragment_.builder().isReport(true).build().showAsActivity((View) this);
    }
}
