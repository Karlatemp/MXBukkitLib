/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketSystemMessage.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:37@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;

public class PacketSystemMessage implements Packet<PacketSystemMessage> {
    public String message;

    public PacketSystemMessage(String message) {
        this.message = message;
    }

    @Override
    public void read(PacketDataSerializer serializer) {
        message = serializer.readString();
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeString(message);
    }
}
