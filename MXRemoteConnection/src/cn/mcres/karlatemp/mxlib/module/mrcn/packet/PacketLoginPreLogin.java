/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketLoginPreLogin.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:21@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;

public class PacketLoginPreLogin implements Packet<PacketLoginPreLogin> {
    public String passwd;
    public int port;
    public String host;

    @Override
    public void read(PacketDataSerializer serializer) {
        host = serializer.readString();
        port = serializer.readUnsignedShort();
        passwd = serializer.readString();
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeString(host);
        serializer.writeShort(port);
        serializer.writeString(passwd);
    }
}
