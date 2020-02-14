/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/UnsafeRawPacket.java
 */

package cn.mcres.karlatemp.mxlib.module.packet;

public interface UnsafeRawPacket extends RawPacket {
    @Override
    default int getPacketId() {
        return 0;
    }
}
