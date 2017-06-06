package org.jsoup.nodes;

public class BooleanAttribute extends Attribute {
    public BooleanAttribute(String key) {
        super(key, "");
    }

    protected boolean isBooleanAttribute() {
        return true;
    }
}
