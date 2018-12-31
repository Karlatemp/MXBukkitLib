/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author 32798
 */
public class Executer implements org.bukkit.command.CommandExecutor {
    public static String get(String[] a,int b){
        if (b < 0){
            b = 0;
        }
        String ret = null;
        if (a == null){
            return "";
        }
        if (b < a.length){
            ret = a[b];
        }
        if (ret == null){
            ret = "";
        }
        return ret;
    }
    protected boolean check(CommandSender sender) {
        return true;
    }
    private Map<String, SubCommandEX> mmp;

    public void reg(String a, SubCommandEX b) {
        mmp.put(a, b);
    }

    public Executer() {
        mmp = new HashMap();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String ali, String[] argc) {

        String exe = get(argc, 0);
//        if (check(sender)) {
        if (exe == null) {
            if (check(sender)) {
                return defexec(sender, ali);
            }
        } else {
            SubCommandEX sub = mmp.get(exe.toLowerCase());
            if (sub == null) {
                if (check(sender)) {
                    return nosub(sender, ali, argc);
                }
            } else {
                return sub.exec(sender, cmd, ali, argc, this);
            }
        }
        return true;
    }

    protected boolean defexec(CommandSender sender, String ali) {
        return true;
    }

    protected boolean nosub(CommandSender sender, String ali, String[] argc) {
        return true;
    }

}
