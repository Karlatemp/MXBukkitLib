/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: STester.java@author: karlatemp@vip.qq.com: 19-12-7 上午11:41@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.model.ServerPing;
import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.remote.netty.NettyPacketEncoder;
import cn.mcres.karlatemp.mxlib.remote.netty.PacketProtocol;
import cn.mcres.karlatemp.mxlib.remote.netty.PacketProtocolProvider;
import cn.mcres.karlatemp.mxlib.remote.netty.PacketProvider;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Arrays;
import java.util.UUID;
import java.lang.management.ManagementFactory;

public class STester {
    public static void main(String[] args) throws Throwable {
        TK.a();
//        ManagementFactory.getClassLoadingMXBean().setVerbose(true);

        final PacketProtocolProvider toClient = PacketListenerGen.getServerToClient();

        PacketStatusOutResponse response = new PacketStatusOutResponse();
        ServerPing sp = response.ping = new ServerPing();
        ServerPing.ServerVersion sv = sp.version = new ServerPing.ServerVersion();
        sv.name = "1.12.2";
        sv.protocol = 49;
        ServerPing.ServerPlayers spp = sp.players = new ServerPing.ServerPlayers();
        spp.max = 233;
        spp.online = 1;
        spp.sample = Arrays.asList(
                new ServerPing.ServerPlayers.SamplePlayer("Karlatemp", UUID.randomUUID()),
                new ServerPing.ServerPlayers.SamplePlayer("iO_5", UUID.randomUUID())
        );
        sp.motd = new TextComponent("Hello World!");


        System.out.println(toClient.doAccept(response));
        System.out.println(new NettyPacketEncoder(toClient).acceptOutboundMessage(response));
        PacketDataSerializer serializer = PacketDataSerializer.fromByteBuf(
                Unpooled.buffer(2048)
        );

        toClient.write(serializer, response, null);

        System.out.println(serializer.readVarInt());
        System.out.println(serializer.readString(25575));

        System.out.println(toClient.doAccept(response));
        toClient.write(serializer, response, null);
        System.out.println(serializer.readVarInt());
        System.out.println(serializer.readString(25575));
    }
}
