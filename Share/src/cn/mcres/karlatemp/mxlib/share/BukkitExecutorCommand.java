/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitExecutorCommand.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:15@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.cmd.*;
import cn.mcres.karlatemp.mxlib.exceptions.CommandException;
import cn.mcres.karlatemp.mxlib.tools.SafeList;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BukkitExecutorCommand implements ICommand, TabExecutor {
    private final ICommandProcessor p;
    CommandExecutor executor;
    TabCompleter tab;

    public BukkitExecutorCommand(Object exe, ICommandProcessor processor) {
        if (exe instanceof CommandExecutor) {
            executor = (CommandExecutor) exe;
        }
        if (exe instanceof TabCompleter) {
            tab = (TabCompleter) exe;
        }
        this.p = processor;
    }

    @NotNull
    @Override
    public String getName() {
        return "";
    }

    @Nullable
    @Override
    public ICommands getParent() {
        return null;
    }

    @Nullable
    @Override
    public String getPermission() {
        return null;
    }

    @Nullable
    @Override
    public ICommand setPermission(String permission) {
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
        return true;
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
    public List<String> tabComplete(
            @NotNull ICommandSender sender,
            @NotNull ICommand command,
            @NotNull String alias,
            @NotNull SafeList<String> args) {
        if (tab != null) {
            return tab.onTabComplete(p.unboxingSender(sender), p.unboxingCommand(command), alias,
                    args.toArray(new String[0]));
        }
        return null;
    }

    @Override
    public boolean command(@NotNull ICommandSender sender, @NotNull ICommand command, @NotNull String label, @NotNull SafeList<String> args, @NotNull List<String> full_path) throws CommandException {

        if (executor != null) {
            return executor.onCommand(p.unboxingSender(sender), p.unboxingCommand(command), label,
                    args.toArray(new String[0]));
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (executor != null) {
            return executor.onCommand(commandSender, command, s, strings);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (tab != null) {
            return tab.onTabComplete(commandSender, command, s, strings);
        }
        return null;
    }
}
