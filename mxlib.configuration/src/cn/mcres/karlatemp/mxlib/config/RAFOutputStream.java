/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/29 15:57:22
 *
 * MXLib/mxlib.configuration/RAFOutputStream.java
 */

package cn.mcres.karlatemp.mxlib.config;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * A output stream of RandomAccessFile
 *
 * @since 2.12
 */
public class RAFOutputStream extends OutputStream implements DataOutput {
    private RandomAccessFile raf;
    private final boolean closeRAF;
    private final boolean autoSetLength;

    @Override
    public void writeBoolean(boolean v) throws IOException {
        raf.writeBoolean(v);
    }

    @Override
    public void writeByte(int v) throws IOException {
        raf.writeByte(v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        raf.writeShort(v);
    }

    @Override
    public void writeChar(int v) throws IOException {
        raf.writeChar(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        raf.writeInt(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        raf.writeLong(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        raf.writeFloat(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        raf.writeDouble(v);
    }

    @Override
    public void writeBytes(@NotNull String s) throws IOException {
        raf.writeBytes(s);
    }

    @Override
    public void writeChars(@NotNull String s) throws IOException {
        raf.writeChars(s);
    }

    @Override
    public void writeUTF(@NotNull String s) throws IOException {
        raf.writeUTF(s);
    }

    /**
     * The constructor
     *
     * @param raf           The RAF using
     * @param closeRAF      Need close RAF in calling {@link #close()}
     * @param autoSetLength Need set file length to wrote length in calling {@link #close()}
     */
    public RAFOutputStream(RandomAccessFile raf, boolean closeRAF, boolean autoSetLength) {
        this.raf = raf;
        this.closeRAF = closeRAF;
        this.autoSetLength = autoSetLength;
    }

    public RAFOutputStream(RandomAccessFile raf) {
        this(raf, true, true);
    }

    @Override
    public void write(int b) throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        raf.write(b);
    }

    @Override
    public void write(@NotNull byte[] b, int off, int len) throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        raf.write(b, off, len);
    }

    @Override
    public void write(@NotNull byte[] b) throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        raf.write(b);
    }

    @Override
    public void close() throws IOException {
        if (raf == null) return;
        if (autoSetLength) {
            raf.setLength(raf.getFilePointer());
        }
        if (closeRAF)
            raf.close();
        raf = null;
    }
}
