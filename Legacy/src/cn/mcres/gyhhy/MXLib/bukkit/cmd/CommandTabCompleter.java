package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import java.util.List;
import org.bukkit.command.CommandSender;

public interface CommandTabCompleter {
    public void onTabComplete(CommandSender cs,
                              org.bukkit.command.Command cmnd,
                              String string, String[] args, SubCommandEX subcommand,
                              List<String> completes);
}
