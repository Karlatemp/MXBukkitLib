/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PlayerNetWorkInjector.java@author: karlatemp@vip.qq.com: 19-11-10 下午2:50@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.annotations.AutoInstall;
import cn.mcres.karlatemp.mxlib.module.packet.PacketSender;
import cn.mcres.karlatemp.mxlib.module.packet.RawPacket;
import cn.mcres.karlatemp.mxlib.reflect.Reflect;
import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import cn.mcres.karlatemp.mxlib.share.MXBukkitLibPluginStartup;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToByteEncoder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;


@AutoInstall
public class PlayerNetWorkInjector extends PacketSender implements Listener {
    private static final Map<UUID, Channel> channels = new ConcurrentHashMap<>();

    static {
        MXBukkitLib.debug(() -> {
            // MXBukkitLib.getLogger().printStackTrace(new Throwable("[PlayerNetWorkInjector Track Dump]"));
            return "[PlayerNetWorkInjector] Class loaded";
        });
    }

    {
        MXBukkitLib.debug("[PlayerNetWorkInjector] Injector init");
        MXBukkitLibPluginStartup.hooks.add((enable) -> {
            for (Player p : BukkitToolkit.getOnlinePlayers()) {
                inject(p, !enable);
            }
        });
    }

    private void $init() {
        MXBukkitLib.debug(() -> "[PlayerNetWorkInjector] $init Method called.");
        BukkitToolkit.getOnlinePlayers().forEach(player -> inject(player, false));
        setInstance(this);
    }

    public static Channel getChannel(@NotNull Player player) {
        UUID uid = player.getUniqueId();
        if (channels.containsKey(uid)) {
            return channels.get(uid);
        } else {
            Channel channel = Reflect.ofObject(player)
                    .invoke("getHandle")
                    .getField("playerConnection").got()
                    .getField("networkManager").got()
                    .getField("channel", Channel.class).get();
            channels.put(uid, channel);
            return channel;
        }
    }

    private static final CustomPacketWriter writer = new CustomPacketWriter();

    public static void inject(@NotNull Player player, boolean remove) {
        Channel channel = getChannel(player);
        if (remove) {
            // FUCK ViaVersion
            // channel.pipeline().remove("mxlib_custom_packet_writer");
            // MXBukkitLib.debug(() -> "[PlayerNetWorkInjector] Removed Custom Packet Writer [" + player.getName() + "]");
            channel.pipeline().remove("mxlib_plugin_message_replacer");
        } else {
            channel.pipeline().addAfter("encoder", "mxlib_plugin_message_replacer", new PluginMessageProvider(player));

            //channel.pipeline().addBefore("encoder", "mxlib_custom_packet_writer", new CustomPacketWriter());
            final ChannelHandler encoder = channel.pipeline().get("encoder");
            if (encoder instanceof FilterMessageEncoder) {
                FilterMessageEncoder filter = (FilterMessageEncoder) encoder;
                List<MessageToByteEncoder> encoders = filter.encoders;
                if (!encoders.contains(writer)) {
                    try {
                        encoders.add(0, writer);
                    } catch (Throwable thr) {
                        encoders = new ArrayList<>(encoders);
                        encoders.add(0, writer);
                        filter.encoders = encoders;
                    }
                }
                if (!encoders.contains(writer)) {
                    encoders = new ArrayList<>(encoders);
                    encoders.add(0, writer);
                    filter.encoders = encoders;
                }
            } else {
                if (encoder instanceof MessageToByteEncoder) {
                    channel.pipeline().replace("encoder", "encoder", new FilterMessageEncoder(writer, (MessageToByteEncoder) encoder));
                } else {
                    String str = String.valueOf(encoder);
                    Class<?> c = encoder.getClass();
                    if (!str.startsWith(c.getName())) {
                        str += " (" + c.getName() + ")@" + Integer.toHexString(encoder.hashCode());
                    }
                    MXBukkitLib.getLogger().error("[PlayerNetWorkInjector] Failed to replace PacketEncoder " + str);
                    return;
                }
            }
            MXBukkitLib.debug(() -> "[PlayerNetWorkInjector] Injected Custom Packet Writer [" + player.getName() + "]");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeft(PlayerQuitEvent event) {
        channels.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent player) {
        MXBukkitLib.debug(() -> "[PlayerNetWorkInjector] Player join the server [ " + player.getPlayer().getName() + "]");
        inject(player.getPlayer(), false);
    }

    @Override
    public PacketSender sendPacket(@NotNull RawPacket packet, @NotNull Object target) throws ClassCastException {
        getChannel((Player) target).writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> PacketSender sendPacketToAll(@NotNull RawPacket packet, @NotNull Predicate<T> filter) {
        for (Player p : BukkitToolkit.getOnlinePlayers()) {
            if (((Predicate) filter).test(p)) {
                sendPacket(packet, p);
            }
        }
        return this;
    }
}
