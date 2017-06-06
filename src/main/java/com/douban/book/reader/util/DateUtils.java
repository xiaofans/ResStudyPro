package com.douban.book.reader.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final Date START_OF_ARK_ERA = new Date(1336320000000L);
    private static final String TAG = DateUtils.class.getSimpleName();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sDateFormatWithUnit = new SimpleDateFormat("yyyy年MM月dd日");
    private static final SimpleDateFormat sDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat sISO8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
    private static final SimpleDateFormat sYearMonthFormat = new SimpleDateFormat("yyyy年MM月");

    public static Date parseIso8601(String timeStr) throws ParseException {
        if (StringUtils.isNotEmpty(timeStr)) {
            return sISO8601Format.parse(timeStr);
        }
        return null;
    }

    public static String formatIso8601(long timestamp) {
        return formatIso8601(new Date(timestamp));
    }

    public static String formatIso8601(Date date) {
        return sISO8601Format.format(date);
    }

    public static String formatYearMonth(Date date) {
        String dateString = "";
        try {
            dateString = sYearMonthFormat.format(date);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
        return dateString;
    }

    public static String formatDate(Date date) {
        String timeString = "";
        try {
            timeString = sDateFormat.format(date);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
        return timeString;
    }

    public static String formatDateWithUnit(Date date) {
        String dateString = "";
        try {
            dateString = sDateFormatWithUnit.format(date);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
        return dateString;
    }

    public static String formatDate(long timeStamp) {
        return formatDate(new Date(1000 * timeStamp));
    }

    public static String formatDate(String timeStr) throws ParseException {
        return formatDate(parseIso8601(timeStr));
    }

    public static String formatDateTime(long timeStamp) {
        return formatDateTime(new Date(1000 * timeStamp));
    }

    public static String formatDateTime(Date date) {
        String timeString = "";
        try {
            timeString = sDateTimeFormat.format(date);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
        return timeString;
    }

    public static long getDaysSince(Date date) {
        if (date == null) {
            return 0;
        }
        return (System.currentTimeMillis() - date.getTime()) / 8640000;
    }

    public static boolean isInRange(Date start, Date end) {
        if (start == null || end == null) {
            return false;
        }
        Date current = new Date();
        if (current.after(start) && current.before(end)) {
            return true;
        }
        return false;
    }

    public static long millisBetween(Date start, Date end) {
        if (start == null || end == null) {
            return Long.MAX_VALUE;
        }
        return end.getTime() - start.getTime();
    }

    public static long millisElapsed(Date date) {
        if (date == null) {
            return Long.MAX_VALUE;
        }
        return millisBetween(date, new Date());
    }

    public static Date round(Date date) {
        if (date == null || date.before(START_OF_ARK_ERA)) {
            return START_OF_ARK_ERA;
        }
        return date;
    }
}
