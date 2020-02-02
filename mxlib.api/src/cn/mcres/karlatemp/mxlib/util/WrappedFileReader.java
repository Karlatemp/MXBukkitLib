/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: WrappedFileReader.java@author: karlatemp@vip.qq.com: 2020/1/11 上午12:11@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.CharBuffer;

public class WrappedFileReader extends FileReader {
    private final Reader reader;

    public WrappedFileReader(@NotNull Reader reader) {
        super(new FileDescriptor());
        this.reader = reader;
    }

    @Override
    public void reset() throws IOException {
        reader.reset();
    }

    @Override
    public int read() throws IOException {
        return reader.read();
    }

    @Override
    public int read(@NotNull char[] cbuf) throws IOException {
        return reader.read(cbuf);
    }

    @Override
    public int read(@NotNull CharBuffer target) throws IOException {
        return reader.read(target);
    }

    @Override
    public boolean ready() throws IOException {
        return reader.ready();
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        reader.mark(readAheadLimit);
    }

    @Override
    public int read(@NotNull char[] cbuf, int offset, int length) throws IOException {
        return reader.read(cbuf, offset, length);
    }

    @Override
    public long transferTo(Writer out) throws IOException {
        return reader.transferTo(out);
    }

    @Override
    public long skip(long n) throws IOException {
        return reader.skip(n);
    }

    @Override
    public boolean markSupported() {
        return reader.markSupported();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
