/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PluginMessageSendEvent.java@author: karlatemp@vip.qq.com: 19-11-16 下午7:51@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester;

import cn.mcres.karlatemp.mxlib.annotations.AutoInstall;
import cn.mcres.karlatemp.mxlib.event.MXEventHandler;
import cn.mcres.karlatemp.mxlib.event.MXEventListener;
import cn.mcres.karlatemp.mxlib.event.bukkit.BukkitPluginMessageSendEvent;
import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

@AutoInstall
public class PluginMessageSendEvent extends MXEventListener {
    @MXEventHandler
    public static void onPluginMessageSend(BukkitPluginMessageSendEvent event) {
        final PacketDataSerializer buffer = event.getBuf();
        final int pos = buffer.readerIndex();
        ByteBuf copied = Unpooled.copiedBuffer(buffer);
        buffer.readerIndex(pos);
        System.out.println("P OUT:" + event.getPlayer().getName() + "@" + event.getKey() + ": " + copied.toString(StandardCharsets.UTF_8));
    }

    static {
        System.out.println("CCTTWW");
        try {
            System.out.println("CCTW " + BukkitToolkit.getPlugin());
            Bukkit.getMessenger().registerIncomingPluginChannel(BukkitToolkit.getPlugin(), "VexView", (channel, player, message) -> {
                System.out.println("P IN: " + player.getName() + "@" + channel + ": " + new String(message, StandardCharsets.UTF_8));
            });
        } catch (Throwable thr) {
            PluginTester.plugin.getLogger().log(Level.SEVERE, thr.toString(), thr);
        }
    }
}
