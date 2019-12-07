/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NettyPacketEncoder.java@author: karlatemp@vip.qq.com: 19-11-29 下午12:22@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.remote.netty;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.jetbrains.annotations.NotNull;

public class NettyPacketEncoder extends MessageToByteEncoder<Packet<?>> {
    private final PacketProvider provider;

    public NettyPacketEncoder(@NotNull PacketProvider provider) {
        this.provider = provider;
    }

    @Override
    public boolean acceptOutboundMessage(Object o) throws Exception {
        return o instanceof Packet && provider.doAccept((Packet) o);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, @NotNull Packet<?> msg, @NotNull ByteBuf out) throws Exception {
        PacketDataSerializer serializer = PacketDataSerializer.fromByteBuf(out);
        provider.write(serializer, msg, ctx);
    }
}
