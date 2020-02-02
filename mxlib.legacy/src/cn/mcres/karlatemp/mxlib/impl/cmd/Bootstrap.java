/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: Bootstrap.java@author: karlatemp@vip.qq.com: 2020/1/5 下午2:07@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl.cmd;

import cn.mcres.karlatemp.mxlib.command.*;
import cn.mcres.karlatemp.mxlib.command.annoations.PBootstrap;
import cn.mcres.karlatemp.mxlib.module.translate.MLocale;
import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import cn.mcres.karlatemp.mxlib.share.MXBukkitLibPluginStartup;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Bootstrap implements Listener, EventExecutor {
    public static void load() {
        for (var p : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (p instanceof JavaPlugin) {
                if (p.isEnabled()) {
                    load((JavaPlugin) p);
                }
            }
        }
        var b = new Bootstrap();
        PluginEnableEvent.getHandlerList().register(new RegisteredListener(b, b, EventPriority.NORMAL, MXBukkitLibPluginStartup.plugin, false));
    }

    private static void load(JavaPlugin p) {
        var f = BukkitToolkit.getFile(p);
        var pc = p.getClass();
        final PBootstrap pb = pc.getDeclaredAnnotation(PBootstrap.class);
        if (pb != null) {
            for (var pcc : pb.value()) {
                PluginCommand cmd = p.getCommand(pcc.name());
                if (cmd == null) {
                    p.getLogger().warning("[MXLib] [CommandAPI] Unknown command: " + pcc.name());
                } else {
                    CommandProvider provider;
                    try {
                        provider = CacheCommandProviders.getProvider(pcc.provider());
                    } catch (Throwable err) {
                        p.getLogger().log(Level.WARNING, "[MXLib] [CommandAPI] Failed to create command provider " + pcc.provider() + " for command `" + pcc.name() + "` with rule " + pcc);
                        continue;
                    }
                    BukkitCommandExecutor exec = new CommandBuilder().provider(provider.withParent(new BaseCommandProvider(
                            null, MLocale.getTranslate(p), p.getLogger()
                    ))).ofFile(f).ofClass(pcc.source()).end(BukkitCommandExecutor::new);
                    cmd.setExecutor(exec);
                    cmd.setTabCompleter(exec);
                }
            }
        }
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        if (event instanceof PluginEnableEvent) {
            var pe = (PluginEnableEvent) event;
            if (pe.getPlugin() instanceof JavaPlugin)
                load((JavaPlugin) pe.getPlugin());
        }
    }
}
