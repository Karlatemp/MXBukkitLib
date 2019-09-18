/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ConfigEditor.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.configuration;

import cn.mcres.karlatemp.mxlib.cmd.ICommands;

/**
 * Usually it is not necessary to use these.
 * <p>
 * 通常来说是不需要使用这些的
 */
@Deprecated
public interface ConfigEditor {
    default void $setRoot(ICommandNestingConfig nestingConfig, ICommands commands) {
        nestingConfig.root = commands;
    }
}
