/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: TitleAPI_Reflect.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl.titleapi;

import cn.mcres.karlatemp.mxlib.bukkit.PacketFormatter;
import cn.mcres.karlatemp.mxlib.bukkit.TitleAPI;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

@SuppressWarnings("Duplicates")
public class TitleAPI_Reflect implements TitleAPI {
    private final String nms;
    private MethodHandle getHandle, playerConnection, sendPacket;
    @SuppressWarnings("rawtypes")
    private Class<? extends Enum> EnumTitleAction;
    private Class<?> ChatSerializer;

    @SuppressWarnings("unchecked")
    private Enum<?> getEnumTitleAction(String name) {
        try {
            if (EnumTitleAction == null) {
                try {
                    EnumTitleAction = Class.forName(nms + ".EnumTitleAction").asSubclass(Enum.class);
                } catch (Throwable thr) {
                    EnumTitleAction = Class.forName(nms + ".PacketPlayOutTitle$EnumTitleAction").asSubclass(Enum.class);
                }
            }
            return Enum.valueOf(EnumTitleAction, name);
        } catch (Throwable thr) {
            return ThrowHelper.thrown(thr);
        }
    }

    public TitleAPI_Reflect(String nms) {
        this.nms = nms;
    }

    private Object getPlayerConnection(Object handle) {
        MethodHandles.Lookup lk = Toolkit.Reflection.getRoot();
        if (playerConnection == null) {
            try {
                playerConnection = lk.unreflectGetter(handle.getClass().getField("playerConnection"));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                ThrowHelper.thrown(e);
            }
        }
        try {
            return playerConnection.invoke(handle);
        } catch (Throwable throwable) {
            return ThrowHelper.thrown(throwable);
        }
    }

    private Object getHandle(Player p) {
        MethodHandles.Lookup lk = Toolkit.Reflection.getRoot();
        if (getHandle == null) {
            final Class<? extends Player> c = p.getClass();
            try {
                Method m = c.getMethod("getHandle");
                getHandle = lk.unreflect(m);
            } catch (Throwable e) {
                return ThrowHelper.thrown(e);
            }
        }
        try {
            return getHandle.invoke(p);
        } catch (Throwable throwable) {
            return ThrowHelper.thrown(throwable);
        }
    }

    private void sendPacket0(Object pc, Object packet) {
        if (sendPacket == null) {
            final Class<?> pc_class = pc.getClass();
            try {
                Class<?> Packet = Class.forName(nms + ".Packet");
                sendPacket = Toolkit.Reflection.getRoot().unreflect(pc_class.getMethod("sendPacket", Packet));
            } catch (Throwable e) {
                ThrowHelper.thrown(e);
            }
        }
        try {
            sendPacket.invoke(pc, packet);
        } catch (Throwable throwable) {
            ThrowHelper.thrown(throwable);
        }
    }

    @Override
    public void sendPacket(@NotNull Player player, @NotNull Object packet) {
        Object handle = getHandle(player);
        Object playerConnection = getPlayerConnection(handle);
        sendPacket0(playerConnection, packet);
    }

    private MethodHandle PacketPlayOutTitle$con$OOIII;

    private Object newPacketPlayOutTitle(Object enumTitleAction, Object chatTitle, int fadeIn, int stay, int fadeOut) {
        //newPacketPlayOutTitle
        try {
            if (PacketPlayOutTitle$con$OOIII == null) {
                Class<?> c = Class.forName(nms + ".PacketPlayOutTitle");
                MethodHandles.Lookup lk = Toolkit.Reflection.getRoot();
                PacketPlayOutTitle$con$OOIII = lk.findConstructor(c, MethodType.methodType(void.class,
                        EnumTitleAction, ChatSerializer, int.class, int.class, int.class));
            }
            return PacketPlayOutTitle$con$OOIII.invoke(enumTitleAction, chatTitle, fadeIn, stay, fadeOut);
        } catch (Throwable thr) {
            return ThrowHelper.thrown(thr);
        }
    }

    private Object newPacketPlayOutTitle(Object enumTitleAction, Object chatTitle) {
        //newPacketPlayOutTitle
        try {
            if (PacketPlayOutTitle$con$OOIII == null) {
                Class<?> c = Class.forName(nms + ".PacketPlayOutTitle");
                MethodHandles.Lookup lk = Toolkit.Reflection.getRoot();
                PacketPlayOutTitle$con$OOIII = lk.findConstructor(c, MethodType.methodType(void.class,
                        EnumTitleAction, ChatSerializer));
            }
            return PacketPlayOutTitle$con$OOIII.invoke(enumTitleAction, chatTitle);
        } catch (Throwable thr) {
            return ThrowHelper.thrown(thr);
        }
    }

