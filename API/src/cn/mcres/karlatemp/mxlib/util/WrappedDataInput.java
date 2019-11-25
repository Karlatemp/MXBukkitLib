/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: WrappedDataInput.java@author: karlatemp@vip.qq.com: 19-11-22 下午12:27@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.IOException;
import java.util.function.Consumer;

public class WrappedDataInput extends WrappedObject<DataInput> implements DataInput {
    public void readFully(@NotNull byte[] b) throws IOException {
        object.readFully(b);
    }

    public void readFully(@NotNull byte[] b, int off, int len) throws IOException {
        object.readFully(b, off, len);
    }

    public int skipBytes(int n) throws IOException {
        return object.skipBytes(n);
    }

    public boolean readBoolean() throws IOException {
        return object.readBoolean();
    }

    public byte readByte() throws IOException {
        return object.readByte();
    }

    public int readUnsignedByte() throws IOException {
        return object.readUnsignedByte();
    }

    public short readShort() throws IOException {
        return object.readShort();
    }

    public int readUnsignedShort() throws IOException {
        return object.readUnsignedShort();
    }

    public char readChar() throws IOException {
        return object.readChar();
    }

    public int readInt() throws IOException {
        return object.readInt();
    }

    public long readLong() throws IOException {
        return object.readLong();
    }

    public float readFloat() throws IOException {
        return object.readFloat();
    }

    public double readDouble() throws IOException {
        return object.readDouble();
    }

    public String readLine() throws IOException {
        return object.readLine();
    }

    @NotNull
    public String readUTF() throws IOException {
        return object.readUTF();
    }
}
