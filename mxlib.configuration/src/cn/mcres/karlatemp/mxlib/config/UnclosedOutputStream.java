/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: UnclosedOutputStream.java@author: karlatemp@vip.qq.com: 19-11-10 下午3:59@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.config;

import org.jetbrains.annotations.NotNull;

import java.io.FilterOutputStream;
import java.io.OutputStream;

public class UnclosedOutputStream extends FilterOutputStream {
    /**
     * Creates an output stream filter built on top of the specified
     * underlying output stream.
     *
     * @param out the underlying output stream to be assigned to
     *            the field {@code this.out} for later use, or
     *            <code>null</code> if this instance is to be
     *            created without an underlying stream.
     */
    public UnclosedOutputStream(@NotNull OutputStream out) {
        super(out);
    }

    @Override
    public void close(){
    }
}
