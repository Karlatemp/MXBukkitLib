/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BungeeChatAPI.java@author: karlatemp@vip.qq.com: 19-11-11 下午11:20@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.chat;

import cn.mcres.karlatemp.mxlib.annotations.Bean;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

@Bean
public interface BungeeChatAPI {
    void send(Player player, BaseComponent bc);

    void send(Player player, BaseComponent... bcs);
}
