/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SpigotChat.java@author: karlatemp@vip.qq.com: 2019/12/24 下午11:11@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

public class SpigotChat implements cn.mcres.karlatemp.mxlib.module.chat.BungeeChatAPI {
    @Override
    public void send(Player player, BaseComponent bc) {
        player.spigot().sendMessage(bc);
    }

    @Override
    public void send(Player player, BaseComponent... bcs) {
        player.spigot().sendMessage(bcs);
    }
}
