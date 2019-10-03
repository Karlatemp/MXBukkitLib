/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ByteBufferOutputStream.java@author: karlatemp@vip.qq.com: 19-9-29 下午1:00@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * OutputStream with ByteBuffer
 *
 * @since 2.3
 */
public class ByteBufferOutputStream extends OutputStream {
    protected final ByteBuffer buffer;

    /**
     * Create OutputStream as buffer.
     *
     * @param buffer The buffer using
     * @throws IllegalArgumentException If buffer is read only.
     */
    public ByteBufferOutputStream(@NotNull ByteBuffer buffer) {
        if (buffer.isReadOnly()) {
            throw new IllegalArgumentException("ByteBuffer is read only.");
        }
        this.buffer = buffer;
    }

    @Override
    public void write(int b) throws IOException {
        if (!buffer.hasRemaining())
            onFull();
        if (buffer.hasRemaining())
            buffer.put((byte) b);
        else throw new IOException("ByteBuffer is no more remaining.");
    }

    @Override
    public void write(@NotNull byte[] b, int off, int len) throws IOException {
        if (off < 0 || len < 0 || (off + len) > b.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (len > buffer.remaining()) {
            int rem = buffer.remaining();
            buffer.put(b, off, rem);
            onFull();
            int size = len - rem;
            if (size > buffer.remaining())
                throw new IOException("ByteBuffer only " + buffer.remaining() + " remaining but " + size + " need of space.");
            else buffer.put(b, off + rem, size);
            return;
        }
        buffer.put(b, off, len);
    }

    /**
     * When buffer is full but need write data then will call it.
     * <p>
     * Warning: The {@link #flush()} operation will also execute this method unless you override the flush method
     */
    @SuppressWarnings("WeakerAccess")
    protected void onFull() {
    }

    @Override
    public void flush() {
        onFull();
    }
}
