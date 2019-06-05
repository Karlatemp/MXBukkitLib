/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.log;

import java.util.logging.LogRecord;
import java.util.logging.Handler;

/**
 *
 * @author 32798
 */
public class LoggerHandler extends Handler {

    private final BasicLogger lgr;

    public LoggerHandler(BasicLogger lgr) {
        this.lgr = lgr;
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        return true;
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
}
