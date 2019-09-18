/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: TitleAPIWrap.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.bukkit;

import cn.mcres.karlatemp.mxlib.bukkit.PacketFormatter;
import org.bukkit.entity.Player;

@Deprecated
public class TitleAPIWrap implements TitleAPI {
    private final cn.mcres.karlatemp.mxlib.bukkit.TitleAPI api;

    public TitleAPIWrap(cn.mcres.karlatemp.mxlib.bukkit.TitleAPI api) {
        this.api = api;
    }

    @Override
    public void sendPacket(Player player, Object packet) {
        api.sendPacket(player, packet);
    }

    @Override
    public Class<?> getNMSClass(String name) {
        return null;
    }

    @Override
    public void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        api.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut, TextFormatType.GSON);

    }

    @Override
    public void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle, TextFormatType format) {
        api.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut, format);
    }

    @Override
    public void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        api.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut, TextFormatType.GSON);
    }

    @Override
    public void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle, TextFormatType format) {
        api.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut, format);
    }

    @Override
    public void clearTitle(Player player) {
        api.sendTitle(player, "", "", 0, 0, 0, PacketFormatter.DEFAULT);
    }

    @Override
    public void sendTabTitle(Player player, String header, String footer) {
        api.setTabTitle(player, header, footer, PacketFormatter.LOSSLESS);
    }

    @Override
    public void sendTabTitle(Player player, String header, String footer, TextFormatType type) {
        api.setTabTitle(player, header, footer, type);
    }

    @Override
    public void sendOutChat(Player player, String text) {
        sendOutChat(player, text, TextFormatType.GSON);
    }

    @Override
    public void sendOutChat(Player player, String text, TextFormatType type) {
        api.sendActionBar(player, text, type);
    }
}
