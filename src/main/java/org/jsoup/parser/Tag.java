package org.jsoup.parser;

import com.alipay.sdk.authjs.a;
import com.alipay.sdk.cons.c;
import com.douban.amonsul.StatConstant;
import com.douban.book.reader.constant.DeviceType;
import com.sina.weibo.sdk.component.WidgetRequestParam;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.helper.Validate;

public class Tag {
    private static final String[] blockTags = new String[]{"html", "head", "body", "frameset", "script", "noscript", "style", "meta", "link", "title", "frame", "noframes", "section", "nav", "aside", "hgroup", "header", "footer", "p", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "pre", "div", "blockquote", "hr", "address", "figure", "figcaption", c.c, "fieldset", "ins", "del", "s", "dl", "dt", "dd", "li", "table", "caption", "thead", "tfoot", "tbody", "colgroup", "col", "tr", "th", "td", "video", "audio", "canvas", "details", "menu", "plaintext", "template", "article", "main", "svg", "math"};
    private static final String[] emptyTags = new String[]{"meta", "link", "base", "frame", "img", "br", "wbr", "embed", "hr", "input", "keygen", "col", "command", "device", "area", "basefont", "bgsound", "menuitem", a.f, "source", "track"};
    private static final String[] formListedTags = new String[]{"button", "fieldset", "input", "keygen", "object", "output", "select", "textarea"};
    private static final String[] formSubmitTags = new String[]{"input", "keygen", "object", "select", "textarea"};
    private static final String[] formatAsInlineTags = new String[]{"title", DeviceType.IPAD, "p", "h1", "h2", "h3", "h4", "h5", "h6", "pre", "address", "li", "th", "td", "script", "style", "ins", "del", "s"};
    private static final String[] inlineTags = new String[]{"object", "base", "font", "tt", "i", "b", "u", "big", "small", "em", "strong", "dfn", "code", "samp", "kbd", "var", "cite", "abbr", "time", "acronym", "mark", "ruby", StatConstant.JSON_KEY_RESOLUTION, "rp", DeviceType.IPAD, "img", "br", "wbr", "map", WidgetRequestParam.REQ_PARAM_COMMENT_TOPIC, "sub", "sup", "bdo", "iframe", "embed", "span", "input", "select", "textarea", "label", "button", "optgroup", "option", "legend", "datalist", "keygen", "output", "progress", "meter", "area", a.f, "source", "track", "summary", "command", "device", "area", "basefont", "bgsound", "menuitem", a.f, "source", "track", "data", "bdi"};
    private static final String[] preserveWhitespaceTags = new String[]{"pre", "plaintext", "title", "textarea"};
    private static final Map<String, Tag> tags = new HashMap();
    private boolean canContainBlock = true;
    private boolean canContainInline = true;
    private boolean empty = false;
    private boolean formList = false;
    private boolean formSubmit = false;
    private boolean formatAsBlock = true;
    private boolean isBlock = true;
    private boolean preserveWhitespace = false;
    private boolean selfClosing = false;
    private String tagName;

    static {
        int i = 0;
        for (String tagName : blockTags) {
            register(new Tag(tagName));
        }
        for (String tagName2 : inlineTags) {
            Tag tag = new Tag(tagName2);
            tag.isBlock = false;
            tag.canContainBlock = false;
            tag.formatAsBlock = false;
            register(tag);
        }
        for (String tagName22 : emptyTags) {
            tag = (Tag) tags.get(tagName22);
            Validate.notNull(tag);
            tag.canContainBlock = false;
            tag.canContainInline = false;
            tag.empty = true;
        }
        for (String tagName222 : formatAsInlineTags) {
            tag = (Tag) tags.get(tagName222);
            Validate.notNull(tag);
            tag.formatAsBlock = false;
        }
        for (String tagName2222 : preserveWhitespaceTags) {
            tag = (Tag) tags.get(tagName2222);
            Validate.notNull(tag);
            tag.preserveWhitespace = true;
        }
        for (String tagName22222 : formListedTags) {
            tag = (Tag) tags.get(tagName22222);
            Validate.notNull(tag);
            tag.formList = true;
        }
        String[] strArr = formSubmitTags;
        int length = strArr.length;
        while (i < length) {
            tag = (Tag) tags.get(strArr[i]);
            Validate.notNull(tag);
            tag.formSubmit = true;
            i++;
        }
    }

