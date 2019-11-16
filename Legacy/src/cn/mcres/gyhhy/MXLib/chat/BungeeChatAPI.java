/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BungeeChatAPI.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.chat;

import cn.mcres.gyhhy.MXLib.spigot.SpigotHelper;
import cn.mcres.karlatemp.mxlib.impl.chat.BCAPI;
import cn.mcres.karlatemp.mxlib.impl.chat.BCP;
import cn.mcres.karlatemp.mxlib.impl.chat.SpigotCHAT;
import cn.mcres.karlatemp.mxlib.impl.chat.PCP;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @version 1.12
 */
public class BungeeChatAPI implements BCAPI, cn.mcres.karlatemp.mxlib.module.chat.BungeeChatAPI {
    public static final BungeeChatAPI api = new BungeeChatAPI();
    private static final BCAPI api_impl;

    static {
        if (SpigotHelper.isSupportSpigot()) {
            api_impl = new SpigotCHAT();
        } else if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            api_impl = new PCP();
        } else {
            api_impl = null;
        }
    }

    public void send(Player player, BaseComponent bc) {
        send(player, new BaseComponent[]{bc});
    }

    public void send(Player player, BaseComponent... bcs) {
        send(player, newPacket(bcs));
    }

    @Override
    public void send(Player player, BCP packet) {
        if (api_impl != null) api.send(player, packet);
        player.sendMessage(String.valueOf(packet));
    }

    public BungeeChatPacket newPacket(BaseComponent bc) {
        return newPacket(new BaseComponent[]{bc});
    }

    public BungeeChatPacket newPacket(BaseComponent... bcs) {
        return new BungeeChatPacket(bcs);
    }

    public void send(Player player, BungeeChatPacket packet) {
        send(player, (BCP) packet);
    }

    private BungeeChatAPI() {
    }
}
