package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.cmd.ICommandProcessor;
import cn.mcres.karlatemp.mxlib.exceptions.CommandException;
import cn.mcres.karlatemp.mxlib.shared.SharedCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class MXBukkitLibCommandsExecutor extends SharedCommands
        implements TabExecutor {
    private final ICommandProcessor processor;

    public MXBukkitLibCommandsExecutor(ICommandProcessor processor) {
        super("");
        this.processor = processor;
    }

    @Override
    public boolean onCommand(CommandSender commandSender,
                             Command command, String s, String[] strings) {
        try {
            return command(processor.boxingSender(commandSender), processor.boxingCommand(command), s, strings);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return tabComplete(processor.boxingSender(commandSender), processor.boxingCommand(command), s, strings);
    }
}
