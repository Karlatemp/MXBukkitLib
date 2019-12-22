/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AutoListener.java@author: karlatemp@vip.qq.com: 19-9-27 下午5:19@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester;

import cn.mcres.karlatemp.mxlib.annotations.AutoInstall;
import cn.mcres.karlatemp.mxlib.impl.PlayerNetWorkInjector;
import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.MessageToByteEncoder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@AutoInstall
public class AutoListener implements Listener {
    RandomAccessFile raf;
    private int pid;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent je) {
        final Logger logger = PluginTester.plugin.getLogger();
        je.getPlayer().sendMessage("[MXLib] [AutoListener] Listener registered.");
        String name = je.getPlayer().getName();
        ChannelOutboundHandlerAdapter a = new MessageToByteEncoder<ByteBuf>() {
            @Override
            protected void encode(ChannelHandlerContext context, ByteBuf input, ByteBuf output) throws Exception {
                byte[] a = new byte[input.readableBytes()];
                input.readBytes(a);
                PacketDataSerializer pds = PacketDataSerializer.fromByteBuf(Unpooled.wrappedBuffer(a));
                int id = pds.readVarInt();
                if (id == pid){
                    logger.info("POST PlayerInfo: " + name + ", " + new String(a, StandardCharsets.UTF_8));
                    raf.seek(raf.length());
                    raf.writeInt(a.length);
                    raf.write(a);
                }
                // else logger.info("Packet ID: " + id);
                output.writeBytes(a);
            }
        };
        final ChannelPipeline pipeline = PlayerNetWorkInjector.getChannel(je.getPlayer()).pipeline();
        /*if (pipeline.get("compress") == null) {
            pipeline.addAfter("prepender", "mx_tester_dump", a);
        } else {
            pipeline.addAfter("compress", "mx_tester_dump", a);
        }*/
        pipeline.addBefore("encoder", "mx_tester_dump", a);
        new BukkitRunnable() {
            @Override
            public void run() {
                final Channel c = PlayerNetWorkInjector.getChannel(je.getPlayer());
                final Map<String, ChannelHandler> handlerMap = c.pipeline().toMap();
                for (Map.Entry<String, ChannelHandler> handler : handlerMap.entrySet()) {
                    logger.log(Level.INFO, "\t" + handler.getKey() + " = " + handler.getValue() + "(" + handler.getValue().getClass() + ")");
                }
            }
        }.runTaskLater(PluginTester.plugin, 20);
    }

    private void $init() throws Throwable {
        PluginTester.plugin.getLogger().info("[AutoListener] $init called.");
        File f = new File("G:\\IDEAProjects\\MXBukkitLibRebuild\\out\\packets.bin");
        f.createNewFile();
        raf = new RandomAccessFile(f, "rw");
        PluginTester.plugin.getLogger().info("PlayerInfo: " + (pid = BukkitToolkit.getPacketId("PacketPlayOutPlayerInfo")));
    }
}
