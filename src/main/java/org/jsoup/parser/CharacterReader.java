package org.jsoup.parser;

import com.douban.book.reader.constant.Char;
import java.util.Arrays;
import java.util.Locale;
import org.jsoup.helper.Validate;

final class CharacterReader {
    static final char EOF = '￿';
    private static final int maxCacheLen = 12;
    private final char[] input;
    private final int length;
    private int mark = 0;
    private int pos = 0;
    private final String[] stringCache = new String[512];

    CharacterReader(String input) {
        Validate.notNull(input);
        this.input = input.toCharArray();
        this.length = this.input.length;
    }

    int pos() {
        return this.pos;
    }

    boolean isEmpty() {
        return this.pos >= this.length;
    }

    char current() {
        return this.pos >= this.length ? EOF : this.input[this.pos];
    }

    char consume() {
        char val = this.pos >= this.length ? EOF : this.input[this.pos];
        this.pos++;
        return val;
    }

    void unconsume() {
        this.pos--;
    }

    void advance() {
        this.pos++;
    }

    void mark() {
        this.mark = this.pos;
    }

    void rewindToMark() {
        this.pos = this.mark;
    }

    String consumeAsString() {
        char[] cArr = this.input;
        int i = this.pos;
        this.pos = i + 1;
        return new String(cArr, i, 1);
    }

    int nextIndexOf(char c) {
        for (int i = this.pos; i < this.length; i++) {
            if (c == this.input[i]) {
                return i - this.pos;
            }
        }
        return -1;
    }

    int nextIndexOf(CharSequence seq) {
        char startChar = seq.charAt(0);
        int offset = this.pos;
        while (offset < this.length) {
            if (startChar != this.input[offset]) {
                do {
                    offset++;
                    if (offset >= this.length) {
                        break;
                    }
                } while (startChar != this.input[offset]);
            }
            int i = offset + 1;
            int last = (seq.length() + i) - 1;
            if (offset < this.length && last <= this.length) {
                int j = 1;
                while (i < last && seq.charAt(j) == this.input[i]) {
                    i++;
                    j++;
                }
                if (i == last) {
                    return offset - this.pos;
                }
            }
            offset++;
        }
        return -1;
    }

    String consumeTo(char c) {
        int offset = nextIndexOf(c);
        if (offset == -1) {
            return consumeToEnd();
        }
        String consumed = cacheString(this.pos, offset);
        this.pos += offset;
        return consumed;
    }

    String consumeTo(String seq) {
        int offset = nextIndexOf((CharSequence) seq);
        if (offset == -1) {
            return consumeToEnd();
        }
        String consumed = cacheString(this.pos, offset);
        this.pos += offset;
        return consumed;
    }

    String consumeToAny(char... chars) {
        int start = this.pos;
        int remaining = this.length;
        loop0:
        while (this.pos < remaining) {
            for (char c : chars) {
                if (this.input[this.pos] == c) {
                    break loop0;
                }
            }
            this.pos++;
        }
        if (this.pos > start) {
            return cacheString(start, this.pos - start);
        }
        return "";
    }

    String consumeToAnySorted(char... chars) {
        int start = this.pos;
        int remaining = this.length;
        char[] val = this.input;
        while (this.pos < remaining && Arrays.binarySearch(chars, val[this.pos]) < 0) {
            this.pos++;
        }
        return this.pos > start ? cacheString(start, this.pos - start) : "";
    }

    String consumeData() {
        int start = this.pos;
        int remaining = this.length;
        char[] val = this.input;
        while (this.pos < remaining) {
            char c = val[this.pos];
            if (c == '&' || c == '<' || c == '\u0000') {
                break;
            }
            this.pos++;
        }
        return this.pos > start ? cacheString(start, this.pos - start) : "";
    }

    String consumeTagName() {
        int start = this.pos;
        int remaining = this.length;
        char[] val = this.input;
        while (this.pos < remaining) {
            char c = val[this.pos];
            if (c == '\t' || c == '\n' || c == Char.CARRIAGE_RETURN || c == '\f' || c == Char.SPACE || c == Char.SLASH || c == '>' || c == '\u0000') {
                break;
            }
            this.pos++;
        }
        return this.pos > start ? cacheString(start, this.pos - start) : "";
    }

    String consumeToEnd() {
        String data = cacheString(this.pos, this.length - this.pos);
        this.pos = this.length;
        return data;
    }

