/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 02:03:45
 *
 * MXLib/mxlib.message/FilteredProcessor.java
 */

package cn.mcres.karlatemp.mxlib.codec;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FilteredProcessor implements SProcessor {
    protected SProcessor parent;

    public FilteredProcessor(SProcessor parent) {
        this.parent = parent;
    }

    @Override
    public Object read(DataInput input) throws IOException {
        return parent.read(input);
    }

    @Override
    public int readAsInt(DataInput input) throws IOException {
        return parent.readAsInt(input);
    }

    @Override
    public double readAsDouble(DataInput input) throws IOException {
        return parent.readAsDouble(input);
    }

    @Override
    public float readAsFloat(DataInput input) throws IOException {
        return parent.readAsFloat(input);
    }

    @Override
    public short readAsShort(DataInput input) throws IOException {
        return parent.readAsShort(input);
    }

    @Override
    public char readAsChar(DataInput input) throws IOException {
        return parent.readAsChar(input);
    }

    @Override
    public byte readAsByte(DataInput input) throws IOException {
        return parent.readAsByte(input);
    }

    @Override
    public long readAsLong(DataInput input) throws IOException {
        return parent.readAsLong(input);
    }

    @Override
    public boolean readAsBoolean(DataInput input) throws IOException {
        return parent.readAsBoolean(input);
    }

    @Override
    public int readAsUnsignedByte(DataInput input) throws IOException {
        return parent.readAsUnsignedByte(input);
    }

    @Override
    public int readAsUnsignedShort(DataInput input) throws IOException {
        return parent.readAsUnsignedShort(input);
    }

    @Override
    public long readAsUnsignedInt(DataInput input) throws IOException {
        return parent.readAsUnsignedInt(input);
    }

    @Override
    public String readAsString(DataInput input) throws IOException {
        return parent.readAsString(input);
    }

    @Override
    public SProcessor write(DataOutput output, Object value) throws IOException {
        return parent.write(output, value);
    }

    @Override
    public SProcessor writeAsInt(DataOutput output, int value) throws IOException {
        return parent.writeAsInt(output, value);
    }

    @Override
    public SProcessor writeAsDouble(DataOutput output, double value) throws IOException {
        return parent.writeAsDouble(output, value);
    }

    @Override
    public SProcessor writeAsFloat(DataOutput output, float value) throws IOException {
        return parent.writeAsFloat(output, value);
    }

    @Override
    public SProcessor writeAsShort(DataOutput output, short value) throws IOException {
        return parent.writeAsShort(output, value);
    }

    @Override
    public SProcessor writeAsChar(DataOutput output, char value) throws IOException {
        return parent.writeAsChar(output, value);
    }

    @Override
    public SProcessor writeAsByte(DataOutput output, byte value) throws IOException {
        return parent.writeAsByte(output, value);
    }

    @Override
    public SProcessor writeAsLong(DataOutput output, long value) throws IOException {
        return parent.writeAsLong(output, value);
    }

    @Override
    public SProcessor writeAsBoolean(DataOutput output, boolean value) throws IOException {
        return parent.writeAsBoolean(output, value);
    }

    @Override
    public SProcessor writeAsByte(DataOutput output, int value) throws IOException {
        return parent.writeAsByte(output, value);
    }

    @Override
    public SProcessor writeAsShort(DataOutput output, int value) throws IOException {
        return parent.writeAsShort(output, value);
    }

    @Override
    public SProcessor writeAsInt(DataOutput output, long value) throws IOException {
        return parent.writeAsInt(output, value);
    }
}
