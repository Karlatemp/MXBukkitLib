/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitCommandProvider.java@author: karlatemp@vip.qq.com: 2020/1/3 下午4:41@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.PrintStream;
import java.util.logging.Level;

public class BukkitCommandProvider extends BaseCommandProvider {
    public BukkitCommandProvider(CommandProvider parent) {
        super(parent);
    }

    @Override
    public Object resolveSender(Object sender, Class<?> toClass) {
        if (!(sender instanceof CommandSender)) return null;
        if (toClass == null) {
            return sender;
        }
        if (toClass.isInstance(sender)) return sender;
        return null;
    }

    @Override
    public boolean hasPermission(Object sender, String permission) {
        if (permission == null || permission.isEmpty()) return true;
        return ((CommandSender) sender).hasPermission(permission);
    }

    @Override
    public void noPermission(Object sender, ICommand command) {
        ((CommandSender) sender).sendMessage(command.noPermissionMessage());
    }

    @Override
    public void senderNotResolve(Object sender, Class<?> toClass) {
        ((CommandSender) sender).sendMessage("§cSorry, but you can't use this command.");
    }

    @Override
    public void sendMessage(Level level, Object sender, String message) {
        if (level == Level.SEVERE) {
            message = "§4" + message;
        } else if (level == Level.WARNING) {
            message = "§c" + message;
        }
        if (sender instanceof Player) {
            int off = 0;
            CommandSender cs = (CommandSender) sender;
            while (true) {
                int next = message.indexOf('\n', off);
                if (next == -1) {
                    cs.sendMessage(message.substring(off));
                    break;
                } else {
                    cs.sendMessage(message.substring(off, next));
                    off = next + 1;
                }
            }
            return;
        }
        ((CommandSender) sender).sendMessage(message);
    }
}
