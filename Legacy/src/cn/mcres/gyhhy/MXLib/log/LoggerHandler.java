/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LoggerHandler.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.log;

import java.io.UnsupportedEncodingException;
import java.util.logging.LogRecord;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author 32798
 */
public class LoggerHandler extends Handler {

    private final BasicLogger lgr;

    public LoggerHandler(BasicLogger lgr) {
        this.lgr = lgr;
    }

    public LoggerHandler copySettings(Handler callee) {
        if (callee == null) {
            return this;
        }
        setLevel(callee.getLevel());
        setFilter(callee.getFilter());
        setFormatter(callee.getFormatter());
        try {
            setEncoding(callee.getEncoding());
        } catch (SecurityException | UnsupportedEncodingException ex) {
        }
        setErrorManager(callee.getErrorManager());
        return this;
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        if (record == null) {
            return false;
        }
        return super.isLoggable(record);
    }

    @Override
    public void publish(LogRecord record) {
        lgr.publish(record, this);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

    public LoggerHandler copySettingsRandom(Logger log) {
        while (log != null) {
            Handler[] h = log.getHandlers();
            if (h != null && h.length != 0) {
                return copySettings(h[Math.min(h.length, (int) (Math.random() * h.length))]);
            }
            if (log.getUseParentHandlers()) {
                log = log.getParent();
            } else {
                break;
            }
        }
        return this;
    }

}
