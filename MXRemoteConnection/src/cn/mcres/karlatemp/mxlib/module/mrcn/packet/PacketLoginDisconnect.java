/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketLoginDisconnect.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:22@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;

public class PacketLoginDisconnect implements Packet<PacketLoginDisconnect> {
    public String reason = "";

    public PacketLoginDisconnect(String reason) {
        this.reason = reason;
    }

    @Override
    public void read(PacketDataSerializer serializer) {
        reason = serializer.readString();
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeString(reason);
    }
}
