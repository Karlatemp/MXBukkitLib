/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitCommandConfig.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.cmd.ICommandProcessor;
import cn.mcres.karlatemp.mxlib.cmd.ICommands;
import cn.mcres.karlatemp.mxlib.cmd.IExceptionProcessor;
import cn.mcres.karlatemp.mxlib.configuration.ICommandConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public abstract class BukkitCommandConfig extends ICommandConfig {
    public static abstract class BukkitCommandByNameConfig extends BukkitCommandConfig {
        public abstract String getCommandName();

        @Override
        protected PluginCommand getCommand() {
            return Bukkit.getServer().getPluginCommand(getCommandName().toLowerCase());
        }
    }

    protected MXBukkitLibCommandsExecutor executor;

    private void $init(ICommandProcessor processor) {
        executor = new MXBukkitLibCommandsExecutor(processor);
        PluginCommand pc = getCommand();
        pc.setExecutor(executor);
        pc.setTabCompleter(executor);
        if (this instanceof ICommandProcessor) {
            executor.setProcessor((IExceptionProcessor) this);
        }
    }

    protected abstract PluginCommand getCommand();

    @Override
    public ICommands getRoot() {
        return executor;
    }
}
