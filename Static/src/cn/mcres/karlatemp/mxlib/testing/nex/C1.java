/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: C1.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.testing.nex;

import cn.mcres.karlatemp.mxlib.annotations.CommandHandle;
import cn.mcres.karlatemp.mxlib.annotations.CommandTabHandle;
import cn.mcres.karlatemp.mxlib.cmd.ICommandSender;

import java.util.List;

public class C1 {
    private static final String $NAME = "FAQ";

    @CommandHandle
    public void helloWorld(ICommandSender command, String[] args) {
        command.sendMessage("Hello World");
        command.sendMessage(args);
    }

    @CommandTabHandle
    public List<String> tabtab() {
        return null;
    }
}
