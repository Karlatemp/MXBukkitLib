/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: SerializeInput.java@author: karlatemp@vip.qq.com: 2020/1/23 下午3:08@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.packet;

import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.IOException;

public class SerializeInput implements DataInput{
    private final SerializeSystem sys;
    private final DataInput input;

    public SerializeInput(SerializeSystem sys, DataInput input) {
        this.sys = sys;
        this.input = input;
    }

    public <T> T read(Class<T> type) throws IOException {
        return sys.getSerializerByType(type).deserialize(type, this);
    }

    public String readString() throws IOException {
        return sys.getSerializerByType(String.class).deserialize(String.class, this);
    }

    public int readInt() throws IOException {
        return input.readInt();
    }

    public DataInput getInput() {
        return input;
    }

    public void readFully(@NotNull byte[] b) throws IOException {
        input.readFully(b);
    }

    public void readFully(@NotNull byte[] b, int off, int len) throws IOException {
        input.readFully(b, off, len);
    }

    public int skipBytes(int n) throws IOException {
        return input.skipBytes(n);
    }

    public boolean readBoolean() throws IOException {
        return input.readBoolean();
    }

    public byte readByte() throws IOException {
        return input.readByte();
    }

    public int readUnsignedByte() throws IOException {
        return input.readUnsignedByte();
    }

    public short readShort() throws IOException {
        return input.readShort();
    }

    public int readUnsignedShort() throws IOException {
        return input.readUnsignedShort();
    }

    public char readChar() throws IOException {
        return input.readChar();
    }

    public long readLong() throws IOException {
        return input.readLong();
    }

    public float readFloat() throws IOException {
        return input.readFloat();
    }

    public double readDouble() throws IOException {
        return input.readDouble();
    }

    public String readLine() throws IOException {
        return input.readLine();
    }

    public String readUTF() throws IOException {
        return input.readUTF();
    }
}
