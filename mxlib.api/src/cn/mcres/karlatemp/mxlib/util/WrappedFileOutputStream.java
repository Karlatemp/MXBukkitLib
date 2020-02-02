/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: WrappedFileOutputStream.java@author: karlatemp@vip.qq.com: 2020/1/11 上午12:06@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class WrappedFileOutputStream extends FileOutputStream {
    private final OutputStream out;

    public WrappedFileOutputStream(@NotNull OutputStream out) {
        super(new FileDescriptor());
        this.out = out;
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void write(@NotNull byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    @Override
    public void write(@NotNull byte[] b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @SuppressWarnings({"ConstantConditions", "NullableProblems"})
    @Override
    public FileChannel getChannel() {
        return null;
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
