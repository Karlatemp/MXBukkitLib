/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MessageSerializer.java@author: karlatemp@vip.qq.com: 2020/1/23 下午3:00@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.packet;

import java.io.IOException;

public interface MessageSerializer {
    default boolean support(Object object) {
        if (object == null) return supportType(void.class);
        return supportType(object.getClass());
    }

    boolean supportType(Class<?> type);

    void serialize(Object object, SerializeOutput output) throws IOException;

    <T> T deserialize(Class<T> type, SerializeInput input) throws IOException;
}
