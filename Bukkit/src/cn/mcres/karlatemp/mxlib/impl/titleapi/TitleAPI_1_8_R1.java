/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: TitleAPI_1_8_R1.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl.titleapi;

import cn.mcres.karlatemp.mxlib.bukkit.PacketFormatter;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

@SuppressWarnings("Duplicates")
public class TitleAPI_1_8_R1 extends TitleAPImpl {

    @Override
    public void sendPacket(@NotNull Player player, @NotNull Object packet) {
        CraftPlayer cp = (CraftPlayer) player;
        final EntityPlayer handle = cp.getHandle();
        handle.playerConnection.sendPacket((Packet) packet);
    }

    @Override
    public void sendTitle(@NotNull Player player, @Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut, @NotNull PacketFormatter formatter) {

        EnumTitleAction e;
        IChatBaseComponent chatTitle;
        IChatBaseComponent chatSubtitle;
        PacketPlayOutTitle titlePacket;
        Object subtitlePacket;

        if (title != null) {
            title = ChatColor.translateAlternateColorCodes('&', title);
            // Times packets
            e = EnumTitleAction.TIMES;
            chatTitle = ChatSerializer.a(formatter.format(title));
            titlePacket = new PacketPlayOutTitle(e, chatTitle, fadeIn, stay, fadeOut);
            sendPacket(player, titlePacket);

            e = EnumTitleAction.TITLE;
            chatTitle = ChatSerializer.a(formatter.format(title));
            titlePacket = new PacketPlayOutTitle(e, chatTitle);
            sendPacket(player, titlePacket);
        }

        if (subtitle != null) {
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            // Times packets
            e = EnumTitleAction.TIMES;
            chatSubtitle = ChatSerializer.a(formatter.format(subtitle));

            subtitlePacket = new PacketPlayOutTitle(e, chatSubtitle, fadeIn, stay, fadeOut);
            sendPacket(player, subtitlePacket);

            e = EnumTitleAction.SUBTITLE;
            chatSubtitle = ChatSerializer.a(formatter.format(subtitle));
            subtitlePacket = new PacketPlayOutTitle(e, chatSubtitle, fadeIn, stay, fadeOut);
            sendPacket(player, subtitlePacket);
        }
    }

    @Override
    public void setTabTitle(@NotNull Player player, @NotNull String header, @NotNull String footer, @NotNull PacketFormatter formatter) {
        header = ChatColor.translateAlternateColorCodes('&', header);
        footer = ChatColor.translateAlternateColorCodes('&', footer);

        IChatBaseComponent tab_header = ChatSerializer.a(formatter.format(header));
        IChatBaseComponent tab_footer = ChatSerializer.a(formatter.format(footer));
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        TAPIKT.PacketPlayOutPlayerListHeaderFooterSetValue(packet, tab_header, tab_footer, PacketPlayOutPlayerListHeaderFooter.class);
        sendPacket(player, packet);
    }

    @Override
    public void sendActionBar(@NotNull Player player, @NotNull String action, @NotNull PacketFormatter formatter) {
        action = ChatColor.translateAlternateColorCodes('&', action);
        IChatBaseComponent chatText = ChatSerializer.a(formatter.format(action));
        PacketPlayOutChat packet = new PacketPlayOutChat(chatText, (byte) 2);
        sendPacket(player, packet);
    }
}
