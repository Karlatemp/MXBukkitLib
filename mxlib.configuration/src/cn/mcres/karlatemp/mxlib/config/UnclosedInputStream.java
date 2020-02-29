/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/29 15:57:22
 *
 * MXLib/mxlib.configuration/UnclosedInputStream.java
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
