/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketSystemDisconnect.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:43@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;

public class PacketSystemDisconnect implements Packet<PacketSystemDisconnect> {
    public String reason;

    public PacketSystemDisconnect(String reason) {
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
