/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ItemStackBBCTest.java@author: karlatemp@vip.qq.com: 19-10-3 下午3:24@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share.system.cmds;

import cn.mcres.gyhhy.MXLib.chat.BungeeChatAPI;
import cn.mcres.karlatemp.mxlib.annotations.CommandHandle;
import cn.mcres.karlatemp.mxlib.module.chat.ItemStackComponent;
import org.bukkit.entity.Player;

public class ItemStackBBCTest {
    private static final String $NAME = "item";

    @CommandHandle
    public void run(Player player) {
        BungeeChatAPI.api.send(player, new ItemStackComponent(player.getInventory().getItemInMainHand()));
    }
}
