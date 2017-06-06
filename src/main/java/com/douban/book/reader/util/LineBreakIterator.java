package com.douban.book.reader.util;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineBreakIterator implements Iterator<CharSequence> {
    private static Pattern sLineBreakPattern = Pattern.compile("\r|\n|\r\n");
    int mLastPos = 0;
    Matcher mMatcher;
    CharSequence mText;

    public LineBreakIterator(CharSequence text) {
        if (text == null) {
            text = "";
        }
        this.mText = text;
        this.mMatcher = sLineBreakPattern.matcher(text);
    }

    public boolean hasNext() {
        return StringUtils.isNotEmpty(this.mText) && this.mLastPos < this.mText.length();
    }

    public CharSequence next() {
        int start = this.mLastPos;
        int end = this.mText.length();
        if (this.mMatcher.find()) {
            start = this.mMatcher.start();
            end = this.mMatcher.end();
        } else {
            start = end;
        }
        CharSequence cs = this.mText.subSequence(this.mLastPos, start);
        this.mLastPos = end;
        return cs;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
