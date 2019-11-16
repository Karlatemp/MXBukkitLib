/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Packet.java@author: karlatemp@vip.qq.com: 19-11-16 下午3:51@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester.commands;

import cn.mcres.gyhhy.MXLib.bukkit.cmd.SubCommand;
import cn.mcres.gyhhy.MXLib.bukkit.cmd.SubCommandHandle;
import cn.mcres.karlatemp.mxlib.module.packet.CustomPacket;
import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;

@SubCommand
public class Packet {
    @SubCommandHandle
    public void run(Player player, String[] args) {
        String cb = String.join(" ", args);
        player.sendMessage(cb);
        try {
            ComponentSerializer.parse(cb);
        } catch (Throwable error) {
            player.sendMessage("Error component");
            return;
        }
        new CustomPacket() {
            @Override
            public int getPacketId() {
                return 14;
            }

            @Override
            public void writeData(PacketDataSerializer serializer) {
                serializer.writeString(cb);
                serializer.writeByte(1);
            }
        }.push(player);
    }
}
