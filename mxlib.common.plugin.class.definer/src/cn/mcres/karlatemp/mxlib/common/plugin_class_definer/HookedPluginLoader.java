/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: HookedPluginLoader.java@author: karlatemp@vip.qq.com: 2020/1/24 上午12:50@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.plugin_class_definer;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.network.NetManager;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class HookedPluginLoader implements PluginLoader {
    final JavaPluginLoader parent;
    final Server server = Bukkit.getServer();
    static final Field JPL_server;
    static final Field JP_server;
    static final Field JP_file;

    static {
        Field a = null, b = null, c = null;
        try {
            a = JavaPluginLoader.class.getDeclaredField("server");
            b = JavaPlugin.class.getDeclaredField("server");
            c = JavaPlugin.class.getDeclaredField("file");
        } catch (Throwable ignore) {
        }
        JPL_server = a;
        JP_server = b;
        JP_file = c;

    }

    public HookedPluginLoader(JavaPluginLoader parent) {
        this.parent = parent;
    }

    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException {
        String tmProtocol = "xtt" + Long.toHexString(UUID.randomUUID().getMostSignificantBits());
        MXBukkitLib.debug(() -> "[HookedPluginLoader] Loading hooked plugin: " + file + ", " + tmProtocol);
        try {
            var jar = new HookedJarFile(file);
            AtomicBoolean a = new AtomicBoolean(true);
            final HookedJarURLStreamHandler handler = new HookedJarURLStreamHandler(jar) {
                @Override
                protected URLConnection openConnection(URL u) throws IOException {
                    if (a.get())
                        MXBukkitLib.debug("[HPL]\t\tConnection open: " + u);
                    return super.openConnection(u);
                }
            };
            NetManager.registerProtocol(tmProtocol, handler);
            MXBukkitLib.debug("[HPL]\tRegister Temp Protocol[" + tmProtocol + "]");
            try {
                new BukkitHookToolkit.PluginPreLoadEvent(file, jar).post();
                var desc = parent.getPluginDescription(file);
                var entry = jar.getEntry(desc.getMain().replace('.', '/') + ".class");
                if (entry == null)
                    throw new InvalidPluginException("Main Class Not FOUND: " + desc.getMain() + ", " + file);
                InputStream stream = jar.getInputStream(entry);
                ClassReader asm = new ClassReader(stream);
                var node = new ClassNode();
                asm.accept(node, 0);
                var anno = node.visibleAnnotations;
                if (anno != null) {
                    for (var a0 : anno) {
                        if (a0.desc.equals("cn/mcres/karlatemp/common/maven/annotations/MavenLocations")) {
                            var vs = a0.values;
                            if (vs.size() < 2) continue;
                            var list = vs.get(1);
                            BukkitHookToolkit.TKIMPL.REGUSER(((Collection<String>) list).toArray());
                        } else if (a0.desc.equals("cn/mcres/karlatemp/common/maven/annotations/Repositories")) {
                            var vs = a0.values;
                            if (vs.size() < 2) continue;
                            var list = vs.get(1);
                            BukkitHookToolkit.TKIMPL.LOADREPO(((Collection<String>) list).toArray());
                        }
                    }
                }
                //var server = Toolkit.Reflection.getObjectValue(parent, JPL_server);
                //try {
                    /*if (WrappedServerBuilder.constructor != null) {
                        var wrapped = WrappedServerBuilder.constructor.newInstance(server);
                        Toolkit.Reflection.setObjectValue(parent, JPL_server, wrapped);
                    }*/
                var pl = parent.loadPlugin(new File(file.getPath()) {
                    @NotNull
                    @Override
                    public URI toURI() {
                        MXBukkitLib.debug("[HPL]\tFile URI Wrap Sys Load.");
                        return URI.create(tmProtocol + ":/");
                    }

                    @NotNull
                    @Override
                    public String getPath() {
                        for (var stack : new Throwable().getStackTrace()) {
                            if (stack.getClassName().equals("org.bukkit.plugin.java.JavaPluginLoader")) {
                                if (stack.getMethodName().equals("getPluginDescription")) {
                                    return super.getPath();
                                }
                            }
                        }
                        return EmptyJar.getPath();
                    }
                });
                // if (JP_server != null) {
                //     Toolkit.Reflection.setObjectValue(pl, JP_server, server);
                // }
                BukkitHookToolkit.inject(pl, jar);
                Toolkit.Reflection.setObjectValue(pl, JP_file, file);
                new BukkitHookToolkit.PluginLoadEvent(pl, jar).post();
                a.set(false);
                MXBukkitLib.debug(() -> "[HPL]\tPlugin URLs:" + Arrays.toString(((URLClassLoader) pl.getClass().getClassLoader()).getURLs()));
                return pl;
                // } finally {
                //    Toolkit.Reflection.setObjectValue(parent, JPL_server, server);
                // }
            } catch (Throwable any) {
                try {
                    handler.getJar().close();
                } catch (Throwable any0) {
                    any.addSuppressed(any0);
                }
                if (any instanceof RuntimeException) throw (RuntimeException) any;
                if (any instanceof Error) throw (Error) any;
                throw new InvalidPluginException(any);
            } finally {
                MXBukkitLib.debug("[HPL] Remove Temp Protocol[" + tmProtocol + "] " + NetManager.removeProtocol(tmProtocol, handler));
            }
        } catch (IOException ioe) {
            throw new InvalidPluginException(ioe);
        }
    }

    @Override
    public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException {
        return parent.getPluginDescription(file);
    }

    @Override
    public Pattern[] getPluginFileFilters() {
        return parent.getPluginFileFilters();
    }

    @Override
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin) {
        return parent.createRegisteredListeners(listener, plugin);
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        parent.enablePlugin(plugin);
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        parent.disablePlugin(plugin);
    }
}
