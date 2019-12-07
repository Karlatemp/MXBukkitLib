/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketLoginOutSuccess.java@author: karlatemp@vip.qq.com: 19-12-7 下午1:59@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;

public class PacketLoginOutSuccess extends PacketToClient<PacketLoginOutSuccess> {
    public String uuid, username;

    @Override
    public void read(PacketDataSerializer serializer) {
        this.uuid = serializer.readString(36);
        username = serializer.readString(16);
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeString(uuid);
        serializer.writeString(username);
    }
}
