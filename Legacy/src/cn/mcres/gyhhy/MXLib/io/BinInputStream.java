/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BinInputStream.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */
package cn.mcres.gyhhy.MXLib.io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;
import java.nio.charset.StandardCharsets;
import java.io.EOFException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import javax.imageio.ImageIO;

/**
 * @author 32798
 */
public class BinInputStream extends InputStream {

    private final InputStream io;

    public BinInputStream(InputStream io) {
        this.io = io;
    }

    public int readUnsignedShort() throws IOException {
        int ch1 = read0();
        int ch2 = read0();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (ch1 << 8) + ch2;
    }

    public BufferedImage readImage() throws IOException {
        try (InputStream str = openStream()) {
            return ImageIO.read(str);
        }
    }

    public ByteArrayInputStream readStream() throws IOException {
        return new ByteArrayInputStream(readBytes());
    }

    public ByteArrayInputStream readStream(int leng) throws IOException {
        return new ByteArrayInputStream(readBytes(leng));
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInteger());
    }

    public byte[] readBytes() throws IOException {
        return readBytes(readInt());
    }

    public byte[] readBytes(int length) throws IOException {
        byte[] buff = new byte[length];
        read(buff);
        return buff;
    }

    public String readString() throws IOException {
        return readString(StandardCharsets.UTF_8);
    }

    public String readString(int length, Charset charset) throws IOException {
        return new String(readBytes(length), charset);
    }

    public LimitInputStream openStream() throws IOException {
        return openStream(readInt());
    }

    public LimitInputStream openStream(long limit) throws IOException {
        return new LimitInputStream(limit, this, false);
    }

    public String readString(int length) throws IOException {
        return readString(length, StandardCharsets.UTF_8);
    }

    public String readString(Charset charset) throws IOException {
        return readString(readInt(), charset);
    }

    @SuppressWarnings("FinalMethod")
    public final int readInt() throws IOException {
        return readInteger();
    }

    public short readShort() throws IOException {
        int ch1 = read0();
        int ch2 = read0();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (short) ((ch1 << 8) + ch2);
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public byte readByte() throws IOException {
        return (byte) read0();
    }

    public char readChar() throws IOException {
        return (char) ((read0() << 8) + read0());
    }

    public int readInteger() throws IOException {
        return (read0() << 24) + (read0() << 16) + (read0() << 8) + read0();
    }

    public long readLong() throws IOException {
        return ((Integer.toUnsignedLong(readInteger())) << 32) + (Integer.toUnsignedLong(readInteger()));
    }

    @Override
    public int read() throws IOException {
        return io.read();
    }

    public int read0() throws IOException {
        return read() & 0xff;
    }

    @Override
    public int available() throws IOException {
        return io.available();
    }

    public boolean readBoolean() throws IOException {
        return (read() & 0x1) == 1;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return io.read(b); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return io.read(b, off, len); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void reset() throws IOException {
        io.reset(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() throws IOException {
        io.close(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void mark(int readlimit) {
        io.mark(readlimit); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean markSupported() {
        return io.markSupported(); //To change body of generated methods, choose Tools | Templates.
    }

    public void readBytes(OutputStream out) throws IOException {
        out.write(readBytes());
    }

    @Override
    public long skip(long n) throws IOException {
        return io.skip(n); //To change body of generated methods, choose Tools | Templates.
    }

}
