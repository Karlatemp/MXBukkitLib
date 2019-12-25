/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CommandTabCompleter.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import java.util.List;
import org.bukkit.command.CommandSender;

public interface CommandTabCompleter {
    public void onTabComplete(CommandSender cs,
                              org.bukkit.command.Command cmnd,
                              String string, String[] args, SubCommandEX subcommand,
                              List<String> completes);
}
