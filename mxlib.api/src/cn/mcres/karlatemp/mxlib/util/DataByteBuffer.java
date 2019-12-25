/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DataByteBuffer.java@author: karlatemp@vip.qq.com: 19-10-19 下午8:22@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A DataInput/Output as ByteBuffer 使用ByteBuffer的数据流
 *
 * @since 2.4
 */
@SuppressWarnings("RedundantCast")
public class DataByteBuffer implements DataInput, DataOutput, Function<Consumer<ByteBuffer>, DataByteBuffer> {
    private final ByteBuffer buffer;
    public static final DataByteBuffer EMPTY = open(ByteBuffer.allocate(0));

    public static DataByteBuffer open(ByteBuffer buffer) {
        return new DataByteBuffer(buffer);
    }

    public DataByteBuffer(Buffer buffer) {
        this((ByteBuffer) buffer);
    }

    public DataByteBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public DataByteBuffer apply(Consumer<ByteBuffer> apply) {
        apply.accept(buffer);
        return this;
    }

    @Override
    public void readFully(@NotNull byte[] b) {
        readFully(b, 0, b.length);
    }

    @Override
    public void readFully(@NotNull byte[] b, int off, int len) {
        buffer.get(b, off, len);
    }

    @Override
    public int skipBytes(int n) {
        buffer.position(buffer.position() + n);
        return n;
    }

    @Override
    public boolean readBoolean() {
        return Byte.toUnsignedInt(buffer.get()) != 0;
    }

    @Override
    public byte readByte() {
        return buffer.get();
    }

    @Override
    public int readUnsignedByte() {
        return Byte.toUnsignedInt(readByte());
    }

    @Override
    public short readShort() {
        return buffer.getShort();
    }

    @Override
    public int readUnsignedShort() {
        return Short.toUnsignedInt(readShort());
    }

    @Override
    public char readChar() {
        return buffer.getChar();
    }

    @Override
    public int readInt() {
        return buffer.getInt();
    }

    @Override
    public long readLong() {
        return buffer.getLong();
    }

    @Override
    public float readFloat() {
        return buffer.getFloat();
    }

    @Override
    public double readDouble() {
        return buffer.getDouble();
    }

    @Override
    public String readLine() {
        int pos = buffer.position();
        int end = buffer.limit();
        while (buffer.hasRemaining()) {
            byte get = buffer.get();
            if (get == '\n') {
                end = buffer.position() - 1;
                break;
            }
        }
        byte[] data = new byte[end - pos];
        buffer.position(pos);
        buffer.get(data);
        return new String(data, StandardCharsets.UTF_8);
    }

    @NotNull
    @Override
    public String readUTF() {
        int length = readUnsignedShort();
        return StandardCharsets.UTF_8.decode((ByteBuffer) buffer.duplicate().limit(buffer.position() + length))
                .toString();

    }

    @Override
    public void write(int b) {
        buffer.put((byte) b);
    }

    @Override
    public void write(@NotNull byte[] b) {
        buffer.put(b);
    }

    @Override
    public void write(@NotNull byte[] b, int off, int len) {
        buffer.put(b, off, len);
    }

    @Override
    public void writeBoolean(boolean v) {
        buffer.put((byte) (v ? 1 : 0));
    }

    @Override
    public void writeByte(int v) {
        write(v);
    }

    @Override
    public void writeShort(int v) {
        buffer.putShort((short) v);
    }

    @Override
    public void writeChar(int v) {
        buffer.putChar((char) v);
    }

    @Override
    public void writeInt(int v) {
        buffer.putInt(v);
    }

    @Override
    public void writeLong(long v) {
        buffer.putLong(v);
    }

    @Override
    public void writeFloat(float v) {
        buffer.putFloat(v);
    }

    @Override
    public void writeDouble(double v) {
        buffer.putDouble(v);
    }

    @Override
    public void writeBytes(@NotNull String s) {
        for (char c : s.toCharArray())
            buffer.put((byte) c);
    }

    @Override
    public void writeChars(@NotNull String s) {
        for (char c : s.toCharArray())
            buffer.putChar(c);
    }

    @Override
    public void writeUTF(@NotNull String s) {
        final ByteBuffer encode = StandardCharsets.UTF_8.encode(s);
        buffer.putShort((short) encode.remaining());
        buffer.put(encode);
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }
}
