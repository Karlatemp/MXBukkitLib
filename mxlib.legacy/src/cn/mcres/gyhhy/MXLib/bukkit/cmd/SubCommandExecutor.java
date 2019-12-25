/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SubCommandExecutor.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import java.lang.reflect.Method;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@SubCommand
public abstract class SubCommandExecutor extends SubCommandEX {

    @Override
    protected abstract boolean check(CommandSender sender, Executer exev);

    /**
     * {@link SubCommandEX#check(CommandSender, Executer)
     * };
     */
    protected boolean check0(CommandSender sender, Executer exev) {
        return super.check(sender, exev);
    }

    protected void presetup(Method met, Object thiz, SubCommand sc) {
        this.norem = sc.noRemoveFirstArg();
        this.sc = sc;
        this.norem = sc.noRemoveFirstArg();
        this.ctc = this;
        this.exec = met;
        this.thiz = thiz;
    }

    @Override
    protected void setup(Method met, Object thiz, SubCommand sc) {
        presetup(met, thiz, sc);
    }

    protected String[] getArgs(String[] argc) {
        String[] ret;
        if (this.norem || argc.length == 0) {
            ret = argc;
        } else {
            int lg = argc.length - 1;
            ret = new String[lg];
            if (lg != 0) {
                System.arraycopy(argc, 1, ret, 0, lg);
            }
        }
        return ret;
    }

    @Override
    public boolean exec(CommandSender sender, Command cmd, String ali, String[] argc, Executer exev) {
        if (check(sender, exev)) {
            return this.exec0(sender, cmd, ali, getArgs(argc), exev);
        }
        return true;
    }

    @SubCommandHandle
    public abstract boolean exec0(CommandSender sender, Command cmd, String ali, String[] argc, Executer exev);

    @SubCommandTabCompleter
    @Override
    @SuppressWarnings("NoopMethodInAbstractClass")
    public void onTabComplete(CommandSender cs, Command cmnd, String string, String[] args, SubCommandEX subcommand, List<String> completes) {
    }

}
