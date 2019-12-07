/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketSplitter.java@author: karlatemp@vip.qq.com: 19-12-7 上午11:30@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.internal;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

public class PacketSplitter extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf bytebuf, List<Object> out) throws Exception {
        bytebuf.markReaderIndex();
        byte[] abyte = new byte[3];

        for (int i = 0; i < abyte.length; ++i) {
            if (!bytebuf.isReadable()) {
                bytebuf.resetReaderIndex();
                return;
            }

            abyte[i] = bytebuf.readByte();
            if (abyte[i] >= 0) {
                PacketDataSerializer packetdataserializer = PacketDataSerializer.fromByteBuf(Unpooled.wrappedBuffer(abyte));

                try {
                    int j = packetdataserializer.readVarInt();

                    if (bytebuf.readableBytes() >= j) {
                        out.add(bytebuf.readBytes(j));
                        return;
                    }

                    bytebuf.resetReaderIndex();
                } finally {
                    packetdataserializer.release();
                }

                return;
            }
        }

        throw new CorruptedFrameException("length wider than 21-bit");
    }
}
