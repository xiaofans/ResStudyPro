package com.douban.book.reader.util;

import android.graphics.Typeface;
import com.douban.book.reader.app.App;

public class Font {
    public static final Typeface ENGLISH = create("formula/font/STIXMath.otf", Typeface.SERIF);
    public static final Typeface ENGLISH_BOLD = create("formula/font/STIXGeneralBold.otf", Typeface.SERIF);
    public static final Typeface SANS_SERIF = create("font/hyqh55s.otf", Typeface.SANS_SERIF);
    public static final Typeface SANS_SERIF_BOLD = create("font/hyqh75w.otf", Typeface.DEFAULT_BOLD);
    public static final Typeface SERIF = create("font/hyc1t.ttf", Typeface.SERIF);

    private static Typeface create(String assetPath, Typeface defaultFont) {
        try {
            defaultFont = Typeface.createFromAsset(App.get().getAssets(), assetPath);
        } catch (Throwable th) {
        }
        return defaultFont;
    }
}
