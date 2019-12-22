/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MinecraftPacketMessageDecoder.java@author: karlatemp@vip.qq.com: 19-12-22 下午3:02@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.network.minecraft;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

/**
 * Packet message decoder for Minecraft.
 *
 * @since 2.9
 */
public class MinecraftPacketMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        root:
        while (in.isReadable()) {
            in.markReaderIndex();
            byte[] b = new byte[3];
            for (int i = 0; i < 3; i++) {
                if (!in.isReadable()) {
                    in.resetReaderIndex();
                    return;
                }
                byte w = b[i] = in.readByte();
                if (w > 0) {
                    int size = PacketDataSerializer.fromByteBuf(Unpooled.wrappedBuffer(b)).readVarInt();
                    if (size > in.readableBytes()) {
                        in.resetReaderIndex();
                        return;
                    }
                    out.add(in.readBytes(size));
                    in.markReaderIndex();
                    continue root;
                }
            }
            throw new CorruptedFrameException("length wider than 21-bit");
        }
    }
}
