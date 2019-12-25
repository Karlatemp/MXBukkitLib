/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LoggerCloseEvent.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

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
public class LoggerCloseEvent extends LoggerEvent{
    
    public LoggerCloseEvent(Logger logger, Handler handler) {
        super(logger, handler);
    }
    
}
