/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/29 15:57:22
 *
 * MXLib/mxlib.configuration/RAFInputStream.java
 */

package cn.mcres.karlatemp.mxlib.config;

import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * A input stream with RandomAccessFile
 *
 * @since 2.12
 */
public class RAFInputStream extends InputStream implements DataInput {
    private RandomAccessFile raf;
    private long pos;
    private final boolean allowClose;

    @Override
    public void readFully(@NotNull byte[] b) throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        raf.readFully(b);
    }

    @Override
    public void readFully(@NotNull byte[] b, int off, int len) throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        raf.readFully(b, off, len);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.skipBytes(n);
    }

    @Override
    public boolean readBoolean() throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.readByte();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.readUnsignedByte();
    }

    @Override
    public short readShort() throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.readShort();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.readUnsignedShort();
    }

    @Override
    public char readChar() throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.readChar();
    }

    @Override
    public int readInt() throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.readInt();
    }

    @Override
    public long readLong() throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.readLong();
    }

    @Override
    public float readFloat() throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.readDouble();
    }

    @Override
    public String readLine() throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.readLine();
    }

    @NotNull
    @Override
    public String readUTF() throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.readUTF();
    }

    @Override
    public int read() throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.read();
    }

    @Override
    public int read(@NotNull byte[] b) throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.read(b);
    }

    @Override
    public int read(@NotNull byte[] b, int off, int len) throws IOException {
        if (raf == null) throw new IOException("Stream closed.");
        return raf.read(b, off, len);
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public synchronized void reset() throws IOException {
        if (raf == null) return;
        raf.seek(pos);
    }

    @Override
    public synchronized void mark(int readlimit) {
        if (raf == null) return;
        try {
            pos = raf.getFilePointer();
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (raf == null) return;
        if (allowClose)
            raf.close();
        raf = null;
    }

    public RAFInputStream(RandomAccessFile raf) throws IOException {
        this(raf, true);
    }

    @Override
    public int available() throws IOException {
        long av = raf.getFilePointer() - raf.length();
        if (av == 0L) return 0;
        if (av > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if (av < 0) return Integer.MAX_VALUE;
        return (int) av;
    }

    /**
     * The constructor of RAFInputStream
     *
     * @param raf        The RAF using
     * @param allowClose Need close RAF in {@link #close()}
     * @throws IOException Error in {@link RandomAccessFile#getFilePointer()}
     */
    public RAFInputStream(RandomAccessFile raf, boolean allowClose) throws IOException {
        this.raf = raf;
        pos = raf.getFilePointer();
        this.allowClose = allowClose;
    }
}
