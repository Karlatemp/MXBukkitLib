/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketSystemMessages.java@author: karlatemp@vip.qq.com: 19-12-17 下午11:43@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;

public class PacketSystemMessages implements Packet<PacketSystemMessages> {
    public String[] messages;

    public PacketSystemMessages(String... messages) {
        this.messages = messages;
    }

    @Override
    public void read(PacketDataSerializer serializer) {
        serializer.writeVarInt(messages.length);
        for (String msg : messages) serializer.writeString(msg);
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        messages = new String[serializer.readVarInt()];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = serializer.readString();
        }
    }
}
