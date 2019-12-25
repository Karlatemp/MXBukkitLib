/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LimitInputStream.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.io;

import java.io.InputStream;
import java.io.IOException;

/**
 *
 * @author Karlatemp
 */
public class LimitInputStream extends InputStream {

    private long limit = 0;
    private boolean close;
    

    public LimitInputStream(long limit, InputStream io) {
        this(limit, io, true);
    }

    public LimitInputStream(long limit, InputStream io, boolean close) {

        this.limit = limit;
        this.io = io;
        this.close = close;
    }

    public boolean hasNext() {
        return limit > 0;
    }

    public long getLimit() {
        return limit;
    }

    public int read() throws IOException {
        if (hasNext() && io != null) {
            limit--;
            return io.read();
        }
        return -1;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int offset, int leng) throws IOException {
        if (!hasNext()) {
            return -1;
        }
        if (offset < 0 || leng < 0 || offset + leng > b.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int lg = 0;
        while (leng-- > 0) {
            if (!hasNext()) {
                break;
            }
            lg++;
            b[offset++] = (byte) read();
        }
        return lg;
    }

    @Override
    public int available() throws IOException {
        return (int) Math.min(limit, io.available());
    }
    private InputStream io;

    public void close() throws IOException {
        while (limit > 0) {
            read();
        }
        if (close && io != null) {
            close = false;
            io.close();
        }
        io = null;
    }

}
