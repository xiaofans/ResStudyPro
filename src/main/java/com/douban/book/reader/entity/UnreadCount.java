package com.douban.book.reader.entity;

import com.douban.book.reader.manager.cache.Identifiable;
import java.util.Date;

public class UnreadCount implements Identifiable {
    public Date feedUpdateTime;
    public int feeds;
    public Date notiUpdateTime;
    public int notifications;

    public Object getId() {
        return Integer.valueOf(0);
    }
}
