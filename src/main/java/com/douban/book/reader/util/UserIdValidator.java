package com.douban.book.reader.util;

import java.util.regex.Pattern;

public class UserIdValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_\\.0-9a-zA-Z+-]+@([0-9a-zA-Z]+[0-9a-zA-Z-]*\\.)+[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]+$");

    public static CharSequence normalizeInput(CharSequence input) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        return input.toString().trim().replace("＠", "@");
    }

    public static boolean looksLikeAnEmail(CharSequence input) {
        CharSequence normalized = normalizeInput(input);
        if (StringUtils.isEmpty(normalized)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(normalized).matches();
    }

    public static boolean looksLikeAPhoneNum(CharSequence input) {
        CharSequence normalized = normalizeInput(input);
        if (StringUtils.isEmpty(normalized)) {
            return false;
        }
        return PHONE_PATTERN.matcher(normalized).matches();
    }

    public static boolean looksLikeValidUid(CharSequence input) {
        return looksLikeAnEmail(input) || looksLikeAPhoneNum(input);
    }
}
