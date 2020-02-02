/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: WrappedFileWriter.java@author: karlatemp@vip.qq.com: 2020/1/11 上午12:09@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class WrappedFileWriter extends FileWriter {
    private final Writer writer;

    public WrappedFileWriter(@NotNull Writer writer) {
        super(new FileDescriptor());
        this.writer = writer;
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return writer.append(csq, start, end);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        return writer.append(csq);
    }

    @Override
    public Writer append(char c) throws IOException {
        return writer.append(c);
    }

    @Override
    public void write(@NotNull char[] cbuf, int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }

    @Override
    public void write(@NotNull String str, int off, int len) throws IOException {
        writer.write(str, off, len);
    }

    @Override
    public void write(@NotNull char[] cbuf) throws IOException {
        writer.write(cbuf);
    }

    @Override
    public void write(@NotNull String str) throws IOException {
        writer.write(str);
    }

    @Override
    public void write(int c) throws IOException {
        writer.write(c);
    }
}
