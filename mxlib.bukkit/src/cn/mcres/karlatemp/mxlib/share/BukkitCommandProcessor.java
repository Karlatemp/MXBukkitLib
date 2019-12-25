/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitCommandProcessor.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.bean.IInjector;
import cn.mcres.karlatemp.mxlib.cmd.ICommand;
import cn.mcres.karlatemp.mxlib.cmd.ICommandSender;
import cn.mcres.karlatemp.mxlib.shared.SharedCommandMethodHandle;
import cn.mcres.karlatemp.mxlib.shared.SharedCommandProcessorImpl;
import cn.mcres.karlatemp.mxlib.tools.Pointer;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class BukkitCommandProcessor extends SharedCommandProcessorImpl {
    @Override
    public ICommand boxingCommand(@NotNull Object command) {
        if (command instanceof ICommand) return (ICommand) command;
        if (command instanceof Command) {
            Command c = (Command) command;
            if (c instanceof PluginCommand) {
                PluginCommand pc = (PluginCommand) c;
                final CommandExecutor executor = pc.getExecutor();
                if (executor instanceof ICommand) {
                    return (ICommand) executor;
                }
            }
            return new BukkitCommand(c, this);
        }
        if (command instanceof CommandExecutor || command instanceof TabCompleter) {
            return new BukkitExecutorCommand(command, this);
        }
        return super.boxingCommand(command);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T unboxingCommand(ICommand command) {
        if (command instanceof BukkitCommand) {
            return (T) ((BukkitCommand) command).getCommand();
        } else if (command instanceof BukkitExecutorCommand) {
            return (T) ((BukkitExecutorCommand) command).tab;
        }
        return (T) command;
    }

    @Override
    public ICommandSender boxingSender(@NotNull Object sender) {
        if (sender instanceof CommandSender) {
            return new BukkitCommandSender((CommandSender) sender);
        }
        return super.boxingSender(sender);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T unboxingSender(ICommandSender sender) {
        if (sender instanceof BukkitCommandSender) {
            return (T) ((BukkitCommandSender) sender).getSender();
        }
        return (T) sender;
    }

    @Override
    protected ICommand create(IBeanManager b, Pointer p, Class clazz, Method cmd, Method tab, String name, String[] alias) {
        return b.getBeanNonNull(IInjector.class).inject(
                new BukkitCommandMethodHandle(name, handle(cmd, p, clazz), handle(tab, p, clazz), alias)
        );
    }
}
