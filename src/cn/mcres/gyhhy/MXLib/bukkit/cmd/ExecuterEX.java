/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import java.lang.annotation.Annotation;
import org.bukkit.command.CommandSender;

/**
 *
 * @author 32798
 */
public class ExecuterEX extends Executer {

    private String per;

    public ExecuterEX(Class cx) {
        if (cx == null) {
            return;
        }
        Annotation anc = cx.getAnnotation(Command.class);
        if (anc == null) {
            throw new java.lang.IllegalArgumentException("This class is not a command class.(No Command Annotation)");
        }
        Command ec = (Command) anc;
        setPermission(ec.permission());

    }

    public String getPermission() {
        return per;
    }

    public void setPermission(String p) {
        per = p;
    }

    @Override
    protected boolean nosub(CommandSender sender, String ali, String[] argc) {
        defexec(sender, ali);
        return true; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean check(CommandSender sender) {
        if (per == null || per.isEmpty()) {
            return true;
        }
        if (sender.hasPermission(per)) {
            return true;
        }
        sender.sendMessage("\u00a7cYou don't have the permission to do that.");
        return false;
    }

    @Override
    protected boolean defexec(CommandSender sender, String ali) {
        sender.sendMessage("\u00a7cPlease use \"/" + ali + " help\" to get help.");
        return true; //To change body of generated methods, choose Tools | Templates.
    }

}
