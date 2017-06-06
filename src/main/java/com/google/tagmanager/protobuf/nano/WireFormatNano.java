package com.google.tagmanager.protobuf.nano;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class WireFormatNano {
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    public static final Boolean[] EMPTY_BOOLEAN_REF_ARRAY = new Boolean[0];
    public static final byte[] EMPTY_BYTES = new byte[0];
    public static final byte[][] EMPTY_BYTES_ARRAY = new byte[0][];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final Double[] EMPTY_DOUBLE_REF_ARRAY = new Double[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final Float[] EMPTY_FLOAT_REF_ARRAY = new Float[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final Integer[] EMPTY_INT_REF_ARRAY = new Integer[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final Long[] EMPTY_LONG_REF_ARRAY = new Long[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    static final int MESSAGE_SET_ITEM = 1;
    static final int MESSAGE_SET_ITEM_END_TAG = makeTag(1, 4);
    static final int MESSAGE_SET_ITEM_TAG = makeTag(1, 3);
    static final int MESSAGE_SET_MESSAGE = 3;
    static final int MESSAGE_SET_MESSAGE_TAG = makeTag(3, 2);
    static final int MESSAGE_SET_TYPE_ID = 2;
    static final int MESSAGE_SET_TYPE_ID_TAG = makeTag(2, 0);
    static final int TAG_TYPE_BITS = 3;
    static final int TAG_TYPE_MASK = 7;
    static final int WIRETYPE_END_GROUP = 4;
    static final int WIRETYPE_FIXED32 = 5;
    static final int WIRETYPE_FIXED64 = 1;
    static final int WIRETYPE_LENGTH_DELIMITED = 2;
    static final int WIRETYPE_START_GROUP = 3;
    static final int WIRETYPE_VARINT = 0;

    private WireFormatNano() {
    }

    static int getTagWireType(int tag) {
        return tag & 7;
    }

    public static int getTagFieldNumber(int tag) {
        return tag >>> 3;
    }

    static int makeTag(int fieldNumber, int wireType) {
        return (fieldNumber << 3) | wireType;
    }

    public static boolean parseUnknownField(CodedInputByteBufferNano input, int tag) throws IOException {
        return input.skipField(tag);
    }

    public static boolean storeUnknownField(List<UnknownFieldData> data, CodedInputByteBufferNano input, int tag) throws IOException {
        int startPos = input.getPosition();
        boolean skip = input.skipField(tag);
        data.add(new UnknownFieldData(tag, input.getData(startPos, input.getPosition() - startPos)));
        return skip;
    }

    public static final int getRepeatedFieldArrayLength(CodedInputByteBufferNano input, int tag) throws IOException {
        int arrayLength = 1;
        int startPos = input.getPosition();
        input.skipField(tag);
        while (input.getBytesUntilLimit() > 0 && input.readTag() == tag) {
            input.skipField(tag);
            arrayLength++;
        }
        input.rewindToPosition(startPos);
        return arrayLength;
    }

    public static <T> T getExtension(Extension<T> extension, List<UnknownFieldData> unknownFields) {
        if (unknownFields == null) {
            return null;
        }
        List<UnknownFieldData> dataForField = new ArrayList();
        for (UnknownFieldData data : unknownFields) {
            if (getTagFieldNumber(data.tag) == extension.fieldNumber) {
                dataForField.add(data);
            }
        }
        if (dataForField.isEmpty()) {
            return null;
        }
        if (extension.isRepeatedField) {
            List<Object> result = new ArrayList(dataForField.size());
            for (UnknownFieldData data2 : dataForField) {
                result.add(readData(extension.fieldType, data2.bytes));
            }
            return extension.listType.cast(result);
        }
        return readData(extension.fieldType, ((UnknownFieldData) dataForField.get(dataForField.size() - 1)).bytes);
    }

    private static <T> T readData(Class<T> clazz, byte[] data) {
        if (data.length == 0) {
            return null;
        }
        CodedInputByteBufferNano buffer = CodedInputByteBufferNano.newInstance(data);
        try {
            if (clazz == String.class) {
                return clazz.cast(buffer.readString());
            }
            if (clazz == Integer.class) {
                return clazz.cast(Integer.valueOf(buffer.readInt32()));
            }
            if (clazz == Long.class) {
                return clazz.cast(Long.valueOf(buffer.readInt64()));
            }
            if (clazz == Boolean.class) {
                return clazz.cast(Boolean.valueOf(buffer.readBool()));
            }
            if (clazz == Float.class) {
                return clazz.cast(Float.valueOf(buffer.readFloat()));
            }
            if (clazz == Double.class) {
                return clazz.cast(Double.valueOf(buffer.readDouble()));
            }
            if (clazz == byte[].class) {
                return clazz.cast(buffer.readBytes());
            }
            if (MessageNano.class.isAssignableFrom(clazz)) {
                MessageNano message = (MessageNano) clazz.newInstance();
                buffer.readMessage(message);
                return clazz.cast(message);
            }
            throw new IllegalArgumentException("Unhandled extension field type: " + clazz);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Error creating instance of class " + clazz, e);
        } catch (InstantiationException e2) {
            throw new IllegalArgumentException("Error creating instance of class " + clazz, e2);
        } catch (IOException e3) {
            throw new IllegalArgumentException("Error reading extension field", e3);
        }
    }

    public static <T> void setExtension(Extension<T> extension, T value, List<UnknownFieldData> unknownFields) {
        Iterator<UnknownFieldData> i = unknownFields.iterator();
        while (i.hasNext()) {
            if (extension.fieldNumber == getTagFieldNumber(((UnknownFieldData) i.next()).tag)) {
                i.remove();
            }
        }
        if (value != null) {
            if (value instanceof List) {
                for (Object item : (List) value) {
                    unknownFields.add(write(extension.fieldNumber, item));
                }
                return;
            }
            unknownFields.add(write(extension.fieldNumber, value));
        }
    }

    private static UnknownFieldData write(int fieldNumber, Object object) {
        Class<?> clazz = object.getClass();
        try {
            byte[] data;
            int tag;
            if (clazz == String.class) {
                String str = (String) object;
                data = new byte[CodedOutputByteBufferNano.computeStringSizeNoTag(str)];
                CodedOutputByteBufferNano.newInstance(data).writeStringNoTag(str);
                tag = makeTag(fieldNumber, 2);
            } else if (clazz == Integer.class) {
                Integer integer = (Integer) object;
                data = new byte[CodedOutputByteBufferNano.computeInt32SizeNoTag(integer.intValue())];
                CodedOutputByteBufferNano.newInstance(data).writeInt32NoTag(integer.intValue());
                tag = makeTag(fieldNumber, 0);
            } else if (clazz == Long.class) {
                Long longValue = (Long) object;
                data = new byte[CodedOutputByteBufferNano.computeInt64SizeNoTag(longValue.longValue())];
                CodedOutputByteBufferNano.newInstance(data).writeInt64NoTag(longValue.longValue());
                tag = makeTag(fieldNumber, 0);
            } else if (clazz == Boolean.class) {
                Boolean boolValue = (Boolean) object;
                data = new byte[CodedOutputByteBufferNano.computeBoolSizeNoTag(boolValue.booleanValue())];
                CodedOutputByteBufferNano.newInstance(data).writeBoolNoTag(boolValue.booleanValue());
                tag = makeTag(fieldNumber, 0);
            } else if (clazz == Float.class) {
                Float floatValue = (Float) object;
                data = new byte[CodedOutputByteBufferNano.computeFloatSizeNoTag(floatValue.floatValue())];
                CodedOutputByteBufferNano.newInstance(data).writeFloatNoTag(floatValue.floatValue());
                tag = makeTag(fieldNumber, 5);
            } else if (clazz == Double.class) {
                Double doubleValue = (Double) object;
                data = new byte[CodedOutputByteBufferNano.computeDoubleSizeNoTag(doubleValue.doubleValue())];
                CodedOutputByteBufferNano.newInstance(data).writeDoubleNoTag(doubleValue.doubleValue());
                tag = makeTag(fieldNumber, 1);
            } else if (clazz == byte[].class) {
                byte[] byteArrayValue = (byte[]) object;
                data = new byte[CodedOutputByteBufferNano.computeByteArraySizeNoTag(byteArrayValue)];
                CodedOutputByteBufferNano.newInstance(data).writeByteArrayNoTag(byteArrayValue);
                tag = makeTag(fieldNumber, 2);
            } else if (MessageNano.class.isAssignableFrom(clazz)) {
                MessageNano messageValue = (MessageNano) object;
                int messageSize = messageValue.getSerializedSize();
                data = new byte[(messageSize + CodedOutputByteBufferNano.computeRawVarint32Size(messageSize))];
                CodedOutputByteBufferNano buffer = CodedOutputByteBufferNano.newInstance(data);
                buffer.writeRawVarint32(messageSize);
                buffer.writeRawBytes(MessageNano.toByteArray(messageValue));
                tag = makeTag(fieldNumber, 2);
            } else {
                throw new IllegalArgumentException("Unhandled extension field type: " + clazz);
            }
            return new UnknownFieldData(tag, data);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static int computeWireSize(List<UnknownFieldData> unknownFields) {
        if (unknownFields == null) {
            return 0;
        }
        int size = 0;
        for (UnknownFieldData unknownField : unknownFields) {
            size = (size + CodedOutputByteBufferNano.computeRawVarint32Size(unknownField.tag)) + unknownField.bytes.length;
        }
        return size;
    }

    public static void writeUnknownFields(List<UnknownFieldData> unknownFields, CodedOutputByteBufferNano outBuffer) throws IOException {
        if (unknownFields != null) {
            for (UnknownFieldData data : unknownFields) {
                outBuffer.writeTag(getTagFieldNumber(data.tag), getTagWireType(data.tag));
                outBuffer.writeRawBytes(data.bytes);
            }
        }
    }
}
