/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NetURIConnectFailedEvent.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.event;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.URI;
import org.bukkit.event.HandlerList;

/**
 *
 * @author 32798
 */
public class NetURIConnectFailedEvent extends NetEvent {

    private static final HandlerList list = new HandlerList();

    public static HandlerList getHandlerList() {
        return list;
    }

    private final IOException ioe;
    private final SocketAddress sa;

    public NetURIConnectFailedEvent(URI uri, SocketAddress sa, IOException ioe) {
        super(uri);
        this.sa = sa;
        this.ioe = ioe;
    }

    public SocketAddress getAddress() {
        return sa;
    }

    public IOException getCause() {
        return ioe;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

}
