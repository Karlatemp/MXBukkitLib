/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: LoggerProvider.java@author: karlatemp@vip.qq.com: 2020/1/5 上午1:37@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command;

import cn.mcres.karlatemp.mxlib.translate.MTranslate;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerProvider extends BaseCommandProvider {
    public LoggerProvider(CommandProvider parent, MTranslate translate) {
        super(parent, translate);
    }

    public LoggerProvider(CommandProvider parent, MTranslate translate, Logger logger) {
        super(parent, translate, logger);
    }

    public LoggerProvider(CommandProvider parent) {
        super(parent);
    }

    public LoggerProvider() {
    }

    @Override
    public void senderNotResolve(Object sender, Class<?> toClass) {
        sendMessage(Level.WARNING, sender, "Cannot case to " + toClass);
    }

    @Override
    public void sendMessage(Level level, Object sender, String message) {
        ((Logger) sender).log(level, message);
    }
}
