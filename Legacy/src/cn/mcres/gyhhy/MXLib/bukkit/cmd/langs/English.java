/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: English.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd.langs;

import cn.mcres.gyhhy.MXLib.bukkit.cmd.Executer;
import cn.mcres.gyhhy.MXLib.bukkit.cmd.LanguageTranslator;
import cn.mcres.gyhhy.MXLib.bukkit.cmd.SubCommandEX;
import org.bukkit.command.CommandSender;

public class English extends LanguageTranslator {

    public void consoleDeny(Executer executer, SubCommandEX subcmd, CommandSender sender) {
        sender.sendMessage("\u00a7cConsole is not allowed to execute this command.");
    }

    public void senderTypeError(Executer executer, SubCommandEX subcmd, CommandSender sender, Class<? extends CommandSender> expected) {
        sender.sendMessage("\u00a7cOh roar, you cannot execute this command.");
    }

    public void noPermission(Executer executer, SubCommandEX subcmd, CommandSender sender, String permission) {
        sender.sendMessage("\u00a7cYou don't have the permission to execute this command.");
    }

    public void noPermission(Executer executer, CommandSender sender, String permission) {
        noPermission(executer, null, sender, permission);
    }

    public boolean promptToHelp(Executer executer, CommandSender sender, org.bukkit.command.Command cmd, String ali, String[] argc) {
        sender.sendMessage("\u00a7cPlease use \"/" + ali + " help\" to get help.");
        return true;
    }
}
