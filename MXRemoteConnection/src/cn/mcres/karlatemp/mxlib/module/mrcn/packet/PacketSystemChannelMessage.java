/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketSystemChannelMessage.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:38@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;

public class PacketSystemChannelMessage implements Packet<PacketSystemChannelMessage> {
    public byte[] message;

    @Override
    public void read(PacketDataSerializer serializer) {
        message = new byte[serializer.readableBytes()];
        serializer.readBytes(message);
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeBytes(message);
    }
}
