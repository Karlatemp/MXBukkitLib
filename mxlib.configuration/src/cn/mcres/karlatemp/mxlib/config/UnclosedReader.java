/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: UnclosedReader.java@author: karlatemp@vip.qq.com: 2020/1/26 下午1:11@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.config;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;

/**
 * A reader removed close()
 *
 * @since 2.12
 */
public class UnclosedReader extends Reader {
    private final Reader reader;

    public UnclosedReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public int read(@NotNull CharBuffer target) throws IOException {
        return reader.read(target);
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
    public int read(@NotNull char[] cbuf, int off, int len) throws IOException {
        return reader.read(cbuf, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return reader.skip(n);
    }

    @Override
    public boolean ready() throws IOException {
        return reader.ready();
    }

    @Override
    public boolean markSupported() {
        return reader.markSupported();
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        reader.mark(readAheadLimit);
    }

    @Override
    public void reset() throws IOException {
        reader.reset();
    }

    @Override
    public long transferTo(Writer out) throws IOException {
        return reader.transferTo(out);
    }

    @Override
    public void close() throws IOException {
    }
}
