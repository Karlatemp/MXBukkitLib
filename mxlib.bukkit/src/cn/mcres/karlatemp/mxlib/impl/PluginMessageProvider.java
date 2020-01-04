/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PluginMessageProvider.java@author: karlatemp@vip.qq.com: 19-11-16 下午7:03@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl;

import cn.mcres.karlatemp.mxlib.event.bukkit.BukkitPluginMessageSendEvent;
import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.bukkit.entity.Player;

public class PluginMessageProvider extends MessageToByteEncoder<ByteBuf> {
    private final Player owner;
    private static int PID = BukkitToolkit.getPacketId("PacketPlayOutCustomPayload");

    public PluginMessageProvider(Player owner) {
        this.owner = owner;
    }

    @Override
    public boolean acceptOutboundMessage(Object o) throws Exception {
        return o instanceof ByteBuf;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf input, ByteBuf output) throws Exception {
        PacketDataSerializer ip = PacketDataSerializer.fromByteBuf(input);
        PacketDataSerializer op = PacketDataSerializer.fromByteBuf(output);
        int id = ip.readVarInt();
        if (id == PID) {
            op.writeVarInt(id);
            String key = ip.readString(32767);
            int len = ip.readableBytes();
            BukkitPluginMessageSendEvent event = new BukkitPluginMessageSendEvent(owner, key, PacketDataSerializer.fromByteBuf(ip.readBytes(len)));
            event.post();
            op.writeString(event.getKey());
            op.writeBytes(event.getBuf());
        } else {
            op.writeVarInt(id);
            op.writeBytes(input);
        }
    }
}
