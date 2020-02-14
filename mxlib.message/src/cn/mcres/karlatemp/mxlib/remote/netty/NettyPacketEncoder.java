/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NettyPacketEncoder.java
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
