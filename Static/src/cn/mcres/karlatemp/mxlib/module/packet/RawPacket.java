/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RawPacket.java@author: karlatemp@vip.qq.com: 19-11-17 上午12:12@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * The CustomPacket. Without Bukkit
 *
 * @since 2.7
 */
public interface RawPacket {
    int getPacketId();

    void writeData(PacketDataSerializer serializer);

    default void send(@NotNull Object player) throws ClassCastException {
        PacketSender.getInstance().sendPacket(this, player);
    }

    default void send(@NotNull Object... players) throws ClassCastException {
        PacketSender.getInstance().sendPacket(this, players);
    }

    default void send() {
        PacketSender.getInstance().sendPacketToAll(this);

    }

    default <T> void send(@NotNull Predicate<T> filter) {
        PacketSender.getInstance().sendPacketToAll(this, filter);
    }
}
