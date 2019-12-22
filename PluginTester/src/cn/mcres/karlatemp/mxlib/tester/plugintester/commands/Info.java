/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Info.java@author: karlatemp@vip.qq.com: 19-12-13 下午4:58@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester.commands;

import cn.mcres.gyhhy.MXLib.bukkit.cmd.SubCommand;
import cn.mcres.gyhhy.MXLib.bukkit.cmd.SubCommandHandle;
import cn.mcres.gyhhy.MXLib.bukkit.profile.ProfileHelper;
import org.bukkit.entity.Player;

@SubCommand
public class Info {
    @SubCommandHandle
    public void handle(Player player) {
        player.sendMessage(String.valueOf(ProfileHelper.getPlayerProfile(player)));
    }
}
