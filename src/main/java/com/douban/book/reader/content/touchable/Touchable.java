package com.douban.book.reader.content.touchable;

import android.support.v4.internal.view.SupportMenu;
import com.douban.book.reader.content.HotArea;
import com.douban.book.reader.util.GradientColorGenerator;

public class Touchable {
    public static final int HIGH_PRIORITY = 30;
    public static final int LOW_PRIORITY = 10;
    public static final int MID_PRIORITY = 20;
    private static GradientColorGenerator sGradientColorGenerator = new GradientColorGenerator(10.0f, 30.0f, -16711936, SupportMenu.CATEGORY_MASK);
    public boolean canTriggerSelect = true;
    public HotArea hotArea = new HotArea();
    public int priority = 10;

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setHotArea(HotArea hotArea) {
        this.hotArea = hotArea.clone();
    }

    public static int getColor(Touchable touchable) {
        return sGradientColorGenerator.getColorForLevel((float) touchable.priority);
    }
}
