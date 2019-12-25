/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Event.java@author: karlatemp@vip.qq.com: 19-9-27 下午1:24@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.event;

import org.jetbrains.annotations.Contract;

/**
 * MXLib's Event System.
 *
 * @since 2.2
 */
public abstract class Event {
    public abstract HandlerList getHandlerList();

    public void post() {
        getHandlerList().post(this);
    }

    @Contract("null -> null;!null -> !null")
    public static <T extends Event> T post(T e) {
        if (e == null) return null;
        e.post();
        return e;
    }
}
