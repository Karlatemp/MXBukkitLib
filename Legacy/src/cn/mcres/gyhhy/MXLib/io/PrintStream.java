/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PrintStream.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.io;

import java.io.OutputStream;
import java.util.Locale;

import cn.mcres.karlatemp.mxlib.tools.InlinePrintStream;

/**
 * @author 32798
 */
public abstract class PrintStream extends InlinePrintStream {

    protected PrintStream(OutputStream out) {
        super(out);
    }

    protected PrintStream() {
        this(cn.mcres.karlatemp.mxlib.tools.EmptyStream.stream.asOutputStream());
    }

}
