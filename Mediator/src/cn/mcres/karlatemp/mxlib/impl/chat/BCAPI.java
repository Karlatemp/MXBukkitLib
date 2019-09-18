/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BCAPI.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

public interface BCAPI {

    void send(Player player, BaseComponent bc);

    void send(Player player, BaseComponent... bcs);

    void send(Player player, BCP packet);

}
