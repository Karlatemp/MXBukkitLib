/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: WrappedDataOutput.java@author: karlatemp@vip.qq.com: 19-11-22 下午12:29@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutput;
import java.io.IOException;
import java.util.function.Consumer;

public class WrappedDataOutput extends WrappedObject<DataOutput> implements DataOutput {

    public WrappedDataOutput() {
    }

    public WrappedDataOutput(DataOutput value) {
        super(value);
    }

    public WrappedDataOutput(@Nullable Consumer<Consumer<DataOutput>> hook_setter) {
        super(hook_setter);
    }

    public WrappedDataOutput(DataOutput value, @Nullable Consumer<Consumer<DataOutput>> hook_setter) {
        super(value, hook_setter);
    }

    public void write(int b) throws IOException {
        object.write(b);
    }

    public void write(@NotNull byte[] b) throws IOException {
        object.write(b);
    }

    public void write(@NotNull byte[] b, int off, int len) throws IOException {
        object.write(b, off, len);
    }

    public void writeBoolean(boolean v) throws IOException {
        object.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        object.writeByte(v);
    }

    public void writeShort(int v) throws IOException {
        object.writeShort(v);
    }

    public void writeChar(int v) throws IOException {
        object.writeChar(v);
    }

    public void writeInt(int v) throws IOException {
        object.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        object.writeLong(v);
    }

    public void writeFloat(float v) throws IOException {
        object.writeFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        object.writeDouble(v);
    }

    public void writeBytes(@NotNull String s) throws IOException {
        object.writeBytes(s);
    }

    public void writeChars(@NotNull String s) throws IOException {
        object.writeChars(s);
    }

    public void writeUTF(@NotNull String s) throws IOException {
        object.writeUTF(s);
    }
}
