/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/PacketProviderLink.java
 */

package cn.mcres.karlatemp.mxlib.remote.netty;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import io.netty.channel.ChannelHandlerContext;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PacketProviderLink implements PacketProvider {
    private final Map<Short, PacketProvider> providers = new ConcurrentHashMap<>();

    public PacketProviderLink register(short id, @NotNull PacketProvider provider, boolean force) {
        if (force) {
            providers.put(id, provider);
        } else {
            providers.putIfAbsent(id, provider);
        }
        return this;
    }

    public PacketProviderLink register(short id, @NotNull PacketProvider provider) {
        return register(id, provider, false);
    }

    public PacketProviderLink register(@NotNull PacketProvider provider) {
        return register((short) providers.size(), provider, false);
    }

    public <T extends Packet<T>> PacketProviderLink register(short id, @NotNull Class<T> provider, boolean force) {
        return register(id, ReflectionPacketProvider.ofClass(provider), force);
    }

    public <T extends Packet<T>> PacketProviderLink register(short id, @NotNull Class<T> provider) {
        return register(id, ReflectionPacketProvider.ofClass(provider));
    }

    public <T extends Packet<T>> PacketProviderLink register(@NotNull Class<T> provider) {
        return register(ReflectionPacketProvider.ofClass(provider));
    }

    public <T extends Packet<T>> PacketProviderLink register(short id, @NotNull T provider, boolean force) {
        return register(id, ReflectionPacketProvider.ofClass(provider.getClass()), force);
    }

    public <T extends Packet<T>> PacketProviderLink register(short id, @NotNull T provider) {
        return register(id, ReflectionPacketProvider.ofClass(provider.getClass()));
    }

    public <T extends Packet<T>> PacketProviderLink register(@NotNull T provider) {
        return register(ReflectionPacketProvider.ofClass(provider.getClass()));
    }

    @Override
    public boolean doAccept(Packet<?> check) {
        if (check != null) {
            for (PacketProvider p : providers.values()) {
                if (p.doAccept(check))
                    return true;
            }
        }
        return false;
    }

    @Override
    public Packet<?> read(PacketDataSerializer serializer, ChannelHandlerContext context) throws PacketProviderException {
        short id = readPacketId(serializer);
        PacketProvider provider = providers.get(id);
        if (provider == null) {
            throw new PacketProviderException("No provider with id:" + id + "(0x" + Integer.toHexString(Short.toUnsignedInt(id)) + ")");
        }
        return provider.read(serializer, context);
    }

    protected short readPacketId(PacketDataSerializer serializer) throws PacketProviderException {
        return serializer.readShort();
    }

    protected void writePacketId(PacketDataSerializer serializer, short id) throws PacketProviderException {
        serializer.writeShort(id);
    }

    @Override
    public void write(PacketDataSerializer serializer, Packet<?> packet, ChannelHandlerContext context) throws PacketProviderException {
        Map.Entry<Short, PacketProvider> provider = null;
        for (Map.Entry<Short, PacketProvider> p : providers.entrySet()) {
            if (p.getValue().doAccept(packet)) {
                provider = p;
                break;
            }
        }
        if (provider == null) {
            throw new PacketProviderException("No provider accept packet " + packet);
        }
        writePacketId(serializer, provider.getKey());
        provider.getValue().write(serializer, packet, context);
    }
}
