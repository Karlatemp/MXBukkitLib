/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.event;

import org.bukkit.event.HandlerList;
import java.util.logging.*;

/**
 *
 * @author 32798
 */
public abstract class LoggerEvent extends org.bukkit.event.Event {

    private static final HandlerList list = new HandlerList();

    public static HandlerList getHandlerList() {
        return list;
    }
    private final Handler handler;
    @SuppressWarnings("NonConstantLogger")
    private final Logger logger;

    public LoggerEvent(Logger logger, Handler handler) {
        this.logger = logger;
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    public Logger getLogger() {
        return logger;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }
}
