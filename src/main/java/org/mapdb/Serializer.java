package org.mapdb;

import io.fabric.sdk.android.services.network.UrlUtils;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.UUID;

public interface Serializer<A> {
    public static final Serializer<Object> BASIC = new SerializerBase();
    public static final Serializer<Boolean> BOOLEAN = new Serializer<Boolean>() {
        public void serialize(DataOutput out, Boolean value) throws IOException {
            out.writeBoolean(value.booleanValue());
        }

        public Boolean deserialize(DataInput in, int available) throws IOException {
            if (available == 0) {
                return null;
            }
            return Boolean.valueOf(in.readBoolean());
        }

        public int fixedSize() {
            return 1;
        }
    };
    public static final Serializer<byte[]> BYTE_ARRAY = new Serializer<byte[]>() {
        public void serialize(DataOutput out, byte[] value) throws IOException {
            DataOutput2.packInt(out, value.length);
            out.write(value);
        }

        public byte[] deserialize(DataInput in, int available) throws IOException {
            byte[] ret = new byte[DataInput2.unpackInt(in)];
            in.readFully(ret);
            return ret;
        }

        public int fixedSize() {
            return -1;
        }
    };
    public static final Serializer<byte[]> BYTE_ARRAY_NOSIZE = new Serializer<byte[]>() {
        public void serialize(DataOutput out, byte[] value) throws IOException {
            if (value != null && value.length != 0) {
                out.write(value);
            }
        }

        public byte[] deserialize(DataInput in, int available) throws IOException {
            if (available == -1) {
                throw new IllegalArgumentException("BYTE_ARRAY_NOSIZE does not work with collections.");
            } else if (available == 0) {
                return null;
            } else {
                byte[] ret = new byte[available];
                in.readFully(ret);
                return ret;
            }
        }

        public int fixedSize() {
            return -1;
        }
    };
    public static final Serializer<char[]> CHAR_ARRAY = new Serializer<char[]>() {
        public void serialize(DataOutput out, char[] value) throws IOException {
            DataOutput2.packInt(out, value.length);
            for (char c : value) {
                out.writeChar(c);
            }
        }

        public char[] deserialize(DataInput in, int available) throws IOException {
            int size = DataInput2.unpackInt(in);
            char[] ret = new char[size];
            for (int i = 0; i < size; i++) {
                ret[i] = in.readChar();
            }
            return ret;
        }

        public int fixedSize() {
            return -1;
        }
    };
    public static final Serializer<double[]> DOUBLE_ARRAY = new Serializer<double[]>() {
        public void serialize(DataOutput out, double[] value) throws IOException {
            DataOutput2.packInt(out, value.length);
            for (double c : value) {
                out.writeDouble(c);
            }
        }

        public double[] deserialize(DataInput in, int available) throws IOException {
            int size = DataInput2.unpackInt(in);
            double[] ret = new double[size];
            for (int i = 0; i < size; i++) {
                ret[i] = in.readDouble();
            }
            return ret;
        }

        public int fixedSize() {
            return -1;
        }
    };
    public static final Serializer<Object> ILLEGAL_ACCESS = new Serializer<Object>() {
        public void serialize(DataOutput out, Object value) throws IOException {
            throw new IllegalAccessError();
        }

        public Object deserialize(DataInput in, int available) throws IOException {
            throw new IllegalAccessError();
        }

        public int fixedSize() {
            return -1;
        }
    };
    public static final Serializer<Integer> INTEGER = new Serializer<Integer>() {
        public void serialize(DataOutput out, Integer value) throws IOException {
            out.writeInt(value.intValue());
        }

        public Integer deserialize(DataInput in, int available) throws IOException {
            return Integer.valueOf(in.readInt());
        }

        public int fixedSize() {
            return 4;
        }
    };
    public static final Serializer<int[]> INT_ARRAY = new Serializer<int[]>() {
        public void serialize(DataOutput out, int[] value) throws IOException {
            DataOutput2.packInt(out, value.length);
            for (int c : value) {
                out.writeInt(c);
            }
        }

        public int[] deserialize(DataInput in, int available) throws IOException {
            int size = DataInput2.unpackInt(in);
            int[] ret = new int[size];
            for (int i = 0; i < size; i++) {
                ret[i] = in.readInt();
            }
            return ret;
        }

        public int fixedSize() {
            return -1;
        }
    };
    public static final Serializer<Object> JAVA = new Serializer<Object>() {
        public void serialize(DataOutput out, Object value) throws IOException {
            ObjectOutputStream out2 = new ObjectOutputStream((OutputStream) out);
            out2.writeObject(value);
            out2.flush();
        }

        public Object deserialize(DataInput in, int available) throws IOException {
            try {
                return new ObjectInputStream((InputStream) in).readObject();
            } catch (ClassNotFoundException e) {
                throw new IOException(e);
            }
        }

        public int fixedSize() {
            return -1;
        }
    };
    public static final Serializer<Long> LONG = new Serializer<Long>() {
        public void serialize(DataOutput out, Long value) throws IOException {
            if (value != null) {
                out.writeLong(value.longValue());
            }
        }

        public Long deserialize(DataInput in, int available) throws IOException {
            if (available == 0) {
                return null;
            }
            return Long.valueOf(in.readLong());
        }

        public int fixedSize() {
            return 8;
        }
    };
    public static final Serializer<long[]> LONG_ARRAY = new Serializer<long[]>() {
        public void serialize(DataOutput out, long[] value) throws IOException {
            DataOutput2.packInt(out, value.length);
            for (long c : value) {
                out.writeLong(c);
            }
        }

        public long[] deserialize(DataInput in, int available) throws IOException {
            int size = DataInput2.unpackInt(in);
            long[] ret = new long[size];
            for (int i = 0; i < size; i++) {
                ret[i] = in.readLong();
            }
            return ret;
        }

        public int fixedSize() {
            return -1;
        }
    };
    public static final Serializer<String> STRING = new Serializer<String>() {
        public void serialize(DataOutput out, String value) throws IOException {
            out.writeUTF(value);
        }

        public String deserialize(DataInput in, int available) throws IOException {
            return in.readUTF();
        }

        public int fixedSize() {
            return -1;
        }
    };
    public static final Serializer<String> STRING_ASCII = new Serializer<String>() {
        public void serialize(DataOutput out, String value) throws IOException {
            char[] cc = new char[value.length()];
            value.getChars(0, cc.length, cc, 0);
            DataOutput2.packInt(out, cc.length);
            for (char c : cc) {
                out.write(c);
            }
        }

        public String deserialize(DataInput in, int available) throws IOException {
            int size = DataInput2.unpackInt(in);
            char[] cc = new char[size];
            for (int i = 0; i < size; i++) {
                cc[i] = (char) in.readUnsignedByte();
            }
            return new String(cc);
        }

        public int fixedSize() {
            return -1;
        }
    };
    public static final Serializer<String> STRING_INTERN = new Serializer<String>() {
        public void serialize(DataOutput out, String value) throws IOException {
            out.writeUTF(value);
        }

        public String deserialize(DataInput in, int available) throws IOException {
            return in.readUTF().intern();
        }

        public int fixedSize() {
            return -1;
        }
    };
    public static final Serializer<String> STRING_NOSIZE = new Serializer<String>() {
        private final Charset UTF8_CHARSET = Charset.forName(UrlUtils.UTF8);

        public void serialize(DataOutput out, String value) throws IOException {
            out.write(value.getBytes(this.UTF8_CHARSET));
        }

        public String deserialize(DataInput in, int available) throws IOException {
            if (available == -1) {
                throw new IllegalArgumentException("STRING_NOSIZE does not work with collections.");
            }
            byte[] bytes = new byte[available];
            in.readFully(bytes);
            return new String(bytes, this.UTF8_CHARSET);
        }

        public int fixedSize() {
            return -1;
        }
    };
    public static final Serializer<UUID> UUID = new Serializer<UUID>() {
        public void serialize(DataOutput out, UUID value) throws IOException {
            out.writeLong(value.getMostSignificantBits());
            out.writeLong(value.getLeastSignificantBits());
        }

        public UUID deserialize(DataInput in, int available) throws IOException {
            return new UUID(in.readLong(), in.readLong());
        }

        public int fixedSize() {
            return 16;
        }
    };

