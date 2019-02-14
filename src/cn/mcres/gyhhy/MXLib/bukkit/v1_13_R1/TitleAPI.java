/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.v1_13_R1;

import cn.mcres.gyhhy.MXLib.bukkit.DefaultTitleAPI;
import java.lang.reflect.Field;
import net.minecraft.server.v1_13_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
/**
 *
 * @author 32798
 */
public class TitleAPI extends DefaultTitleAPI{
    @Override
    public void sendPacket(Player player, Object packet) {
        CraftPlayer pl = (CraftPlayer) player;
        Packet p = (Packet) packet;
        pl.getHandle().playerConnection.sendPacket(p);
    }

    @Override
    public void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {

        PacketPlayOutTitle.EnumTitleAction e;
        IChatBaseComponent chatTitle;
        IChatBaseComponent chatSubtitle;
        PacketPlayOutTitle titlePacket;
        Object subtitlePacket;

        if (title != null) {
            title = ChatColor.translateAlternateColorCodes('&', title);
            title = title.replaceAll("%player%", player.getDisplayName());
            // Times packets
            e = PacketPlayOutTitle.EnumTitleAction.TIMES;
            chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + title + "\"}");
            titlePacket = new PacketPlayOutTitle(e, chatTitle, fadeIn, stay, fadeOut);
            sendPacket(player, titlePacket);

            e = PacketPlayOutTitle.EnumTitleAction.TITLE;
            chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + title + "\"}");
            titlePacket = new PacketPlayOutTitle(e, chatTitle);
            sendPacket(player, titlePacket);
        }

        if (subtitle != null) {
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
            // Times packets
            e = PacketPlayOutTitle.EnumTitleAction.TIMES;
            chatSubtitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + title + "\"}");

            subtitlePacket = new PacketPlayOutTitle(e, chatSubtitle, fadeIn, stay, fadeOut);
            sendPacket(player, subtitlePacket);

            e = PacketPlayOutTitle.EnumTitleAction.SUBTITLE;
            chatSubtitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + subtitle + "\"}");
            subtitlePacket = new PacketPlayOutTitle(e, chatSubtitle, fadeIn, stay, fadeOut);
            sendPacket(player, subtitlePacket);
        }
    }

    @Override
    public void sendTabTitle(Player player, String header, String footer) {
        if (header == null) {
            header = "";
        }
        header = ChatColor.translateAlternateColorCodes('&', header);

        if (footer == null) {
            footer = "";
        }
        footer = ChatColor.translateAlternateColorCodes('&', footer);

        header = header.replaceAll("%player%", player.getDisplayName());
        footer = footer.replaceAll("%player%", player.getDisplayName());
        IChatBaseComponent tab_header = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + header + "\"}");
        IChatBaseComponent tab_footer = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field field = PacketPlayOutPlayerListHeaderFooter.class.getDeclaredField("b");
            field.setAccessible(true);
            field.set(packet, tab_footer);
            field = PacketPlayOutPlayerListHeaderFooter.class.getDeclaredField("a");
            field.setAccessible(true);
            field.set(packet, tab_header);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        sendPacket(player, packet);
    }

    @Override
    public void sendOutChat(Player player, String text) {
        if (text == null) {
            text = "";
        }
        text = ChatColor.translateAlternateColorCodes('&', text);
        IChatBaseComponent chatText = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}");
        //Object chatText = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{});
        PacketPlayOutChat packet = new PacketPlayOutChat(chatText, ChatMessageType.GAME_INFO);
//        Constructor<?> titleConstructor = getNMSClass("PacketPlayOutChat").getConstructor(new Class[]{getNMSClass("IChatBaseComponent"), Byte.TYPE});
//        Object packet = titleConstructor.newInstance(new Object[]{chatText, Byte.valueOf((byte) 2)});
        sendPacket(player, packet);
    }
}
