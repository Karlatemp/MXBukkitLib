/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: SerializeSystem.java@author: karlatemp@vip.qq.com: 2020/1/23 下午3:02@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.packet;

import cn.mcres.karlatemp.mxlib.common.packet.internal.MPacketSerializer;
import cn.mcres.karlatemp.mxlib.common.packet.internal.PArraySerializer;
import cn.mcres.karlatemp.mxlib.common.packet.internal.StringSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SerializeSystem {
    private final Deque<MessageSerializer> serializers = new ConcurrentLinkedDeque<>();

    public SerializeSystem() {
        register(new PArraySerializer()).register(new StringSerializer()).register(new MPacketSerializer());
    }

    public SerializeSystem register(@NotNull MessageSerializer serializer) {
        serializers.addFirst(serializer);
        return this;
    }

    public MessageSerializer getSerializer(Object object) {
        for (MessageSerializer serializer : serializers) {
            if (serializer.support(object)) return serializer;
        }
        throw new UnsupportedOperationException("No serializer for " + object);
    }

    public MessageSerializer getSerializerByType(Class<?> type) {
        for (MessageSerializer serializer : serializers) {
            if (serializer.supportType(type)) return serializer;
        }
        throw new UnsupportedOperationException("No serializer for " + type);
    }
}
