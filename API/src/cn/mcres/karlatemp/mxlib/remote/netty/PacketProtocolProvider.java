/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketProtocolProvider.java@author: karlatemp@vip.qq.com: 19-11-29 下午6:42@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.remote.netty;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import io.netty.channel.ChannelHandlerContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class PacketProtocolProvider implements PacketProvider {
    protected final Map<PacketProtocol, PacketProvider> protocolMap = initProtocolMap();

    @NotNull
    protected Map<PacketProtocol, PacketProvider> initProtocolMap() {
        return new ConcurrentHashMap<>();
    }

    protected PacketProtocol protocol;

    public PacketProtocol getProtocol(@Nullable ChannelHandlerContext context) {
        return protocol;
    }

    public void setProtocol(@Nullable ChannelHandlerContext context, @NotNull PacketProtocol protocol) {
        this.protocol = protocol;
    }

    public PacketProtocolProvider register(@NotNull PacketProtocol protocol, @NotNull PacketProvider provider) {
        protocolMap.putIfAbsent(protocol, provider);
        return this;
    }

    @Override
    public Packet<?> read(PacketDataSerializer serializer, ChannelHandlerContext context) throws PacketProviderException {
        return a(getProtocol(context)).read(serializer, context);
    }

    protected PacketProvider a(PacketProtocol protocol) {
        if (protocol == null)
            throw new PacketProviderException("No protocol selected.");
        final PacketProvider provider = protocolMap.get(protocol);
        if (provider == null)
            throw new PacketProviderException("No provider for protocol:" + protocol);
        return provider;
    }

    protected PacketProvider findProvider(Packet<?> support, ChannelHandlerContext context) throws PacketProviderException {
        return findProvider(support, context, true, true);
    }

    protected PacketProvider findProvider(
            Packet<?> support, ChannelHandlerContext context,
            boolean override, boolean error) throws PacketProviderException {
        if (!override) {
            PacketProvider p = protocolMap.get(getProtocol(context));
            if (p.doAccept(support))
                return p;
        } else
            for (Map.Entry<PacketProtocol, PacketProvider> entry : protocolMap.entrySet()) {
                final PacketProvider provider = entry.getValue();
                if (provider.doAccept(support)) {
                    setProtocol(context, entry.getKey());
                    return provider;
                }
            }
        if (!error) return null;
        throw new PacketProviderException("No provider for packet " + support);
    }

    @Override
    public void write(PacketDataSerializer serializer, Packet<?> packet, ChannelHandlerContext context) throws PacketProviderException {
        findProvider(packet, context).write(serializer, packet, context);
    }

    @Override
    public boolean doAccept(Packet<?> check) {
        final PacketProtocol protocol = getProtocol(null);
        if (protocol == null) return false;
        final PacketProvider provider = protocolMap.get(protocol);
        if (provider == null) return false;
        return provider.doAccept(check);
    }
}
