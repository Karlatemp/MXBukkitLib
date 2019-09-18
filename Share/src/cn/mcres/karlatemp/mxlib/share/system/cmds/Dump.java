/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Dump.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share.system.cmds;

import cn.mcres.karlatemp.mxlib.annotations.CommandHandle;
import cn.mcres.karlatemp.mxlib.cmd.ICommand;
import cn.mcres.karlatemp.mxlib.cmd.ICommandSender;
import cn.mcres.karlatemp.mxlib.cmd.ICommands;

import java.util.Arrays;

public class Dump {
    public static final String $NAME = "dmp";
    private static final String[] $ALIAS = {"dump"};

    @CommandHandle
    public void dump(ICommandSender sender, ICommand ic) {
        sender.sendMessage("POI!");
        dump(sender, getRoot(ic), 0);
    }

    private StringBuilder a(int x) {
        StringBuilder sb = new StringBuilder(x * 2);
        while (x-- > 0) {
            sb.append("  ");
        }
        return sb;
    }

    private void dump(ICommandSender sender, ICommands root, int i) {
        if (root == null) return;
        sender.sendMessage(a(i).append(root.getName()).toString());
        i++;
        for (ICommand ic : root.getCommands().values()) {
            if (ic instanceof ICommands) {
                dump(sender, (ICommands) ic, i);
            } else {
                sender.sendMessage(a(i).append(ic.getName())
                        .append(Arrays.toString(ic.getAlias()))
                        .append('(').append(ic.getPermission()).append(')').toString());
            }
        }
    }

    private ICommands getRoot(ICommand ic) {
        ICommands icc = null;
        if (ic != null) {
            ICommands top = null;
            if (ic instanceof ICommands) icc = (ICommands) ic;
            else icc = ic.getParent();
            while (icc != null) {
                top = icc;
                icc = icc.getParent();
            }
            return top;
        }
        return null;
    }
}