    private Tag(String tagName) {
        this.tagName = tagName.toLowerCase();
    }

    public String getName() {
        return this.tagName;
    }

    public static Tag valueOf(String tagName) {
        Validate.notNull(tagName);
        Tag tag = (Tag) tags.get(tagName);
        if (tag != null) {
            return tag;
        }
        tagName = tagName.trim().toLowerCase();
        Validate.notEmpty(tagName);
        tag = (Tag) tags.get(tagName);
        if (tag != null) {
            return tag;
        }
        tag = new Tag(tagName);
        tag.isBlock = false;
        tag.canContainBlock = true;
        return tag;
    }

    public boolean isBlock() {
        return this.isBlock;
    }

    public boolean formatAsBlock() {
        return this.formatAsBlock;
    }

    public boolean canContainBlock() {
        return this.canContainBlock;
    }

    public boolean isInline() {
        return !this.isBlock;
    }

    public boolean isData() {
        return (this.canContainInline || isEmpty()) ? false : true;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean isSelfClosing() {
        return this.empty || this.selfClosing;
    }

    public boolean isKnownTag() {
        return tags.containsKey(this.tagName);
    }

    public static boolean isKnownTag(String tagName) {
        return tags.containsKey(tagName);
    }

    public boolean preserveWhitespace() {
        return this.preserveWhitespace;
    }

    public boolean isFormListed() {
        return this.formList;
    }

    public boolean isFormSubmittable() {
        return this.formSubmit;
    }

    Tag setSelfClosing() {
        this.selfClosing = true;
        return this;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        Tag tag = (Tag) o;
        if (!this.tagName.equals(tag.tagName) || this.canContainBlock != tag.canContainBlock || this.canContainInline != tag.canContainInline || this.empty != tag.empty || this.formatAsBlock != tag.formatAsBlock || this.isBlock != tag.isBlock || this.preserveWhitespace != tag.preserveWhitespace || this.selfClosing != tag.selfClosing || this.formList != tag.formList) {
            return false;
        }
        if (this.formSubmit != tag.formSubmit) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int i;
        int i2 = 1;
        int hashCode = ((this.tagName.hashCode() * 31) + (this.isBlock ? 1 : 0)) * 31;
        if (this.formatAsBlock) {
            i = 1;
        } else {
            i = 0;
        }
        hashCode = (hashCode + i) * 31;
        if (this.canContainBlock) {
            i = 1;
        } else {
            i = 0;
        }
        hashCode = (hashCode + i) * 31;
        if (this.canContainInline) {
            i = 1;
        } else {
            i = 0;
        }
        hashCode = (hashCode + i) * 31;
        if (this.empty) {
            i = 1;
        } else {
            i = 0;
        }
        hashCode = (hashCode + i) * 31;
        if (this.selfClosing) {
            i = 1;
        } else {
            i = 0;
        }
        hashCode = (hashCode + i) * 31;
        if (this.preserveWhitespace) {
            i = 1;
        } else {
            i = 0;
        }
        hashCode = (hashCode + i) * 31;
        if (this.formList) {
            i = 1;
        } else {
            i = 0;
        }
        i = (hashCode + i) * 31;
        if (!this.formSubmit) {
            i2 = 0;
        }
        return i + i2;
    }

    public String toString() {
        return this.tagName;
    }

    private static void register(Tag tag) {
        tags.put(tag.tagName, tag);
    }
}
