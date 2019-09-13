/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.event;

import java.io.IOException;
import java.net.URI;
import org.bukkit.event.HandlerList;

public class NetURIConnectEvent extends NetEvent implements C {

    private static final HandlerList list = new HandlerList();

    public static HandlerList getHandlerList() {
        return list;
    }
    private boolean c = false;

    public NetURIConnectEvent(URI uri) {
        super(uri);
    }
    IOException ioe;

    public IOException getCancelThrow() {
        return ioe;
    }

    public NetURIConnectEvent setCancelThrow(IOException ioe) {
        this.ioe = ioe;
        return this;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    @Override
    public boolean isCancelled() {
        return c;
    }

    @Override
    public void setCancelled(boolean c) {
        this.c = c;
    }

}
