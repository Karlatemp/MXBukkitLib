/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AbstractCommand.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.shared;

import cn.mcres.karlatemp.mxlib.cmd.ICommand;
import cn.mcres.karlatemp.mxlib.cmd.ICommandSender;
import cn.mcres.karlatemp.mxlib.cmd.ICommands;
import cn.mcres.karlatemp.mxlib.cmd.IExceptionProcessor;
import cn.mcres.karlatemp.mxlib.tools.SafeList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractCommand implements ICommand {
    protected final String name;
    protected ICommands parent;
    protected String perm;
    protected IExceptionProcessor processor;

    public AbstractCommand(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public ICommands getParent() {
        return parent;
    }

    @Nullable
    @Override
    public String getPermission() {
        return perm;
    }

    @Nullable
    @Override
    public ICommand setPermission(String permission) {
        perm = permission;
        return this;
    }

    @NotNull
    @Override
    @Deprecated
    public ICommand setParent(@NotNull ICommands parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public boolean checkPermission(@NotNull ICommandSender sender) {
        if (perm != null) return sender.hasPermission(perm);
        return true;
    }

    @Nullable
    @Override
    public IExceptionProcessor getProcessor() {
        if (processor == null) {
            if (parent != null) return parent.getProcessor();
        }
        return processor;
    }

    @Override
    public ICommand setProcessor(@Nullable IExceptionProcessor processor) {
        this.processor = processor;
        return this;
    }
}
