/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import org.bukkit.command.CommandSender;

/**
 * Add in version 0.10
 * @author 32798
 */
public class LanguageTranslator {
    private static final LanguageTranslator def = new LanguageTranslator();
    public static LanguageTranslator getDefault() {
        return def;
    }

    public void consoleDeny(Executer executer, SubCommandEX subcmd, CommandSender sender) {
        sender.sendMessage(subcmd.msg$console_deny);
    }
    public void senderTypeError(Executer executer, SubCommandEX subcmd, CommandSender sender,Class<? extends CommandSender> expected){
                sender.sendMessage(subcmd.msg$no_type_sender);
    }
    public void noPermission(Executer executer, SubCommandEX subcmd, CommandSender sender, String permission) {
        sender.sendMessage(subcmd.msg$permission_deny);
    }

    public void noPermission(Executer executer, CommandSender sender, String permission) {
        if (executer instanceof ExecuterEX) {
            sender.sendMessage(((ExecuterEX) executer).msg$noper);
            return;
        }
        sender.sendMessage("\u00a7cYou don't have the permission to do that.");
    }

    public boolean promptToHelp(Executer executer, CommandSender sender, org.bukkit.command.Command cmd, String ali, String[] argc) {
        sender.sendMessage("\u00a7cPlease use \"/" + ali + " help\" to get help.");
        return true;
    }
}
