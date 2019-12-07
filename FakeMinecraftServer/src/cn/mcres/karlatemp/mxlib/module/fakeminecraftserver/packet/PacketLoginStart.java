/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketLoginStart.java@author: karlatemp@vip.qq.com: 19-12-7 下午1:51@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;

public class PacketLoginStart extends PacketToServer<PacketLoginStart> {
    public String player_name;

    @Override
    public void read(PacketDataSerializer serializer) {
        player_name = serializer.readString(16);
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeString(player_name);
    }
}
