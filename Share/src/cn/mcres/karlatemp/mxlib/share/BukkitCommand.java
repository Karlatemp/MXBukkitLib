package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.annotations.Resource;
import cn.mcres.karlatemp.mxlib.cmd.*;
import cn.mcres.karlatemp.mxlib.exceptions.CommandException;
import cn.mcres.karlatemp.mxlib.tools.SafeList;
import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BukkitCommand implements ICommand {
    private final Command c;
    private ICommandProcessor processor;

    public Command getCommand() {
        return c;
    }

    public BukkitCommand(Command command, ICommandProcessor processor) {
        this.c = command;
        this.processor = processor;
    }

    @NotNull
    @Override
    public String getName() {
        return c.getName();
    }

    @Nullable
    @Override
    public ICommands getParent() {
        return null;
    }

    @Nullable
    @Override
    public String getPermission() {
        return c.getDescription();
    }

    @Nullable
    @Override
    public ICommand setPermission(String permission) {
        c.setPermission(permission);
        return this;
    }

    @NotNull
    @Override
    @Deprecated
    public ICommand setParent(@NotNull ICommands parent) {
        return this;
    }

    @Override
    public boolean checkPermission(@NotNull ICommandSender sender) {
        return c.testPermission(processor.unboxingSender(sender));
    }

    @Nullable
    @Override
    public IExceptionProcessor getProcessor() {
        return null;
    }

    @Override
    public ICommand setProcessor(@Nullable IExceptionProcessor processor) {
        return this;
    }

    @Nullable
    @Override
    public List<String> tabComplete(@NotNull ICommandSender sender,
                                    @NotNull ICommand command,
                                    @NotNull String alias,
                                    @NotNull String[] args) {
        return c.tabComplete(processor.unboxingSender(sender), alias, args);
    }

    @Nullable
    @Override
    public List<String> tabComplete(@NotNull ICommandSender sender, @NotNull ICommand command, @NotNull String alias, @NotNull SafeList<String> args) {
        return tabComplete(sender, command, alias, args.toArray(new String[0]));
    }

    @Override
    public boolean command(@NotNull ICommandSender sender,
                           @NotNull ICommand command,
                           @NotNull String label,
                           @NotNull SafeList<String> args,
                           @NotNull List<String> full_path)
            throws CommandException {
        return command(sender, command, label, args.toArray(new String[0]));
    }

    @Override
    public boolean command(@NotNull ICommandSender sender, @NotNull ICommand command, @NotNull String label, @NotNull String[] args) throws CommandException {
        return c.execute(processor.unboxingSender(sender), label, args);
    }
}
