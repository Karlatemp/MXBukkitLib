/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Executer.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import cn.mcres.gyhhy.MXLib.bukkit.CommandHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * The name of the class should be Executor. Yep, ignore these details
 */
public class Executer implements Translator, org.bukkit.command.CommandExecutor,
        org.bukkit.command.TabCompleter,
        org.bukkit.command.TabExecutor,
        CommandTabCompleter {

    public boolean tab_case = false;

    private LanguageTranslator lt;

    public void setLanguageTranslator(LanguageTranslator lt) {
        this.setLanguageTranslator(lt, false);
    }

    /**
     * Set the Language Translator
     *
     * @param lt LT
     * @param setall If it is true, set all subcommands to this LT.
     */
    public Executer setLanguageTranslator(final LanguageTranslator lt, boolean setall) {
        Objects.requireNonNull(lt);
        this.lt = lt;
        if (setall) {
            this.getSubs().values().forEach((c) -> c.setLanguageTranslator(lt));
        }
        return this;
    }

    public LanguageTranslator getLanguageTranslator() {
        if (lt == null) {
            lt = LanguageTranslator.getDefault();
        }
        return lt;
    }

    public static String get(String[] a, int b) {
        if (b < 0) {
            b = 0;
        }
        String ret = null;
        if (a == null) {
            return "";
        }
        if (b < a.length) {
            ret = a[b];
        }
        if (ret == null) {
            ret = "";
        }
        return ret;
    }

    protected boolean check(CommandSender sender) {
        return true;
    }
    Map<String, SubCommandEX> mmp;

    public Map<String, SubCommandEX> getSubs() {
        return mmp;
    }

    public void reg(String a, SubCommandEX b) {
        mmp.put(a.toLowerCase(), b);
    }

    public Executer() {
        mmp = new HashMap<>();
        lt = LanguageTranslator.getDefault();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String ali, String[] argc) {

        String exe = get(argc, 0);
//        if (check(sender)) {
        if (exe == null || exe.isEmpty()) {
            if (check(sender)) {
                return defexec(sender, cmd, ali, argc);
            }
        } else {
            SubCommandEX sub = mmp.get(exe.toLowerCase());
            if (sub == null) {
                if (check(sender)) {
                    return nosub(sender, cmd, ali, argc);
                }
            } else {
                return sub.exec(sender, cmd, ali, argc, this);
            }
        }
        return true;
    }

    protected boolean defexec(CommandSender sender, Command cmd, String ali, String[] argc) {
        return true;
    }

    protected boolean nosub(CommandSender sender, Command cmd, String ali, String[] argc) {
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmnd, String string, String[] args) {
        ArrayList<String> str = new ArrayList<>();
        if (args.length < 2) {
            str.addAll(mmp.keySet());
        } else {
            SubCommandEX ex = getSub(args[0]);
            if (ex != null) {
                String[] igs;
//                = new String[args.length-1];
                if (ex.norem) {
                    igs = args;
                } else {
                    igs = new String[args.length - 1];
                    if (igs.length != 0) {
                        System.arraycopy(args, 1, igs, 0, igs.length);
                    }
                }
                this.onTabComplete(cs, cmnd, string, igs, ex, str);
            }
        }
        String last = "";
        if (args.length > 0) {
            last = args[args.length - 1];
        }
        return CommandHelper.getHelper().format(str, last, tab_case);
    }

    public SubCommandEX getSub(String vl) {
        return mmp.get(vl.toLowerCase());
    }

    @Override
    public void onTabComplete(CommandSender cs, Command cmnd, String string, String[] args, SubCommandEX sce, List<String> completes) {
        sce.onTabComplete(cs, cmnd, string, args, sce, completes);
    }

}
