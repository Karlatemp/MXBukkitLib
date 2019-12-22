/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NioFileOutputStream.java@author: karlatemp@vip.qq.com: 19-12-21 下午1:59@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.sr;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class NioFileOutputStream extends OutputStream {
    private final RandomAccessFile raf;
    private boolean closed;

    @Override
    public void write(@NotNull byte[] b, int off, int len) throws IOException {
        raf.write(b, off, len);
    }

    @Override
    public void write(@NotNull byte[] b) throws IOException {
        raf.write(b);
    }

    @Override
    public void write(int b) throws IOException {
        raf.write(b);
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public synchronized void close() throws IOException {
        if (closed) return;
        closed = true;
        raf.setLength(raf.getFilePointer());
        raf.close();
    }

    public NioFileOutputStream(RandomAccessFile raf) {
        this.raf = raf;
    }
}
