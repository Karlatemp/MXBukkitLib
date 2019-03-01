/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd.langs;

import cn.mcres.gyhhy.MXLib.bukkit.cmd.Executer;
import cn.mcres.gyhhy.MXLib.bukkit.cmd.SubCommandEX;
import org.bukkit.command.CommandSender;

/**
 *
 * @author 32798
 */
public class Chinese {
    
    public void consoleDeny(Executer executer, SubCommandEX subcmd, CommandSender sender) {
        sender.sendMessage("\u00a7c此命令不允许从控制台执行");
    }

    public void senderTypeError(Executer executer, SubCommandEX subcmd, CommandSender sender, Class<? extends CommandSender> expected) {
        sender.sendMessage("\u00a7哦吼, 也行你不能执行这个命令呢");
    }

    public void noPermission(Executer executer, SubCommandEX subcmd, CommandSender sender, String permission) {
        sender.sendMessage("\u00a7你没有权限来执行这个命令");
    }

    public void noPermission(Executer executer, CommandSender sender, String permission) {
        noPermission(executer, null, sender, permission);
    }

    public boolean promptToHelp(Executer executer, CommandSender sender, org.bukkit.command.Command cmd, String ali, String[] argc) {
        sender.sendMessage("\u00a7c请使用 \"/" + ali + " help\" 来获取帮助");
        return true;
    }
}
