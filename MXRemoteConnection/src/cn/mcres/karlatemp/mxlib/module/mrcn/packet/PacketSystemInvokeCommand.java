/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketSystemInvokeCommand.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:36@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;

public class PacketSystemInvokeCommand implements Packet<PacketSystemInvokeCommand> {
    public String command;

    public PacketSystemInvokeCommand(String command) {
        this.command = command;
    }

    @Override
    public void read(PacketDataSerializer serializer) {
        command = serializer.readString();
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeString(command);
    }
}
