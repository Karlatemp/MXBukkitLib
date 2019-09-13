/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SpigotCHAT.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:37@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

public class SpigotCHAT implements BCAPI {
    @Override
    public void send(Player player, BaseComponent bc) {
        player.spigot().sendMessage(bc);
    }

    @Override
    public void send(Player player, BaseComponent... bcs) {
        player.spigot().sendMessage(bcs);
    }

    @Override
    public void send(Player player, BCP packet) {
        player.spigot().sendMessage(packet.getComponents());
    }
}
