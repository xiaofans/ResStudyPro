package com.igexin.download;

import com.douban.book.reader.constant.Char;
import com.j256.ormlite.stmt.query.ManyClause;
import java.util.Set;

class i {
    private final String a;
    private final Set b;
    private int c = 0;
    private int d = 0;
    private final char[] e;

    public i(String str, Set set) {
        this.a = str;
        this.b = set;
        this.e = new char[this.a.length()];
        this.a.getChars(0, this.e.length, this.e, 0);
        b();
    }

    private static final boolean a(char c) {
        return c == Char.UNDERLINE || ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'));
    }

    private static final boolean b(char c) {
        return c == Char.UNDERLINE || ((c >= 'A' && c <= 'Z') || ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')));
    }

    public int a() {
        return this.d;
    }

    public void b() {
        char[] cArr = this.e;
        while (this.c < cArr.length && cArr[this.c] == Char.SPACE) {
            this.c++;
        }
        if (this.c == cArr.length) {
            this.d = 9;
        } else if (cArr[this.c] == Char.LEFT_PARENTHESIS) {
            this.c++;
            this.d = 1;
        } else if (cArr[this.c] == Char.RIGHT_PARENTHESIS) {
            this.c++;
            this.d = 2;
        } else if (cArr[this.c] == '?') {
            this.c++;
            this.d = 6;
        } else if (cArr[this.c] == '=') {
            this.c++;
            this.d = 5;
            if (this.c < cArr.length && cArr[this.c] == '=') {
                this.c++;
            }
        } else if (cArr[this.c] == '>') {
            this.c++;
            this.d = 5;
            if (this.c < cArr.length && cArr[this.c] == '=') {
                this.c++;
            }
        } else if (cArr[this.c] == '<') {
            this.c++;
            this.d = 5;
            if (this.c >= cArr.length) {
                return;
            }
            if (cArr[this.c] == '=' || cArr[this.c] == '>') {
                this.c++;
            }
        } else if (cArr[this.c] == '!') {
            this.c++;
            this.d = 5;
            if (this.c >= cArr.length || cArr[this.c] != '=') {
                throw new IllegalArgumentException("Unexpected character after !");
            }
            this.c++;
        } else if (a(cArr[this.c])) {
            int i = this.c;
            this.c++;
            while (this.c < cArr.length && b(cArr[this.c])) {
                this.c++;
            }
            String substring = this.a.substring(i, this.c);
            if (this.c - i <= 4) {
                if (substring.equals("IS")) {
                    this.d = 7;
                    return;
                } else if (substring.equals(ManyClause.OR_OPERATION) || substring.equals(ManyClause.AND_OPERATION)) {
                    this.d = 3;
                    return;
                } else if (substring.equals("NULL")) {
                    this.d = 8;
                    return;
                }
            }
            if (this.b.contains(substring)) {
                this.d = 4;
                return;
            }
            throw new IllegalArgumentException("unrecognized column or keyword");
        } else if (cArr[this.c] == '\'') {
            this.c++;
            while (this.c < cArr.length) {
                if (cArr[this.c] == '\'') {
                    if (this.c + 1 >= cArr.length || cArr[this.c + 1] != '\'') {
                        break;
                    }
                    this.c++;
                }
                this.c++;
            }
            if (this.c == cArr.length) {
                throw new IllegalArgumentException("unterminated string");
            }
            this.c++;
            this.d = 6;
        } else {
            throw new IllegalArgumentException("illegal character");
        }
    }
}
