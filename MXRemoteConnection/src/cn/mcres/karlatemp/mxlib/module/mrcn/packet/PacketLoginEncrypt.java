/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketLoginEncrypt.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:24@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;

public class PacketLoginEncrypt implements Packet<PacketLoginEncrypt> {
    public byte[] rsa_public;

    @Override
    public void read(PacketDataSerializer serializer) {
        rsa_public = serializer.readByteArray();
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeByteArray(rsa_public);
    }
}
