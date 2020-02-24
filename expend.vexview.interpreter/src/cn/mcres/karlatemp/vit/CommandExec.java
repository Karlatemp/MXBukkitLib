/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/20 21:28:15
 *
 * MXLib/expend.vexview.interpreter/CommandExec.java
 */

package cn.mcres.karlatemp.vit;

import cn.mcres.karlatemp.mxlib.plugin.base.I18n;
import cn.mcres.karlatemp.mxlib.plugin.base.I18nExecutor;
import lk.vexview.api.VexViewAPI;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandExec extends I18nExecutor {
    public CommandExec(@NotNull I18n i18n) {
        super(i18n);
    }

    @NotNull
    @Override
    public I18nCommandContext newContext() {
        return new I18nCommandContext() {
            @Override
            public void onCommand(CommandSender sender, Command command, String label, String[] args) {
                permission("vit.command");
                if (length(1)) {
                    switch (args[0].toLowerCase()) {
                        case "gui": {
                            if (length(3)) {
                                PM.openGUI(getPlayer(get(1)), get(2));
                                return;
                            }
                            send("§e/vit gui <player> <path>");
                            return;
                        }
                        case "hud":
                            if (length(5)) {
                                int x = 0, y = 0, z = 0;
                                if (length(8)) {
                                    x = getInt(5);
                                    y = getInt(6);
                                    z = getInt(7);
                                }
                                PM.openHUD(getPlayer(get(1)), get(2), get(3), x, y, z, getInt(4));
                            }
                            send("§e/vit hud <player> <path> <id> <time> [<x> <y> <z>]");
                            return;
                        case "tag":
                            if (length(2)) {
                                World world = null;
                                Player player = null;
                                switch (get(1).toLowerCase()) {
                                    case "world":
                                        world = getWorld(get(2));
                                        break;
                                    case "player":
                                        player = getPlayer(get(2));
                                        break;
                                }
                                if (world != null || player != null) {
                                    if (length(8)) {
                                        PM.openTag(player, world, get(3), get(4), getDouble(5), getDouble(6), getDouble(7));
                                        return;
                                    }
                                    if (world == null)
                                        send("§e/vit tag player <player> <path> <id> <x> <y> <z>");
                                    else
                                        send("§e/vit tag world <world> <path> <id> <x> <y> <z>");
                                    return;
                                }
                            }
                            send("§e/vit tag world <world> <path> <id> <x> <y> <z>");
                            send("§e/vit tag player <player> <path> <id> <x> <y> <z>");
                            return;
                        case "remove":
                            if (length(2)) {
                                switch (get(1).toLowerCase()) {
                                    case "tag": {
                                        if (length(5)) {
                                            switch (get(2).toLowerCase()) {
                                                case "world": {
                                                    VexViewAPI.removeWorldTag(getWorld(get(3)), get(4));
                                                    return;
                                                }
                                                case "player": {
                                                    VexViewAPI.removePlayerTag(getPlayer(get(3)), get(4));
                                                    return;
                                                }
                                            }
                                        }
                                        send("§e/vit remove tag world <world> <id>");
                                        send("§e/vit remove tag player <player> <id>");
                                        return;
                                    }
                                    case "hud": {
                                        if (length(4)) {
                                            VexViewAPI.removeHUD(getPlayer(get(2)), get(3));
                                            return;
                                        }
                                        send("§e/vit remove hud <player> <id>");
                                        return;
                                    }
                                }
                            }
                            send("§e/vit remove tag world <world> <id>");
                            send("§e/vit remove tag player <player> <id>");
                            send("§e/vit remove hud <player> <id>");
                            return;
                        case "reloadconfig":
                            PM.instance.reloadConfig();
                            PM.cached.cleanUp();
                            PM.cached_handles.cleanUp();
                            PM.cached_gui.cleanUp();
                            return;
                    }
                }
                send("§e/vit gui <player> <path>");
                send("§e/vit hud <player> <path> <id> <time> [<x> <y> <z>]");
                send("§e/vit tag world <world> <path> <id> <x> <y> <z>");
                send("§e/vit tag player <player> <path> <id> <x> <y> <z>");
                send("§e/vit remove tag world <world> <id>");
                send("§e/vit remove tag player <player> <id>");
                send("§e/vit remove hud <player> <id>");
                send("§e/vit reloadconfig");
            }
        };
    }
}
