package com.douban.book.reader.util;

import java.text.BreakIterator;
import java.util.Locale;

public class BreakIteratorUtils {
    public static final String TAG = "BreakIteratorUtils";
    private static final BreakIterator[] sLineIteratorCache = new BreakIterator[3];
    private static final BreakIterator[] sWordIteratorCache = new BreakIterator[3];

    public static BreakIterator getWordBreakIterator() {
        synchronized (sWordIteratorCache) {
            for (int i = 0; i < sWordIteratorCache.length; i++) {
                if (sWordIteratorCache[i] != null) {
                    BreakIterator result = sWordIteratorCache[i];
                    sWordIteratorCache[i] = null;
                    return result;
                }
            }
            return BreakIterator.getWordInstance(Locale.CHINA);
        }
    }

    public static void recycleWordBreakIterator(BreakIterator iterator) {
        synchronized (sWordIteratorCache) {
            for (int i = 0; i < sWordIteratorCache.length; i++) {
                if (sWordIteratorCache[i] == null) {
                    sWordIteratorCache[i] = iterator;
                    break;
                }
            }
        }
    }

    public static BreakIterator getLineBreakIterator() {
        synchronized (sLineIteratorCache) {
            for (int i = 0; i < sLineIteratorCache.length; i++) {
                if (sLineIteratorCache[i] != null) {
                    BreakIterator result = sLineIteratorCache[i];
                    sLineIteratorCache[i] = null;
                    return result;
                }
            }
            return BreakIterator.getLineInstance(Locale.CHINA);
        }
    }

    public static void recycleLineBreakIterator(BreakIterator iterator) {
        synchronized (sLineIteratorCache) {
            for (int i = 0; i < sLineIteratorCache.length; i++) {
                if (sLineIteratorCache[i] == null) {
                    sLineIteratorCache[i] = iterator;
                    break;
                }
            }
        }
    }
}
