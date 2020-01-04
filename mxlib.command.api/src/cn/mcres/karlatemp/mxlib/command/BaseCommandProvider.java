/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BaseCommandProvider.java@author: karlatemp@vip.qq.com: 2019/12/31 下午1:24@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command;

import cn.mcres.karlatemp.mxlib.translate.MTranslate;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class BaseCommandProvider extends AbstractCommandProvider {
    protected CommandProvider parent;
    protected MTranslate translate;

    public BaseCommandProvider(CommandProvider parent, MTranslate translate) {
        this.parent = parent;
        this.translate = translate;
    }

    public BaseCommandProvider(CommandProvider parent) {
        this(parent, null);
    }

    public BaseCommandProvider() {
        this(null, null);
    }

    @Override
    public Object resolveSender(Object sender, Class<?> toClass) {
        if (parent == null) {
            if (toClass == null) return sender;
            if (toClass.isInstance(sender)) return sender;
            return null;
        }
        return parent.resolveSender(sender, toClass);
    }

    @Override
    public void senderNotResolve(Object sender, Class<?> toClass) {
        if (parent != null)
            parent.senderNotResolve(sender, toClass);
    }

    @NotNull
    @Override
    public CommandProvider withParent(CommandProvider provider) {
        return new BaseCommandProvider(provider);
    }

    @Override
    public boolean hasPermission(Object sender, String permission) {
        if (parent == null) {
            return permission == null || permission.trim().isEmpty();
        }
        return parent.hasPermission(sender, permission);
    }

    @Override
    public void noPermission(Object sender, ICommand command) {
        if (parent != null) parent.noPermission(sender, command);
    }

    @Override
    public void sendMessage(Level level, Object sender, String message) {
        if (parent != null) parent.sendMessage(level, sender, message);
    }

    @Override
    public void translate(Level level, Object sender, String trans) {
        if (translate != null) {
            sendMessage(level, sender, translate.asMessage(trans));
        } else if (parent != null) {
            parent.translate(level, sender, trans);
        }
    }

    @Override
    public void translate(Level level, Object sender, String trans, Object... params) {
        if (translate != null) {
            sendMessage(level, sender, translate.asMessage(trans, params));
        } else if (parent != null) {
            parent.translate(level, sender, trans, params);
        }
    }

    @Override
    public HelpTemplate getHelp() {
        if (parent != null) return parent.getHelp();
        return new HelpTemplate();
    }
}
