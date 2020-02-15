/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:58:38
 *
 * MXLib/mxlib.message/STemplate.java
 */

package cn.mcres.karlatemp.mxlib.codec;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class STemplate {
    protected STemplateGroup group;

    public STemplateGroup getGroup() {
        return group;
    }

    public void setGroup(STemplateGroup group) {
        this.group = group;
    }

    public abstract @NotNull SProcessor getProcessor();

    abstract static class ST extends STemplate implements SProcessor {
        @Override
        public @NotNull SProcessor getProcessor() {
            return this;
        }
    }

    public static class BasicTemplate extends STemplate {
        private final SProcessor processor;

        public BasicTemplate(@NotNull SProcessor processor) {
            this.processor = processor;
        }

        @Override
        public STemplateGroup getGroup() {
            return null;
        }

        @Override
        public void setGroup(STemplateGroup group) {
        }

        @Override
        public @NotNull SProcessor getProcessor() {
            return processor;
        }
    }

    public static final STemplate INTEGER = new ST() {
        @Override
        public Object read(DataInput input) throws IOException {
            return readAsInt(input);
        }

        @Override
        public int readAsInt(DataInput input) throws IOException {
            return input.readInt();
        }

        @Override
        public double readAsDouble(DataInput input) throws IOException {
            return input.readInt();
        }

        @Override
        public float readAsFloat(DataInput input) throws IOException {
            return (float) input.readInt();
        }

        @Override
        public short readAsShort(DataInput input) throws IOException {
            return (short) input.readInt();
        }

        @Override
        public char readAsChar(DataInput input) throws IOException {
            return (char) input.readInt();
        }

        @Override
        public byte readAsByte(DataInput input) throws IOException {
            return (byte) input.readInt();
        }

        @Override
        public long readAsLong(DataInput input) throws IOException {
            return input.readInt();
        }

        @Override
        public boolean readAsBoolean(DataInput input) throws IOException {
            return input.readInt() != 0;
        }

        @Override
        public int readAsUnsignedByte(DataInput input) throws IOException {
            return input.readInt() & 0xFF;
        }

        @Override
        public int readAsUnsignedShort(DataInput input) throws IOException {
            return input.readInt() & 0xFFFF;
        }

        @Override
        public long readAsUnsignedInt(DataInput input) throws IOException {
            return input.readInt() & 0xFFFF_FFFFL;
        }

        @Override
        public SProcessor write(DataOutput output, Object value) throws IOException {
            if (!(value instanceof Number)) {
                throw new TypeNotMatchException(value + " cannot match to number");
            }
            return writeAsInt(output, ((Number) value).intValue());
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, int value) throws IOException {
            output.writeInt(value);
            return this;
        }

        @Override
        public SProcessor writeAsDouble(DataOutput output, double value) throws IOException {
            output.writeInt((int) value);
            return this;
        }

        @Override
        public SProcessor writeAsFloat(DataOutput output, float value) throws IOException {
            output.writeInt((int) value);
            return this;
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, short value) throws IOException {
            output.writeInt(value & 0xFFFF);
            return this;
        }

        @Override
        public SProcessor writeAsChar(DataOutput output, char value) throws IOException {
            output.writeInt(value & 0xFFFF);
            return this;
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, byte value) throws IOException {
            output.writeInt(value & 0xFF);
            return this;
        }

        @Override
        public SProcessor writeAsLong(DataOutput output, long value) throws IOException {
            output.writeInt((int) value);
            return this;
        }

        @Override
        public SProcessor writeAsBoolean(DataOutput output, boolean value) throws IOException {
            return writeAsInt(output, value ? 1 : 0);
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, int value) throws IOException {
            return writeAsInt(output, value & 0xFF);
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, int value) throws IOException {
            return writeAsInt(output, value & 0xFFFF);
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, long value) throws IOException {
            output.writeInt((int) value);
            return this;
        }

    };
    public static final STemplate DOUBLE = new ST() {
        @Override
        public Object read(DataInput input) throws IOException {
            return readAsDouble(input);
        }

        @Override
        public int readAsInt(DataInput input) throws IOException {
            return (int) input.readDouble();
        }

        @Override
        public double readAsDouble(DataInput input) throws IOException {
            return input.readDouble();
        }

        @Override
        public float readAsFloat(DataInput input) throws IOException {
            return (float) input.readDouble();
        }

        @Override
        public short readAsShort(DataInput input) throws IOException {
            return (short) input.readDouble();
        }

        @Override
        public char readAsChar(DataInput input) throws IOException {
            return (char) input.readDouble();
        }

        @Override
        public byte readAsByte(DataInput input) throws IOException {
            return (byte) input.readDouble();
        }

        @Override
        public long readAsLong(DataInput input) throws IOException {
            return (long) input.readDouble();
        }

        @Override
        public boolean readAsBoolean(DataInput input) throws IOException {
            return input.readDouble() != 0;
        }

        @Override
        public int readAsUnsignedByte(DataInput input) throws IOException {
            return readAsInt(input) & 0xFF;
        }

        @Override
        public int readAsUnsignedShort(DataInput input) throws IOException {
            return readAsInt(input) & 0xFFFF;
        }

        @Override
        public long readAsUnsignedInt(DataInput input) throws IOException {
            return readAsInt(input) & 0xFFFF_FFFFL;
        }

        @Override
        public SProcessor write(DataOutput output, Object value) throws IOException {
            if (!(value instanceof Number)) {
                throw new TypeNotMatchException(value + " cannot match to number");
            }
            return writeAsDouble(output, ((Number) value).doubleValue());
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, int value) throws IOException {
            output.writeDouble(value);
            return this;
        }

        @Override
        public SProcessor writeAsDouble(DataOutput output, double value) throws IOException {
            output.writeDouble(value);
            return this;
        }

        @Override
        public SProcessor writeAsFloat(DataOutput output, float value) throws IOException {
            output.writeDouble(value);
            return this;
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, short value) throws IOException {
            output.writeDouble(value & 0xFFFF);
            return this;
        }

        @Override
        public SProcessor writeAsChar(DataOutput output, char value) throws IOException {
            output.writeDouble(value & 0xFFFF);
            return this;
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, byte value) throws IOException {
            output.writeDouble(value & 0xFF);
            return this;
        }

        @Override
        public SProcessor writeAsLong(DataOutput output, long value) throws IOException {
            output.writeDouble((double) value);
            return this;
        }

        @Override
        public SProcessor writeAsBoolean(DataOutput output, boolean value) throws IOException {
            return writeAsInt(output, value ? 1 : 0);
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, int value) throws IOException {
            return writeAsInt(output, value & 0xFF);
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, int value) throws IOException {
            return writeAsInt(output, value & 0xFFFF);
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, long value) throws IOException {
            output.writeDouble((int) value);
            return this;
        }

    };
    public static final STemplate LONG = new ST() {
        @Override
        public Object read(DataInput input) throws IOException {
            return readAsLong(input);
        }

        @Override
        public int readAsInt(DataInput input) throws IOException {
            return (int) input.readLong();
        }

        @Override
        public double readAsDouble(DataInput input) throws IOException {
            return input.readLong();
        }

        @Override
        public float readAsFloat(DataInput input) throws IOException {
            return (float) input.readLong();
        }

        @Override
        public short readAsShort(DataInput input) throws IOException {
            return (short) input.readLong();
        }

        @Override
        public char readAsChar(DataInput input) throws IOException {
            return (char) input.readLong();
        }

        @Override
        public byte readAsByte(DataInput input) throws IOException {
            return (byte) input.readLong();
        }

        @Override
        public long readAsLong(DataInput input) throws IOException {
            return input.readLong();
        }

        @Override
        public boolean readAsBoolean(DataInput input) throws IOException {
            return input.readLong() != 0;
        }

        @Override
        public int readAsUnsignedByte(DataInput input) throws IOException {
            return readAsInt(input) & 0xFF;
        }

        @Override
        public int readAsUnsignedShort(DataInput input) throws IOException {
            return readAsInt(input) & 0xFFFF;
        }

        @Override
        public long readAsUnsignedInt(DataInput input) throws IOException {
            return readAsInt(input) & 0xFFFF_FFFFL;
        }

        @Override
        public SProcessor write(DataOutput output, Object value) throws IOException {
            if (!(value instanceof Number)) {
                throw new TypeNotMatchException(value + " cannot match to number");
            }
            return writeAsLong(output, ((Number) value).longValue());
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, int value) throws IOException {
            return writeAsLong(output, value & 0xFFFFFFFFL);
        }

        @Override
        public SProcessor writeAsDouble(DataOutput output, double value) throws IOException {
            output.writeLong((long) value);
            return this;
        }

        @Override
        public SProcessor writeAsFloat(DataOutput output, float value) throws IOException {
            output.writeLong((long) value);
            return this;
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, short value) throws IOException {
            return writeAsLong(output, value & 0xFFFFL);
        }

        @Override
        public SProcessor writeAsChar(DataOutput output, char value) throws IOException {
            return writeAsLong(output, value & 0xFFFFL);
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, byte value) throws IOException {
            return writeAsLong(output, value & 0xFFL);
        }

        @Override
        public SProcessor writeAsLong(DataOutput output, long value) throws IOException {
            output.writeLong(value);
            return this;
        }

        @Override
        public SProcessor writeAsBoolean(DataOutput output, boolean value) throws IOException {
            return writeAsInt(output, value ? 1 : 0);
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, int value) throws IOException {
            return writeAsInt(output, value & 0xFF);
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, int value) throws IOException {
            return writeAsInt(output, value & 0xFFFF);
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, long value) throws IOException {
            return writeAsInt(output, (int) value);
        }

    };
    public static final STemplate FLOAT = new ST() {
        @Override
        public Object read(DataInput input) throws IOException {
            return readAsFloat(input);
        }

        @Override
        public int readAsInt(DataInput input) throws IOException {
            return (int) input.readFloat();
        }

        @Override
        public double readAsDouble(DataInput input) throws IOException {
            return input.readFloat();
        }

        @Override
        public float readAsFloat(DataInput input) throws IOException {
            return input.readFloat();
        }

        @Override
        public short readAsShort(DataInput input) throws IOException {
            return (short) input.readFloat();
        }

        @Override
        public char readAsChar(DataInput input) throws IOException {
            return (char) input.readFloat();
        }

        @Override
        public byte readAsByte(DataInput input) throws IOException {
            return (byte) input.readFloat();
        }

        @Override
        public long readAsLong(DataInput input) throws IOException {
            return (long) input.readFloat();
        }

        @Override
        public boolean readAsBoolean(DataInput input) throws IOException {
            return readAsInt(input) != 0;
        }

        @Override
        public int readAsUnsignedByte(DataInput input) throws IOException {
            return readAsInt(input) & 0xFF;
        }

        @Override
        public int readAsUnsignedShort(DataInput input) throws IOException {
            return readAsInt(input) & 0xFFFF;
        }

        @Override
        public long readAsUnsignedInt(DataInput input) throws IOException {
            return readAsInt(input) & 0xFFFF_FFFFL;
        }

        @Override
        public SProcessor write(DataOutput output, Object value) throws IOException {
            if (!(value instanceof Number)) {
                throw new TypeNotMatchException(value + " cannot match to number");
            }
            return writeAsFloat(output, ((Number) value).floatValue());
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, int value) throws IOException {
            return writeAsFloat(output, (float) value);
        }

        @Override
        public SProcessor writeAsDouble(DataOutput output, double value) throws IOException {
            return writeAsFloat(output, (float) value);
        }

        @Override
        public SProcessor writeAsFloat(DataOutput output, float value) throws IOException {
            output.writeFloat(value);
            return this;
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, short value) throws IOException {
            return writeAsFloat(output, value);
        }

        @Override
        public SProcessor writeAsChar(DataOutput output, char value) throws IOException {
            return writeAsFloat(output, value);
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, byte value) throws IOException {
            return writeAsFloat(output, value);
        }

        @Override
        public SProcessor writeAsLong(DataOutput output, long value) throws IOException {
            output.writeFloat(value);
            return this;
        }

        @Override
        public SProcessor writeAsBoolean(DataOutput output, boolean value) throws IOException {
            return writeAsInt(output, value ? 1 : 0);
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, int value) throws IOException {
            return writeAsInt(output, value & 0xFF);
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, int value) throws IOException {
            return writeAsInt(output, value & 0xFFFF);
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, long value) throws IOException {
            output.writeFloat((int) value);
            return this;
        }

    };
    public static final STemplate SHORT = new ST() {
        @Override
        public Object read(DataInput input) throws IOException {
            return readAsShort(input);
        }

        @Override
        public int readAsInt(DataInput input) throws IOException {
            return input.readShort();
        }

        @Override
        public double readAsDouble(DataInput input) throws IOException {
            return input.readShort();
        }

        @Override
        public float readAsFloat(DataInput input) throws IOException {
            return input.readShort();
        }

        @Override
        public short readAsShort(DataInput input) throws IOException {
            return input.readShort();
        }

        @Override
        public char readAsChar(DataInput input) throws IOException {
            return (char) input.readShort();
        }

        @Override
        public byte readAsByte(DataInput input) throws IOException {
            return (byte) input.readShort();
        }

        @Override
        public long readAsLong(DataInput input) throws IOException {
            return input.readShort();
        }

        @Override
        public boolean readAsBoolean(DataInput input) throws IOException {
            return readAsInt(input) != 0;
        }

        @Override
        public int readAsUnsignedByte(DataInput input) throws IOException {
            return readAsInt(input) & 0xFF;
        }

        @Override
        public int readAsUnsignedShort(DataInput input) throws IOException {
            return input.readUnsignedShort();
        }

        @Override
        public long readAsUnsignedInt(DataInput input) throws IOException {
            return input.readUnsignedShort();
        }

        @Override
        public SProcessor write(DataOutput output, Object value) throws IOException {
            if (!(value instanceof Number)) {
                throw new TypeNotMatchException(value + " cannot match to number");
            }
            return writeAsShort(output, ((Number) value).shortValue());
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, int value) throws IOException {
            return writeAsShort(output, (short) value);
        }

        @Override
        public SProcessor writeAsDouble(DataOutput output, double value) throws IOException {
            return writeAsShort(output, (short) value);
        }

        @Override
        public SProcessor writeAsFloat(DataOutput output, float value) throws IOException {
            return writeAsShort(output, (short) value);
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, short value) throws IOException {
            output.writeShort(value);
            return this;
        }

        @Override
        public SProcessor writeAsChar(DataOutput output, char value) throws IOException {
            return writeAsShort(output, value);
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, byte value) throws IOException {
            return writeAsShort(output, value);
        }

        @Override
        public SProcessor writeAsLong(DataOutput output, long value) throws IOException {
            output.writeShort((short) value);
            return this;
        }

        @Override
        public SProcessor writeAsBoolean(DataOutput output, boolean value) throws IOException {
            return writeAsInt(output, value ? 1 : 0);
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, int value) throws IOException {
            return writeAsInt(output, value & 0xFF);
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, int value) throws IOException {
            return writeAsInt(output, value & 0xFFFF);
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, long value) throws IOException {
            output.writeShort((short) value);
            return this;
        }

    };
    public static final STemplate BYTE = new ST() {
        @Override
        public Object read(DataInput input) throws IOException {
            return readAsByte(input);
        }

        @Override
        public int readAsInt(DataInput input) throws IOException {
            return input.readUnsignedByte();
        }

        @Override
        public double readAsDouble(DataInput input) throws IOException {
            return input.readByte();
        }

        @Override
        public float readAsFloat(DataInput input) throws IOException {
            return input.readByte();
        }

        @Override
        public short readAsShort(DataInput input) throws IOException {
            return input.readByte();
        }

        @Override
        public char readAsChar(DataInput input) throws IOException {
            return (char) input.readByte();
        }

        @Override
        public byte readAsByte(DataInput input) throws IOException {
            return input.readByte();
        }

        @Override
        public long readAsLong(DataInput input) throws IOException {
            return input.readUnsignedByte();
        }

        @Override
        public boolean readAsBoolean(DataInput input) throws IOException {
            return readAsInt(input) != 0;
        }

        @Override
        public int readAsUnsignedByte(DataInput input) throws IOException {
            return input.readUnsignedByte();
        }

        @Override
        public int readAsUnsignedShort(DataInput input) throws IOException {
            return input.readUnsignedByte();
        }

        @Override
        public long readAsUnsignedInt(DataInput input) throws IOException {
            return input.readUnsignedByte();
        }

        @Override
        public SProcessor write(DataOutput output, Object value) throws IOException {
            if (value instanceof Number)
                return writeAsByte(output, ((Number) value).byteValue());
            throw new TypeNotMatchException();
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, int value) throws IOException {
            return writeAsByte(output, value);
        }

        @Override
        public SProcessor writeAsDouble(DataOutput output, double value) throws IOException {
            return writeAsByte(output, (byte) value);
        }

        @Override
        public SProcessor writeAsFloat(DataOutput output, float value) throws IOException {
            return writeAsByte(output, (byte) value);
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, short value) throws IOException {
            return writeAsByte(output, (byte) value);
        }

        @Override
        public SProcessor writeAsChar(DataOutput output, char value) throws IOException {
            return writeAsByte(output, value);
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, byte value) throws IOException {
            output.write(value & 0xFF);
            return this;
        }

        @Override
        public SProcessor writeAsLong(DataOutput output, long value) throws IOException {
            return writeAsByte(output, (byte) value);
        }

        @Override
        public SProcessor writeAsBoolean(DataOutput output, boolean value) throws IOException {
            return writeAsByte(output, value ? 1 : 0);
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, int value) throws IOException {
            output.write(value & 0xFF);
            return this;
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, int value) throws IOException {
            return writeAsByte(output, value);
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, long value) throws IOException {
            return writeAsByte(output, (byte) value);
        }
    };
    public static final STemplate BOOLEAN = new ST() {
        @Override
        public Object read(DataInput input) throws IOException {
            return readAsBoolean(input);
        }

        @Override
        public int readAsInt(DataInput input) throws IOException {
            return input.readUnsignedByte();
        }

        @Override
        public double readAsDouble(DataInput input) throws IOException {
            return input.readByte();
        }

        @Override
        public float readAsFloat(DataInput input) throws IOException {
            return input.readByte();
        }

        @Override
        public short readAsShort(DataInput input) throws IOException {
            return input.readByte();
        }

        @Override
        public char readAsChar(DataInput input) throws IOException {
            return (char) input.readByte();
        }

        @Override
        public byte readAsByte(DataInput input) throws IOException {
            return input.readByte();
        }

        @Override
        public long readAsLong(DataInput input) throws IOException {
            return input.readUnsignedByte();
        }

        @Override
        public boolean readAsBoolean(DataInput input) throws IOException {
            return input.readBoolean();
        }

        @Override
        public int readAsUnsignedByte(DataInput input) throws IOException {
            return input.readUnsignedByte();
        }

        @Override
        public int readAsUnsignedShort(DataInput input) throws IOException {
            return input.readUnsignedByte();
        }

        @Override
        public long readAsUnsignedInt(DataInput input) throws IOException {
            return input.readUnsignedByte();
        }

        @Override
        public SProcessor write(DataOutput output, Object value) throws IOException {
            if (value instanceof Number)
                return writeAsByte(output, ((Number) value).byteValue());
            throw new TypeNotMatchException();
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, int value) throws IOException {
            return writeAsByte(output, value);
        }

        @Override
        public SProcessor writeAsDouble(DataOutput output, double value) throws IOException {
            return writeAsByte(output, (byte) value);
        }

        @Override
        public SProcessor writeAsFloat(DataOutput output, float value) throws IOException {
            return writeAsByte(output, (byte) value);
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, short value) throws IOException {
            return writeAsByte(output, (byte) value);
        }

        @Override
        public SProcessor writeAsChar(DataOutput output, char value) throws IOException {
            return writeAsByte(output, value);
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, byte value) throws IOException {
            output.write(value & 0xFF);
            return this;
        }

        @Override
        public SProcessor writeAsLong(DataOutput output, long value) throws IOException {
            return writeAsByte(output, (byte) value);
        }

        @Override
        public SProcessor writeAsBoolean(DataOutput output, boolean value) throws IOException {
            return writeAsByte(output, value ? 1 : 0);
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, int value) throws IOException {
            output.write(value & 0xFF);
            return this;
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, int value) throws IOException {
            return writeAsByte(output, value);
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, long value) throws IOException {
            return writeAsByte(output, (byte) value);
        }
    };
    public static final STemplate CHARACTER = new ST() {
        @Override
        public Object read(DataInput input) throws IOException {
            return readAsChar(input);
        }

        @Override
        public int readAsInt(DataInput input) throws IOException {
            return input.readChar();
        }

        @Override
        public double readAsDouble(DataInput input) throws IOException {
            return input.readChar();
        }

        @Override
        public float readAsFloat(DataInput input) throws IOException {
            return input.readChar();
        }

        @Override
        public short readAsShort(DataInput input) throws IOException {
            return (short) input.readChar();
        }

        @Override
        public char readAsChar(DataInput input) throws IOException {
            return input.readChar();
        }

        @Override
        public byte readAsByte(DataInput input) throws IOException {
            return (byte) readAsChar(input);
        }

        @Override
        public long readAsLong(DataInput input) throws IOException {
            return readAsChar(input);
        }

        @Override
        public boolean readAsBoolean(DataInput input) throws IOException {
            return readAsInt(input) != 0;
        }

        @Override
        public int readAsUnsignedByte(DataInput input) throws IOException {
            return readAsByte(input) & 0xFF;
        }

        @Override
        public int readAsUnsignedShort(DataInput input) throws IOException {
            return readAsShort(input) & 0xFFFF;
        }

        @Override
        public long readAsUnsignedInt(DataInput input) throws IOException {
            return readAsChar(input) & 0xFFFF;
        }

        @Override
        public SProcessor write(DataOutput output, Object value) throws IOException {
            if (value instanceof Character)
                return writeAsChar(output, (Character) value);
            if (value instanceof Number)
                return writeAsInt(output, ((Number) value).intValue());
            throw new TypeNotMatchException();
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, int value) throws IOException {
            return writeAsChar(output, (char) value);
        }

        @Override
        public SProcessor writeAsDouble(DataOutput output, double value) throws IOException {
            return writeAsChar(output, (char) value);
        }

        @Override
        public SProcessor writeAsFloat(DataOutput output, float value) throws IOException {
            return writeAsChar(output, (char) value);
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, short value) throws IOException {
            return writeAsChar(output, (char) value);
        }

        @Override
        public SProcessor writeAsChar(DataOutput output, char value) throws IOException {
            output.writeChar(value);
            return this;
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, byte value) throws IOException {
            return writeAsChar(output, (char) value);
        }

        @Override
        public SProcessor writeAsLong(DataOutput output, long value) throws IOException {
            return writeAsChar(output, (char) value);
        }

        @Override
        public SProcessor writeAsBoolean(DataOutput output, boolean value) throws IOException {
            return writeAsByte(output, value ? 1 : 0);
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, int value) throws IOException {
            return writeAsChar(output, (char) (byte) value);
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, int value) throws IOException {
            return writeAsChar(output, (char) (short) value);
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, long value) throws IOException {
            return writeAsChar(output, (char) value);
        }
    };
    public static final STemplate STRING = new ST() {
        @Override
        public Object read(DataInput input) throws IOException {
            return readAsString(input);
        }

        @Override
        public int readAsInt(DataInput input) throws IOException {
            try {
                return Integer.parseInt(readAsString(input));
            } catch (NumberFormatException e) {
                throw new TypeNotMatchException(e);
            }
        }

        @Override
        public double readAsDouble(DataInput input) throws IOException {
            try {
                return Double.parseDouble(readAsString(input));
            } catch (NumberFormatException e) {
                throw new TypeNotMatchException(e);
            }
        }

        @Override
        public float readAsFloat(DataInput input) throws IOException {
            try {
                return Float.parseFloat(readAsString(input));
            } catch (NumberFormatException e) {
                throw new TypeNotMatchException(e);
            }
        }

        @Override
        public short readAsShort(DataInput input) throws IOException {
            try {
                return Short.parseShort(readAsString(input));
            } catch (NumberFormatException e) {
                throw new TypeNotMatchException(e);
            }
        }

        @Override
        public char readAsChar(DataInput input) throws IOException {
            try {
                return (char) Integer.parseInt(readAsString(input));
            } catch (NumberFormatException e) {
                throw new TypeNotMatchException(e);
            }
        }

        @Override
        public byte readAsByte(DataInput input) throws IOException {
            try {
                return Byte.parseByte(readAsString(input));
            } catch (NumberFormatException e) {
                throw new TypeNotMatchException(e);
            }
        }

        @Override
        public long readAsLong(DataInput input) throws IOException {
            try {
                return Long.parseLong(readAsString(input));
            } catch (NumberFormatException e) {
                throw new TypeNotMatchException(e);
            }
        }

        @Override
        public boolean readAsBoolean(DataInput input) throws IOException {
            try {
                return Boolean.parseBoolean(readAsString(input));
            } catch (NumberFormatException e) {
                throw new TypeNotMatchException(e);
            }
        }

        @Override
        public int readAsUnsignedByte(DataInput input) throws IOException {
            return readAsByte(input) & 0xFF;
        }

        @Override
        public int readAsUnsignedShort(DataInput input) throws IOException {
            return readAsShort(input) & 0xFFFF;
        }

        @Override
        public long readAsUnsignedInt(DataInput input) throws IOException {
            return readAsInt(input) & 0xFFFFFFFFL;
        }

        @Override
        public String readAsString(DataInput input) throws IOException {
            return input.readUTF();
        }

        @Override
        public SProcessor write(DataOutput output, Object value) throws IOException {
            output.writeUTF(String.valueOf(value));
            return this;
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, int value) throws IOException {
            return write(output, String.valueOf(value));
        }

        @Override
        public SProcessor writeAsDouble(DataOutput output, double value) throws IOException {
            return write(output, String.valueOf(value));
        }

        @Override
        public SProcessor writeAsFloat(DataOutput output, float value) throws IOException {
            return write(output, String.valueOf(value));
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, short value) throws IOException {
            return write(output, String.valueOf(value));
        }

        @Override
        public SProcessor writeAsChar(DataOutput output, char value) throws IOException {
            return write(output, String.valueOf(value));
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, byte value) throws IOException {
            return write(output, String.valueOf(value));
        }

        @Override
        public SProcessor writeAsLong(DataOutput output, long value) throws IOException {
            return write(output, String.valueOf(value));
        }

        @Override
        public SProcessor writeAsBoolean(DataOutput output, boolean value) throws IOException {
            return write(output, String.valueOf(value));
        }

        @Override
        public SProcessor writeAsByte(DataOutput output, int value) throws IOException {
            return write(output, String.valueOf(value));
        }

        @Override
        public SProcessor writeAsShort(DataOutput output, int value) throws IOException {
            return write(output, String.valueOf(value));
        }

        @Override
        public SProcessor writeAsInt(DataOutput output, long value) throws IOException {
            return write(output, String.valueOf(value));
        }
    };
    private static final SProcessor Z_ARRAY = new AbstractProcessor() {
        @Override
        public Object read(DataInput input) throws IOException {
            int size = input.readUnsignedShort();
            boolean[] array = new boolean[size];
            for (var step = 0; step < size; step += 8) {
                // 0b76543210
                int val = input.readUnsignedByte();
                for (var x = 0; x < 8; x++) {
                    var offset = step + x;
                    if (offset < size) {
                        array[offset] = ((val >> x) & 1) == 1;
                    } else break;
                }
            }
            return array;
        }

        @Override
        public SProcessor write(DataOutput output, Object value) throws IOException {
            if (value instanceof boolean[]) {
                var arr = (boolean[]) value;
                var size = arr.length;
                output.writeShort(size);
                for (var step = 0; step < size; step += 8) {
                    // 0b76543210
                    int val = 0;
                    for (var x = 0; x < 8; x++) {
                        var offset = step + x;
                        if (offset < size) {
                            if (arr[offset])
                                val |= 1 << x;
                        } else break;
                    }
                    output.writeByte(val);
                }
            }
            return null;
        }
    };
    public static final STemplate BOOLEAN_ARRAY = new BasicTemplate(Z_ARRAY);
    public static final STemplate ATOMIC_INTEGER = new BasicTemplate(new FilteredProcessor(INTEGER.getProcessor()) {
        @Override
        public Object read(DataInput input) throws IOException {
            return new AtomicInteger(readAsInt(input));
        }
    });
    public static final STemplate ATOMIC_LONG = new BasicTemplate(new FilteredProcessor(LONG.getProcessor()) {
        @Override
        public Object read(DataInput input) throws IOException {
            return new AtomicLong(readAsLong(input));
        }
    });
    public static final STemplate ATOMIC_STRING = new BasicTemplate(new FilteredProcessor(STRING.getProcessor()) {
        @Override
        public Object read(DataInput input) throws IOException {
            return new AtomicReference<>(readAsString(input));
        }
    });

    @Contract("!null -> !null; null -> null")
    public static STemplate atomic(STemplate template) {
        if (template == null) return null;
        return new BasicTemplate(new FilteredProcessor(template.getProcessor()) {
            @Override
            public SProcessor write(DataOutput output, Object value) throws IOException {
                if (value instanceof AtomicReference) value = ((AtomicReference<?>) value).get();
                return super.write(output, value);
            }

            @Override
            public Object read(DataInput input) throws IOException {
                return new AtomicReference<>(super.read(input));
            }
        }) {
            @Override
            public void setGroup(STemplateGroup group) {
                template.setGroup(group);
            }

            @Override
            public STemplateGroup getGroup() {
                return template.getGroup();
            }
        };
    }
}
