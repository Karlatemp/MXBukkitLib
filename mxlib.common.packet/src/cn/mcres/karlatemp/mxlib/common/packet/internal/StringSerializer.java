/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: StringSerializer.java@author: karlatemp@vip.qq.com: 2020/1/23 下午3:11@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.packet.internal;

import cn.mcres.karlatemp.mxlib.common.packet.MessageSerializer;
import cn.mcres.karlatemp.mxlib.common.packet.SerializeInput;
import cn.mcres.karlatemp.mxlib.common.packet.SerializeOutput;

import java.io.IOException;

public class StringSerializer implements MessageSerializer {
    @Override
    public boolean supportType(Class<?> type) {
        return type == String.class;
    }

    @Override
    public void serialize(Object object, SerializeOutput output) throws IOException {
        String s = (String) object;
        if (s == null) {
            output.getOutput().writeBoolean(false);
        } else {
            output.getOutput().writeBoolean(true);
            output.getOutput().writeUTF(s);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(Class<T> type, SerializeInput input) throws IOException {
        if (input.getInput().readBoolean()) {
            return (T) input.getInput().readUTF();
        }
        return null;
    }
}
