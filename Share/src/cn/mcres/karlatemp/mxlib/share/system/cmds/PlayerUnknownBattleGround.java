/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PlayerUnknownBattleGround.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share.system.cmds;

import cn.mcres.karlatemp.mxlib.annotations.CommandHandle;
import cn.mcres.karlatemp.mxlib.cmd.ICommand;
import cn.mcres.karlatemp.mxlib.cmd.ICommandSender;

import java.util.List;

public class PlayerUnknownBattleGround {
    private static final Object $NAME = ICommand.COMMAND_NOT_FOUND;

    @CommandHandle
    public void run(List<String> args, ICommandSender sender) {
        args.forEach(sender::sendMessage);
    }
}
