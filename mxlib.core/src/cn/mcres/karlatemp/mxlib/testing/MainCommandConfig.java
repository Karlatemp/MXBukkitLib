/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MainCommandConfig.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.testing;

import cn.mcres.karlatemp.mxlib.cmd.ICommand;
import cn.mcres.karlatemp.mxlib.cmd.ICommands;
import cn.mcres.karlatemp.mxlib.configuration.ICommandConfig;
import cn.mcres.karlatemp.mxlib.shared.SharedCommands;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class MainCommandConfig extends ICommandConfig {
    public static MainCommandConfig conf;

    public MainCommandConfig() {
        conf = this;
    }

    ICommands is = new SharedCommands("root");

    private void $init() {
        System.out.println("Here is init func.");
    }


    @Override
    protected boolean check(String cname) {
        return true;
    }

    @Override
    public ICommands getRoot() {
        return is;
    }
}
