/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketStatusOutPong.java@author: karlatemp@vip.qq.com: 19-11-29 下午11:43@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;

public class PacketStatusOutPong extends PacketToClient<PacketStatusOutPong> {
    public long payload;

    @Override
    public void read(PacketDataSerializer serializer) {
        payload = serializer.readLong();
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeLong(payload);
    }
}
