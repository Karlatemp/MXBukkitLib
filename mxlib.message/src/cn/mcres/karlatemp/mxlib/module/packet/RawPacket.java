/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/RawPacket.java
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
