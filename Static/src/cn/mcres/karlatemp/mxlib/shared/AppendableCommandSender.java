/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AppendableCommandSender.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.shared;

import cn.mcres.karlatemp.mxlib.cmd.ICommandSender;

import java.io.IOException;

public class AppendableCommandSender implements ICommandSender {
    private final String name;
    private final Appendable app;

    public AppendableCommandSender(String name, Appendable app) {
        this.name = name;
        this.app = app;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String s : messages) sendMessage(s);
    }

    @Override
    public void sendMessage(String message) {
        if (message != null) {
            try {
                app.append(message).append('\n');
            } catch (IOException e) {
            }
        }
    }

    @Override
    public boolean hasPermission(String name) {
        return false;
    }

    @Override
    public boolean isSetPermission(String name) {
        return false;
    }
}
