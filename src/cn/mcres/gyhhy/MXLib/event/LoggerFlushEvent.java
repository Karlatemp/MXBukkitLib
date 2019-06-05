/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.event;

import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 *
 * @author 32798
 */
public class LoggerFlushEvent extends LoggerEvent{
    
    public LoggerFlushEvent(Logger logger, Handler handler) {
        super(logger, handler);
    }
    
}
