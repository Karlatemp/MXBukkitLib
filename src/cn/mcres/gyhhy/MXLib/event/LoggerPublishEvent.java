/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.event;

import java.util.logging.*;

/**
 *
 * @author 32798
 */
public class LoggerPublishEvent extends LoggerEvent {

    private final LogRecord lr;

    public LoggerPublishEvent(Logger logger, Handler handler, LogRecord lr) {
        super(logger, handler);
        this.lr = lr;
    }

    public LogRecord getLogRecord() {
        return lr;
    }

}
