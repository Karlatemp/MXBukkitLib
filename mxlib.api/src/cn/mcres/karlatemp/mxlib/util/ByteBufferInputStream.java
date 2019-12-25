/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ByteBufferInputStream.java@author: karlatemp@vip.qq.com: 19-9-29 下午12:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * InputStream with ByteBuffer.
 *
 * @since 2.3
 */
public class ByteBufferInputStream extends InputStream {
    private final ByteBuffer buffer;

    public ByteBufferInputStream(@NotNull ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read() {
        if (buffer.hasRemaining()) return Byte.toUnsignedInt(buffer.get());
        return -1;
    }

    @Override
    public int read(@NotNull byte[] b) {
        return read(b, 0, b.length);
    }

    @Override
    public int read(@NotNull byte[] b, int off, int len) {
        if (buffer.hasRemaining()) {
            if (len < 0 || off < 0 || (len + off) > b.length) {
                throw new ArrayIndexOutOfBoundsException();
            }
            len = Math.min(buffer.remaining(), len);
            buffer.get(b, off, len);
            return len;
        }
        return -1;
    }

    @Override
    public int available() throws IOException {
        return buffer.remaining();
    }

}
