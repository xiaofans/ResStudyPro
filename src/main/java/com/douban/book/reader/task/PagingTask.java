package com.douban.book.reader.task;

import android.os.Debug;
import com.douban.book.reader.content.Book;
import com.douban.book.reader.content.PageMetrics;
import com.douban.book.reader.content.chapter.Chapter.PagingProgressListener;
import com.douban.book.reader.entity.Progress;
import com.douban.book.reader.event.EventBusUtils;
import com.douban.book.reader.event.PagingProgressUpdatedEvent;
import com.douban.book.reader.executor.TaggedRunnable;
import com.douban.book.reader.manager.AnnotationManager;
import com.douban.book.reader.manager.BookmarkManager;
import com.douban.book.reader.manager.ProgressManager;
import com.douban.book.reader.util.Logger;
import com.douban.book.reader.util.Tag;

public class PagingTask extends TaggedRunnable {
    int mBookId;
    int mPageByCurrentProgress = -1;
    PageMetrics mPageMetrics;

    public PagingTask(int bookId, PageMetrics pageMetrics) {
        super(Integer.valueOf(bookId));
        this.mBookId = bookId;
        this.mPageMetrics = pageMetrics;
    }

    public void run() {
        Book book = null;
        try {
            book = Book.get(this.mBookId);
            Logger.dc(Tag.PAGING, "---- start paging for %s (task=%s)", book, this);
            Logger.d(Tag.PAGING, "Native heap allocated size (before paging): %s", Long.valueOf(Debug.getNativeHeapAllocatedSize()));
            long startTime = System.currentTimeMillis();
            book.openBook();
            final Book finalBook = book;
            book.paging(this.mPageMetrics, new PagingProgressListener() {
                public void onNewPage() {
                    if (PagingTask.this.mPageByCurrentProgress < 0) {
                        Progress progress = ProgressManager.ofWorks(PagingTask.this.mBookId).getLocalProgress();
                        PagingTask.this.mPageByCurrentProgress = finalBook.getPageForPosition(progress.getPosition());
                        Logger.d(Tag.PAGING, "--- onNewPage, progress=%s, mPageByCurrentProgress=%d", progress, Integer.valueOf(PagingTask.this.mPageByCurrentProgress));
                    }
                    Logger.d(Tag.PAGING, "---- new pages for %s", finalBook);
                    EventBusUtils.post(new PagingProgressUpdatedEvent(PagingTask.this.mBookId, PagingTask.this.mPageByCurrentProgress));
                }
            });
            BookmarkManager.ofWorks(this.mBookId).updateIndex();
            AnnotationManager.ofWorks(this.mBookId).updateIndex();
            Logger.dc(Tag.PAGING, "----- paging for %s (task=%s) succeed. elapsed: %.3fs", book, this, Float.valueOf(((float) (System.currentTimeMillis() - startTime)) / 1000.0f));
            Logger.d(Tag.PAGING, "Native heap allocated size (after paging): %s", Long.valueOf(Debug.getNativeHeapAllocatedSize()));
            if (book != null) {
                book.closeBook();
            }
        } catch (Throwable th) {
            if (book != null) {
                book.closeBook();
            }
        }
    }
}
