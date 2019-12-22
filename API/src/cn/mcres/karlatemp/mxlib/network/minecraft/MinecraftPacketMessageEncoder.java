/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MinecraftPacketMessageEncoder.java@author: karlatemp@vip.qq.com: 19-12-22 下午2:59@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.network.minecraft;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Packet encoder for Minecraft.
 *
 * @since 2.9
 */
public class MinecraftPacketMessageEncoder extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        int bytes = msg.readableBytes();
        int int_size = PacketDataSerializer.$unknown_a(bytes);

        if (int_size > 3) {
            throw new IllegalArgumentException("unable to fit " + int_size + " into " + 3);
        } else {
            PacketDataSerializer packetdataserializer = PacketDataSerializer.fromByteBuf(out);
            packetdataserializer.ensureWritable(bytes + int_size);
            packetdataserializer.writeVarInt(bytes);
            packetdataserializer.writeBytes(msg, msg.readerIndex(), bytes);
        }
    }
}
