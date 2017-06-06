package com.douban.book.reader.util;

import android.content.UriMatcher;
import android.net.Uri;
import com.douban.book.reader.content.pack.Package;
import com.douban.book.reader.exception.PackageException;
import com.douban.book.reader.fragment.BaseShareEditFragment;
import com.douban.book.reader.manager.WorksManager;

public class ReaderUriUtils {
    private static final String TAG = ReaderUriUtils.class.getSimpleName();
    private static final UriMatcher sURIMatcher = new UriMatcher(-1);

    public static class Type {
        public static final int GIFT_PACK = 10;
        public static final int ILLUS_LARGE = 4;
        public static final int ILLUS_NORMAL = 3;
        public static final int PACKAGE = 2;
        public static final int WORKS = 0;
        public static final int WORKS_COVER = 1;
    }

    static {
        sURIMatcher.addURI("data", "works/#", 0);
        sURIMatcher.addURI("data", "works/#/cover", 1);
        sURIMatcher.addURI("data", "works/#/package/#", 2);
        sURIMatcher.addURI("data", "works/#/package/#/illus/#/normal", 3);
        sURIMatcher.addURI("data", "works/#/package/#/illus/#/large", 4);
        sURIMatcher.addURI("data", "gift_pack/#", 10);
    }

    public static int getType(Uri uri) {
        return sURIMatcher.match(uri);
    }

    public static int getWorksId(Uri uri) {
        return StringUtils.toInt(UriUtils.getPathSegmentNextTo(uri, BaseShareEditFragment.CONTENT_TYPE_WORKS));
    }

    public static int getPackageId(Uri uri) {
        return StringUtils.toInt(UriUtils.getPathSegmentNextTo(uri, "package"));
    }

    public static int getIllusSeq(Uri uri) {
        return StringUtils.toInt(UriUtils.getPathSegmentNextTo(uri, BaseShareEditFragment.CONTENT_TYPE_ILLUS));
    }

    public static int getGiftPackId(Uri uri) {
        return StringUtils.toInt(UriUtils.getPathSegmentNextTo(uri, BaseShareEditFragment.CONTENT_TYPE_GIFT_PACK));
    }

    public static boolean isValidForWorks(Uri uri, int worksId) {
        return getWorksId(uri) == worksId;
    }

    public static boolean isWorksUri(Uri uri) {
        return getType(uri) == 0;
    }

    public static boolean isPackageUri(Uri uri) {
        return getType(uri) == 2;
    }

    public static int getPrice(Uri uri) {
        int amount = 0;
        switch (getType(uri)) {
            case 0:
                try {
                    amount = WorksManager.getInstance().getWorks(getWorksId(uri)).price;
                    break;
                } catch (Exception e) {
                    Logger.e(TAG, e);
                    break;
                }
            case 2:
                try {
                    amount = Package.get(uri).getPrice();
                    break;
                } catch (PackageException e2) {
                    Logger.e(TAG, e2);
                    break;
                }
        }
        return amount;
    }
}
