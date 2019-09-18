/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: IExceptionProcessor.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.cmd;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ProhibitBean
public interface IExceptionProcessor {
    int NO_PERMISSION = 0,
            ERROR_CATEGORY = 1,
            COMMAND_NOT_FOUND = 2;

    boolean onCommandDeny(
            @NotNull ICommandSender sender,
            @NotNull ICommand command,
            @NotNull String label,
            @NotNull List<String> args,
            @NotNull List<String> full_args,
            int type);
}
