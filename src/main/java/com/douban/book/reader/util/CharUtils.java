package com.douban.book.reader.util;

import android.graphics.Paint;
import com.douban.book.reader.constant.Char;
import com.douban.book.reader.lib.hyphenate.Hyphenate;
import java.text.BreakIterator;
import java.util.Iterator;

public class CharUtils {
    public static final float FULL_WIDTH_CHAR_OFFSET_ADJUSTMENT_RATIO = 0.4f;
    private static char[] sFullWidthEndPunctuations = new char[]{'，', '。', '、', Char.FULLWIDTH_COLON, '；', '？', '！', '’', '”', '）', Char.RIGHT_DOUBLE_ANGLE_BRACKET, '〉', '．', '〞', '＇', '』', Char.RIGHT_COMER_BRACKET, '】', '〕', '〗', '〙', '〛', '］', '｝', '｠'};
    private static char[] sFullWidthStartPunctuations = new char[]{'“', '‘', Char.LEFT_DOUBLE_ANGLE_BRACKET, '（', '〈', Char.LEFT_COMER_BRACKET, '『', '【', '〔', '〖', '〘', '〚', '〝', '［', '｛', '｟'};

    public static int getBreakDownIndex(String str) {
        int len = str.length();
        if (len < 3) {
            return len;
        }
        BreakIterator iterator = BreakIteratorUtils.getWordBreakIterator();
        iterator.setText(str);
        int start = iterator.first();
        int end = start;
        boolean lastWordCanInEnd = false;
        while (true) {
            end = iterator.next();
            if (end != -1) {
                String word = str.substring(start, end);
                if (word.length() > 0) {
                    char c = word.charAt(0);
                    if (lastWordCanInEnd && canInStartOfLine(c)) {
                        BreakIteratorUtils.recycleWordBreakIterator(iterator);
                        return start;
                    }
                    lastWordCanInEnd = canInEndOfLine(word.charAt(word.length() - 1));
                }
                start = end;
            } else {
                BreakIteratorUtils.recycleWordBreakIterator(iterator);
                return len;
            }
        }
    }

    public static boolean shouldHyphenate(String str) {
        boolean hasLowercase = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isLetter(c)) {
                return false;
            }
            if (!hasLowercase && Character.isLowerCase(c)) {
                hasLowercase = true;
            }
        }
        if (hasLowercase) {
            return true;
        }
        return false;
    }

    public static int getHyphenateIndexByWidth(String str, Paint paint, float width) {
        float totalWidth = 0.0f;
        int index = 0;
        Iterator it = Hyphenate.hyphenateWord(str).iterator();
        while (it.hasNext()) {
            String part = (String) it.next();
            totalWidth += paint.measureText(part);
            if (totalWidth > width) {
                break;
            }
            index += part.length();
        }
        if (index <= 0) {
            return str.length();
        }
        return index;
    }

    public static boolean isPunctuation(char c) {
        switch (Character.getType(c)) {
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 29:
            case 30:
                return true;
            default:
                return false;
        }
    }

    public static boolean isFullWidthStartPunctuation(char c) {
        if (isPunctuation(c)) {
            for (char c2 : sFullWidthStartPunctuations) {
                if (c == c2) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isFullWidthEndPunctuation(char c) {
        if (isPunctuation(c)) {
            for (char c2 : sFullWidthEndPunctuations) {
                if (c == c2) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canInStartOfLine(char c) {
        switch (Character.getType(c)) {
            case 22:
            case 24:
            case 30:
                return false;
            default:
                return true;
        }
    }

    public static boolean canInEndOfLine(char c) {
        switch (Character.getType(c)) {
            case 21:
            case 26:
            case 29:
                return false;
            default:
                return true;
        }
    }
}
