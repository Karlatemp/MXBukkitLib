/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ICommandNestingConfig.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.configuration;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import cn.mcres.karlatemp.mxlib.cmd.ICommands;
import org.jetbrains.annotations.NotNull;

/**
 * The Nesting Command. 嵌套命令配置
 */
@ProhibitBean
public abstract class ICommandNestingConfig extends ICommandConfig {
    ICommands root;

    @Override
    public final ICommands getRoot() {
        return root;
    }

    @NotNull
    public abstract String getName();
}
