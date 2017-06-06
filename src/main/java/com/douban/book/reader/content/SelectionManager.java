package com.douban.book.reader.content;

import com.douban.book.reader.content.page.Position;
import com.douban.book.reader.content.page.Range;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

@EBean(scope = Scope.Singleton)
public class SelectionManager {
    public static final int PIN_END = 2;
    public static final int PIN_NONE = 0;
    public static final int PIN_START = 1;
    private int mMovingPin = 0;
    private Position mMovingPosition = null;
    private Range mSelectionRange = Range.EMPTY;

    public Range getSelectionRange() {
        if (isEnlargingBackwards() && Position.isValid(this.mMovingPosition)) {
            return new Range(this.mMovingPosition, this.mSelectionRange.endPosition);
        }
        if (isEnlargingAfterwards() && Position.isValid(this.mMovingPosition)) {
            return new Range(this.mSelectionRange.startPosition, this.mMovingPosition);
        }
        return this.mSelectionRange;
    }

    public Position getSelectionStart() {
        return getSelectionRange().startPosition;
    }

    public Position getSelectionEnd() {
        return getSelectionRange().endPosition;
    }

    public void setSelectionRange(Range range) {
        this.mSelectionRange = range;
        this.mMovingPin = 0;
    }

    public void clearSelection() {
        this.mSelectionRange = Range.EMPTY;
        this.mMovingPin = 0;
    }

    public void editStartPosition() {
        this.mMovingPin = 1;
    }

    public void editEndPosition() {
        this.mMovingPin = 2;
    }

    public void moveTo(Position position) {
        this.mMovingPosition = position;
    }

    public void commit() {
        this.mSelectionRange = getSelectionRange();
        this.mMovingPosition = null;
        this.mMovingPin = 0;
    }

    public boolean isEnlargingBackwards() {
        boolean z = true;
        if (!Range.isValid(this.mSelectionRange) || !Position.isValid(this.mMovingPosition)) {
            if (this.mMovingPin != 1) {
                z = false;
            }
            return z;
        } else if (Range.isPositionBeforeRange(this.mSelectionRange, this.mMovingPosition) || (this.mMovingPin == 1 && Range.contains(this.mSelectionRange, this.mMovingPosition))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEnlargingAfterwards() {
        boolean z = true;
        if (!Range.isValid(this.mSelectionRange) || !Position.isValid(this.mMovingPosition)) {
            if (this.mMovingPin != 2) {
                z = false;
            }
            return z;
        } else if (Range.isPositionAfterRange(this.mSelectionRange, this.mMovingPosition) || (this.mMovingPin == 2 && Range.contains(this.mSelectionRange, this.mMovingPosition))) {
            return true;
        } else {
            return false;
        }
    }
}
