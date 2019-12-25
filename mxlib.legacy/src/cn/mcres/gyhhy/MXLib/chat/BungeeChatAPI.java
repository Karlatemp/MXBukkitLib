/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BungeeChatAPI.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.chat;

import cn.mcres.gyhhy.MXLib.spigot.SpigotHelper;
import cn.mcres.karlatemp.mxlib.impl.chat.PacketChat;
import cn.mcres.karlatemp.mxlib.impl.chat.ProtocolChat;
import cn.mcres.karlatemp.mxlib.impl.chat.SpigotChat;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @version 1.12
 */
public class BungeeChatAPI implements cn.mcres.karlatemp.mxlib.module.chat.BungeeChatAPI {
    public static final BungeeChatAPI api = new BungeeChatAPI();
    private static final cn.mcres.karlatemp.mxlib.module.chat.BungeeChatAPI api_impl;

    static {
        if (SpigotHelper.isSupportSpigot()) {
            api_impl = new SpigotChat();
        } else if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            api_impl = new ProtocolChat();
        } else {
            api_impl = new PacketChat();
        }
    }

    public void send(Player player, BaseComponent bc) {
        api_impl.send(player, bc);
    }

    public void send(Player player, BaseComponent... bcs) {
        api_impl.send(player, bcs);
    }

    private BungeeChatAPI() {
    }
}
