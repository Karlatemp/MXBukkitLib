/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CustomPayloadPacket.java@author: karlatemp@vip.qq.com: 19-11-16 下午6:31@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.packet;

import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Objects;

public class CustomPayloadPacket implements CustomPacket {
    private String channel;
    private ByteBuf message;

    public static CustomPayloadPacket newPluginMessagePacket(@NotNull String channel, @NotNull byte[] message) {
        return new CustomPayloadPacket(channel, Unpooled.wrappedBuffer(message));
    }

    public static CustomPayloadPacket newPluginMessagePacket(@NotNull String channel, @NotNull ByteBuf message) {
        return new CustomPayloadPacket(channel, message);
    }

    public static CustomPayloadPacket newPluginMessagePacket(@NotNull String channel, @NotNull ByteBuffer message) {
        return new CustomPayloadPacket(channel, Unpooled.wrappedBuffer(message));
    }

    public CustomPayloadPacket(@NotNull String channel, @NotNull ByteBuf buf) {
        channel = BukkitToolkit.validateAndCorrectChannel(channel);
        this.channel = BukkitToolkit.toMinecraftKey(channel).toString();
        this.message = buf;
    }

    @Override
    public int getPacketId() {
        return 0x18;
    }

    @Override
    public void writeData(PacketDataSerializer serializer) {
        serializer.writeString(channel);
        serializer.writeBytes(message);
    }
}
