/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketPrepender.java@author: karlatemp@vip.qq.com: 19-12-7 上午11:33@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.internal;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketPrepender extends MessageToByteEncoder<ByteBuf> {
    protected void encode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, ByteBuf bytebuf1) throws Exception {
        int i = bytebuf.readableBytes();
        int j = PacketDataSerializer.$unknown_a(i);

        if (j > 3) {
            throw new IllegalArgumentException("unable to fit " + i + " into " + 3);
        } else {
            PacketDataSerializer packetdataserializer = PacketDataSerializer.fromByteBuf(bytebuf1);

            packetdataserializer.ensureWritable(j + i);
            packetdataserializer.writeVarInt(i);
            packetdataserializer.writeBytes(bytebuf, bytebuf.readerIndex(), i);
        }
    }
}
