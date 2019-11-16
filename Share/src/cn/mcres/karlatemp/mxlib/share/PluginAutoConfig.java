/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PluginAutoConfig.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.annotations.Bean;
import cn.mcres.karlatemp.mxlib.annotations.Configuration;
import cn.mcres.karlatemp.mxlib.bean.IEnvironmentFactory;
import cn.mcres.karlatemp.mxlib.bukkit.IBukkitConfigurationProcessor;
import cn.mcres.karlatemp.mxlib.cmd.ICommandProcessor;
import cn.mcres.karlatemp.mxlib.exceptions.ObjectCreateException;
import cn.mcres.karlatemp.mxlib.logging.Ansi;
import cn.mcres.karlatemp.mxlib.logging.IMessageFactory;
import cn.mcres.karlatemp.mxlib.scheduler.MXScheduler;
import cn.mcres.karlatemp.mxlib.tools.*;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class PluginAutoConfig {
    static class BukkitCheck {
        static boolean bukkit;

        static {
            try {
                Class<?> b = Class.forName("org.bukkit.Bukkit");
                bukkit = b.getMethod("getServer").invoke(null) != null;
            } catch (Throwable thr) {
            }
        }
    }

    @Bean
    IMessageFactory mf() {
        return new BukkitPluginMessageFactory();
    }


    @Bean
    ICommandProcessor processor(ICommandProcessor p) {
        if (BukkitCheck.bukkit)
            return new BukkitCommandProcessor();
        return p;
    }

    @Bean
    void zboot(IEnvironmentFactory factory) {
        IBukkitConfigurationProcessor bc = new BukkitConfigurationProcessor();
        MXBukkitLib.getBeanManager().addBean(IBukkitConfigurationProcessor.class, bc);
        $MXBukkitLibConfiguration.load(factory, bc);
    }

    @Bean
    CommandMap cm(IMemberScanner ms) {
        final Server server = Bukkit.getServer();
        try {
            return (CommandMap) server.getClass().getMethod("getCommandMap").invoke(server);
        } catch (Throwable ignored) {
        }
        try {
            //noinspection OptionalGetWithoutIsPresent
            return (CommandMap) ms.getAllMethod(server.getClass()).stream().filter(
                    x -> CommandMap.class.isAssignableFrom(x.getReturnType())
            ).findFirst().get().invoke(server);
        } catch (Throwable ignored) {
        }
        try {
            Field f = Command.class.getDeclaredField("commandMap");
            f.setAccessible(true);
            final Pointer<CommandMap> pointer = new Pointer<>();
            Stream.of(server.getPluginManager().getPlugins())
                    .map(
                            Plugin::getDescription
                    ).map(des -> {
                String p = des.getName().toLowerCase() + ":";
                return des.getCommands().keySet().stream().map(
                        cn -> server.getPluginCommand(p + cn)
                ).collect(Collectors.toList());
            }).forEach(x -> {
                if (pointer.exists()) {
                    return;
                }
                x.forEach(c -> {
                    if (pointer.exists()) {
                        return;
                    }
                    try {
                        pointer.value((CommandMap) f.get(c));
                    } catch (Throwable ignore) {
                    }
                });
            });
            if (pointer.exists()) return pointer.value();
        } catch (Throwable ignore) {
        }
        return null;
    }

    @Bean
    DependChecker dependChecker() {
        return new BukkitDependChecker();
    }

    @Bean
    void onBoot(ServiceInstallers installers, final IObjectCreator creator) {
        installers.add(listener -> {
            Plugin p = BukkitToolkit.getPluginByClass(listener);
            if (p == null) return false;
            if (Listener.class.isAssignableFrom(listener)) {
                try {
                    Listener instance = creator.newInstance(listener.asSubclass(Listener.class));
                    Bukkit.getPluginManager().registerEvents(instance, p);
                    return true;
                } catch (Exception e) {
                    MXBukkitLib.getLogger().error("[ServiceInstaller] [Listener] Failed to create instance for " + listener);
                    MXBukkitLib.getLogger().printStackTrace(e);
                }
            }
            return false;
        });
    }

    @Bean
    void classLoad() {
        Plugin p = BukkitToolkit.getPluginByClass(getClass());
        if (p == null) {
            MXBukkitLibPluginStartup.plugin.getLogger().severe("[BukkitToolkit] [getPluginByCLass]" + Ansi._C + " This should not happen" + Ansi.RESET);
        }
    }

    @Bean
    MXScheduler mxScheduler() {
        return new BukkitWrapScheduler();
    }
}