    @Override
    public void sendTitle(@NotNull Player player, @Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut, @NotNull PacketFormatter formatter) {

        Object e;
        Object chatTitle;
        Object chatSubtitle;
        Object titlePacket;
        Object subtitlePacket;

        if (title != null) {
            title = ChatColor.translateAlternateColorCodes('&', title);
            // Times packets
            e = getEnumTitleAction("TIMES");
            chatTitle = ChatSerializer$a(formatter.format(title));
            titlePacket = newPacketPlayOutTitle(e, chatTitle, fadeIn, stay, fadeOut);
            sendPacket(player, titlePacket);

            e = getEnumTitleAction("TITLE");
            chatTitle = ChatSerializer$a(formatter.format(title));
            titlePacket = newPacketPlayOutTitle(e, chatTitle);
            sendPacket(player, titlePacket);
        }

        if (subtitle != null) {
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            // Times packets
            e = getEnumTitleAction("TIMES");
            chatSubtitle = ChatSerializer$a(formatter.format(subtitle));

            subtitlePacket = newPacketPlayOutTitle(e, chatSubtitle, fadeIn, stay, fadeOut);
            sendPacket(player, subtitlePacket);

            e = getEnumTitleAction("SUBTITLE");
            chatSubtitle = ChatSerializer$a(formatter.format(subtitle));
            subtitlePacket = newPacketPlayOutTitle(e, chatSubtitle, fadeIn, stay, fadeOut);
            sendPacket(player, subtitlePacket);
        }
        //player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    private MethodHandle ChatSerializer$a, PacketPlayOutPlayerListHeaderFooter$con,
            PacketPlayOutChat$con;

    private Object ChatSerializer$a(String line) {
        try {
            if (ChatSerializer$a == null) {
                MethodHandles.Lookup lk = Toolkit.Reflection.getRoot();
                if (this.ChatSerializer == null) {
                    Class<?> ChatSerializer;
                    try {
                        ChatSerializer = Class.forName(nms + ".ChatSerializer");
                    } catch (Exception e) {
                        ChatSerializer = Class.forName(nms + ".IChatBaseComponent$ChatSerializer");
                    }
                    this.ChatSerializer = ChatSerializer;
                }
                ChatSerializer$a = lk.unreflect(ChatSerializer.getMethod("a", String.class));
            }
            return ChatSerializer$a.invoke(line);
        } catch (Throwable throwable) {
            return ThrowHelper.thrown(throwable);
        }
    }

    private Object newPacketPlayOutPlayerListHeaderFooter() {
        try {
            if (PacketPlayOutPlayerListHeaderFooter$con == null) {
                PacketPlayOutPlayerListHeaderFooter$con = Toolkit.Reflection.getRoot()
                        .findConstructor(Class.forName(nms + ".PacketPlayOutPlayerListHeaderFooter"), MethodType.methodType(void.class));
            }
            return PacketPlayOutPlayerListHeaderFooter$con.invoke();
        } catch (Throwable t) {
            return ThrowHelper.thrown(t);
        }
    }

    private void PacketPlayOutPlayerListHeaderFooterSetValue(Object packet, Object header, Object footer) {
        try {
            Class<?> c = Class.forName(nms + ".PacketPlayOutPlayerListHeaderFooter");
            TAPIKT.PacketPlayOutPlayerListHeaderFooterSetValue(packet, header, footer, c);
        } catch (Throwable thr) {
            ThrowHelper.thrown(thr);
        }
    }

    @Override
    public void setTabTitle(@NotNull Player player, @NotNull String header, @NotNull String footer, @NotNull PacketFormatter formatter) {
        header = ChatColor.translateAlternateColorCodes('&', header);
        footer = ChatColor.translateAlternateColorCodes('&', footer);
        Object tab_header = ChatSerializer$a(formatter.format(header));
        Object tab_footer = ChatSerializer$a(formatter.format(footer));
        Object packet = newPacketPlayOutPlayerListHeaderFooter();
        PacketPlayOutPlayerListHeaderFooterSetValue(packet, tab_header, tab_footer);
        sendPacket(player, packet);
    }

    private Object newPacketPlayOutChat(Object chatText) {
        try {
            if (PacketPlayOutChat$con == null) {
                MethodHandles.Lookup lk = Toolkit.Reflection.getRoot();
                Class<?> c = Class.forName(nms + ".PacketPlayOutChat");
                Class<?> b = Class.forName(nms + ".IChatBaseComponent");
                try {
                    Class<?> ChatMessageType = Class.forName(nms + ".ChatMessageType");
                    Object em = Enum.valueOf(ChatMessageType.asSubclass(Enum.class), "GAME_INFO");
                    MethodHandle m = lk.findConstructor(c, MethodType.methodType(void.class, b, ChatMessageType));
                    PacketPlayOutChat$con = MethodHandles.insertArguments(m, 1, em);
                } catch (Throwable t) {
                    MethodHandle m = lk.findConstructor(c, MethodType.methodType(void.class, b, byte.class));
                    PacketPlayOutChat$con = MethodHandles.insertArguments(m, 1, (byte) 2);
                }
            }
            return PacketPlayOutChat$con.invoke(chatText);
        } catch (Throwable thr) {
            return ThrowHelper.thrown(thr);
        }
    }

    @Override
    public void sendActionBar(@NotNull Player player, @NotNull String action, @NotNull PacketFormatter formatter) {
        action = ChatColor.translateAlternateColorCodes('&', action);
        Object chatText = ChatSerializer$a(formatter.format(action));
        Object packet = newPacketPlayOutChat(chatText);
        sendPacket(player, packet);
    }
}
