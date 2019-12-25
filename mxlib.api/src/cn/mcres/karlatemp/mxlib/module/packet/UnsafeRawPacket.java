/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: UnsafeRawPacket.java@author: karlatemp@vip.qq.com: 19-11-17 上午12:13@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.packet;

public interface UnsafeRawPacket extends RawPacket {
    @Override
    default int getPacketId() {
        return 0;
    }
}
