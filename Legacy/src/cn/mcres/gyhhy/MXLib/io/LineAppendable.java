/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LineAppendable.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:13@version: 2.0
 */
package cn.mcres.gyhhy.MXLib.io;

import java.io.IOError;
import java.io.IOException;

/**
 * @author Karlatemp
 */
public class LineAppendable implements LineWritable {

    private final Appendable app;

    public LineAppendable(Appendable app) {
        this.app = app;
    }

    @Override
    public void println(String line) {
        try {
            app.append(line).append('\n');
        } catch (IOException ex) {
            catch_(ex);
        }
    }

    protected void catch_(IOException io) {
        throw new IOError(io);
    }
}
