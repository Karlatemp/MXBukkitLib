
/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: EmptyStream.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.io;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link cn.mcres.karlatemp.mxlib.tools.EmptyStream}
 */
@Deprecated
public class EmptyStream {
    public static final EmptyStream stream = new EmptyStream();

    private final EmptyInputStream i;
    private final EmptyOutputStream o;

    private EmptyStream() {
        this.i = new EmptyInputStream();
        this.o = new EmptyOutputStream();
    }

    public EmptyInputStream asInputStream() {
        return i;
    }

    public EmptyOutputStream asOutputStream() {
        return o;
    }

    public static final class EmptyInputStream extends InputStream {

        @Override
        public int read() {
            return -1;
        }

        @Override
        public int read(byte[] b) {
            return -1;
        }

        @Override
        public int read(byte[] b, int off, int len) {
            return -1;
        }

    }

    public static class EmptyOutputStream extends OutputStream {

        public EmptyOutputStream() {
        }

        @Override
        public void write(int b) {
        }

        @Override
        public void write(byte[] b) {
        }

        @Override
        public void write(byte[] b, int off, int len) {
        }

    }
}
