package com.douban.book.reader.helper;

import android.net.Uri;
import com.douban.book.reader.activity.ReaderActivity_;
import com.douban.book.reader.activity.ReaderActivity_.IntentBuilder_;
import com.douban.book.reader.app.App;
import com.douban.book.reader.app.PageOpenHelper;
import com.douban.book.reader.content.pack.WorksData;
import com.douban.book.reader.content.pack.WorksData.Status;
import com.douban.book.reader.task.DownloadManager;
import com.douban.book.reader.util.ReaderUri;
import com.douban.book.reader.util.ReaderUriUtils;

public class BookItemClickHelper {
    private PageOpenHelper mPageOpenHelper;

    public BookItemClickHelper(PageOpenHelper helper) {
        this.mPageOpenHelper = helper;
    }

    public void performBookItemClick(int bookId) {
        performBookItemClick(ReaderUri.works(bookId));
    }

    public void performBookItemClick(Uri uri) {
        int bookId = ReaderUriUtils.getWorksId(uri);
        int type = ReaderUriUtils.getType(uri);
        Status status = Status.EMPTY;
        if (type == 0) {
            status = WorksData.get(bookId).getStatus();
        } else if (type == 2) {
            status = WorksData.get(bookId).getPackage(ReaderUriUtils.getPackageId(uri)).getStatus();
        }
        switch (status) {
            case DOWNLOADING:
                DownloadManager.stopDownloading(uri);
                return;
            case PENDING:
                DownloadManager.stopDownloading(uri);
                return;
            case READY:
                this.mPageOpenHelper.open(((IntentBuilder_) ReaderActivity_.intent(App.get()).flags(67108864)).mBookId(bookId).get());
                return;
            default:
                DownloadManager.scheduleDownload(uri);
                return;
        }
    }
}
