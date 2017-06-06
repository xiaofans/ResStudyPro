package com.douban.book.reader.entity;

import com.douban.book.reader.manager.cache.Identifiable;
import java.util.Date;

public class RedeemRecord implements Identifiable {
    public int amount;
    public int id;
    public Date time;
    public int type;
    public Works works;

    public static class Type {
        public static int CASH = 2;
        public static int WORKS = 1;
    }

    public Integer getId() {
        return Integer.valueOf(this.id);
    }
}
