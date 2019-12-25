/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NettyPacketDecoder.java@author: karlatemp@vip.qq.com: 19-11-29 下午5:01@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.remote.netty;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class NettyPacketDecoder extends ByteToMessageDecoder {
    private final PacketProvider provider;

    public NettyPacketDecoder(PacketProvider provider) {
        this.provider = provider;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(provider.read(PacketDataSerializer.fromByteBuf(in), ctx));
    }
}
