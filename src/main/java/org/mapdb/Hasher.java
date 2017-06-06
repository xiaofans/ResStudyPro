package org.mapdb;

import java.util.Arrays;

public interface Hasher<K> {
    public static final Hasher<Object[]> ARRAY = new Hasher<Object[]>() {
        public final int hashCode(Object[] k) {
            return Arrays.hashCode(k);
        }

        public boolean equals(Object[] k1, Object[] k2) {
            return Arrays.equals(k1, k2);
        }
    };
    public static final Hasher BASIC = new Hasher() {
        public final int hashCode(Object k) {
            return k.hashCode();
        }

        public boolean equals(Object k1, Object k2) {
            return k1.equals(k2);
        }
    };
    public static final Hasher<byte[]> BYTE_ARRAY = new Hasher<byte[]>() {
        public final int hashCode(byte[] k) {
            return Arrays.hashCode(k);
        }

        public boolean equals(byte[] k1, byte[] k2) {
            return Arrays.equals(k1, k2);
        }
    };
    public static final Hasher<char[]> CHAR_ARRAY = new Hasher<char[]>() {
        public final int hashCode(char[] k) {
            return Arrays.hashCode(k);
        }

        public boolean equals(char[] k1, char[] k2) {
            return Arrays.equals(k1, k2);
        }
    };
    public static final Hasher<double[]> DOUBLE_ARRAY = new Hasher<double[]>() {
        public final int hashCode(double[] k) {
            return Arrays.hashCode(k);
        }

        public boolean equals(double[] k1, double[] k2) {
            return Arrays.equals(k1, k2);
        }
    };
    public static final Hasher<int[]> INT_ARRAY = new Hasher<int[]>() {
        public final int hashCode(int[] k) {
            return Arrays.hashCode(k);
        }

        public boolean equals(int[] k1, int[] k2) {
            return Arrays.equals(k1, k2);
        }
    };
    public static final Hasher<long[]> LONG_ARRAY = new Hasher<long[]>() {
        public final int hashCode(long[] k) {
            return Arrays.hashCode(k);
        }

        public boolean equals(long[] k1, long[] k2) {
            return Arrays.equals(k1, k2);
        }
    };

    boolean equals(K k, K k2);

    int hashCode(K k);
}
