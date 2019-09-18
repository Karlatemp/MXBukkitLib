/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ICommand.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.cmd;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@ProhibitBean
public interface ICommand extends IExecutor {
    /**
     * Strict judgment. NOT FOUND COMMAND Must use this field
     */
    String COMMAND_NOT_FOUND = "${COMMAND_NOT_FOUND}" + new Object() + UUID.randomUUID();

    @NotNull
    String getName();

    @Nullable
    ICommands getParent();

    @Nullable
    String getPermission();

    @Nullable
    ICommand setPermission(String permission);

    @Nullable
    default String[] getAlias() {
        return new String[0];
    }

    /**
     * Dont call this method. Only use in {@link ICommands}
     * <p>
     * 不要调用此方法, 只有 {@link ICommands} 会使用
     *
     * @deprecated
     */
    @Deprecated
    @NotNull
    ICommand setParent(@NotNull ICommands parent);

    boolean checkPermission(@NotNull ICommandSender sender);


    /**
     * 获取执行错误处理器，当前没有执行处理器时拿上级处理器, 依次递归, 直到获取一个处理器/无法获取处理器
     */
    @Nullable
    IExceptionProcessor getProcessor();

    /**
     * 设置<b>当前</b>处理器
     */
    ICommand setProcessor(@Nullable IExceptionProcessor processor);
}
