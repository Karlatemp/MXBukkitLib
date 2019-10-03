/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Item.java@author: karlatemp@vip.qq.com: 19-10-3 下午4:29@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester.commands;

import cn.mcres.gyhhy.MXLib.bukkit.cmd.SubCommand;
import cn.mcres.gyhhy.MXLib.bukkit.cmd.SubCommandHandle;
import cn.mcres.karlatemp.mxlib.module.chat.ItemStackComponent;
import org.bukkit.entity.Player;

@SubCommand
public class Item {
    @SubCommandHandle
    public void run(Player p) {
        ItemStackComponent item = new ItemStackComponent(p.getInventory().getItemInMainHand());
        p.spigot().sendMessage(item);
        p.spigot().sendMessage(item.duplicate());
        System.out.println(item.toJson());
    }
}
