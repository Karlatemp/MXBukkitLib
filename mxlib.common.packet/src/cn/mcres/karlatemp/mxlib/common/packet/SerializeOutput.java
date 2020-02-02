/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: SerializeOutput.java@author: karlatemp@vip.qq.com: 2020/1/23 下午3:09@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.packet;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;

public class SerializeOutput implements DataOutput {
    private final DataOutput output;
    private final SerializeSystem sys;

    public SerializeOutput(SerializeSystem sys, DataOutput output) {
        this.sys = sys;
        this.output = output;
    }

    public DataOutput getOutput() {
        return output;
    }

    public SerializeOutput writeString(String data) throws IOException {
        sys.getSerializerByType(String.class).serialize(data, this);
        return this;
    }

    public SerializeOutput write(Class<?> type, Object data) throws IOException {
        sys.getSerializerByType(type).serialize(data, this);
        return this;
    }

    public SerializeOutput write(Object data) throws IOException {
        sys.getSerializer(data).serialize(data, this);
        return this;
    }

    public void write(int b) throws IOException {
        output.write(b);
    }

    public void write(@NotNull byte[] b) throws IOException {
        output.write(b);
    }

    public void write(@NotNull byte[] b, int off, int len) throws IOException {
        output.write(b, off, len);
    }

    public void writeBoolean(boolean v) throws IOException {
        output.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        output.writeByte(v);
    }

    public void writeShort(int v) throws IOException {
        output.writeShort(v);
    }

    public void writeChar(int v) throws IOException {
        output.writeChar(v);
    }

    public void writeInt(int v) throws IOException {
        output.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        output.writeLong(v);
    }

    public void writeFloat(float v) throws IOException {
        output.writeFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        output.writeDouble(v);
    }

    public void writeBytes(@NotNull String s) throws IOException {
        output.writeBytes(s);
    }

    public void writeChars(@NotNull String s) throws IOException {
        output.writeChars(s);
    }

    public void writeUTF(@NotNull String s) throws IOException {
        output.writeUTF(s);
    }
}
