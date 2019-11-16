/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitPluginMessageSendEvent.java@author: karlatemp@vip.qq.com: 19-11-16 下午7:07@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.event.bukkit;

import cn.mcres.karlatemp.mxlib.event.Event;
import cn.mcres.karlatemp.mxlib.event.EventHandler;
import cn.mcres.karlatemp.mxlib.event.HandlerList;
import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.share.MinecraftKey;
import io.netty.buffer.ByteBuf;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitPluginMessageSendEvent extends Event {
    public static final HandlerList<BukkitPluginMessageSendEvent> handlers = new HandlerList<>();
    @NotNull
    private final Player owner;
    @NotNull
    private String key;
    @NotNull
    private PacketDataSerializer buf;

    public BukkitPluginMessageSendEvent(
            @NotNull Player owner, @NotNull String key,
            @NotNull PacketDataSerializer buf) {
        this.owner = owner;
        this.key = key;
        this.buf = buf;
    }

    @NotNull
    public Player getPlayer() {
        return owner;
    }

    public void setBuf(@NotNull PacketDataSerializer buf) {
        this.buf = buf;
    }

    @NotNull
    public PacketDataSerializer getBuf() {
        return buf;
    }

    @NotNull
    public String getKey() {
        return key;
    }

    public void setKey(@NotNull String key) {
        this.key = key;
    }

    @Override
    public HandlerList getHandlerList() {
        return handlers;
    }
}
