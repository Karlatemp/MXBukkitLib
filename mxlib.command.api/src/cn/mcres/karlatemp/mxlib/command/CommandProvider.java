/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CommandProvider.java@author: karlatemp@vip.qq.com: 2019/12/29 下午1:28@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command;

import cn.mcres.karlatemp.mxlib.translate.MTranslate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The commands provider
 */
public interface CommandProvider {
    /**
     * Create new command in a package.
     *
     * @param package_ Command Class package.
     * @param classes  Class found.
     * @return The build-in command.
     */
    ICommand buildCommands(Package package_, List<Class<?>> classes);

    /**
     * Build command in a class
     *
     * @param commandClass The source of command.
     * @return A command build.
     */
    @Nullable
    ICommand buildCommand(Class<?> commandClass);

    /**
     * Try resolve sender. null if fail.
     *
     * @param sender  The sender object.
     * @param toClass Target class check(use in command). null if don't check type.
     * @return null if fail. or resolved object.
     * @see cn.mcres.karlatemp.mxlib.command.annoations.MSender
     * @see DefaultCommand#check(Object)
     */
    @Nullable
    Object resolveSender(Object sender, @Nullable Class<?> toClass);

    /**
     * Call when sender cannot resolve to target class.
     *
     * @param sender  The resolved object.
     * @param toClass Target class.
     */
    void senderNotResolve(Object sender, Class<?> toClass);

    /**
     * Call when command class used current class.
     *
     * @param provider The parent provider.
     * @return A Provider with parent. Normally it should return a new instance.
     */
    @NotNull
    CommandProvider withParent(CommandProvider provider);

    /**
     * Check sender has permission or not.
     *
     * @param sender     The sender need check
     * @param permission The permission need check.
     * @return The sender has permission or not.
     */
    boolean hasPermission(Object sender, String permission);

    /**
     * Executed when denied for lack of permissions
     *
     * @param sender  The sender.
     * @param command The denied command.
     */
    void noPermission(Object sender, ICommand command);

    /**
     * Send a message to sender.
     *
     * @param level   The message level.
     * @param sender  The sender.
     * @param message The message.
     */
    void sendMessage(Level level, Object sender, String message);

    default void sendMessage(Level level, Object sender, Object message) {
        sendMessage(level, sender, String.valueOf(message));
    }

    /**
     * Try send a translate message to sender.
     *
     * @param level  The sending level.
     * @param sender The sender.
     * @param trans  The translate.
     */
    void translate(Level level, Object sender, String trans);

    /**
     * Try send a translate message to sender.
     *
     * @param level  The sending level.
     * @param sender The sender.
     * @param trans  The translate.
     * @param params The translate parameters.
     */
    void translate(Level level, Object sender, String trans, Object... params);

    /**
     * Get help template.
     *
     * @return The template using.
     */
    HelpTemplate getHelp();

    /**
     * Get Provider's Logger
     */
    Logger logger();

    MTranslate translate();

    default String parse_message(String desc) {
        return desc;
    }
}
