/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Packet.java@author: karlatemp@vip.qq.com: 19-11-28 下午11:26@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.remote.netty;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;

public interface Packet<T extends Packet<T>> {
    void read(PacketDataSerializer serializer);

    void write(PacketDataSerializer serializer);

}
