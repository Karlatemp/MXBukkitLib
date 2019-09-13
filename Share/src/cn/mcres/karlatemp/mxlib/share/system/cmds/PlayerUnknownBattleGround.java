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
