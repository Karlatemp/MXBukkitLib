/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketStatusOutResponse.java@author: karlatemp@vip.qq.com: 19-11-29 下午11:09@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.model.ServerPing;
import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;

public class PacketStatusOutResponse extends PacketToClient<PacketStatusOutResponse> {
    public ServerPing ping;

    @Override
    public void read(PacketDataSerializer serializer) {
        ping = ServerPing.formatter.fromJson(serializer.readString(32767), ServerPing.class);
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeString(ServerPing.formatter.toJson(ping, ServerPing.class));
    }
}
