package org.mapdb;

import java.io.DataInput;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;

public final class Fun {
    public static final Comparator<byte[]> BYTE_ARRAY_COMPARATOR = new Comparator<byte[]>() {
        public int compare(byte[] o1, byte[] o2) {
            if (o1 == o2) {
                return 0;
            }
            int len = Math.min(o1.length, o2.length);
            int i = 0;
            while (i < len) {
                if (o1[i] == o2[i]) {
                    i++;
                } else if (o1[i] > o2[i]) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return Fun.intCompare(o1.length, o2.length);
        }
    };
    public static final Comparator<char[]> CHAR_ARRAY_COMPARATOR = new Comparator<char[]>() {
        public int compare(char[] o1, char[] o2) {
            if (o1 == o2) {
                return 0;
            }
            int len = Math.min(o1.length, o2.length);
            int i = 0;
            while (i < len) {
                if (o1[i] == o2[i]) {
                    i++;
                } else if (o1[i] > o2[i]) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return Fun.intCompare(o1.length, o2.length);
        }
    };
    public static final Comparator<Object[]> COMPARABLE_ARRAY_COMPARATOR = new Comparator<Object[]>() {
        public int compare(Object[] o1, Object[] o2) {
            if (o1 == o2) {
                return 0;
            }
            int len = Math.min(o1.length, o2.length);
            for (int i = 0; i < len; i++) {
                int r = Fun.COMPARATOR.compare(o1[i], o2[i]);
                if (r != 0) {
                    return r;
                }
            }
            return Fun.intCompare(o1.length, o2.length);
        }
    };
    public static final Comparator COMPARATOR = new Comparator<Comparable>() {
        public int compare(Comparable o1, Comparable o2) {
            if (o1 == null) {
                if (o2 == null) {
                    return 0;
                }
                return -1;
            } else if (o2 == null) {
                return 1;
            } else {
                if (o1 == Fun.HI) {
                    if (o2 != Fun.HI) {
                        return 1;
                    }
                    return 0;
                } else if (o2 == Fun.HI) {
                    return -1;
                } else {
                    return o1.compareTo(o2);
                }
            }
        }
    };
    public static final Comparator<double[]> DOUBLE_ARRAY_COMPARATOR = new Comparator<double[]>() {
        public int compare(double[] o1, double[] o2) {
            if (o1 == o2) {
                return 0;
            }
            int len = Math.min(o1.length, o2.length);
            int i = 0;
            while (i < len) {
                if (o1[i] == o2[i]) {
                    i++;
                } else if (o1[i] > o2[i]) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return Fun.intCompare(o1.length, o2.length);
        }
    };
    public static final Iterator EMPTY_ITERATOR = new ArrayList(0).iterator();
    public static final Object HI = new Comparable() {
        public String toString() {
            return "HI";
        }

        public int compareTo(Object o) {
            return o == Fun.HI ? 0 : 1;
        }
    };
    public static final Comparator<int[]> INT_ARRAY_COMPARATOR = new Comparator<int[]>() {
        public int compare(int[] o1, int[] o2) {
            if (o1 == o2) {
                return 0;
            }
            int len = Math.min(o1.length, o2.length);
            int i = 0;
            while (i < len) {
                if (o1[i] == o2[i]) {
                    i++;
                } else if (o1[i] > o2[i]) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return Fun.intCompare(o1.length, o2.length);
        }
    };
    public static final Comparator<long[]> LONG_ARRAY_COMPARATOR = new Comparator<long[]>() {
        public int compare(long[] o1, long[] o2) {
            if (o1 == o2) {
                return 0;
            }
            int len = Math.min(o1.length, o2.length);
            int i = 0;
            while (i < len) {
                if (o1[i] == o2[i]) {
                    i++;
                } else if (o1[i] > o2[i]) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return Fun.intCompare(o1.length, o2.length);
        }
    };
    public static final Comparator<Comparable> REVERSE_COMPARATOR = new Comparator<Comparable>() {
        public int compare(Comparable o1, Comparable o2) {
            return -Fun.COMPARATOR.compare(o1, o2);
        }
    };
    public static final Comparator<Tuple2> TUPLE2_COMPARATOR = new Tuple2Comparator(null, null);
    public static final Comparator<Tuple3> TUPLE3_COMPARATOR = new Tuple3Comparator(null, null, null);
    public static final Comparator<Tuple4> TUPLE4_COMPARATOR = new Tuple4Comparator(null, null, null, null);
    public static final Comparator<Tuple5> TUPLE5_COMPARATOR = new Tuple5Comparator(null, null, null, null, null);
    public static final Comparator<Tuple6> TUPLE6_COMPARATOR = new Tuple6Comparator(null, null, null, null, null, null);

    public static final class ArrayComparator implements Comparator<Object[]> {
        protected final Comparator[] comparators;

        public ArrayComparator(Comparator<?>[] comparators2) {
            this.comparators = (Comparator[]) comparators2.clone();
            for (int i = 0; i < this.comparators.length; i++) {
                if (this.comparators[i] == null) {
                    this.comparators[i] = Fun.COMPARATOR;
                }
            }
        }

        protected ArrayComparator(SerializerBase serializer, DataInput in, FastArrayList<Object> objectStack) throws IOException {
            objectStack.add(this);
            this.comparators = (Comparator[]) serializer.deserialize(in, (FastArrayList) objectStack);
        }

        public int compare(Object[] o1, Object[] o2) {
            if (o1 == o2) {
                return 0;
            }
            int len = Math.min(o1.length, o2.length);
            for (int i = 0; i < len; i++) {
                int r = this.comparators[i].compare(o1[i], o2[i]);
                if (r != 0) {
                    return r;
                }
            }
            return Fun.intCompare(o1.length, o2.length);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            return Arrays.equals(this.comparators, ((ArrayComparator) o).comparators);
        }

        public int hashCode() {
            return Arrays.hashCode(this.comparators);
        }
    }

    public interface Function0<R> {
        R run();
    }

    public interface Function1<R, A> {
        R run(A a);
    }

    public interface Function2<R, A, B> {
        R run(A a, B b);
    }

    public static final class Tuple2<A, B> implements Comparable<Tuple2<A, B>>, Serializable {
        private static final long serialVersionUID = -8816277286657643283L;
        public final A a;
        public final B b;

        public Tuple2(A a, B b) {
            this.a = a;
            this.b = b;
        }

        protected Tuple2(SerializerBase serializer, DataInput in, FastArrayList<Object> objectStack) throws IOException {
            objectStack.add(this);
            this.a = serializer.deserialize(in, (FastArrayList) objectStack);
            this.b = serializer.deserialize(in, (FastArrayList) objectStack);
        }

        public int compareTo(Tuple2<A, B> o) {
            return Fun.TUPLE2_COMPARATOR.compare(this, o);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple2 t = (Tuple2) o;
            if (Fun.eq(this.a, t.a) && Fun.eq(this.b, t.b)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int result;
            int i = 0;
            if (this.a != null) {
                result = this.a.hashCode();
            } else {
                result = 0;
            }
            int i2 = result * 31;
            if (this.b != null) {
                i = this.b.hashCode();
            }
            return i2 + i;
        }

        public String toString() {
            return "Tuple2[" + this.a + ", " + this.b + "]";
        }
    }

    public static final class Tuple2Comparator<A, B> implements Comparator<Tuple2<A, B>>, Serializable {
        private static final long serialVersionUID = 1156568632023474010L;
        protected final Comparator<A> a;
        protected final Comparator<B> b;

        public Tuple2Comparator(Comparator<A> a, Comparator<B> b) {
            if (a == null) {
                a = Fun.COMPARATOR;
            }
            this.a = a;
            if (b == null) {
                b = Fun.COMPARATOR;
            }
            this.b = b;
        }

        protected Tuple2Comparator(SerializerBase serializer, DataInput in, FastArrayList<Object> objectStack) throws IOException {
            objectStack.add(this);
            this.a = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.b = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
        }

        public int compare(Tuple2<A, B> o1, Tuple2<A, B> o2) {
            int i = this.a.compare(o1.a, o2.a);
            if (i != 0) {
                return i;
            }
            return this.b.compare(o1.b, o2.b);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple2Comparator that = (Tuple2Comparator) o;
            if (this.a.equals(that.a) && this.b.equals(that.b)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.a.hashCode() * 31) + this.b.hashCode();
        }
    }

    public static final class Tuple3<A, B, C> implements Comparable<Tuple3<A, B, C>>, Serializable {
        private static final long serialVersionUID = 11785034935947868L;
        public final A a;
        public final B b;
        public final C c;

        public Tuple3(A a, B b, C c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        protected Tuple3(SerializerBase serializer, DataInput in, FastArrayList<Object> objectStack, int extra) throws IOException {
            objectStack.add(this);
            this.a = serializer.deserialize(in, (FastArrayList) objectStack);
            this.b = serializer.deserialize(in, (FastArrayList) objectStack);
            this.c = serializer.deserialize(in, (FastArrayList) objectStack);
        }

        public int compareTo(Tuple3<A, B, C> o) {
            return Fun.TUPLE3_COMPARATOR.compare(this, o);
        }

        public String toString() {
            return "Tuple3[" + this.a + ", " + this.b + ", " + this.c + "]";
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple3 t = (Tuple3) o;
            if (Fun.eq(this.a, t.a) && Fun.eq(this.b, t.b) && Fun.eq(this.c, t.c)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int result;
            int hashCode;
            int i = 0;
            if (this.a != null) {
                result = this.a.hashCode();
            } else {
                result = 0;
            }
            int i2 = result * 31;
            if (this.b != null) {
                hashCode = this.b.hashCode();
            } else {
                hashCode = 0;
            }
            hashCode = (i2 + hashCode) * 31;
            if (this.c != null) {
                i = this.c.hashCode();
            }
            return hashCode + i;
        }
    }

    public static final class Tuple3Comparator<A, B, C> implements Comparator<Tuple3<A, B, C>>, Serializable {
        private static final long serialVersionUID = 6908945189367914695L;
        protected final Comparator<A> a;
        protected final Comparator<B> b;
        protected final Comparator<C> c;

        public Tuple3Comparator(Comparator<A> a, Comparator<B> b, Comparator<C> c) {
            if (a == null) {
                a = Fun.COMPARATOR;
            }
            this.a = a;
            if (b == null) {
                b = Fun.COMPARATOR;
            }
            this.b = b;
            if (c == null) {
                c = Fun.COMPARATOR;
            }
            this.c = c;
        }

        protected Tuple3Comparator(SerializerBase serializer, DataInput in, FastArrayList<Object> objectStack, int extra) throws IOException {
            objectStack.add(this);
            this.a = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.b = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.c = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
        }

        public int compare(Tuple3<A, B, C> o1, Tuple3<A, B, C> o2) {
            int i = this.a.compare(o1.a, o2.a);
            if (i != 0) {
                return i;
            }
            i = this.b.compare(o1.b, o2.b);
            if (i != 0) {
                return i;
            }
            return this.c.compare(o1.c, o2.c);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple3Comparator that = (Tuple3Comparator) o;
            if (this.a.equals(that.a) && this.b.equals(that.b) && this.c.equals(that.c)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (((this.a.hashCode() * 31) + this.b.hashCode()) * 31) + this.c.hashCode();
        }
    }

    public static final class Tuple4<A, B, C, D> implements Comparable<Tuple4<A, B, C, D>>, Serializable {
        private static final long serialVersionUID = 1630397500758650718L;
        public final A a;
        public final B b;
        public final C c;
        public final D d;

        public Tuple4(A a, B b, C c, D d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        protected Tuple4(SerializerBase serializer, DataInput in, FastArrayList<Object> objectStack) throws IOException {
            objectStack.add(this);
            this.a = serializer.deserialize(in, (FastArrayList) objectStack);
            this.b = serializer.deserialize(in, (FastArrayList) objectStack);
            this.c = serializer.deserialize(in, (FastArrayList) objectStack);
            this.d = serializer.deserialize(in, (FastArrayList) objectStack);
        }

        public int compareTo(Tuple4<A, B, C, D> o) {
            return Fun.TUPLE4_COMPARATOR.compare(this, o);
        }

        public String toString() {
            return "Tuple4[" + this.a + ", " + this.b + ", " + this.c + ", " + this.d + "]";
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple4 t = (Tuple4) o;
            if (Fun.eq(this.a, t.a) && Fun.eq(this.b, t.b) && Fun.eq(this.c, t.c) && Fun.eq(this.d, t.d)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int result;
            int hashCode;
            int i = 0;
            if (this.a != null) {
                result = this.a.hashCode();
            } else {
                result = 0;
            }
            int i2 = result * 31;
            if (this.b != null) {
                hashCode = this.b.hashCode();
            } else {
                hashCode = 0;
            }
            i2 = (i2 + hashCode) * 31;
            if (this.c != null) {
                hashCode = this.c.hashCode();
            } else {
                hashCode = 0;
            }
            hashCode = (i2 + hashCode) * 31;
            if (this.d != null) {
                i = this.d.hashCode();
            }
            return hashCode + i;
        }
    }

    public static final class Tuple4Comparator<A, B, C, D> implements Comparator<Tuple4<A, B, C, D>>, Serializable {
        private static final long serialVersionUID = 4994247318830102213L;
        protected final Comparator<A> a;
        protected final Comparator<B> b;
        protected final Comparator<C> c;
        protected final Comparator<D> d;

        public Tuple4Comparator(Comparator<A> a, Comparator<B> b, Comparator<C> c, Comparator<D> d) {
            if (a == null) {
                a = Fun.COMPARATOR;
            }
            this.a = a;
            if (b == null) {
                b = Fun.COMPARATOR;
            }
            this.b = b;
            if (c == null) {
                c = Fun.COMPARATOR;
            }
            this.c = c;
            if (d == null) {
                d = Fun.COMPARATOR;
            }
            this.d = d;
        }

        protected Tuple4Comparator(SerializerBase serializer, DataInput in, FastArrayList<Object> objectStack) throws IOException {
            objectStack.add(this);
            this.a = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.b = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.c = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.d = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
        }

        public int compare(Tuple4<A, B, C, D> o1, Tuple4<A, B, C, D> o2) {
            int i = this.a.compare(o1.a, o2.a);
            if (i != 0) {
                return i;
            }
            i = this.b.compare(o1.b, o2.b);
            if (i != 0) {
                return i;
            }
            i = this.c.compare(o1.c, o2.c);
            if (i != 0) {
                return i;
            }
            return this.d.compare(o1.d, o2.d);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple4Comparator that = (Tuple4Comparator) o;
            if (this.a.equals(that.a) && this.b.equals(that.b) && this.c.equals(that.c) && this.d.equals(that.d)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (((((this.a.hashCode() * 31) + this.b.hashCode()) * 31) + this.c.hashCode()) * 31) + this.d.hashCode();
        }
    }

    public static final class Tuple5<A, B, C, D, E> implements Comparable<Tuple5<A, B, C, D, E>>, Serializable {
        private static final long serialVersionUID = 3975016300758650718L;
        public final A a;
        public final B b;
        public final C c;
        public final D d;
        public final E e;

        public Tuple5(A a, B b, C c, D d, E e) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
        }

        protected Tuple5(SerializerBase serializer, DataInput in, FastArrayList<Object> objectStack) throws IOException {
            objectStack.add(this);
            this.a = serializer.deserialize(in, (FastArrayList) objectStack);
            this.b = serializer.deserialize(in, (FastArrayList) objectStack);
            this.c = serializer.deserialize(in, (FastArrayList) objectStack);
            this.d = serializer.deserialize(in, (FastArrayList) objectStack);
            this.e = serializer.deserialize(in, (FastArrayList) objectStack);
        }

        public int compareTo(Tuple5<A, B, C, D, E> o) {
            return Fun.TUPLE5_COMPARATOR.compare(this, o);
        }

        public String toString() {
            return "Tuple5[" + this.a + ", " + this.b + ", " + this.c + ", " + this.d + ", " + this.e + "]";
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple5 t = (Tuple5) o;
            if (Fun.eq(this.a, t.a) && Fun.eq(this.b, t.b) && Fun.eq(this.c, t.c) && Fun.eq(this.d, t.d) && Fun.eq(this.e, t.e)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int result;
            int hashCode;
            int i = 0;
            if (this.a != null) {
                result = this.a.hashCode();
            } else {
                result = 0;
            }
            int i2 = result * 31;
            if (this.b != null) {
                hashCode = this.b.hashCode();
            } else {
                hashCode = 0;
            }
            i2 = (i2 + hashCode) * 31;
            if (this.c != null) {
                hashCode = this.c.hashCode();
            } else {
                hashCode = 0;
            }
            i2 = (i2 + hashCode) * 31;
            if (this.d != null) {
                hashCode = this.d.hashCode();
            } else {
                hashCode = 0;
            }
            hashCode = (i2 + hashCode) * 31;
            if (this.e != null) {
                i = this.e.hashCode();
            }
            return hashCode + i;
        }
    }

    public static final class Tuple5Comparator<A, B, C, D, E> implements Comparator<Tuple5<A, B, C, D, E>>, Serializable {
        private static final long serialVersionUID = -6571610438255691118L;
        protected final Comparator<A> a;
        protected final Comparator<B> b;
        protected final Comparator<C> c;
        protected final Comparator<D> d;
        protected final Comparator<E> e;

        public Tuple5Comparator(Comparator<A> a, Comparator<B> b, Comparator<C> c, Comparator<D> d, Comparator<E> e) {
            if (a == null) {
                a = Fun.COMPARATOR;
            }
            this.a = a;
            if (b == null) {
                b = Fun.COMPARATOR;
            }
            this.b = b;
            if (c == null) {
                c = Fun.COMPARATOR;
            }
            this.c = c;
            if (d == null) {
                d = Fun.COMPARATOR;
            }
            this.d = d;
            if (e == null) {
                e = Fun.COMPARATOR;
            }
            this.e = e;
        }

        protected Tuple5Comparator(SerializerBase serializer, DataInput in, FastArrayList<Object> objectStack) throws IOException {
            objectStack.add(this);
            this.a = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.b = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.c = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.d = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.e = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
        }

        public int compare(Tuple5<A, B, C, D, E> o1, Tuple5<A, B, C, D, E> o2) {
            int i = this.a.compare(o1.a, o2.a);
            if (i != 0) {
                return i;
            }
            i = this.b.compare(o1.b, o2.b);
            if (i != 0) {
                return i;
            }
            i = this.c.compare(o1.c, o2.c);
            if (i != 0) {
                return i;
            }
            i = this.d.compare(o1.d, o2.d);
            if (i != 0) {
                return i;
            }
            return this.e.compare(o1.e, o2.e);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple5Comparator that = (Tuple5Comparator) o;
            if (this.a.equals(that.a) && this.b.equals(that.b) && this.c.equals(that.c) && this.d.equals(that.d) && this.e.equals(that.e)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (((((((this.a.hashCode() * 31) + this.b.hashCode()) * 31) + this.c.hashCode()) * 31) + this.d.hashCode()) * 31) + this.e.hashCode();
        }
    }

    public static final class Tuple6<A, B, C, D, E, F> implements Comparable<Tuple6<A, B, C, D, E, F>>, Serializable {
        private static final long serialVersionUID = 7500397586163050718L;
        public final A a;
        public final B b;
        public final C c;
        public final D d;
        public final E e;
        public final F f;

        public Tuple6(A a, B b, C c, D d, E e, F f) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
        }

        protected Tuple6(SerializerBase serializer, DataInput in, FastArrayList<Object> objectStack) throws IOException {
            objectStack.add(this);
            this.a = serializer.deserialize(in, (FastArrayList) objectStack);
            this.b = serializer.deserialize(in, (FastArrayList) objectStack);
            this.c = serializer.deserialize(in, (FastArrayList) objectStack);
            this.d = serializer.deserialize(in, (FastArrayList) objectStack);
            this.e = serializer.deserialize(in, (FastArrayList) objectStack);
            this.f = serializer.deserialize(in, (FastArrayList) objectStack);
        }

        public int compareTo(Tuple6<A, B, C, D, E, F> o) {
            return Fun.TUPLE6_COMPARATOR.compare(this, o);
        }

        public String toString() {
            return "Tuple6[" + this.a + ", " + this.b + ", " + this.c + ", " + this.d + ", " + this.e + ", " + this.f + "]";
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple6 t = (Tuple6) o;
            if (Fun.eq(this.a, t.a) && Fun.eq(this.b, t.b) && Fun.eq(this.c, t.c) && Fun.eq(this.d, t.d) && Fun.eq(this.e, t.e) && Fun.eq(this.f, t.f)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int result;
            int hashCode;
            int i = 0;
            if (this.a != null) {
                result = this.a.hashCode();
            } else {
                result = 0;
            }
            int i2 = result * 31;
            if (this.b != null) {
                hashCode = this.b.hashCode();
            } else {
                hashCode = 0;
            }
            i2 = (i2 + hashCode) * 31;
            if (this.c != null) {
                hashCode = this.c.hashCode();
            } else {
                hashCode = 0;
            }
            i2 = (i2 + hashCode) * 31;
            if (this.d != null) {
                hashCode = this.d.hashCode();
            } else {
                hashCode = 0;
            }
            i2 = (i2 + hashCode) * 31;
            if (this.e != null) {
                hashCode = this.e.hashCode();
            } else {
                hashCode = 0;
            }
            hashCode = (i2 + hashCode) * 31;
            if (this.f != null) {
                i = this.f.hashCode();
            }
            return hashCode + i;
        }
    }

    public static final class Tuple6Comparator<A, B, C, D, E, F> implements Comparator<Tuple6<A, B, C, D, E, F>>, Serializable {
        private static final long serialVersionUID = 4254578670751190479L;
        protected final Comparator<A> a;
        protected final Comparator<B> b;
        protected final Comparator<C> c;
        protected final Comparator<D> d;
        protected final Comparator<E> e;
        protected final Comparator<F> f;

        public Tuple6Comparator(Comparator<A> a, Comparator<B> b, Comparator<C> c, Comparator<D> d, Comparator<E> e, Comparator<F> f) {
            if (a == null) {
                a = Fun.COMPARATOR;
            }
            this.a = a;
            if (b == null) {
                b = Fun.COMPARATOR;
            }
            this.b = b;
            if (c == null) {
                c = Fun.COMPARATOR;
            }
            this.c = c;
            if (d == null) {
                d = Fun.COMPARATOR;
            }
            this.d = d;
            if (e == null) {
                e = Fun.COMPARATOR;
            }
            this.e = e;
            if (f == null) {
                f = Fun.COMPARATOR;
            }
            this.f = f;
        }

        protected Tuple6Comparator(SerializerBase serializer, DataInput in, FastArrayList<Object> objectStack) throws IOException {
            objectStack.add(this);
            this.a = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.b = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.c = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.d = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.e = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
            this.f = (Comparator) serializer.deserialize(in, (FastArrayList) objectStack);
        }

        public int compare(Tuple6<A, B, C, D, E, F> o1, Tuple6<A, B, C, D, E, F> o2) {
            int i = this.a.compare(o1.a, o2.a);
            if (i != 0) {
                return i;
            }
            i = this.b.compare(o1.b, o2.b);
            if (i != 0) {
                return i;
            }
            i = this.c.compare(o1.c, o2.c);
            if (i != 0) {
                return i;
            }
            i = this.d.compare(o1.d, o2.d);
            if (i != 0) {
                return i;
            }
            i = this.e.compare(o1.e, o2.e);
            if (i != 0) {
                return i;
            }
            return this.f.compare(o1.f, o2.f);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple6Comparator that = (Tuple6Comparator) o;
            if (this.a.equals(that.a) && this.b.equals(that.b) && this.c.equals(that.c) && this.d.equals(that.d) && this.e.equals(that.e) && this.f.equals(that.f)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (((((((((this.a.hashCode() * 31) + this.b.hashCode()) * 31) + this.c.hashCode()) * 31) + this.d.hashCode()) * 31) + this.e.hashCode()) * 31) + this.f.hashCode();
        }
    }

    private Fun() {
    }

    public static <A> A HI() {
        return HI;
    }

    public static <A, B> Tuple2<A, B> t2(A a, B b) {
        return new Tuple2(a, b);
    }

    public static <A, B, C> Tuple3<A, B, C> t3(A a, B b, C c) {
        return new Tuple3(a, b, c);
    }

    public static <A, B, C, D> Tuple4<A, B, C, D> t4(A a, B b, C c, D d) {
        return new Tuple4(a, b, c, d);
    }

    public static <A, B, C, D, E> Tuple5<A, B, C, D, E> t5(A a, B b, C c, D d, E e) {
        return new Tuple5(a, b, c, d, e);
    }

    public static <A, B, C, D, E, F> Tuple6<A, B, C, D, E, F> t6(A a, B b, C c, D d, E e, F f) {
        return new Tuple6(a, b, c, d, e, f);
    }

    public static boolean eq(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static <K, V> Function1<K, Tuple2<K, V>> extractKey() {
        return new Function1<K, Tuple2<K, V>>() {
            public K run(Tuple2<K, V> t) {
                return t.a;
            }
        };
    }

    public static <K, V> Function1<V, Tuple2<K, V>> extractValue() {
        return new Function1<V, Tuple2<K, V>>() {
            public V run(Tuple2<K, V> t) {
                return t.b;
            }
        };
    }

    public static <K> Function1<K, K> extractNoTransform() {
        return new Function1<K, K>() {
            public K run(K k) {
                return k;
            }
        };
    }

    private static int intCompare(int x, int y) {
        if (x < y) {
            return -1;
        }
        return x == y ? 0 : 1;
    }

    public static <K2, K1> Iterable<K1> filter(NavigableSet<Tuple2<K2, K1>> secondaryKeys, K2 secondaryKey) {
        return filter(secondaryKeys, secondaryKey, true, secondaryKey, true);
    }

    public static <K2, K1> Iterable<K1> filter(NavigableSet<Tuple2<K2, K1>> secondaryKeys, K2 lo, boolean loInc, K2 hi, boolean hiInc) {
        final NavigableSet<Tuple2<K2, K1>> navigableSet = secondaryKeys;
        final K2 k2 = lo;
        final boolean z = loInc;
        final K2 k22 = hi;
        final boolean z2 = hiInc;
        return new Iterable<K1>() {
            public Iterator<K1> iterator() {
                Object obj = null;
                NavigableSet navigableSet = navigableSet;
                Tuple2 t2 = Fun.t2(k2, null);
                boolean z = z;
                Object obj2 = k22;
                if (z2) {
                    obj = Fun.HI;
                }
                final Iterator<Tuple2<K2, K1>> iter = navigableSet.subSet(t2, z, Fun.t2(obj2, obj), z2).iterator();
                return new Iterator<K1>() {
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public K1 next() {
                        return ((Tuple2) iter.next()).b;
                    }

                    public void remove() {
                        iter.remove();
                    }
                };
            }
        };
    }

    public static <A, B, C> Iterable<C> filter(final NavigableSet<Tuple3<A, B, C>> secondaryKeys, final A a, final B b) {
        return new Iterable<C>() {
            public Iterator<C> iterator() {
                final Iterator<Tuple3> iter = secondaryKeys.subSet(Fun.t3(a, b, null), Fun.t3(a, b == null ? Fun.HI() : b, Fun.HI())).iterator();
                return new Iterator<C>() {
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public C next() {
                        return ((Tuple3) iter.next()).c;
                    }

                    public void remove() {
                        iter.remove();
                    }
                };
            }
        };
    }

    public static <A, B, C, D> Iterable<D> filter(final NavigableSet<Tuple4<A, B, C, D>> secondaryKeys, final A a, final B b, final C c) {
        return new Iterable<D>() {
            public Iterator<D> iterator() {
                final Iterator<Tuple4> iter = secondaryKeys.subSet(Fun.t4(a, b, c, null), Fun.t4(a, b == null ? Fun.HI() : b, c == null ? Fun.HI() : c, Fun.HI())).iterator();
                return new Iterator<D>() {
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public D next() {
                        return ((Tuple4) iter.next()).d;
                    }

                    public void remove() {
                        iter.remove();
                    }
                };
            }
        };
    }
}
