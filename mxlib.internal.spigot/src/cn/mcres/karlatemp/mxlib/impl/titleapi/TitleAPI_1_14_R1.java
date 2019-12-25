/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: TitleAPI_1_14_R1.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl.titleapi;

import cn.mcres.karlatemp.mxlib.bukkit.PacketFormatter;
import cn.mcres.karlatemp.mxlib.bukkit.TitleAPI;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@SuppressWarnings("Duplicates")
public class TitleAPI_1_14_R1 implements TitleAPI {

    @Override
    public void sendPacket(@NotNull Player player, @NotNull Object packet) {
        CraftPlayer cp = (CraftPlayer) player;
        final EntityPlayer handle = cp.getHandle();
        handle.playerConnection.sendPacket((Packet) packet);
    }

    @Override
    public void sendTitle(@NotNull Player player, @Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut, @NotNull PacketFormatter formatter) {

        PacketPlayOutTitle.EnumTitleAction e;
        IChatBaseComponent chatTitle;
        IChatBaseComponent chatSubtitle;
        PacketPlayOutTitle titlePacket;
        Object subtitlePacket;

        if (title != null) {
            title = ChatColor.translateAlternateColorCodes('&', title);
            // Times packets
            e = PacketPlayOutTitle.EnumTitleAction.TIMES;
            chatTitle = IChatBaseComponent.ChatSerializer.a(formatter.format(title));
            titlePacket = new PacketPlayOutTitle(e, chatTitle, fadeIn, stay, fadeOut);
            sendPacket(player, titlePacket);

            e = PacketPlayOutTitle.EnumTitleAction.TITLE;
            chatTitle = IChatBaseComponent.ChatSerializer.a(formatter.format(title));
            titlePacket = new PacketPlayOutTitle(e, chatTitle);
            sendPacket(player, titlePacket);
        }

        if (subtitle != null) {
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            // Times packets
            e = PacketPlayOutTitle.EnumTitleAction.TIMES;
            chatSubtitle = IChatBaseComponent.ChatSerializer.a(formatter.format(subtitle));

            subtitlePacket = new PacketPlayOutTitle(e, chatSubtitle, fadeIn, stay, fadeOut);
            sendPacket(player, subtitlePacket);

            e = PacketPlayOutTitle.EnumTitleAction.SUBTITLE;
            chatSubtitle = IChatBaseComponent.ChatSerializer.a(formatter.format(subtitle));
            subtitlePacket = new PacketPlayOutTitle(e, chatSubtitle, fadeIn, stay, fadeOut);
            sendPacket(player, subtitlePacket);
        }
    }

    @Override
    public void setTabTitle(@NotNull Player player, @NotNull String header, @NotNull String footer, @NotNull PacketFormatter formatter) {
        header = ChatColor.translateAlternateColorCodes('&', header);
        footer = ChatColor.translateAlternateColorCodes('&', footer);

        IChatBaseComponent tab_header = IChatBaseComponent.ChatSerializer.a(formatter.format(header));
        IChatBaseComponent tab_footer = IChatBaseComponent.ChatSerializer.a(formatter.format(footer));
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        packet.header = tab_header;
        packet.footer = tab_footer;
        sendPacket(player, packet);
    }

    @Override
    public void sendActionBar(@NotNull Player player, @NotNull String action, @NotNull PacketFormatter formatter) {
        action = ChatColor.translateAlternateColorCodes('&', action);
        IChatBaseComponent chatText = IChatBaseComponent.ChatSerializer.a(formatter.format(action));
        PacketPlayOutChat packet = new PacketPlayOutChat(chatText, ChatMessageType.GAME_INFO);
        sendPacket(player, packet);
    }
}
