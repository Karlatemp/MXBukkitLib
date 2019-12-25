/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SharedCommands.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.shared;

import cn.mcres.karlatemp.mxlib.cmd.ICommand;
import cn.mcres.karlatemp.mxlib.cmd.ICommandSender;
import cn.mcres.karlatemp.mxlib.cmd.ICommands;
import cn.mcres.karlatemp.mxlib.cmd.IExceptionProcessor;
import cn.mcres.karlatemp.mxlib.exceptions.CommandException;
import cn.mcres.karlatemp.mxlib.tools.SafeList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedCommands extends AbstractCommand implements ICommands {
    private final Map<String, ICommand> commands;

    public SharedCommands(@NotNull String name) {
        super(name);
        this.commands = new HashMap<>();
    }

    @Override
    public ICommand getCommand(String name) {
        return commands.get(name.toLowerCase());
    }

    @Override
    public Map<String, ICommand> getCommands() {
        return commands;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ICommands register(ICommand sub, boolean force) {
        if (sub == null) return this;
        synchronized (commands) {
            String s = sub.getName();
            //noinspection StringEquality
            String key = s.toLowerCase();
            if (force) {
                commands.put(key, sub);
                sub.setParent(this);
            } else {
                if (commands.putIfAbsent(key, sub) == sub) {
                    sub.setParent(this);
                } else return this;
            }

            String[] alias = sub.getAlias();
            if (alias != null) {
                for (String ali : alias) {
                    if (ali != null) {
                        commands.putIfAbsent(ali.toLowerCase(), sub);
                    }
                }
            }
        }
        return this;
    }

    @NotNull
    @Override
    @Deprecated
    public ICommands setParent(@NotNull ICommands parent) {
        super.setParent(parent);
        return this;
    }

    @Override
    public ICommands register(ICommand sub) {
        return register(sub, false);
    }

    @Nullable
    @Override
    public List<String> tabComplete(@NotNull ICommandSender sender, @NotNull ICommand command, @NotNull String alias, @NotNull SafeList<String> args) {
        if (args.size() > 0) {
            ICommand i = getCommand(args.get(0));
            if (i != null) {
                args.remove(0);
                return i.tabComplete(sender, command, alias, args);
            }
        }
        return null;
    }

    @Override
    public boolean command(@NotNull ICommandSender sender, @NotNull ICommand command, @NotNull String label, @NotNull SafeList<String> args, @NotNull List<String> full_path) throws CommandException {
        final IExceptionProcessor processor = getProcessor();
        if (!checkPermission(sender)) {
            if (processor != null)
                return processor.onCommandDeny(sender, command, label, args, full_path, IExceptionProcessor.NO_PERMISSION);
            return true;
        }
        if (args.size() > 0) {
            ICommand i = getCommand(args.get(0));
            if (i != null) {
                args.remove(0);
                return i.command(sender, command, label, args, full_path);
            } else {
                if (processor != null) {
                    return processor.onCommandDeny(sender, command, label, args, full_path, IExceptionProcessor.COMMAND_NOT_FOUND);
                }
            }
        }
        return true;
    }

}
