/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: WrappedFileInputStream.java@author: karlatemp@vip.qq.com: 2020/1/11 上午12:08@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class WrappedFileInputStream extends FileInputStream {
    private final InputStream input;

    public WrappedFileInputStream(@NotNull InputStream input) {
        super(new FileDescriptor());
        this.input = input;
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        return input.readAllBytes();
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        return input.readNBytes(len);
    }

    @Override
    public int read() throws IOException {
        return input.read();
    }

    @Override
    public int read(@NotNull byte[] b) throws IOException {
        return input.read(b);
    }

    @Override
    public int read(@NotNull byte[] b, int off, int len) throws IOException {
        return input.read(b, off, len);
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        return input.readNBytes(b, off, len);
    }

    @Override
    public synchronized void reset() throws IOException {
        input.reset();
    }

    @Override
    public boolean markSupported() {
        return input.markSupported();
    }

    @Override
    public synchronized void mark(int readlimit) {
        input.mark(readlimit);
    }

    @Override
    public void close() throws IOException {
        input.close();
    }

    @Override
    public long skip(long n) throws IOException {
        return input.skip(n);
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        input.skipNBytes(n);
    }

    @Override
    public int available() throws IOException {
        return input.available();
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return input.transferTo(out);
    }
}
