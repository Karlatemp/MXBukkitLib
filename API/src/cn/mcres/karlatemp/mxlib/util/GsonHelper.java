/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: GsonHelper.java@author: karlatemp@vip.qq.com: 19-10-3 下午2:14@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import cn.mcres.karlatemp.mxlib.annotations.JsonFormattable;
import cn.mcres.karlatemp.mxlib.tools.EmptyStream;
import com.google.gson.stream.JsonWriter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * The Gson Helper.
 */
public class GsonHelper {
    private static class $ extends Number {
        private final Object str;

        @Override
        public String toString() {
            if (str instanceof JsonSerializable) return ((JsonSerializable) str).toJson();
            return String.valueOf(str);
        }

        private $(Object o) {
            this.str = o;
        }

        @Override
        public int intValue() {
            return 0;
        }

        @Override
        public long longValue() {
            return 0;
        }

        @Override
        public float floatValue() {
            return 0;
        }

        @Override
        public double doubleValue() {
            return 0;
        }
    }

    public interface JsonSerializable {
        String toJson();
    }

    public static void write(JsonWriter writer, Object object) throws IOException {
        if (object == null) {
            writer.nullValue();
        } else {
            if (object instanceof String) {
                writer.value((String) object);
            } else if (object instanceof Number) {
                writer.value((Number) object);
            } else if (object instanceof Boolean) {
                writer.value((boolean) object);
            } else if (object instanceof Iterable) {
                writer.beginArray();
                for (Object o : (Iterable) object) {
                    write(writer, o);
                }
                writer.endArray();
            } else if (object instanceof Map) {
                writer.beginObject();
                for (Object entry : ((Map) object).entrySet()) {
                    write(writer, entry);
                }
                writer.endObject();
            } else if (object instanceof Map.Entry) {
                writer.name(String.valueOf(((Map.Entry) object).getKey()));
                write(writer, ((Map.Entry) object).getValue());
            } else if (object instanceof JsonSerializable) {
                writer.value(toRawString(object));
            } else {
                final Class<?> c = object.getClass();
                if (c.getAnnotation(JsonFormattable.class) != null) {
                    writer.value(toRawString(object));
                } else {
                    writer.value(String.valueOf(object));
                }
            }
        }
    }

    public static Number toRawString(Object raw) {
        return new $(raw);
    }

    public enum BinTokenType {
        BEGIN_ARRAY, BEGIN_OBJECT,
        END_ARRAY, END_OBJECT,
        NAME,
        VALUE_STRING, VALUE_NULL,
        VALUE_BOOLEAN, VALUE_DOUBLE, VALUE_LONG, VALUE_PARSABLE;
    }

    public static class BinJsonWriter extends JsonWriter {

        private final DataOutput output;

        public BinJsonWriter(DataOutput output) {
            super(EmptyStream.getInstance());
            this.output = output;
        }

        @Override
        public boolean isLenient() {
            return super.isLenient();
        }

        @Override
        public JsonWriter beginArray() throws IOException {
            super.beginArray();
            output.writeInt(BinTokenType.BEGIN_ARRAY.ordinal());
            return this;
        }

        @Override
        public JsonWriter endArray() throws IOException {
            super.endArray();
            output.writeInt(BinTokenType.END_ARRAY.ordinal());
            return this;
        }

        @Override
        public JsonWriter beginObject() throws IOException {
            super.beginObject();
            output.writeInt(BinTokenType.BEGIN_OBJECT.ordinal());
            return this;
        }

        @Override
        public JsonWriter endObject() throws IOException {
            super.endObject();
            output.writeInt(BinTokenType.END_OBJECT.ordinal());
            return this;
        }

        @Override
        public JsonWriter name(String name) throws IOException {
            super.name(name);
            output.writeInt(BinTokenType.NAME.ordinal());
            output.writeUTF(name);
            return this;
        }

        @Override
        public JsonWriter value(String value) throws IOException {
            if (value == null) return nullValue();
            super.value(value);
            output.writeInt(BinTokenType.VALUE_STRING.ordinal());
            output.writeUTF(value);
            return this;
        }

        @Override
        public JsonWriter nullValue() throws IOException {
            super.nullValue();
            output.writeInt(BinTokenType.VALUE_NULL.ordinal());
            return this;
        }

        @Override
        public JsonWriter value(boolean value) throws IOException {
            super.value(value);
            output.write(BinTokenType.VALUE_BOOLEAN.ordinal());
            output.writeBoolean(value);
            return this;
        }

        @Override
        public JsonWriter value(double value) throws IOException {
            super.value(value);
            output.write(BinTokenType.VALUE_DOUBLE.ordinal());
            output.writeDouble(value);
            return this;
        }

        @Override
        public JsonWriter value(long value) throws IOException {
            super.value(value);
            output.write(BinTokenType.VALUE_LONG.ordinal());
            output.writeDouble(value);
            return this;
        }

        @Override
        public JsonWriter value(Number value) throws IOException {
            if (value == null) return nullValue();
            super.value(value);
            output.write(BinTokenType.VALUE_PARSABLE.ordinal());
            output.writeUTF(String.valueOf(value));
            return this;
        }

        @Override
        public void flush() throws IOException {
            super.flush();
        }

        @Override
        public void close() throws IOException {
            super.close();
        }
    }
}
