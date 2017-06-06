package com.douban.book.reader.manager;

import com.douban.book.reader.entity.UnreadCount;
import com.douban.book.reader.event.EventBusUtils;
import com.douban.book.reader.event.UnreadCountChangedEvent;
import com.douban.book.reader.manager.exception.DataLoadException;
import com.douban.book.reader.util.Logger;
import java.util.Date;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

@EBean(scope = Scope.Singleton)
public class UnreadCountManager extends BaseManager<UnreadCount> {
    public UnreadCountManager() {
        super("people/unread", UnreadCount.class);
        maxStaleness(300000);
    }

    public int getUnreadFeedsCount() throws DataLoadException {
        return ((UnreadCount) get()).feeds;
    }

    public int getUnreadNotificationsCount() throws DataLoadException {
        return ((UnreadCount) get()).notifications;
    }

    public boolean notificationUpdatedSince(Date since) {
        boolean z = false;
        if (since != null) {
            try {
                z = ((UnreadCount) get()).notiUpdateTime.after(since);
            } catch (Throwable th) {
            }
        }
        return z;
    }

    public void refresh() {
        try {
            getFromRemote(null);
        } catch (DataLoadException e) {
            Logger.e(this.TAG, e);
        }
    }

    public UnreadCount getFromRemote(Object id) throws DataLoadException {
        UnreadCount counts = (UnreadCount) super.getFromRemote(id);
        if (counts != null) {
            EventBusUtils.post(new UnreadCountChangedEvent());
        }
        return counts;
    }
}
