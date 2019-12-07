/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketCompressor.java@author: karlatemp@vip.qq.com: 19-12-7 下午2:41@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.internal;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.zip.Deflater;

public class PacketCompressor extends MessageToByteEncoder<ByteBuf> {

    private final byte[] a = new byte[8192];
    private final Deflater b;
    private int c;

    public PacketCompressor(int i) {
        this.c = i;
        this.b = new Deflater();
    }

    protected void encode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, ByteBuf bytebuf1) throws Exception {
        int i = bytebuf.readableBytes();
        PacketDataSerializer packetdataserializer = PacketDataSerializer.fromByteBuf(bytebuf1);

        if (i < this.c) {
            packetdataserializer.writeVarInt(0);
            packetdataserializer.writeBytes(bytebuf);
        } else {
            byte[] abyte = new byte[i];

            bytebuf.readBytes(abyte);
            packetdataserializer.writeVarInt(abyte.length);
            this.b.setInput(abyte, 0, i);
            this.b.finish();

            while (!this.b.finished()) {
                int j = this.b.deflate(this.a);

                packetdataserializer.writeBytes(this.a, 0, j);
            }

            this.b.reset();
        }

    }

    public void a(int i) {
        this.c = i;
    }
}
