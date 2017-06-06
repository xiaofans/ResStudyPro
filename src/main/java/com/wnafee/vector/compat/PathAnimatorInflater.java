package com.wnafee.vector.compat;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import android.view.animation.AnimationUtils;
import com.wnafee.vector.R;
import com.wnafee.vector.compat.PathParser.PathDataNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class PathAnimatorInflater {
    private static final boolean DBG_ANIMATOR_INFLATER = false;
    private static final int SEQUENTIALLY = 1;
    private static final String TAG = "PathAnimatorInflater";
    private static final int TOGETHER = 0;
    private static final int VALUE_TYPE_PATH = 2;

    private static class PathDataEvaluator implements TypeEvaluator<PathDataNode[]> {
        private PathDataNode[] mNodeArray;

        private PathDataEvaluator() {
        }

        public PathDataEvaluator(PathDataNode[] nodeArray) {
            this.mNodeArray = nodeArray;
        }

        public PathDataNode[] evaluate(float fraction, PathDataNode[] startPathData, PathDataNode[] endPathData) {
            if (PathParser.canMorph(startPathData, endPathData)) {
                if (this.mNodeArray == null || !PathParser.canMorph(this.mNodeArray, startPathData)) {
                    this.mNodeArray = PathParser.deepCopyNodes(startPathData);
                }
                for (int i = 0; i < startPathData.length; i++) {
                    this.mNodeArray[i].interpolatePathDataNode(startPathData[i], endPathData[i], fraction);
                }
                return this.mNodeArray;
            }
            throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
        }
    }

    public static Animator loadAnimator(Context c, Resources resources, Theme theme, int id, float pathErrorScale) throws NotFoundException {
        NotFoundException rnf;
        XmlResourceParser xmlResourceParser = null;
        try {
            xmlResourceParser = resources.getAnimation(id);
            Animator createAnimatorFromXml = createAnimatorFromXml(c, resources, theme, xmlResourceParser, pathErrorScale);
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
            return createAnimatorFromXml;
        } catch (XmlPullParserException ex) {
            rnf = new NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } catch (IOException ex2) {
            rnf = new NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(ex2);
            throw rnf;
        } catch (Throwable th) {
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
        }
    }

    private static Animator createAnimatorFromXml(Context c, Resources res, Theme theme, XmlPullParser parser, float pixelSize) throws XmlPullParserException, IOException {
        return createAnimatorFromXml(c, res, theme, parser, Xml.asAttributeSet(parser), null, 0, pixelSize);
    }

    private static Animator createAnimatorFromXml(Context c, Resources res, Theme theme, XmlPullParser parser, AttributeSet attrs, AnimatorSet parent, int sequenceOrdering, float pixelSize) throws XmlPullParserException, IOException {
        Animator anim = null;
        ArrayList<Animator> childAnims = null;
        int depth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if ((type != 3 || parser.getDepth() > depth) && type != 1) {
                if (type == 2) {
                    String name = parser.getName();
                    if (name.equals("objectAnimator")) {
                        anim = loadObjectAnimator(c, res, theme, attrs, pixelSize);
                    } else {
                        if (name.equals("animator")) {
                            anim = loadAnimator(c, res, theme, attrs, null, pixelSize);
                        } else {
                            if (name.equals("set")) {
                                anim = new AnimatorSet();
                                createAnimatorFromXml(c, res, theme, parser, attrs, (AnimatorSet) anim, 0, pixelSize);
                            } else {
                                throw new RuntimeException("Unknown animator name: " + parser.getName());
                            }
                        }
                    }
                    if (parent != null) {
                        if (childAnims == null) {
                            childAnims = new ArrayList();
                        }
                        childAnims.add(anim);
                    }
                }
            }
        }
        if (!(parent == null || childAnims == null)) {
            Animator[] animsArray = new Animator[childAnims.size()];
            int index = 0;
            Iterator i$ = childAnims.iterator();
            while (i$.hasNext()) {
                int index2 = index + 1;
                animsArray[index] = (Animator) i$.next();
                index = index2;
            }
            if (sequenceOrdering == 0) {
                parent.playTogether(animsArray);
            } else {
                parent.playSequentially(animsArray);
            }
        }
        return anim;
    }

    private static ObjectAnimator loadObjectAnimator(Context c, Resources res, Theme theme, AttributeSet attrs, float pathErrorScale) throws NotFoundException {
        ObjectAnimator anim = new ObjectAnimator();
        loadAnimator(c, res, theme, attrs, anim, pathErrorScale);
        return anim;
    }

    private static ValueAnimator loadAnimator(Context c, Resources res, Theme theme, AttributeSet attrs, ValueAnimator anim, float pathErrorScale) throws NotFoundException {
        TypedArray arrayAnimator;
        TypedArray arrayObjectAnimator = null;
        if (theme != null) {
            arrayAnimator = theme.obtainStyledAttributes(attrs, R.styleable.Animator, 0, 0);
        } else {
            arrayAnimator = res.obtainAttributes(attrs, R.styleable.Animator);
        }
        if (anim != null) {
            if (theme != null) {
                arrayObjectAnimator = theme.obtainStyledAttributes(attrs, R.styleable.PropertyAnimator, 0, 0);
            } else {
                arrayObjectAnimator = res.obtainAttributes(attrs, R.styleable.PropertyAnimator);
            }
        }
        if (anim == null) {
            anim = new ValueAnimator();
        }
        parseAnimatorFromTypeArray(anim, arrayAnimator, arrayObjectAnimator);
        int resId = arrayAnimator.getResourceId(R.styleable.Animator_android_interpolator, 0);
        if (resId > 0) {
            anim.setInterpolator(AnimationUtils.loadInterpolator(c, resId));
        }
        arrayAnimator.recycle();
        if (arrayObjectAnimator != null) {
            arrayObjectAnimator.recycle();
        }
        return anim;
    }

    private static void parseAnimatorFromTypeArray(ValueAnimator anim, TypedArray arrayAnimator, TypedArray arrayObjectAnimator) {
        long duration = (long) arrayAnimator.getInt(R.styleable.Animator_android_duration, 300);
        long startDelay = (long) arrayAnimator.getInt(R.styleable.Animator_android_startOffset, 0);
        if (arrayAnimator.getInt(R.styleable.Animator_vc_valueType, 0) == 2) {
            TypeEvaluator evaluator = setupAnimatorForPath(anim, arrayAnimator);
            anim.setDuration(duration);
            anim.setStartDelay(startDelay);
            if (arrayAnimator.hasValue(R.styleable.Animator_android_repeatCount)) {
                anim.setRepeatCount(arrayAnimator.getInt(R.styleable.Animator_android_repeatCount, 0));
            }
            if (arrayAnimator.hasValue(R.styleable.Animator_android_repeatMode)) {
                anim.setRepeatMode(arrayAnimator.getInt(R.styleable.Animator_android_repeatMode, 1));
            }
            if (evaluator != null) {
                anim.setEvaluator(evaluator);
            }
            if (arrayObjectAnimator != null) {
                setupObjectAnimator(anim, arrayObjectAnimator);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("target is not a pathType target");
    }

    private static TypeEvaluator setupAnimatorForPath(ValueAnimator anim, TypedArray arrayAnimator) {
        String fromString = arrayAnimator.getString(R.styleable.Animator_android_valueFrom);
        String toString = arrayAnimator.getString(R.styleable.Animator_android_valueTo);
        PathDataNode[] nodesFrom = PathParser.createNodesFromPathData(fromString);
        PathDataNode[] nodesTo = PathParser.createNodesFromPathData(toString);
        if (nodesFrom != null) {
            if (nodesTo != null) {
                anim.setObjectValues(new Object[]{nodesFrom, nodesTo});
                if (!PathParser.canMorph(nodesFrom, nodesTo)) {
                    throw new InflateException(arrayAnimator.getPositionDescription() + " Can't morph from " + fromString + " to " + toString);
                }
            }
            anim.setObjectValues(new Object[]{nodesFrom});
            return new PathDataEvaluator(PathParser.deepCopyNodes(nodesFrom));
        } else if (nodesTo == null) {
            return null;
        } else {
            anim.setObjectValues(new Object[]{nodesTo});
            return new PathDataEvaluator(PathParser.deepCopyNodes(nodesTo));
        }
    }

    private static void setupObjectAnimator(ValueAnimator anim, TypedArray arrayObjectAnimator) {
        ((ObjectAnimator) anim).setPropertyName(arrayObjectAnimator.getString(R.styleable.PropertyAnimator_android_propertyName));
    }
}
