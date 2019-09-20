/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NetEvent.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.event;

import org.bukkit.plugin.Plugin;

import java.net.URI;

public abstract class NetEvent extends org.bukkit.event.Event {

    protected final URI uri;
    private final Plugin plugin;

    protected NetEvent(URI uri) {
        this(uri, null);
    }

    protected NetEvent(URI uri, Plugin plugin) {
        this.uri = uri;
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    @SuppressWarnings("FinalMethod")
    public final URI getURI() {
        return uri;
    }
}