    String consumeLetterSequence() {
        int start = this.pos;
        while (this.pos < this.length) {
            char c = this.input[this.pos];
            if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z')) {
                break;
            }
            this.pos++;
        }
        return cacheString(start, this.pos - start);
    }

    String consumeLetterThenDigitSequence() {
        int start = this.pos;
        while (this.pos < this.length) {
            char c = this.input[this.pos];
            if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z')) {
                break;
            }
            this.pos++;
        }
        while (!isEmpty()) {
            c = this.input[this.pos];
            if (c < '0' || c > '9') {
                break;
            }
            this.pos++;
        }
        return cacheString(start, this.pos - start);
    }

    String consumeHexSequence() {
        int start = this.pos;
        while (this.pos < this.length) {
            char c = this.input[this.pos];
            if ((c < '0' || c > '9') && ((c < 'A' || c > 'F') && (c < 'a' || c > 'f'))) {
                break;
            }
            this.pos++;
        }
        return cacheString(start, this.pos - start);
    }

    String consumeDigitSequence() {
        int start = this.pos;
        while (this.pos < this.length) {
            char c = this.input[this.pos];
            if (c < '0' || c > '9') {
                break;
            }
            this.pos++;
        }
        return cacheString(start, this.pos - start);
    }

    boolean matches(char c) {
        return !isEmpty() && this.input[this.pos] == c;
    }

    boolean matches(String seq) {
        int scanLength = seq.length();
        if (scanLength > this.length - this.pos) {
            return false;
        }
        for (int offset = 0; offset < scanLength; offset++) {
            if (seq.charAt(offset) != this.input[this.pos + offset]) {
                return false;
            }
        }
        return true;
    }

    boolean matchesIgnoreCase(String seq) {
        int scanLength = seq.length();
        if (scanLength > this.length - this.pos) {
            return false;
        }
        for (int offset = 0; offset < scanLength; offset++) {
            if (Character.toUpperCase(seq.charAt(offset)) != Character.toUpperCase(this.input[this.pos + offset])) {
                return false;
            }
        }
        return true;
    }

    boolean matchesAny(char... seq) {
        if (isEmpty()) {
            return false;
        }
        char c = this.input[this.pos];
        for (char seek : seq) {
            if (seek == c) {
                return true;
            }
        }
        return false;
    }

    boolean matchesAnySorted(char[] seq) {
        return !isEmpty() && Arrays.binarySearch(seq, this.input[this.pos]) >= 0;
    }

    boolean matchesLetter() {
        if (isEmpty()) {
            return false;
        }
        char c = this.input[this.pos];
        if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z')) {
            return false;
        }
        return true;
    }

    boolean matchesDigit() {
        if (isEmpty()) {
            return false;
        }
        char c = this.input[this.pos];
        if (c < '0' || c > '9') {
            return false;
        }
        return true;
    }

    boolean matchConsume(String seq) {
        if (!matches(seq)) {
            return false;
        }
        this.pos += seq.length();
        return true;
    }

    boolean matchConsumeIgnoreCase(String seq) {
        if (!matchesIgnoreCase(seq)) {
            return false;
        }
        this.pos += seq.length();
        return true;
    }

    boolean containsIgnoreCase(String seq) {
        return nextIndexOf(seq.toLowerCase(Locale.ENGLISH)) > -1 || nextIndexOf(seq.toUpperCase(Locale.ENGLISH)) > -1;
    }

    public String toString() {
        return new String(this.input, this.pos, this.length - this.pos);
    }

    private String cacheString(int start, int count) {
        char[] val = this.input;
        String[] cache = this.stringCache;
        if (count > 12) {
            return new String(val, start, count);
        }
        int hash = 0;
        int i = 0;
        int offset = start;
        while (i < count) {
            hash = (hash * 31) + val[offset];
            i++;
            offset++;
        }
        int index = hash & (cache.length - 1);
        String cached = cache[index];
        if (cached == null) {
            cached = new String(val, start, count);
            cache[index] = cached;
            return cached;
        } else if (rangeEquals(start, count, cached)) {
            return cached;
        } else {
            return new String(val, start, count);
        }
    }

    boolean rangeEquals(int start, int count, String cached) {
        if (count != cached.length()) {
            return false;
        }
        char[] one = this.input;
        int j = 0;
        int i = start;
        int count2 = count;
        while (true) {
            count = count2 - 1;
            if (count2 == 0) {
                return true;
            }
            int i2 = i + 1;
            int j2 = j + 1;
            if (one[i] != cached.charAt(j)) {
                return false;
            }
            j = j2;
            i = i2;
            count2 = count;
        }
    }
}
