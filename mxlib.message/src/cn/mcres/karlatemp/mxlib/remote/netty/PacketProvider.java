/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/PacketProvider.java
 */

package cn.mcres.karlatemp.mxlib.remote.netty;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import io.netty.channel.ChannelHandlerContext;

public interface PacketProvider {
    Packet<?> read(PacketDataSerializer serializer, ChannelHandlerContext context) throws PacketProviderException;

    void write(PacketDataSerializer serializer, Packet<?> packet, ChannelHandlerContext context) throws PacketProviderException;

    default boolean doAccept(Packet<?> packet) {
        return false;
    }
}
