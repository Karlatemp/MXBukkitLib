/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: UnsafePacket.java@author: karlatemp@vip.qq.com: 19-11-10 下午2:48@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.packet;

/**
 * If you use this interface.
 * You need to manually write to the PacketId
 */
public interface UnsafePacket extends CustomPacket, UnsafeRawPacket {
    @Override
    default int getPacketId() {
        return 0;
    }
}
