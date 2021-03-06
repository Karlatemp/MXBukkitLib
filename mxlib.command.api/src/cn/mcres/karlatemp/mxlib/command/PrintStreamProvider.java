/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PrintStreamProvider.java@author: karlatemp@vip.qq.com: 2019/12/31 下午1:27@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command;

import java.io.PrintStream;
import java.util.logging.Level;

public class PrintStreamProvider extends BaseCommandProvider {
    public PrintStreamProvider(CommandProvider parent) {
        super(parent);
    }

    @Override
    public Object resolveSender(Object sender, Class<?> toClass) {
        if (toClass == null || toClass.isInstance(sender)) {
            if (sender instanceof PrintStream)
                return sender;
        }
        return null;
    }

    @Override
    public void senderNotResolve(Object sender, Class<?> toClass) {
        ((PrintStream) sender).println("Cannot resolve to " + toClass);
    }

    @Override
    public void sendMessage(Level level, Object sender, String message) {
        ((PrintStream) sender).println(message);
    }
}
