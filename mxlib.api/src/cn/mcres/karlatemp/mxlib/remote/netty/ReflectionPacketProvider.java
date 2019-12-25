/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ReflectionPacketProvider.java@author: karlatemp@vip.qq.com: 19-11-28 下午11:33@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.remote.netty;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.tools.Unsafe;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ReflectionPacketProvider implements PacketProvider {
    public static ReflectionPacketProvider ofClass(Class<?> type) {
        return new ReflectionPacketProvider(type.asSubclass(Packet.class));
    }

    private final Class<? extends Packet> type;
    private final List<Field> context_fields = new ArrayList<>();

    public ReflectionPacketProvider(Class<?> type) {
        if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
            throw new ClassCastException(type + " was cannot instanced.");
        }
        this.type = type.asSubclass(Packet.class);
        Class<?> matching = type;
        do {
            try {
                final Field field = matching.getDeclaredField("context");
                if (Modifier.isStatic(field.getModifiers()))
                    continue;
                Class<?> typ = field.getType();
                if (typ.isAssignableFrom(ChannelHandlerContext.class)) {
                    context_fields.add(field);
                }
            } catch (NoSuchFieldException ignore) {
            }
        } while ((matching = matching.getSuperclass()) != null);
    }

    @Override
    public Packet<?> read(PacketDataSerializer serializer, ChannelHandlerContext context) {
        try {
            Packet<?> packet = (Packet<?>) Unsafe.getUnsafe().allocateInstance(type);
            for (Field f : context_fields) {
                Toolkit.Reflection.setObjectValue(packet, f, context);
            }
            packet.read(serializer);
            return packet;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean doAccept(Packet<?> check) {
        return type.isInstance(check);
    }

    @Override
    public void write(PacketDataSerializer serializer, Packet<?> packet, ChannelHandlerContext context) {
        type.cast(packet);
        packet.write(serializer);
    }
}
