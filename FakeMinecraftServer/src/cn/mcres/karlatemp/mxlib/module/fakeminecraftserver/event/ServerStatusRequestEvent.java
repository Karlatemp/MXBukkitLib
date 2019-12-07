/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ServerStatusRequestEvent.java@author: karlatemp@vip.qq.com: 19-12-7 下午6:16@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.event;

import cn.mcres.karlatemp.mxlib.event.Event;
import cn.mcres.karlatemp.mxlib.event.HandlerList;
import cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.model.ServerPing;
import org.jetbrains.annotations.NotNull;

public class ServerStatusRequestEvent extends Event {
    private ServerPing sp;

    @NotNull
    public ServerPing getInfo() {
        return sp;
    }

    public void setInfo(@NotNull ServerPing sp) {
        this.sp = sp;
    }

    public static final HandlerList<ServerStatusRequestEvent> handlers = new HandlerList<>();

    @Override
    public HandlerList<ServerStatusRequestEvent> getHandlerList() {
        return handlers;
    }
}
