/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketHandshakingInSetProtocol.java@author: karlatemp@vip.qq.com: 19-11-29 下午9:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;

public class PacketHandshakingInSetProtocol extends PacketToServer<PacketHandshakingInSetProtocol> {
    /**
     * See <a href="https://wiki.vg/Protocol_version_numbers">protocol version numbers</a> (currently 498 in Minecraft 1.14.4)
     */
    public int ProtocolVersion = -1;
    public String ServerAddress;
    /**
     * Default is 25565. The Notchian server does not use this information.
     */
    public int ServerPort = 25565;
    /**
     * 1 for status, 2 for login.
     */
    public int NextState;

    @Override
    public void read(PacketDataSerializer serializer) {
        ProtocolVersion = serializer.readVarInt();
        ServerAddress = serializer.readString(255);
        ServerPort = serializer.readUnsignedShort();
        NextState = serializer.readVarInt();
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeVarInt(ProtocolVersion);
        serializer.writeString(ServerAddress, 255);
        serializer.writeShort(ServerPort);
        serializer.writeVarInt(NextState);
    }
}
