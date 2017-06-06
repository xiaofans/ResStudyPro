package com.douban.book.reader.manager;

import com.douban.book.reader.constant.Key;
import com.douban.book.reader.entity.Notification;
import com.douban.book.reader.event.EventBusUtils;
import com.douban.book.reader.event.NotificationLastCheckedTimeUpdatedEvent;
import com.douban.book.reader.manager.exception.DataLoadException;
import com.douban.book.reader.network.exception.RestException;
import com.douban.book.reader.util.DateUtils;
import com.douban.book.reader.util.Pref;
import java.util.Date;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

@EBean(scope = Scope.Singleton)
public class NotificationManager extends BaseManager<Notification> {
    private Date mLastCheckedTime;
    @Bean
    UnreadCountManager mUnreadCountManager;

    public NotificationManager() {
        super("notifications", Notification.class);
    }

    public void markAsRead(int id) throws DataLoadException {
        try {
            getRestClient().getSubClientWithId(Integer.valueOf(id), "read", Notification.class).put();
            this.mUnreadCountManager.refresh();
        } catch (RestException e) {
            throw wrapDataLoadException(e);
        }
    }

    public void markAllAsRead() throws DataLoadException {
        try {
            getSubManager("read_all", Notification.class).getRestClient().put();
            this.mUnreadCountManager.refresh();
        } catch (RestException e) {
            throw wrapDataLoadException(e);
        }
    }

    public int getUnreadCount() throws DataLoadException {
        return this.mUnreadCountManager.getUnreadNotificationsCount();
    }

    public void updateLastCheckedTime() {
        this.mLastCheckedTime = new Date();
        Pref.ofUser().set(Key.APP_NOTIFICATION_LAST_CHECKED_TIME, this.mLastCheckedTime);
        EventBusUtils.post(new NotificationLastCheckedTimeUpdatedEvent());
    }

    public boolean hasNewNotificationsSinceLastCheck() {
        if (this.mLastCheckedTime == null) {
            this.mLastCheckedTime = Pref.ofUser().getDate(Key.APP_NOTIFICATION_LAST_CHECKED_TIME, DateUtils.START_OF_ARK_ERA);
        }
        return this.mUnreadCountManager.notificationUpdatedSince(this.mLastCheckedTime);
    }

    public Lister<Notification> myList() {
        return getSubManager("mine", Notification.class).list();
    }
}
