/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MPacketSerializer.java@author: karlatemp@vip.qq.com: 2020/1/23 下午3:37@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.packet.internal;

import cn.mcres.karlatemp.mxlib.common.packet.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MPacketSerializer implements MessageSerializer {
    @Override
    public boolean supportType(Class<?> type) {
        return MPacket.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(Object object, SerializeOutput output) throws IOException {
        ((MPacket) object).serialize(output);
    }

    @Override
    public <T> T deserialize(Class<T> type, SerializeInput input) throws IOException {
        if (!supportType(type)) throw new UnsupportedEncodingException();
        T mp = UTools.allocate(type);
        ((MPacket) mp).deserialize(input);
        return mp;
    }
}
