/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitToolkit.java@author: karlatemp@vip.qq.com: 19-9-20 下午7:35@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.impl.InternalClasses;
import cn.mcres.karlatemp.mxlib.module.chat.RFT;
import cn.mcres.karlatemp.mxlib.reflect.RMethod;
import cn.mcres.karlatemp.mxlib.reflect.Reflect;
import cn.mcres.karlatemp.mxlib.reflect.WrappedClassImplements;
import cn.mcres.karlatemp.mxlib.tools.MinecraftKey;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.tools.security.AccessToolkit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.messaging.ChannelNameTooLongException;
import org.bukkit.plugin.messaging.Messenger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("deprecation")
public class BukkitToolkit {
    private static final Class<?> PluginClassLoader;
    private static final Field pfield;
    private static final String NMS_ver, nms_start, craft_start;
    private static final Function<ItemStack, Material> itemToMaterial;

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
            AccessToolkit.setAccessible(plugin, true);
            pfield = plugin;
        } catch (Throwable thr) {
            throw new ExceptionInInitializerError(thr);
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
        {
            byte[] code = Base64.getDecoder().decode(InternalClasses.MaterialGetter());
            try {
                //noinspection unchecked
                itemToMaterial = Toolkit.Reflection.defineClass(
                        Toolkit.Reflection.getClassLoader(BukkitToolkit.class), null,
                        code, 0, code.length, BukkitToolkit.class.getProtectionDomain())
                        .asSubclass(Function.class)
                        .getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    /**
     * For 1.13+ Server.
     *
     * @param stack The source ItemStack
     * @return The real material of item stack
     * @since 2.7
     */
    @NotNull
    public static Material getMaterial(ItemStack stack) {
        return itemToMaterial.apply(stack);
    }

    @SuppressWarnings("unchecked")
    public static Collection<Player> getOnlinePlayers() {
        try {
            return (Collection<Player>) Bukkit.getOnlinePlayers();
        } catch (Error methodNotFoundError) {
            return Reflect.ofClass(Bukkit.class).getMethod("getOnlinePlayers", new Class[0])
                    .invoke().cast(Object.class).toOptional()
                    .map(array -> {
                        if (array instanceof Collection) {
                            return (Collection<Player>) array;
                        }
                        Collection<Player> ps = new ArrayList<>();
                        int length = Array.getLength(array);
                        for (int i = 0; i < length; i++) {
                            ps.add((Player) Array.get(array, i));
                        }
                        return ps;
                    }).orElse(null);
        }
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
        final ClassLoader cl = Toolkit.Reflection.getClassLoader(clazz);
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

    /**
     * @param key The key
     * @return The minecraft key
     * @since 2.6
     */
    public static MinecraftKey toMinecraftKey(String key) {
        return MinecraftKey.valueOf(key);
    }

    /**
     * Validates and corrects a Plugin Channel name. Method is not reentrant / idempotent.
     *
     * @param channel Channel name to validate.
     * @return corrected channel name
     */
    @NotNull
    public static String validateAndCorrectChannel(@NotNull String channel) {
        /*
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null");
        }
        */
        // This will correct registrations / outgoing messages
        // It is not legal to send "BungeeCord" incoming anymore so we are fine there,
        // but we must make sure that none of the API methods repeatedly call validate
        if (channel.equals("BungeeCord")) {
            return "bungeecord:main";
        }
        // And this will correct incoming messages.
        if (channel.equals("bungeecord:main")) {
            return "BungeeCord";
        }
        if (channel.length() > Messenger.MAX_CHANNEL_SIZE) {
            throw new ChannelNameTooLongException(channel);
        }
        if (channel.indexOf(':') == -1) {
            throw new IllegalArgumentException("Channel must contain : separator (attempted to use " + channel + ")");
        }
        if (!channel.toLowerCase(Locale.ROOT).equals(channel)) {
            // TODO: use NamespacedKey validation here
            throw new IllegalArgumentException("Channel must be entirely lowercase (attempted to use " + channel + ")");
        }
        return channel;
    }

    public static Plugin getPlugin() {
        return getCallerPlugin();
    }

    public static int getPacketId(@NotNull String packetType) {
        try {
            return GetPacketIdImpl.a(packetType);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.toString());
        }
    }


    private static final Function<Object, Object> getFile$impl;

    static {
//        Toolkit.Reflection.getRoot().findVirtual()
        String cname = "org/bukkit/plugin/java/MXLib$JavaPlugin$FileGetter";
        Function<Object, Object> a;
        try {
            //noinspection unchecked
            a = (Function<Object, Object>) Toolkit.Reflection.allocObject(Class.forName(cname.replace('/', '.')));
        } catch (Throwable err) {
            ClassWriter cw = new ClassWriter(0);
            cw.visit(52, Opcodes.ACC_PUBLIC, cname, null, "java/lang/Object", new String[]{"java/util/function/Function"});
            final MethodVisitor met = cw.visitMethod(Opcodes.ACC_PUBLIC, "apply", "(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
            met.visitCode();
            met.visitVarInsn(Opcodes.ALOAD, 1);
            met.visitTypeInsn(Opcodes.CHECKCAST, "org/bukkit/plugin/java/JavaPlugin");
            met.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/bukkit/plugin/java/JavaPlugin", "getFile", "()Ljava/io/File;", false);
            met.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Object");
            met.visitInsn(Opcodes.ARETURN);
            met.visitMaxs(2, 2);
            met.visitEnd();
            //noinspection unchecked
            a = (Function<Object, Object>) Toolkit.Reflection.allocObject(
                    Toolkit.Reflection.defineClass(JavaPlugin.class.getClassLoader(), cw, null)
            );
        }
        getFile$impl = a;
    }

    /**
     * @param p Get Plugin's file
     * @return The file of plugin
     * @since 2.12
     */
    public static File getFile(JavaPlugin p) {
        return (File) getFile$impl.apply(p);
    }

    public static Plugin getPluginByLoader(ClassLoader jpl) {
        if (PluginClassLoader.isInstance(jpl)) {
            try {
                return (Plugin) pfield.get(jpl);
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    static class GetPacketIdImpl {
        static RMethod<Object, Object> EnumProtocol$a_Packet_EnumProtocol;
        static RMethod<Object, Integer> EnumProtocol$a_EnumProtocolDirection_Packet_Integer;
        static Class<?> EnumProtocolDirection;
        static final ClassLoader loader = Toolkit.Reflection.getClassLoader(GetPacketIdImpl.class);

        static int a(String ptype) throws ClassNotFoundException {
            if (EnumProtocol$a_Packet_EnumProtocol == null) {
                Class<?> EnumProtocol = Toolkit.Reflection.forName(getNMSPackage() + ".EnumProtocol", true, loader),
                        Packet = Toolkit.Reflection.forName(getNMSPackage() + ".Packet", true, loader);
                final Reflect<?> reflect = Reflect.ofClass(EnumProtocol);
                EnumProtocol$a_Packet_EnumProtocol = (RMethod<Object, Object>) reflect.getMethod("a", new Class[]{Packet});
                EnumProtocolDirection = Toolkit.Reflection.forName(getNMSPackage() + ".EnumProtocolDirection", true, loader);
                EnumProtocol$a_EnumProtocolDirection_Packet_Integer = (RMethod<Object, Integer>) reflect.getMethod("a", Integer.class, EnumProtocolDirection, Packet);
            }
            Object packet = Toolkit.Reflection.allocObject(Toolkit.Reflection.loadClassLink(Arrays.asList(getNMSPackage() + "." + ptype, ptype), loader));
            Object protocol = EnumProtocol$a_Packet_EnumProtocol.invoke(packet).get();
            if (protocol == null) throw new UnsupportedOperationException("Unknwon protocol for packet:" + ptype);
            final RMethod<Object, Integer> context = EnumProtocol$a_EnumProtocolDirection_Packet_Integer.newContext().self(protocol);
            for (Object dire : EnumProtocolDirection.getEnumConstants()) {
                Integer result = context.invoke(dire, packet).get();
                if (result != null) {
                    return result;
                }
            }
            return -1;
        }
    }
}
