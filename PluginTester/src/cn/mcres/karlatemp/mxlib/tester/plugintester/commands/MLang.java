/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MLang.java@author: karlatemp@vip.qq.com: 19-11-25 下午10:47@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester.commands;

import cn.mcres.gyhhy.MXLib.bukkit.cmd.SubCommand;
import cn.mcres.gyhhy.MXLib.bukkit.cmd.SubCommandHandle;
import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.translate.SystemTranslate;
import org.bukkit.command.CommandSender;

@SubCommand
public class MLang {
    @SubCommandHandle
    public void handle(CommandSender cs, String[] args) {
        cs.sendMessage(MXBukkitLib.getBeanManager().getBeanNonNull(
                SystemTranslate.class
        ).asMessage(String.join(" ", args)));
    }
}
