/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: UnclosedInputStream.java@author: karlatemp@vip.qq.com: 19-11-10 下午3:59@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.config;

import java.io.FilterInputStream;
import java.io.InputStream;

public class UnclosedInputStream extends FilterInputStream {
    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */
    public UnclosedInputStream(InputStream in) {
        super(in);
    }

    @Override
    public void close() {
    }
}
