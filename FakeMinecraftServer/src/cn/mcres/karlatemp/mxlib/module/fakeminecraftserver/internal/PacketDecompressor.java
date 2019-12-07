/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketDecompressor.java@author: karlatemp@vip.qq.com: 19-12-7 下午2:43@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.internal;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

import java.util.List;
import java.util.zip.Inflater;

public class PacketDecompressor extends ByteToMessageDecoder {

    private final Inflater a;
    private int b;

    public PacketDecompressor(int i) {
        this.b = i;
        this.a = new Inflater();
    }

    protected void decode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, List<Object> list) throws Exception {
        if (bytebuf.readableBytes() != 0) {
            PacketDataSerializer packetdataserializer = PacketDataSerializer.fromByteBuf(bytebuf);
            int i = packetdataserializer.readVarInt();

            if (i == 0) {
                list.add(packetdataserializer.readBytes(packetdataserializer.readableBytes()));
            } else {
                if (i < this.b) {
                    throw new DecoderException("Badly compressed packet - size of " + i + " is below server threshold of " + this.b);
                }

                if (i > 2097152) {
                    throw new DecoderException("Badly compressed packet - size of " + i + " is larger than protocol maximum of " + 2097152);
                }

                byte[] abyte = new byte[packetdataserializer.readableBytes()];

                packetdataserializer.readBytes(abyte);
                this.a.setInput(abyte);
                byte[] abyte1 = new byte[i];

                this.a.inflate(abyte1);
                list.add(Unpooled.wrappedBuffer(abyte1));
                this.a.reset();
            }

        }
    }

    public void a(int i) {
        this.b = i;
    }
}
