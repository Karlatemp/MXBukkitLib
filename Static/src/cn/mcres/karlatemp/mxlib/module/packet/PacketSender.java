/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketSender.java@author: karlatemp@vip.qq.com: 19-11-17 上午12:15@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.packet;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.annotations.Bean;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * The packet sender.
 *
 * @since 2.7
 */
@Bean
public abstract class PacketSender {
    public static final Predicate ALLOW_ALL = w -> true;

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> allowAll() {
        return ALLOW_ALL;
    }

    private static PacketSender instance;

    public static PacketSender getInstance() {
        return instance;
    }

    public static void setInstance(@NotNull PacketSender instance) {
        PacketSender.instance = instance;
        MXBukkitLib.getBeanManager().addBean(PacketSender.class, instance);
    }

    public abstract PacketSender sendPacket(@NotNull RawPacket packet, @NotNull Object target) throws ClassCastException;

    public PacketSender sendPacket(@NotNull RawPacket packet, @NotNull Object... targets) throws ClassCastException {
        for (Object target : targets) {
            sendPacket(packet, target);
        }
        return this;
    }

    public PacketSender sendPacketToAll(@NotNull RawPacket packet) {
        sendPacketToAll(packet, allowAll());
        return this;
    }

    public abstract <T> PacketSender sendPacketToAll(@NotNull RawPacket packet, @NotNull Predicate<T> filter);
}
