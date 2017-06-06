package com.douban.book.reader.content.page;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.douban.book.reader.content.Book;
import java.util.Arrays;
import java.util.List;

public class Range implements Parcelable {
    public static final Creator<Position> CREATOR = new Creator<Position>() {
        public Position createFromParcel(Parcel in) {
            return new Position(in);
        }

        public Position[] newArray(int size) {
            return new Position[size];
        }
    };
    public static final Range EMPTY = new Range(Position.NOT_FOUND, Position.NOT_FOUND);
    public Position endPosition;
    public Position startPosition;

    public enum Topology {
        AHEAD,
        LEFT_JOIN,
        FRONT_COVER,
        BACK_COVER,
        RIGHT_JOIN,
        BEHIND,
        INSIDE,
        BACK_EXTENT,
        BACK_SHRINK,
        FRONT_EXTENT,
        FRONT_SHRINK,
        FULL_COVER,
        EQUAL
    }

    public Range(Position startPos, Position endPos) {
        if (startPos == null) {
            startPos = Position.NOT_FOUND;
        }
        this.startPosition = startPos;
        if (endPos == null) {
            endPos = Position.NOT_FOUND;
        }
        this.endPosition = endPos;
    }

    public Range(Range another) {
        this(another.startPosition, another.endPosition);
    }

    public boolean isValid() {
        return Position.isValid(this.startPosition) && Position.isValid(this.endPosition) && this.endPosition.compareTo(this.startPosition) >= 0;
    }

    public void updateStartPosition(Position start) {
        if (isPositionAfterRange(this, start)) {
            this.endPosition = this.startPosition;
        }
        this.startPosition = start;
    }

    public void updateEndPosition(Position end) {
        if (isPositionBeforeRange(this, end)) {
            this.startPosition = this.endPosition;
        }
        this.endPosition = end;
    }

    public Topology compareTopology(Range opponent) {
        if (!isValid(opponent)) {
            return Topology.AHEAD;
        }
        int diffStart = this.startPosition.compareTo(opponent.startPosition);
        int diffStartEnd = this.startPosition.compareTo(opponent.endPosition);
        int diffEnd = this.endPosition.compareTo(opponent.endPosition);
        int diffEndStart = this.endPosition.compareTo(opponent.startPosition);
        Topology topology = Topology.EQUAL;
        if (diffStart >= 0 || diffEnd >= 0) {
            if (diffStart <= 0 || diffEnd <= 0) {
                if (diffStart < 0 && diffEnd > 0) {
                    return Topology.FULL_COVER;
                }
                if (diffStart > 0 && diffEnd < 0) {
                    return Topology.INSIDE;
                }
                if (diffStart == 0 && diffEnd > 0) {
                    return Topology.BACK_EXTENT;
                }
                if (diffStart == 0 && diffEnd < 0) {
                    return Topology.BACK_SHRINK;
                }
                if (diffStart < 0 && diffEnd == 0) {
                    return Topology.FRONT_EXTENT;
                }
                if (diffStart <= 0 || diffEnd != 0) {
                    return topology;
                }
                return Topology.FRONT_SHRINK;
            } else if (diffStartEnd > 1) {
                return Topology.BEHIND;
            } else {
                if (diffStartEnd == 1) {
                    return Topology.RIGHT_JOIN;
                }
                return Topology.BACK_COVER;
            }
        } else if (diffEndStart < -1) {
            return Topology.AHEAD;
        } else {
            if (diffEndStart == -1) {
                return Topology.LEFT_JOIN;
            }
            return Topology.FRONT_COVER;
        }
    }

    public static boolean isValid(Range range) {
        return range != null && range.isValid();
    }

    public static Range normalize(Range range) {
        if (isValid(range)) {
            return range;
        }
        if (range == null) {
            return EMPTY;
        }
        return new Range(range.endPosition, range.startPosition);
    }

    public static Range merge(Range range1, Range range2) {
        if (isValid(range1) && isValid(range2)) {
            Topology topology = range1.compareTopology(range2);
            if (topology == Topology.AHEAD || topology == Topology.BEHIND) {
                return EMPTY;
            }
            return new Range(Position.min(range1.startPosition, range2.startPosition), Position.max(range1.endPosition, range2.endPosition));
        } else if (isValid(range1)) {
            return range1;
        } else {
            return isValid(range2) ? range2 : EMPTY;
        }
    }

    public static List<Range> cut(int worksId, Range original, Range cut) {
        if (isValid(original) && isValid(cut)) {
            Book book = Book.get(worksId);
            switch (original.compareTopology(cut)) {
                case FULL_COVER:
                    return Arrays.asList(new Range[]{new Range(original.startPosition, book.getPrevPosition(cut.startPosition)), new Range(book.getNextPosition(cut.endPosition), original.endPosition)});
                case FRONT_COVER:
                case FRONT_EXTENT:
                case FRONT_SHRINK:
                    return Arrays.asList(new Range[]{new Range(original.startPosition, book.getPrevPosition(cut.startPosition))});
                case BACK_COVER:
                case BACK_EXTENT:
                case BACK_SHRINK:
                    return Arrays.asList(new Range[]{new Range(book.getNextPosition(cut.endPosition), original.endPosition)});
                case AHEAD:
                case BEHIND:
                case LEFT_JOIN:
                case RIGHT_JOIN:
                    return Arrays.asList(new Range[]{original});
                default:
                    return Arrays.asList(new Range[0]);
            }
        }
        return Arrays.asList(new Range[]{original});
    }

    public static Range enlarge(Range original, Position position) {
        if (isPositionBeforeRange(original, position)) {
            return new Range(position, original.endPosition);
        }
        if (isPositionAfterRange(original, position)) {
            return new Range(original.startPosition, position);
        }
        return original;
    }

    public static boolean isPositionBeforeRange(Range range, Position position) {
        return isValid(range) && Position.isValid(position) && position.compareTo(range.startPosition) < 0;
    }

    public static boolean isPositionAfterRange(Range range, Position position) {
        return isValid(range) && Position.isValid(position) && position.compareTo(range.endPosition) > 0;
    }

    public static boolean contains(Range range, Position position) {
        return isValid(range) && Position.isValid(position) && position.compareTo(range.startPosition) >= 0 && position.compareTo(range.endPosition) <= 0;
    }

    public static boolean intersects(Range range1, Range range2) {
        if (!isValid(range1) || !isValid(range2)) {
            return false;
        }
        switch (range1.compareTopology(range2)) {
            case FULL_COVER:
            case FRONT_COVER:
            case FRONT_EXTENT:
            case FRONT_SHRINK:
            case BACK_COVER:
            case BACK_EXTENT:
            case BACK_SHRINK:
            case INSIDE:
            case EQUAL:
                return true;
            default:
                return false;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.startPosition, flags);
        dest.writeParcelable(this.endPosition, flags);
    }

    public String toString() {
        return String.format("{%s - %s}", new Object[]{this.startPosition, this.endPosition});
    }
}
