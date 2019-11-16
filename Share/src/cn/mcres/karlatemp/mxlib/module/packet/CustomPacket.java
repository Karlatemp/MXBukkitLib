/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CustomPacket.java@author: karlatemp@vip.qq.com: 19-11-10 下午2:27@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.packet;

import cn.mcres.karlatemp.mxlib.impl.PlayerNetWorkInjector;
import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.group.ChannelGroupFutureListener;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface CustomPacket {
    int getPacketId();

    void writeData(PacketDataSerializer serializer);

    default void push(@NotNull Player player) {
        Channel channel = PlayerNetWorkInjector.getChannel(player);
        if (channel.eventLoop().inEventLoop())
            channel.writeAndFlush(this).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        else {
            channel.eventLoop().execute(() ->
                    channel.writeAndFlush(this).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
            );
        }
    }

    default void push(@NotNull Player... players) {
        for (Player p : players) {
            push(p);
        }
    }

    default void push() {
        BukkitToolkit.getOnlinePlayers().forEach(this::push);
    }

    default void push(@NotNull Predicate<Player> filter) {
        for (Player p : BukkitToolkit.getOnlinePlayers()) {
            if (filter.test(p)) {
                push(p);
            }
        }
    }
}
