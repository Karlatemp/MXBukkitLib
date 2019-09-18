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

import java.net.URI;

public abstract class NetEvent extends org.bukkit.event.Event {

    protected final URI uri;

    protected NetEvent(URI uri) {
        this.uri = uri;
    }

    @SuppressWarnings("FinalMethod")
    public final URI getURI() {
        return uri;
    }
}
