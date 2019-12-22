/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketLoginAuth.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:25@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;

public class PacketLoginAuth implements Packet<PacketLoginAuth> {
    public String user, display;
    public byte[] passwd;
    public boolean sudo;

    @Override
    public void read(PacketDataSerializer serializer) {
        user = serializer.readString();
        display = serializer.readString();
        passwd = serializer.readByteArray();
        sudo = serializer.readBoolean();
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeString(user);
        serializer.writeString(display == null ? user : display);
        serializer.writeByteArray(passwd);
        serializer.writeBoolean(sudo);
    }
}
