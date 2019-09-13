package cn.mcres.karlatemp.mxlib.share.system.cmds;

import cn.mcres.karlatemp.mxlib.annotations.CommandHandle;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Version {
    private static final String $NAME = "version";
    private static final String[] $alias = {"ver", "v"};

    @CommandHandle
    public static void run(CommandSender sender, List<String> args) {
        sender.sendMessage("Hello World! " + args);
    }
}
