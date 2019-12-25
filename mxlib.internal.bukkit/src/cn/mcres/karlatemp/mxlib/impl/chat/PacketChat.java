/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketChat.java@author: karlatemp@vip.qq.com: 2019/12/24 下午11:15@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl.chat;

import cn.mcres.karlatemp.mxlib.module.chat.BungeeChatAPI;
import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.module.packet.RawPacket;
import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import org.bukkit.entity.Player;

public class PacketChat implements BungeeChatAPI {
    public static final int ID = BukkitToolkit.getPacketId("PacketPlayOutChat");

    @Override
    public void send(Player player, BaseComponent bc) {
        new ChatPacket(ComponentSerializer.toString(bc)).send(player);
    }

    @Override
    public void send(Player player, BaseComponent... bcs) {
        new ChatPacket(ComponentSerializer.toString(bcs)).send(player);
    }

    public static class ChatPacket implements RawPacket {
        private final String msg;

        ChatPacket(String msg) {
            this.msg = msg;
        }

        @Override
        public int getPacketId() {
            return ID;
        }

        @Override
        public void writeData(PacketDataSerializer serializer) {
            serializer.writeString(msg);
            serializer.writeByte(0);
        }
    }

}
