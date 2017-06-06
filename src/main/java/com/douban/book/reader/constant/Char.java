package com.douban.book.reader.constant;

public class Char {
    public static final char BULLET = '•';
    public static final char CARRIAGE_RETURN = '\r';
    public static final String CRLF = "\r\n";
    public static final char DOT = '.';
    public static final char FULLWIDTH_COLON = '：';
    public static final char HYPHEN = '-';
    public static final char LEFT_COMER_BRACKET = '「';
    public static final char LEFT_DOUBLE_ANGLE_BRACKET = '《';
    public static final char LEFT_PARENTHESIS = '(';
    public static final char LINE_FEED = '\n';
    public static final char PIPE = '|';
    public static final char RIGHT_COMER_BRACKET = '」';
    public static final char RIGHT_DOUBLE_ANGLE_BRACKET = '》';
    public static final char RIGHT_PARENTHESIS = ')';
    public static final char SLASH = '/';
    public static final char SPACE = ' ';
    public static final char UNDERLINE = '_';

    public static char getMatchingQuote(char startQuote) {
        if (startQuote == LEFT_COMER_BRACKET) {
            return RIGHT_COMER_BRACKET;
        }
        if (startQuote == LEFT_PARENTHESIS) {
            return RIGHT_PARENTHESIS;
        }
        if (startQuote == LEFT_DOUBLE_ANGLE_BRACKET) {
            return RIGHT_DOUBLE_ANGLE_BRACKET;
        }
        throw new IllegalArgumentException(String.format("Unrecognized startQuote: %s", new Object[]{Character.valueOf(startQuote)}));
    }
}
