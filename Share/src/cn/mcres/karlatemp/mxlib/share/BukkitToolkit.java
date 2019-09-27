/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitToolkit.java@author: karlatemp@vip.qq.com: 19-9-20 下午7:35@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class BukkitToolkit {
    private static final Class<?> PluginClassLoader;
    private static final Field pfield;

    static {
        try {
            PluginClassLoader = Class.forName(
                    "org.bukkit.plugin.java.PluginClassLoader");
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.toString());
        }
        try {
            Field plugin = null;
            try {
                plugin = PluginClassLoader.getDeclaredField("plugin");
            } catch (Throwable thr) {
                for (Field f : PluginClassLoader.getDeclaredFields()) {
                    if (Plugin.class.isAssignableFrom(f.getType())) {
                        plugin = f;
                        break;
                    }
                }
            }
            //noinspection ConstantConditions
            plugin.setAccessible(true);
            pfield = plugin;
        } catch (Throwable thr) {
            throw new java.lang.ExceptionInInitializerError(thr);
        }
    }

    public static Plugin getPluginByClass(@NotNull final Class<?> clazz) {
        final ClassLoader cl = clazz.getClassLoader();
        if (cl == null) return null;// is Java System class
        if (PluginClassLoader.isInstance(cl)) {
            try {
                return (Plugin) pfield.get(cl);
            } catch (IllegalAccessException e) {
                MXBukkitLibPluginStartup.plugin.getLogger().warning(() -> "Cannot get plugin for classloader " + cl + " in " + clazz);
                return null;
            }
        }
        return null;
    }
}
