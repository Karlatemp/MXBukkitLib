/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ServerAuthSuccessEvent.java@author: karlatemp@vip.qq.com: 19-12-7 下午6:21@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.event;

import cn.mcres.karlatemp.mxlib.event.Event;
import cn.mcres.karlatemp.mxlib.event.HandlerList;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.UUID;

public class ServerAuthSuccessEvent extends Event {
    public static final HandlerList<ServerAuthSuccessEvent> handlers = new HandlerList<>();
    private final String username;
    private final UUID uuid;
    private BaseComponent disconnectMessage;

    public BaseComponent getDisconnectMessage() {
        return disconnectMessage;
    }

    public void setDisconnectMessage(BaseComponent disconnectMessage) {
        this.disconnectMessage = disconnectMessage;
    }

    public ServerAuthSuccessEvent(String username, UUID uid) {
        this.username = username;
        this.uuid = uid;
    }

    @Override
    public HandlerList<ServerAuthSuccessEvent> getHandlerList() {
        return handlers;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUniqueId() {
        return uuid;
    }
}
