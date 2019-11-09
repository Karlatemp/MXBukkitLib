/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitToolkit.java@author: karlatemp@vip.qq.com: 19-9-20 下午7:35@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.module.chat.RFT;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

@SuppressWarnings("deprecation")
public class BukkitToolkit {
    private static final Class<?> PluginClassLoader;
    private static final Field pfield;
    private static final String NMS_ver, nms_start, craft_start;

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
        Server server = Bukkit.getServer();
        if (server == null) throw new NullPointerException();
        Class c = server.getClass();
        String path = c.getName();
        String pre = "org.bukkit.craftbukkit.";
        if (!path.startsWith(pre))
            throw new ClassCastException("Running Server not CraftServer!");
        path = path.substring(pre.length());
        int ed = path.indexOf('.');
        String ct = path.substring(0, ed); // v____;
        NMS_ver = ct;
        craft_start = pre + ct;
        nms_start = "net.minecraft.server." + ct;
        //noinspection deprecation
        RFT.nms = BukkitToolkit::getNMSPackage;
        //noinspection deprecation
        RFT.obc = BukkitToolkit::getCraftBukkitPackage;
    }

    /**
     * Get Minecraft NMS Version.
     * <p>
     * net.minecraft.server.<b>v1_14_R1</b>.MinecraftServer
     *
     * @return Minecraft Server Version
     */
    @Contract(pure = true)
    public static String getNMS() {
        return NMS_ver;
    }

    /**
     * Get CraftBukkit Package.
     * <p>
     * <b>org.bukkit.craftserver.v_1_14_R1</b>.CraftServer
     *
     * @return CraftBukkit Package.
     */
    @Contract(pure = true)
    public static String getCraftBukkitPackage() {
        return craft_start;
    }

    /**
     * Get NMS Package.
     * <p>
     * <b>net.minecraft.server.v1_14_R1</b>.MinecraftServer
     *
     * @return NMS Package.
     */
    @Contract(pure = true)
    public static String getNMSPackage() {
        return nms_start;
    }

    /**
     * Get who is invoking your method.
     * <p>
     * See the Thread Stack Trace Here.
     * <table border="1">
     * <tr><th>Plugin</th><th>Calling Class</th><th>Track trace(from value)</th></tr>
     * <tr><td>MXLib</td><th>BukkitToolkit</th><th></th></tr>
     * <tr><td>&lt;Your Plugin&gt;</td><th>&lt;The class invoking this method&gt;</th><th></th></tr>
     * <tr><td>&lt;Your Method Caller Plugin&gt;</td><th>&lt;The class invoking your method&gt;</th><th>0</th></tr>
     * </table>
     * Wel. It’s impossible to understand this.
     * <pre>{@code
     * // Plugin info: "MyPlugin v1.0"
     * class MyService implements Runnable{
     *     public void run(){
     *         // Waiting for the form is right here.
     *         System.out.println(BukkitToolkit.getCallerPlugin(0));
     *     }
     * }
     *
     * // Plugin info: "ProxyPlugin v1.0.0"
     * class ServiceProxy implements Runnable{
     *     public MyService parent;
     *     public void run(){
     *         parent.run();
     *     }
     * }
     *
     * // Plugin info: "OtherPlugin v1.0.1"
     * class ServiceUser extends JavaPlugin{
     *     public void onEnable(){
     *         ServiceProxy service = ....; // It is ServiceProxy.
     *         service.run();
     *     }
     * }
     * }</pre>
     * <table border="1">
     * <tr><th>Plugin</th><th>Calling Class</th><th>Track trace(from value)</th></tr>
     * <tr><td>MXLib</td><th>BukkitToolkit</th><th></th></tr>
     * <tr><td>MyPlugin v1.0</td><th>MyService</th><th></th></tr>
     * <tr><td>ServiceProxy v1.0.0</td><th>ServiceProxy</th><th>0</th></tr>
     * <tr><td>ServiceUser v1.0.1</td><th>ServiceUser</th><th>1</th></tr>
     * <tr><td>[null][Spigot Plugin Loader]</td><th>[Spigot Plugin Class Loader]</th><th>2</th></tr>
     * <tr><td>...</td><th>...</th><th>...</th></tr>
     * </table>
     * Sometimes the stack is also wrapped in several layers of non-plugin agents, like this
     *
     * <table border="1">
     * <tr><th>Plugin</th><th>Calling Class</th><th>Track trace(from value)</th></tr>
     * <tr><td>MXLib</td><th>BukkitToolkit</th><th></th></tr>
     * <tr><td>MyPlugin v1.0</td><th>MyService</th><th></th></tr>
     * <tr style="text-decoration: underline"><td>[Java Runtime.jar]</td><th>sun.reflect....Impl</th><th>0</th></tr>
     * <tr style="text-decoration: underline"><td>[Java Runtime.jar]</td><th>sun.reflect....</th><th>1</th></tr>
     * <tr style="text-decoration: underline"><td>[Java Runtime.jar]</td><th>sun.reflect....</th><th>2</th></tr>
     * <tr style="text-decoration: underline"><td>[Java Runtime.jar]</td><th>sun.reflect....</th><th>3</th></tr>
     * <tr><td>ServiceProxy v1.0.0</td><th>ServiceProxy</th><th>4</th></tr>
     * <tr><td>ServiceUser v1.0.1</td><th>ServiceUser</th><th>5</th></tr>
     * <tr><td>[null][Spigot Plugin Loader]</td><th>[Spigot Plugin Class Loader]</th><th>2</th></tr>
     * <tr><td>...</td><th>...</th><th>...</th></tr>
     * </table>
     * Using getCallerPlugin(0) will return "ServiceProxy v1.0.0".
     * [Java Runtime.jar] not a plugin. Then find the next track stack. into end.
     *
     * @param from Stack lookup starting point
     * @return The plugin at stack [from](or deeper)
     * @since 2.3
     */
    @Nullable
    public static Plugin getCallerPlugin(int from) {
        if (from < 0) {
            return null;
        }
        from += 2;// Skip class BukkitToolkit and getCallerPlugin(int)'s caller.
        Class[] traces = Toolkit.StackTrace.getClassContext();
        for (; from < traces.length; from++) {
            Plugin p = getPluginByClass(traces[from]);
            if (p != null) return p;
        }
        return null;
    }

    /**
     * get your method caller.
     *
     * @return The method caller.
     * @see #getCallerPlugin(int)
     * @since 2.3
     */
    @Nullable
    public static Plugin getCallerPlugin() {
        return getCallerPlugin(1);
    }

    /**
     * Get Class Owner..
     *
     * @param clazz The class will check
     * @return The class owner or null if it is not loaded by org.bukkit.java.plugin.PluginClassLoader.
     */
    @Contract(pure = true)
    @Nullable
    public static Plugin getPluginByClass(@NotNull final Class<?> clazz) {
        try {
            return JavaPlugin.getProvidingPlugin(clazz);
        } catch (Exception ignore) {
        }
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