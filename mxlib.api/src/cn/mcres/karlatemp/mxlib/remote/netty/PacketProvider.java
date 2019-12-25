/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketProvider.java@author: karlatemp@vip.qq.com: 19-11-29 下午6:32@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.remote.netty;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import io.netty.channel.ChannelHandlerContext;
import org.jetbrains.annotations.NotNull;

public interface PacketProvider {
    Packet<?> read(PacketDataSerializer serializer, ChannelHandlerContext context) throws PacketProviderException;

    void write(PacketDataSerializer serializer, Packet<?> packet, ChannelHandlerContext context) throws PacketProviderException;

    default boolean doAccept(Packet<?> packet) {
        return false;
    }
}
