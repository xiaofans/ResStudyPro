package com.douban.book.reader.content.paragraph;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.SparseArray;
import com.douban.book.reader.app.App;
import com.douban.book.reader.constant.Char;
import com.douban.book.reader.util.CharUtils;
import com.douban.book.reader.util.Logger;
import com.douban.book.reader.util.PaintUtils;
import com.j256.ormlite.stmt.query.SimpleComparison;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Formula {
    private static final int MAX_SYMBOL_SIZE = 4;
    private static final int MIN_SYMBOL_SIZE = 2;
    public static final float SIZE2_RATIO = 1.95f;
    public static final float SIZE3_RATIO = 2.5f;
    public static final float SIZE4_RATIO = 3.11f;
    private static final String TAG = "Formula";
    private static HashMap<String, String> sCodes;
    private static Typeface sMathTypeface;
    private static SparseArray<Typeface> sSizedTypefaces = new SparseArray();
    private float mBaseTextSize = 40.0f;
    private boolean mIsRootFormula = false;
    private List<Node> mNodeList = new ArrayList();
    private float mScale = 1.0f;
    private int mTextColor = 0;

    public static class Node {
        public static final int SUB = -1;
        public static final int SUP = -2;
        private static ArrayList<String> sSymbolsNeedMargin = new ArrayList(Arrays.asList(new String[]{"+", "-", "/", SimpleComparison.EQUAL_TO_OPERATION, SimpleComparison.GREATER_THAN_OPERATION, SimpleComparison.LESS_THAN_OPERATION, "times", "cdot", ",", "approx", "approxeq", "leqq", "leq", "le", "geqq", "geq", "ge"}));
        public float mBaseStrokeWidth = 2.0f;
        private float mBaseTextSize = 40.0f;
        private String mData = "";
        private float mDisplayRatio = 1.0f;
        private List<Formula> mParamList = null;
        private float mScale = 1.0f;
        private Formula mSub = null;
        private Formula mSup = null;
        private int mTextColor = 0;
        private String mType = "";
        private int mTypefaceSize = 1;

        public void setType(String type) {
            this.mType = type;
        }

        public void setData(String data) {
            this.mData = data;
        }

        public void setTypefaceSize(int size) {
            if (size >= 2 && size <= 4) {
                this.mTypefaceSize = size;
            }
        }

        public int getTypefaceSize() {
            return this.mTypefaceSize;
        }

        public void setScale(float scale) {
            this.mScale = scale;
        }

        public float getScale() {
            return this.mScale;
        }

        public void setBaseTextSize(float baseTextSize) {
            this.mBaseTextSize = baseTextSize;
            this.mBaseStrokeWidth = Math.max(1.0f, this.mBaseTextSize / 20.0f);
            getParam(-1).setBaseTextSize(baseTextSize);
            getParam(-2).setBaseTextSize(baseTextSize);
            if (this.mParamList != null) {
                for (Formula param : this.mParamList) {
                    param.setBaseTextSize(baseTextSize);
                }
            }
        }

        public void setTextColor(int color) {
            this.mTextColor = color;
            getParam(-1).setTextColor(color);
            getParam(-2).setTextColor(color);
            if (this.mParamList != null) {
                for (Formula param : this.mParamList) {
                    param.setTextColor(color);
                }
            }
        }

        public void setDisplayRatio(float ratio) {
            this.mDisplayRatio = ratio;
            getParam(-1).setDisplayRatio(ratio);
            getParam(-2).setDisplayRatio(ratio);
            if (this.mParamList != null) {
                for (Formula param : this.mParamList) {
                    param.setDisplayRatio(ratio);
                }
            }
        }

        public void addSub(Formula sub) {
            this.mSub = sub;
        }

        public void addSup(Formula sup) {
            this.mSup = sup;
        }

        public void addParam(Formula param) {
            if (this.mParamList == null) {
                this.mParamList = new ArrayList();
            }
            this.mParamList.add(param);
        }

        public Formula getParam() {
            return getParam(0);
        }

        public Formula getParam(int index) {
            Formula ret = null;
            if (index == -1) {
                ret = this.mSub;
            } else if (index == -2) {
                ret = this.mSup;
            } else if (this.mParamList != null && this.mParamList.size() > index) {
                ret = (Formula) this.mParamList.get(index);
            }
            if (ret == null) {
                return new Formula();
            }
            return ret;
        }

        private float getTextWidth() {
            float margin = 0.0f;
            String str = this.mData;
            if (TextUtils.isEmpty(str)) {
                str = this.mType;
                if (getParam(-1).isEmpty() && getParam(-2).isEmpty()) {
                    margin = (this.mBaseTextSize * this.mScale) * 0.1f;
                }
            }
            if (TextUtils.isEmpty(str)) {
                return 0.0f;
            }
            Paint paint = PaintUtils.obtainPaint();
            paint.setTextSize(getTextSize());
            if (getTypefaceSize() > 1) {
                paint.setTypeface(Formula.getSizedTypeface(getTypefaceSize()));
            } else {
                paint.setTypeface(Formula.sMathTypeface);
            }
            float result = paint.measureText(str) + margin;
            PaintUtils.recyclePaint(paint);
            return result;
        }

        private float getTextHeight() {
            float textHeight = this.mBaseTextSize * this.mScale;
            if (getTypefaceSize() == 4) {
                return textHeight * Formula.SIZE4_RATIO;
            }
            if (getTypefaceSize() == 3) {
                return textHeight * Formula.SIZE3_RATIO;
            }
            if (getTypefaceSize() == 2) {
                return textHeight * Formula.SIZE2_RATIO;
            }
            return textHeight;
        }

        public float getMargin() {
            if (sSymbolsNeedMargin.contains(this.mData) || sSymbolsNeedMargin.contains(this.mType)) {
                return (this.mBaseTextSize * this.mScale) * 0.1f;
            }
            return 0.0f;
        }

        public float getWidth() {
            float width0;
            float width1;
            if (this.mType.equals("frac")) {
                width0 = getParam(0).getWidth();
                width1 = getParam(1).getWidth();
                if (width0 <= width1) {
                    width0 = width1;
                }
                return width0;
            } else if (this.mType.equals("sum")) {
                maxWidth = getTextWidth();
                subWidth = getParam(-1).getWidth();
                if (subWidth > maxWidth) {
                    maxWidth = subWidth;
                }
                supWidth = getParam(-2).getWidth();
                if (supWidth > maxWidth) {
                    return supWidth;
                }
                return maxWidth;
            } else if (this.mType.equals("sqrt")) {
                float width = getParam(0).getWidth() + getTextWidth();
                supWidth = getParam(-2).getWidth();
                if (supWidth > getTextWidth() / 1.5f) {
                    width += supWidth - (getTextWidth() / 1.5f);
                }
                return width;
            } else if (this.mType.equals("overline")) {
                return getParam(0).getWidth();
            } else {
                if (this.mType.equals("int")) {
                    float textWidth = getTextWidth();
                    subWidth = getParam(-1).getWidth() + (textWidth * 1.5f);
                    supWidth = getParam(-2).getWidth() + textWidth;
                    if (subWidth > supWidth) {
                        maxWidth = subWidth;
                    } else {
                        maxWidth = supWidth;
                    }
                    return maxWidth;
                }
                width0 = getParam(-1).getWidth();
                width1 = getParam(-2).getWidth();
                if (width0 > width1) {
                    maxWidth = width0;
                } else {
                    maxWidth = width1;
                }
                if (!(this.mParamList == null || this.mParamList.isEmpty())) {
                    for (Formula param : this.mParamList) {
                        maxWidth += param.getWidth();
                    }
                }
                return (getTextWidth() + maxWidth) + (getMargin() * 2.0f);
            }
        }

        public float getHeight() {
            if (this.mType.equals("frac")) {
                return (getParam(0).getHeight() + getParam(1).getHeight()) + 5.0f;
            }
            if (this.mType.equals("sum")) {
                return (getParam(0).getHeight() + getParam(1).getHeight()) + (getTextHeight() * 1.2f);
            }
            if (this.mType.equals("overline")) {
                return getParam().getHeight();
            }
            if (this.mType.equals("sqrt")) {
                return getParam().getHeight() * 1.2f;
            }
            float maxHeight = getTextHeight();
            if (this.mParamList == null || this.mParamList.isEmpty()) {
                return maxHeight;
            }
            for (Formula param : this.mParamList) {
                float paramHeight = param.getHeight();
                if (paramHeight > maxHeight) {
                    maxHeight = paramHeight;
                }
            }
            return maxHeight;
        }

        private float getTextSize() {
            float textSize = (this.mBaseTextSize * this.mScale) * this.mDisplayRatio;
            if (textSize < 10.0f) {
                return 10.0f;
            }
            return textSize;
        }

        private float getStrokeWidth() {
            return (this.mBaseStrokeWidth * this.mScale) * this.mDisplayRatio;
        }

        public String enbrace(String str) {
            if (str.length() > 2 && str.charAt(0) == '{' && str.charAt(str.length() - 1) == '}') {
                return str;
            }
            return "{" + str + "}";
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            int typefaceSize = getTypefaceSize();
            if (typefaceSize == 4) {
                builder.append("\\bigg");
            } else if (typefaceSize == 3) {
                builder.append("\\Big");
            } else if (typefaceSize == 2) {
                builder.append("\\big");
            }
            if (TextUtils.isEmpty(this.mType)) {
                builder.append(this.mData);
            } else {
                builder.append("\\").append(this.mType).append(" ");
            }
            if (getParam(-2).getNodeCount() > 0) {
                if (this.mType.equals("sqrt")) {
                    builder.append("[").append(getParam(-2).toString()).append("]");
                } else {
                    builder.append(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR).append(enbrace(getParam(-2).toString()));
                }
            }
            if (getParam(-1).getNodeCount() > 0) {
                builder.append("^").append(enbrace(getParam(-1).toString()));
            }
            if (!(this.mParamList == null || this.mParamList.isEmpty())) {
                for (Formula param : this.mParamList) {
                    builder.append(enbrace(param.toString()));
                }
            }
            return builder.toString();
        }

        public void draw(Canvas canvas, float offsetX, float offsetY) {
            Paint paint = PaintUtils.obtainPaint();
            paint.setTypeface(Formula.sMathTypeface);
            paint.setTextSize(getTextSize());
            paint.setStrokeWidth(getStrokeWidth());
            paint.setColor(this.mTextColor);
            offsetX += getMargin();
            float sizedFontAdjustmentY = 0.0f;
            if (getTypefaceSize() > 1) {
                paint.setTypeface(Formula.getSizedTypeface(getTypefaceSize()));
                sizedFontAdjustmentY = 0.0f + (getTextHeight() * 0.15f);
            }
            float y0;
            Canvas canvas2;
            if (this.mType.equals("frac")) {
                getParam(0).draw(canvas, offsetX + ((getWidth() - getParam(0).getWidth()) / 2.0f), offsetY - (getParam(0).getHeight() * 0.65f));
                getParam(1).draw(canvas, offsetX + ((getWidth() - getParam(1).getWidth()) / 2.0f), offsetY + (getParam(1).getHeight() * 0.75f));
                y0 = offsetY - (getTextHeight() * 0.25f);
                canvas2 = canvas;
                canvas2.drawLine(offsetX, y0, offsetX + getWidth(), y0, paint);
            } else if (this.mType.equals("overline")) {
                y0 = offsetY - getParam().getHeight();
                canvas2 = canvas;
                canvas2.drawLine(offsetX, y0, offsetX + getParam().getWidth(), y0, paint);
                getParam().draw(canvas, offsetX, offsetY);
            } else if (this.mType.equals("int")) {
                getParam(-1).draw(canvas, offsetX + (getTextWidth() * 1.5f), offsetY - getParam(-1).getHeight());
                getParam(-2).draw(canvas, offsetX + getTextWidth(), offsetY + (getParam(-2).getHeight() / 2.0f));
                canvas.drawText(this.mData, offsetX, offsetY, paint);
            } else if (this.mType.equals("sum")) {
                getParam(-1).draw(canvas, offsetX + ((getWidth() - getParam(-1).getWidth()) / 2.0f), (offsetY - (getTextHeight() * CharUtils.FULL_WIDTH_CHAR_OFFSET_ADJUSTMENT_RATIO)) - getParam(-1).getHeight());
                getParam(-2).draw(canvas, offsetX + ((getWidth() - getParam(-2).getWidth()) / 2.0f), ((getTextHeight() * 0.2f) + offsetY) + getParam(-2).getHeight());
                canvas.drawText(this.mData, ((getWidth() - getTextWidth()) / 2.0f) + offsetX, offsetY, paint);
            } else if (this.mType.equals("sqrt")) {
                float supWidth = getParam(-2).getWidth();
                if (supWidth > getTextWidth() / 1.5f) {
                    getParam(-2).draw(canvas, offsetX, offsetY - (getTextHeight() / 2.0f));
                    offsetX += supWidth - (getTextWidth() / 1.5f);
                } else {
                    getParam(-2).draw(canvas, ((getTextWidth() / 1.5f) + offsetX) - supWidth, offsetY - (getTextHeight() / 2.0f));
                }
                float x0 = offsetX + getTextWidth();
                y0 = (offsetY - getTextHeight()) + sizedFontAdjustmentY;
                canvas.drawLine(x0, y0, x0 + getParam().getWidth(), y0, paint);
                canvas.drawText(this.mData, offsetX, offsetY + sizedFontAdjustmentY, paint);
                getParam().draw(canvas, getTextWidth() + offsetX, offsetY);
            } else {
                String str = this.mData;
                if (TextUtils.isEmpty(this.mData)) {
                    str = this.mType;
                }
                canvas.drawText(str, offsetX, offsetY + sizedFontAdjustmentY, paint);
                offsetX += getTextWidth();
                getParam(-1).draw(canvas, offsetX, offsetY - (getTextHeight() * 0.5f));
                getParam(-2).draw(canvas, offsetX, (getTextHeight() * 0.15f) + offsetY);
                if (!(this.mParamList == null || this.mParamList.isEmpty())) {
                    float maxWidth;
                    float width0 = getParam(-1).getWidth();
                    float width1 = getParam(-2).getWidth();
                    if (width0 > width1) {
                        maxWidth = width0;
                    } else {
                        maxWidth = width1;
                    }
                    offsetX += maxWidth;
                    for (Formula param : this.mParamList) {
                        param.draw(canvas, offsetX, offsetY);
                        offsetX += param.getWidth();
                    }
                }
            }
            PaintUtils.recyclePaint(paint);
        }
    }

    private static class Parser {
        int cursor = 0;
        String latex = "";

        public Parser(String latex) {
            this.latex = latex;
            this.cursor = 0;
        }

        public Formula parse() {
            return parse(null, 1.0f);
        }

        public Formula parse(String startToken, float scale) {
            Formula formula = new Formula();
            formula.setScale(scale);
            Node node = new Node();
            formula.addNode(node);
            while (true) {
                String token = getNextToken();
                if (token == null) {
                    break;
                } else if (token.length() > 1 && token.charAt(0) == '\\') {
                    String type = token.substring(1);
                    int typefaceSize;
                    float ratio;
                    if (type.equals("left")) {
                        Node startNode = getNextNode(scale);
                        formula.addNode(startNode);
                        Formula subFormula = parse("left", scale);
                        startNode.addParam(subFormula);
                        Node endNode = getNextNode(scale);
                        formula.addNode(endNode);
                        typefaceSize = 0;
                        ratio = subFormula.getHeight() / startNode.getTextSize();
                        if (ratio > Formula.SIZE4_RATIO) {
                            typefaceSize = 4;
                        } else if (ratio > Formula.SIZE3_RATIO) {
                            typefaceSize = 3;
                        } else if (ratio > Formula.SIZE2_RATIO) {
                            typefaceSize = 2;
                        }
                        if (typefaceSize > 1) {
                            startNode.setTypefaceSize(typefaceSize);
                            endNode.setTypefaceSize(typefaceSize);
                        }
                    } else if (type.equals("right")) {
                        if (startToken != null && startToken.equals("left")) {
                            break;
                        }
                    } else if (type.equals("Bigg") || type.equals("bigg") || type.equals("big") || type.equals("Big")) {
                        node = getNextNode(scale);
                        formula.addNode(node);
                        if (type.equals("Bigg") || type.equals("bigg")) {
                            node.setTypefaceSize(4);
                        } else if (type.equals("Big")) {
                            node.setTypefaceSize(3);
                        } else if (type.equals("big")) {
                            node.setTypefaceSize(2);
                        }
                    } else {
                        node = new Node();
                        node.setType(type);
                        if (Formula.sCodes.containsKey(type)) {
                            node.setData(Formula.getCodeStr(type));
                        }
                        formula.addNode(node);
                        if (type.equals("sqrt")) {
                            if (peekNextChar() == '[') {
                                skip(1);
                                node.addSup(parse("[", 0.5f * scale));
                            }
                            Formula param = new Formula();
                            Node paramNode = getNextNode(scale);
                            param.addNode(paramNode);
                            node.addParam(param);
                            typefaceSize = 0;
                            ratio = param.getHeight() / paramNode.getTextSize();
                            if (ratio > Formula.SIZE4_RATIO) {
                                typefaceSize = 4;
                            } else if (ratio > Formula.SIZE3_RATIO) {
                                typefaceSize = 3;
                            } else if (ratio > Formula.SIZE2_RATIO) {
                                typefaceSize = 2;
                            }
                            if (typefaceSize > 1) {
                                node.setTypefaceSize(typefaceSize);
                            }
                        }
                        if (startToken != null && (startToken.equals("^") || startToken.equals(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR))) {
                            while (peekNextChar() == '{') {
                                skip(1);
                                node.addParam(parse("{", scale));
                            }
                        }
                    }
                } else if (token.charAt(0) == '{') {
                    node.addParam(parse("{", scale));
                } else if (token.charAt(0) == '}') {
                    if (startToken != null && startToken.equals("{")) {
                        break;
                    }
                } else if (token.charAt(0) == ']') {
                    if (startToken != null && startToken.equals("[")) {
                        break;
                    }
                    node = new Node();
                    node.setData(token);
                    formula.addNode(node);
                    if (startToken == null) {
                        continue;
                    } else if (startToken.equals("^")) {
                        break;
                    } else if (startToken.equals(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR)) {
                        break;
                    }
                } else if (token.charAt(0) == '^') {
                    if (peekNextChar() == '{') {
                        skip(1);
                        node.addSub(parse("{", 0.5f * scale));
                    } else {
                        node.addSub(parse("^", 0.5f * scale));
                    }
                } else if (token.charAt(0) != Char.UNDERLINE) {
                    node = new Node();
                    node.setData(token);
                    formula.addNode(node);
                    if (startToken == null) {
                        continue;
                    } else if (startToken.equals("^")) {
                        break;
                    } else if (startToken.equals(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR)) {
                        break;
                    }
                } else if (peekNextChar() == '{') {
                    skip(1);
                    node.addSup(parse("{", 0.5f * scale));
                } else {
                    node.addSup(parse(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR, 0.5f * scale));
                }
            }
            return formula;
        }

        private Node getNextNode(float scale) {
            Node node = new Node();
            String token = getNextToken();
            if (token.equals("{")) {
                node.addParam(parse("{", scale));
            } else if (token.length() <= 1 || token.charAt(0) != '\\') {
                node.setData(token);
            } else {
                String type = token.substring(1);
                node.setType(type);
                if (Formula.sCodes.containsKey(type)) {
                    node.setData(Formula.getCodeStr(type));
                }
            }
            char next = peekNextChar();
            if (next == '^' || next == Char.UNDERLINE) {
                skip(1);
                if (next == '^') {
                    if (peekNextChar() == '{') {
                        skip(1);
                        node.addSub(parse("{", scale * 0.5f));
                    } else {
                        node.addSub(parse("^", scale * 0.5f));
                    }
                } else if (next == Char.UNDERLINE) {
                    if (peekNextChar() == '{') {
                        skip(1);
                        node.addSup(parse("{", scale * 0.5f));
                    } else {
                        node.addSup(parse(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR, scale * 0.5f));
                    }
                }
            }
            return node;
        }

        public String getNextToken() {
            int len = 0;
            String ret = null;
            while (len == 0 && this.cursor < this.latex.length()) {
                char c = this.latex.charAt(this.cursor);
                if (c == '\\') {
                    len = 0;
                    if (this.cursor < this.latex.length() - 1) {
                        len = 1;
                    }
                    char nextc;
                    do {
                        len++;
                        if (this.cursor + len < this.latex.length()) {
                            nextc = this.latex.charAt(this.cursor + len);
                        } else {
                            nextc = Char.SPACE;
                        }
                    } while (Character.isLetter(nextc));
                } else if (Character.isWhitespace(c)) {
                    len = 0;
                    this.cursor++;
                } else {
                    len = 1;
                }
            }
            if (len > 0) {
                ret = this.latex.substring(this.cursor, this.cursor + len);
            }
            this.cursor += len;
            return ret;
        }

        public char peekNextChar() {
            if (this.cursor >= this.latex.length()) {
                return Char.SPACE;
            }
            Character ch = Character.valueOf(this.latex.charAt(this.cursor));
            while (Character.isWhitespace(ch.charValue())) {
                int i = this.cursor + 1;
                this.cursor = i;
                if (i >= this.latex.length()) {
                    return Char.SPACE;
                }
                ch = Character.valueOf(this.latex.charAt(this.cursor));
            }
            return ch.charValue();
        }

        public void skip(int len) {
            this.cursor += len;
        }
    }

    static {
        sMathTypeface = null;
        AssetManager assetManager = App.get().getAssets();
        try {
            sMathTypeface = Typeface.createFromAsset(assetManager, "formula/font/STIXMath.otf");
        } catch (Exception e) {
            Logger.e(TAG, e);
            sMathTypeface = Typeface.SERIF;
        }
        for (int i = 2; i <= 4; i++) {
            if (sSizedTypefaces.get(i) == null) {
                Typeface typeface;
                try {
                    typeface = Typeface.createFromAsset(assetManager, "formula/font/STIXSize" + i + "Sym.otf");
                } catch (Exception e2) {
                    Logger.e(TAG, e2);
                    typeface = sMathTypeface;
                }
                sSizedTypefaces.put(i, typeface);
            }
        }
        try {
            sCodes = new HashMap();
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open("formula/symbols"), Charset.forName("UTF-8")));
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    try {
                        String[] cols = line.split("\\s+");
                        sCodes.put(cols[0].substring(1), cols[1]);
                    } catch (Exception e3) {
                        Logger.d(TAG, "initSymbolMap failed in line: " + line, new Object[0]);
                    }
                } else {
                    return;
                }
            }
        } catch (Exception e22) {
            Logger.e(TAG, e22);
        }
    }

    public static Formula parseLatex(String latex) {
        Formula result = new Parser(latex).parse();
        result.mIsRootFormula = true;
        return result;
    }

    public Formula subFormula(int start) {
        return subFormula(start, this.mNodeList.size());
    }

    public Formula subFormula(int start, int end) {
        Formula result = new Formula();
        result.setScale(getScale());
        if (end > start) {
            if (start < 0) {
                start = 0;
            }
            if (end > this.mNodeList.size()) {
                end = this.mNodeList.size();
            }
            int i = start;
            while (i < end) {
                try {
                    result.addNode((Node) this.mNodeList.get(i));
                    i++;
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    public boolean isEmpty() {
        return getNodeCount() == 0;
    }

    public float getMargin() {
        if (this.mIsRootFormula) {
            return (this.mBaseTextSize * this.mScale) * 0.1f;
        }
        return 0.0f;
    }

    public float getWidth() {
        float result = 0.0f;
        for (Node node : this.mNodeList) {
            result += node.getWidth();
        }
        if (result > 0.0f) {
            return result + (getMargin() * 2.0f);
        }
        return result;
    }

    public float getHeight() {
        float result = 0.0f;
        for (Node node : this.mNodeList) {
            float nodeHeight = node.getHeight();
            if (result < nodeHeight) {
                result = nodeHeight;
            }
        }
        return result;
    }

    public void setScale(float scale) {
        this.mScale = scale;
        for (Node node : this.mNodeList) {
            node.setScale(scale);
        }
    }

    public float getScale() {
        return this.mScale;
    }

    public void setDisplayRatio(float ratio) {
        for (Node node : this.mNodeList) {
            node.setDisplayRatio(ratio);
        }
    }

    public void addNode(Node node) {
        if (this.mScale != 1.0f) {
            node.setScale(this.mScale);
        }
        this.mNodeList.add(node);
    }

    public int getNodeCount() {
        return this.mNodeList.size();
    }

    public void setBaseTextSize(float textSize) {
        if (this.mBaseTextSize != textSize) {
            this.mBaseTextSize = textSize;
            for (Node node : this.mNodeList) {
                node.setBaseTextSize(this.mBaseTextSize);
            }
        }
    }

    public void setTextColor(int color) {
        if (this.mTextColor != color) {
            this.mTextColor = color;
            for (Node node : this.mNodeList) {
                node.setTextColor(color);
            }
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Node node : this.mNodeList) {
            builder.append(node.toString());
        }
        return builder.toString();
    }

    public int getSubFormulaOffsetByWidth(float limitWidth) {
        return getSubFormulaOffsetByWidth(limitWidth, false);
    }

    public int getSubFormulaOffsetByWidth(float limitWidth, boolean canExceed) {
        if (getWidth() <= limitWidth) {
            return getNodeCount();
        }
        float formulaWidth = getMargin();
        float width = 0.0f;
        int offset = 0;
        int subcount = 0;
        for (Node node : this.mNodeList) {
            width += node.getWidth();
            subcount++;
            String type = node.mType;
            String data = node.mData;
            if (data.equals("+") || data.equals(SimpleComparison.EQUAL_TO_OPERATION) || data.equals(",") || type.equals("leqq") || type.equals("leq") || type.equals("le") || type.equals("geqq") || type.equals("geq") || type.equals("ge") || type.equals("approx") || type.equals("approxeq")) {
                if (formulaWidth + width >= limitWidth) {
                    if (canExceed) {
                        offset++;
                    }
                    if (offset != 0 && canExceed) {
                        return getNodeCount();
                    }
                }
                offset += subcount;
                subcount = 0;
                formulaWidth += width;
                width = 0.0f;
            }
        }
        return offset != 0 ? offset : offset;
    }

    public void draw(Canvas canvas, float offsetX, float offsetY) {
        offsetX += getMargin();
        for (Node node : this.mNodeList) {
            node.draw(canvas, offsetX, offsetY);
            offsetX += node.getWidth();
        }
    }

    private static Typeface getSizedTypeface(int size) {
        return (Typeface) sSizedTypefaces.get(size, sMathTypeface);
    }

    private static String getCodeStr(String name) {
        return !sCodes.containsKey(name) ? name : (String) sCodes.get(name);
    }
}
