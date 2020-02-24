/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/19 20:15:00
 *
 * MXLib/expend.vexview.interpreter/ButtonHandle.java
 */

package cn.mcres.karlatemp.vit;

import cn.mcres.karlatemp.mxlib.interpreter.CommandBlock;
import cn.mcres.karlatemp.mxlib.interpreter.CommandLine;
import cn.mcres.karlatemp.mxlib.interpreter.CommandVisitor;
import lk.vexview.gui.components.ButtonFunction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.List;

public class ButtonHandle implements ButtonFunction {
    public List<String> permissions = new ArrayList<>();
    public String permission;
    public String trans_gui;
    public boolean close;
    public List<String> commands = new ArrayList<>();
    public String noPermissionMessage = "§cYou don't have permission to perform that.";

    @Override
    public void run(Player player) {
        if (permission != null)
            if (!player.hasPermission(permission)) {
                player.sendMessage(noPermissionMessage);
                return;
            }
        PermissionAttachment attachment = null;
        if (!permissions.isEmpty()) {
            attachment = player.addAttachment(PM.instance);
            for (var perm : permissions) {
                if (!perm.isEmpty()) {
                    if (perm.charAt(0) == '-') {
                        attachment.setPermission(perm.substring(1), false);
                        continue;
                    }
                }
                attachment.setPermission(perm, true);
            }
        }
        for (var cmd : commands) {
            Bukkit.dispatchCommand(player, PM.support.run(player, cmd.replace("%player%", player.getName())));
        }
        if (attachment != null) attachment.remove();
        if (close) {
            player.closeInventory();
            return;
        }
        if (trans_gui != null) {
            PM.openGUI(player, trans_gui);
        }
    }

    public static class HandleAllocator implements CommandVisitor<ButtonHandle, ButtonHandle> {
        public static final HandleAllocator INSTANCE = new HandleAllocator();

        @Override
        public CommandVisitor<ButtonHandle, ButtonHandle> visitBlock(CommandBlock block, ButtonHandle value, ButtonHandle contextReturn) {
            return this;
        }

        @Override
        public CommandVisitor<ButtonHandle, ButtonHandle> visitLine(CommandLine line, ButtonHandle value, ButtonHandle contextReturn) {
            var it = line.getArguments().iterator();
            switch (it.next().toString()) {
                case "noPermissionMsg":
                case "nopermmsg":
                case "no_perm_msg":
                case "no-perm-msg":
                    value.noPermissionMessage = it.next().toString();
                    break;
                case "add_perm":
                case "add_permission":
                case "addPerm":
                case "addPermission":
                    while (it.hasNext())
                        value.permissions.add(it.next().toString());
                    break;
                case "perm":
                case "permission":
                    value.permission = it.next().toString();
                    break;
                case "cmd":
                case "command":
                    var x = it.next().toString();
                    if (it.hasNext()) {
                        var l = new ArrayList<String>();
                        l.add(x);
                        do {
                            l.add(it.next().toString());
                        } while (it.hasNext());
                        x = String.join(" ", l);
                    }
                    value.commands.add(x);
                    break;
                case "close":
                    value.close = true;
                    break;
                case "open":
                case "to":
                    value.trans_gui = it.next().toString();
                    break;
            }
            return null;
        }

        @Override
        public ButtonHandle defaultValue(ButtonHandle value) {
            return value;
        }
    }
}
