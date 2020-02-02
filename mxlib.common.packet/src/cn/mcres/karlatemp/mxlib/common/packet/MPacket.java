/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MPacket.java@author: karlatemp@vip.qq.com: 2020/1/23 下午3:37@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.packet;

import java.io.IOException;

public interface MPacket {
    void serialize(SerializeOutput output) throws IOException;

    void deserialize(SerializeInput input) throws IOException;
}
