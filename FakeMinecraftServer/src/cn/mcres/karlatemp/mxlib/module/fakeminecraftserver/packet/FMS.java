/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: FMS.java@author: karlatemp@vip.qq.com: 19-12-7 下午6:56@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.karlatemp.mxlib.logging.ILogger;
import cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.event.ServerAuthSuccessEvent;
import cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.event.ServerStatusRequestEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.Date;

/**
 * Here is FakeMinecraftServer premain.
 */
public class FMS {
    private static final ILogger logger = TK.named("FMS Application");

    public static void main(String[] args) throws Throwable {
        logger.printf("System startup");
        FakeMinecraftServer.port = 25565;
        logger.printf("Internet port override: " + FakeMinecraftServer.port);
        ServerStatusRequestEvent.handlers.register(event -> {
            event.getInfo().motd = new TextComponent(new ComponentBuilder("Modify ").color(
                    ChatColor.AQUA
            ).italic(true).bold(true).append("Message").color(ChatColor.RED).create());
            logger.printf("Server Status Request... " + event.getInfo().motd.toLegacyText());
        });
        ServerAuthSuccessEvent.handlers.register(event -> {
            event.setDisconnectMessage(new TextComponent(
                    new ComponentBuilder("Opp. Error: ")
                            .append("Permission denied.").color(ChatColor.RED)
                            .append(new Date().toString()).color(ChatColor.DARK_AQUA).create()
            ));
            logger.printf(event.getUsername() + " is for uuid " + event.getUniqueId());
        });
        FakeMinecraftServer.main(args);
    }
}
