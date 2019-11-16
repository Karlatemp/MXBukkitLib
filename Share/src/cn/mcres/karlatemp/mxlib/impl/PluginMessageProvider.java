/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PluginMessageProvider.java@author: karlatemp@vip.qq.com: 19-11-16 下午7:03@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl;

import cn.mcres.karlatemp.mxlib.event.bukkit.BukkitPluginMessageSendEvent;
import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.share.MinecraftKey;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.bukkit.entity.Player;

public class PluginMessageProvider extends MessageToByteEncoder<ByteBuf> {
    private final Player owner;

    public PluginMessageProvider(Player owner) {
        this.owner = owner;
    }

    @Override
    public boolean acceptOutboundMessage(Object o) throws Exception {
        return o instanceof ByteBuf;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf input, ByteBuf output) throws Exception {
        PacketDataSerializer ip = new PacketDataSerializer(input);
        PacketDataSerializer op = new PacketDataSerializer(output);
        int id = ip.readVarInt();
        if (id == 0x18) {
            op.writeVarInt(id);
            String key = ip.readString(32767);
            int len = ip.readableBytes();
            BukkitPluginMessageSendEvent event = new BukkitPluginMessageSendEvent(owner, key, new PacketDataSerializer(ip.readBytes(len)));
            event.post();
            op.writeString(event.getKey());
            op.writeBytes(event.getBuf());
        } else {
            op.writeVarInt(id);
            op.writeBytes(input);
        }
    }
}
