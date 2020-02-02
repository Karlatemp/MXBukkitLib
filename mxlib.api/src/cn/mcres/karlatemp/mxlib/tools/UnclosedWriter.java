/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: UnclosedWriter.java@author: karlatemp@vip.qq.com: 2020/1/26 下午1:12@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

/**
 * A writer removed close()
 *
 * @since 2.12
 */
public class UnclosedWriter extends Writer {
    private final Writer writer;

    public UnclosedWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void write(int c) throws IOException {
        writer.write(c);
    }

    @Override
    public void write(@NotNull char[] cbuf) throws IOException {
        writer.write(cbuf);
    }

    @Override
    public void write(@NotNull char[] cbuf, int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }

    @Override
    public void write(@NotNull String str) throws IOException {
        writer.write(str);
    }

    @Override
    public void write(@NotNull String str, int off, int len) throws IOException {
        writer.write(str, off, len);
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        return writer.append(csq);
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return writer.append(csq, start, end);
    }

    @Override
    public Writer append(char c) throws IOException {
        return writer.append(c);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
    }
}
