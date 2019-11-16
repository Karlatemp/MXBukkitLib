/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Lang.java@author: karlatemp@vip.qq.com: 19-11-12 下午10:10@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester.commands;

import cn.mcres.gyhhy.MXLib.bukkit.cmd.SubCommand;
import cn.mcres.gyhhy.MXLib.bukkit.cmd.SubCommandHandle;
import cn.mcres.karlatemp.mxlib.module.translate.MLocale;
import org.bukkit.command.CommandSender;

@SubCommand
public class Lang {
    @SubCommandHandle
    public void run(CommandSender cs, String[] args) {
        MLocale.sendMessage(cs, args[0]);
    }
}
