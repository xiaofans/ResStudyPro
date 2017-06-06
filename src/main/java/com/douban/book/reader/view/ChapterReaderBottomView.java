package com.douban.book.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.activity.ReaderActivity_;
import com.douban.book.reader.activity.ReaderActivity_.IntentBuilder_;
import com.douban.book.reader.app.App;
import com.douban.book.reader.app.PageOpenHelper;
import com.douban.book.reader.content.pack.WorksData;
import com.douban.book.reader.entity.Works;
import com.douban.book.reader.event.DownloadProgressChangedEvent;
import com.douban.book.reader.event.DownloadStatusChangedEvent;
import com.douban.book.reader.fragment.WorksProfileFragment_;
import com.douban.book.reader.fragment.share.ShareChapterEditFragment_;
import com.douban.book.reader.manager.WorksManager;
import com.douban.book.reader.manager.exception.DataLoadException;
import com.douban.book.reader.util.Logger;
import com.douban.book.reader.util.RichText;
import com.douban.book.reader.util.ViewUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EViewGroup(2130903152)
public class ChapterReaderBottomView extends FrameLayout {
    private static final String TAG = ChapterReaderBottomView.class.getSimpleName();
    private int mChapterId;
    @ViewById(2131558771)
    WorksCoverView mCoverView;
    @ViewById(2131558854)
    TextView mTvAction;
    @ViewById(2131558855)
    TextView mTvShare;
    private Works mWorks;
    private int mWorksId;
    @Bean
    WorksManager mWorksManager;

    public ChapterReaderBottomView(Context context) {
        super(context);
    }

    public ChapterReaderBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChapterReaderBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    void init() {
        ViewUtils.setEventAware(this);
    }

    public ChapterReaderBottomView setData(int worksId, int chapterId) {
        this.mWorksId = worksId;
        this.mChapterId = chapterId;
        loadData();
        return this;
    }

    @Background
    void loadData() {
        try {
            this.mWorks = this.mWorksManager.getWorks(this.mWorksId);
            updateViews();
        } catch (DataLoadException e) {
            Logger.e(TAG, e);
        }
    }

    @UiThread
    void updateViews() {
        if (this.mWorks != null) {
            this.mCoverView.setDrawableRatio(1.2857143f);
            this.mCoverView.works(this.mWorks);
            switch (WorksData.get(this.mWorksId).getStatus()) {
                case READY:
                    this.mTvAction.setText(RichText.textWithIcon((int) R.drawable.v_read, (int) R.string.text_open_in_reader));
                    break;
                case DOWNLOADING:
                    this.mTvAction.setText(String.format("%d%%", new Object[]{Integer.valueOf(WorksData.get(this.mWorksId).getDownloadProgress())}));
                    break;
                default:
                    this.mTvAction.setText(RichText.textWithIcon((int) R.drawable.v_read, (int) R.string.text_download_to_shelf));
                    break;
            }
            this.mTvShare.setText(RichText.singleIcon(R.drawable.v_share));
        }
    }

    public void onEventMainThread(DownloadProgressChangedEvent event) {
        updateViews();
    }

    public void onEventMainThread(DownloadStatusChangedEvent event) {
        updateViews();
    }

    @Click({2131558771})
    void openProfile() {
        WorksProfileFragment_.builder().worksId(this.mWorksId).build().showAsActivity((View) this);
    }

    @Click({2131558854})
    void openReader() {
        PageOpenHelper.from((View) this).open(((IntentBuilder_) ReaderActivity_.intent(App.get()).flags(67108864)).mBookId(this.mWorksId).chapterToShow(this.mChapterId).get());
    }

    @Click({2131558855})
    void startShare() {
        ShareChapterEditFragment_.builder().worksId(this.mWorksId).chapterId(this.mChapterId).build().showAsActivity(PageOpenHelper.from((View) this));
    }
}
