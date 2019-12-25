/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CustomPacketWriter.java@author: karlatemp@vip.qq.com: 19-11-10 下午2:26@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl;

import cn.mcres.karlatemp.mxlib.module.packet.RawPacket;
import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.module.packet.UnsafeRawPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CustomPacketWriter extends MessageToByteEncoder<RawPacket> {
    @Override
    public boolean acceptOutboundMessage(Object o) throws Exception {
        return o instanceof RawPacket;
    }

    @Override
    protected void encode(ChannelHandlerContext context, RawPacket packet, ByteBuf byteBuf) throws Exception {
        PacketDataSerializer serializer = PacketDataSerializer.fromByteBuf(byteBuf);
        if (!(packet instanceof UnsafeRawPacket))
            serializer.writeVarInt(packet.getPacketId());
        packet.writeData(serializer);
    }
}