    public static final class CompressionWrapper<E> implements Serializer<E>, Serializable {
        static final /* synthetic */ boolean $assertionsDisabled = (!Serializer.class.desiredAssertionStatus());
        private static final long serialVersionUID = 4440826457939614346L;
        protected final ThreadLocal<CompressLZF> LZF = new ThreadLocal<CompressLZF>() {
            protected CompressLZF initialValue() {
                return new CompressLZF();
            }
        };
        protected final Serializer<E> serializer;

        public CompressionWrapper(Serializer<E> serializer) {
            this.serializer = serializer;
        }

        protected CompressionWrapper(SerializerBase serializerBase, DataInput is, FastArrayList<Object> objectStack) throws IOException {
            objectStack.add(this);
            this.serializer = (Serializer) serializerBase.deserialize(is, (FastArrayList) objectStack);
        }

        public void serialize(DataOutput out, E value) throws IOException {
            int newLen;
            DataOutput2 out2 = new DataOutput2();
            this.serializer.serialize(out2, value);
            byte[] tmp = new byte[(out2.pos + 41)];
            try {
                newLen = ((CompressLZF) this.LZF.get()).compress(out2.buf, out2.pos, tmp, 0);
            } catch (IndexOutOfBoundsException e) {
                newLen = 0;
            }
            if (newLen >= out2.pos) {
                DataOutput2.packInt(out, 0);
                out.write(out2.buf, 0, out2.pos);
                return;
            }
            DataOutput2.packInt(out, out2.pos + 1);
            out.write(tmp, 0, newLen);
        }

        public E deserialize(DataInput in, int available) throws IOException {
            int unpackedSize = DataInput2.unpackInt(in) - 1;
            if (unpackedSize == -1) {
                Serializer serializer = this.serializer;
                if (available > 0) {
                    available--;
                }
                return serializer.deserialize(in, available);
            }
            byte[] unpacked = new byte[unpackedSize];
            ((CompressLZF) this.LZF.get()).expand(in, unpacked, 0, unpackedSize);
            DataInput2 in2 = new DataInput2(unpacked);
            E ret = this.serializer.deserialize(in2, unpackedSize);
            if ($assertionsDisabled || in2.pos == unpackedSize) {
                return ret;
            }
            throw new AssertionError("data were not fully read");
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            return this.serializer.equals(((CompressionWrapper) o).serializer);
        }

        public int hashCode() {
            return this.serializer.hashCode();
        }

        public int fixedSize() {
            return -1;
        }
    }

    A deserialize(DataInput dataInput, int i) throws IOException;

    int fixedSize();

    void serialize(DataOutput dataOutput, A a) throws IOException;
}
