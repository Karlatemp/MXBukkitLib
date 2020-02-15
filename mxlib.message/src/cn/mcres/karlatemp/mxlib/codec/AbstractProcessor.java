/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 01:16:59
 *
 * MXLib/mxlib.message/AbstractProcessor.java
 */

package cn.mcres.karlatemp.mxlib.codec;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class AbstractProcessor implements SProcessor {
    @Override
    public abstract Object read(DataInput input) throws IOException;

    @Override
    public int readAsInt(DataInput input) throws IOException {
        var val = read(input);
        if (val instanceof Number) return ((Number) val).intValue();
        throw new TypeNotMatchException();
    }

    @Override
    public double readAsDouble(DataInput input) throws IOException {
        var val = read(input);
        if (val instanceof Number) return ((Number) val).doubleValue();
        throw new TypeNotMatchException();
    }

    @Override
    public float readAsFloat(DataInput input) throws IOException {
        var val = read(input);
        if (val instanceof Number) return ((Number) val).floatValue();
        throw new TypeNotMatchException();
    }

    @Override
    public short readAsShort(DataInput input) throws IOException {
        var val = read(input);
        if (val instanceof Number) return ((Number) val).shortValue();
        throw new TypeNotMatchException();
    }

    @Override
    public char readAsChar(DataInput input) throws IOException {
        var val = read(input);
        if (val instanceof Number) return (char) ((Number) val).intValue();
        if (val instanceof Character) return (Character) val;
        throw new TypeNotMatchException();
    }

    @Override
    public byte readAsByte(DataInput input) throws IOException {
        var val = read(input);
        if (val instanceof Number) return ((Number) val).byteValue();
        throw new TypeNotMatchException();
    }

    @Override
    public long readAsLong(DataInput input) throws IOException {
        var val = read(input);
        if (val instanceof Number) return ((Number) val).longValue();
        throw new TypeNotMatchException();
    }

    @Override
    public boolean readAsBoolean(DataInput input) throws IOException {
        var val = read(input);
        if (val == null) return false;
        if (val instanceof Boolean) return (Boolean) val;
        if (val instanceof Number) return ((Number) val).intValue() != 0;
        throw new TypeNotMatchException();
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
        return readAsInt(input) & 0xFFFF_FFFFL;
    }

    @Override
    public abstract SProcessor write(DataOutput output, Object value) throws IOException;

    @Override
    public SProcessor writeAsInt(DataOutput output, int value) throws IOException {
        return write(output, value);
    }

    @Override
    public SProcessor writeAsDouble(DataOutput output, double value) throws IOException {
        return write(output, value);
    }

    @Override
    public SProcessor writeAsFloat(DataOutput output, float value) throws IOException {
        return write(output, value);
    }

    @Override
    public SProcessor writeAsShort(DataOutput output, short value) throws IOException {
        return write(output, value);
    }

    @Override
    public SProcessor writeAsChar(DataOutput output, char value) throws IOException {
        return write(output, value);
    }

    @Override
    public SProcessor writeAsByte(DataOutput output, byte value) throws IOException {
        return write(output, value);
    }

    @Override
    public SProcessor writeAsLong(DataOutput output, long value) throws IOException {
        return write(output, value);
    }

    @Override
    public SProcessor writeAsBoolean(DataOutput output, boolean value) throws IOException {
        return write(output, value);
    }

    @Override
    public SProcessor writeAsByte(DataOutput output, int value) throws IOException {
        return write(output, (byte) value);
    }

    @Override
    public SProcessor writeAsShort(DataOutput output, int value) throws IOException {
        return write(output, (short) value);
    }

    @Override
    public SProcessor writeAsInt(DataOutput output, long value) throws IOException {
        return write(output, (int) value);
    }
}
