/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 01:01:49
 *
 * MXLib/mxlib.message/SProcessor.java
 */

package cn.mcres.karlatemp.mxlib.codec;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface SProcessor {
    Object read(DataInput input) throws IOException;

    int readAsInt(DataInput input) throws IOException;

    double readAsDouble(DataInput input) throws IOException;

    float readAsFloat(DataInput input) throws IOException;

    short readAsShort(DataInput input) throws IOException;

    char readAsChar(DataInput input) throws IOException;

    byte readAsByte(DataInput input) throws IOException;

    long readAsLong(DataInput input) throws IOException;

    boolean readAsBoolean(DataInput input) throws IOException;

    int readAsUnsignedByte(DataInput input) throws IOException;

    int readAsUnsignedShort(DataInput input) throws IOException;

    long readAsUnsignedInt(DataInput input) throws IOException;

    default String readAsString(DataInput input) throws IOException {
        return String.valueOf(read(input));
    }

    SProcessor write(DataOutput output, Object value) throws IOException;

    SProcessor writeAsInt(DataOutput output, int value) throws IOException;

    SProcessor writeAsDouble(DataOutput output, double value) throws IOException;

    SProcessor writeAsFloat(DataOutput output, float value) throws IOException;

    SProcessor writeAsShort(DataOutput output, short value) throws IOException;

    SProcessor writeAsChar(DataOutput output, char value) throws IOException;

    SProcessor writeAsByte(DataOutput output, byte value) throws IOException;

    SProcessor writeAsLong(DataOutput output, long value) throws IOException;

    SProcessor writeAsBoolean(DataOutput output, boolean value) throws IOException;

    SProcessor writeAsByte(DataOutput output, int value) throws IOException;

    SProcessor writeAsShort(DataOutput output, int value) throws IOException;

    SProcessor writeAsInt(DataOutput output, long value) throws IOException;

}
