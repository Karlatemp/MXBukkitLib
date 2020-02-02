/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitHookToolkit.java@author: karlatemp@vip.qq.com: 2020/1/20 下午8:53@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.plugin_class_definer;

import cn.mcres.karlatemp.common.maven.*;
import cn.mcres.karlatemp.common.maven.annotations.MavenLocations;
import cn.mcres.karlatemp.common.maven.annotations.Repositories;
import cn.mcres.karlatemp.common.maven.exceptions.RepositoryNotFoundException;
import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.event.HandlerList;
import cn.mcres.karlatemp.mxlib.logging.*;
import cn.mcres.karlatemp.mxlib.network.NetManager;
import cn.mcres.karlatemp.mxlib.reflect.MagicAccessorMarkerLoader;
import cn.mcres.karlatemp.mxlib.reflect.RField;
import cn.mcres.karlatemp.mxlib.reflect.Reflect;
import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import cn.mcres.karlatemp.mxlib.share.MXBukkitLibPluginStartup;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class BukkitHookToolkit {
    private static final BukkitHookToolkit instance;

    public static BukkitHookToolkit getInstance() {
        return instance;
    }

    static void inject(Plugin pl, HookedJarFile hooked) {
        instance.inject0(pl, hooked);
    }

    protected void inject0(Plugin pl, HookedJarFile hooked) {
    }

    public void LOADPLUGIN(Plugin p) throws IOException, RepositoryNotFoundException {
    }

    protected void boot() throws Exception {
    }

    protected HookedJarFile hooked(Class<?> caller) {
        return null;
    }

    public static void install() {
        try {
            Class.forName(BukkitHookToolkit.class.getName());
        } catch (ClassNotFoundException ignore) {
        }
    }

    static {
        MagicAccessorMarkerLoader.load();
        BukkitHookToolkit hook = new BukkitHookToolkit();
        try {
            Class.forName("org.bukkit.Bukkit").getMethod("getLogger").invoke(null);
            hook = new TKIMPL();
        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }
        instance = hook;
        try {
            hook.boot();
        } catch (Throwable err) {
            Bukkit.getLogger().log(Level.SEVERE, "[MXLib] Error in load class changer.", err);
        }
    }

    public static HookedJarFile getListener() {
        return instance.hooked(Toolkit.Reflection.getCallerClass());
    }

    public static HookedJarFile getListener(@NotNull Class<?> target) {
        return instance.hooked(target);
    }

    public static class PluginPreLoadEvent extends cn.mcres.karlatemp.mxlib.event.Event {
        public static final HandlerList<PluginPreLoadEvent> handlers = new HandlerList<>();
        private final HookedJarFile jar;

        public HookedJarFile getJar() {
            return jar;
        }

        public PluginPreLoadEvent(File target, HookedJarFile jar) {
            this.target = target;
            this.jar = jar;
        }

        @Override
        public HandlerList<PluginPreLoadEvent> getHandlerList() {
            return handlers;
        }

        private final File target;

        public File getTarget() {
            return target;
        }
    }

    public static class PluginLoadEvent extends cn.mcres.karlatemp.mxlib.event.Event {
        public static final HandlerList<PluginLoadEvent> handlers = new HandlerList<>();
        private final HookedJarFile jar;

        public HookedJarFile getJar() {
            return jar;
        }

        public PluginLoadEvent(Plugin target, HookedJarFile jar) {
            this.target = target;
            this.jar = jar;
        }

        @Override
        public HandlerList<PluginLoadEvent> getHandlerList() {
            return handlers;
        }

        private final Plugin target;

        public Plugin getTarget() {
            return target;
        }
    }

    static class TKIMPL extends BukkitHookToolkit implements Listener, EventExecutor {
        static RepositoriesLink FULL = new RepositoriesLink(), USER_REMOTE = new RepositoriesLink(), SYS_FINALLY_REMOTE = new RepositoriesLink();
        static LocalRepositories LOCALHOST = new LocalRepositories(new File(System.getProperty("user.home"), ".m2/repository"));
        static MavenLoader loader;
        private static final ILogger MLG;

        static {
            var ML = new AbstractBaseLogger(new MessageFactoryImpl()) {
                @Override
                protected void writeLine(String pre, String message, boolean error) {
                }

                @Override
                protected @NotNull String getPrefix(boolean error, String line, Level level, LogRecord lr) {
                    return "";
                }

                @Override
                public @NotNull ILogger publish(LogRecord record, Handler handler) {
                    MXBukkitLib.getLogger().publish(record, handler);
                    return this;
                }
            };
            MLG = ML;
            loader = new MavenLoader(MavenLoader.URLClassLoaderAddURL((URLClassLoader) TKIMPL.class.getClassLoader()));
            LOCALHOST.logger = new MLogger("Localhost Repo", null, ML);
            if (MXBukkitLib.DEBUG)
                LOCALHOST.logger.setLevel(Level.ALL);
            FULL.register(LOCALHOST);
            FULL.register(USER_REMOTE);
            FULL.register(SYS_FINALLY_REMOTE);
            try {
                SYS_FINALLY_REMOTE.register(new RemoteRepositories(new LocalRepositories(LOCALHOST), new URL(RemoteRepositories.ALIYUN), new MLogger("Aliyun Remote Repo", null, ML)));
                SYS_FINALLY_REMOTE.register(new RemoteRepositories(new LocalRepositories(LOCALHOST), new URL(RemoteRepositories.DEFAULT), new MLogger("Default Remote Repo", null, ML)));
            } catch (MalformedURLException e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        @Override
        protected void inject0(Plugin p, HookedJarFile hooked) {
            if (p instanceof JavaPlugin) {
                ClassLoader loader = Toolkit.Reflection.getClassLoader(p.getClass());
                if (loader instanceof URLClassLoader) {
                    var l = (URLClassLoader) loader;
                    hook(l, p, hooked);
                }
            }
        }

        static void LOADREPO(Object[] ox) throws IOException, RepositoryNotFoundException {
            for (var t : ox) {
                MXBukkitLib.debug(() -> "[Maven] Loading " + t);
                loader.append(FULL.loadRepo(String.valueOf(t)));
            }
        }

        static void REGUSER(Object[] ox) throws MalformedURLException {
            rt:
            for (var mav0 : ox) {
                var mav = String.valueOf(mav0);
                for (var repo : USER_REMOTE.repositories) {
                    if (repo instanceof RemoteRepositories) {
                        if (((RemoteRepositories) repo).base.toExternalForm().equalsIgnoreCase(mav))
                            continue rt;
                    }
                }
                var log = USER_REMOTE.register(new RemoteRepositories(new LocalRepositories(LOCALHOST), new URL(mav), new MLogger(
                        "User Remote Repos", null, MLG
                )));
                if (MXBukkitLib.DEBUG) {
                    log.logger.setLevel(Level.ALL);
                    MXBukkitLib.getLogger().println("Maven Repos Register: " + mav);
                }
            }
        }

        @Override
        public void LOADPLUGIN(Plugin p) throws IOException, RepositoryNotFoundException {
            var loc = p.getClass().getAnnotation(MavenLocations.class);
            if (loc != null) {
                REGUSER(loc.value());
            }
            var repos = p.getClass().getAnnotation(Repositories.class);
            if (repos != null) {
                LOADREPO(repos.value());
            }
            if (p instanceof JavaPlugin) {
                ClassLoader loader = Toolkit.Reflection.getClassLoader(p.getClass());
                if (loader instanceof URLClassLoader) {
                    var l = (URLClassLoader) loader;
                    hook(l, p, null);
                }
            }
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override
        protected void boot() throws Exception {
            {
                Class.forName("cn.mcres.karlatemp.mxlib.common.plugin_class_definer.HookedJarFile");
                Class.forName("cn.mcres.karlatemp.mxlib.common.plugin_class_definer.HookedJarFile$InputStreamResolver");
                Class.forName("cn.mcres.karlatemp.mxlib.common.plugin_class_definer.HookedJarFile$InputStreamSuppler");
                Class.forName("cn.mcres.karlatemp.mxlib.common.plugin_class_definer.HookedJarFile$JarStreamGetEvent");
                Class.forName("cn.mcres.karlatemp.mxlib.event.HandlerList");
                Class.forName("cn.mcres.karlatemp.mxlib.event.EventHandler");
                Class.forName("cn.mcres.karlatemp.mxlib.common.plugin_class_definer.HookedJarURLStreamHandler");
                Class.forName("cn.mcres.karlatemp.mxlib.common.plugin_class_definer.HookedJarURLStreamHandler$JRConnection");
                Class.forName("cn.mcres.karlatemp.mxlib.common.plugin_class_definer.HookedJarURLStreamHandler$JRConnection");
                Class.forName("cn.mcres.karlatemp.mxlib.common.plugin_class_definer.HookToolkit");
                Class.forName("cn.mcres.karlatemp.mxlib.common.plugin_class_definer.BukkitHookToolkit$PluginLoadEvent");
                Class.forName("cn.mcres.karlatemp.mxlib.common.plugin_class_definer.BukkitHookToolkit$PluginPreLoadEvent");
                Class.forName("cn.mcres.karlatemp.mxlib.common.plugin_class_definer.HookedPluginLoader");
                Class.forName("cn.mcres.karlatemp.mxlib.tools.CharCompiler");
                Class.forName("cn.mcres.karlatemp.mxlib.common.plugin_class_definer.BukkitHookedPluginOutput", true, BukkitToolkit.class.getClassLoader());
            }
            BukkitHookedPluginOutput.load();
            for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
                LOADPLUGIN(p);
            }
            var spm = (SimplePluginManager) Bukkit.getServer().getPluginManager();
            var mp = (Map<Object, Object>) Reflect.ofClass(SimplePluginManager.class).getField("fileAssociations").self(spm)
                    .get();
            MXBukkitLibPluginStartup.hooks.add(isEnable -> {
                if (isEnable) {
                    for (var ent : mp.entrySet()) {
                        var ld = ent.getValue();
                        if (ld instanceof JavaPluginLoader) {
                            var jpl = (JavaPluginLoader) ld;
                            ent.setValue(new HookedPluginLoader(jpl));
                        }
                    }
                } else {
                    for (var ent : mp.entrySet()) {
                        var ld = ent.getValue();
                        if (ld instanceof HookedPluginLoader) {
                            ent.setValue(((HookedPluginLoader) ld).parent);
                        }
                    }
                }
            });
            for (var ent : mp.entrySet()) {
                var ld = ent.getValue();
                if (ld instanceof JavaPluginLoader) {
                    var jpl = (JavaPluginLoader) ld;
                    ent.setValue(new HookedPluginLoader(jpl));
                }
            }

        }

        @Override
        public void execute(Listener listener, Event event) throws EventException {
            if (event instanceof PluginEnableEvent) {
                var enb = (PluginEnableEvent) event;
                var pl = enb.getPlugin();
                if (pl instanceof JavaPlugin) {
                    var cl = Toolkit.Reflection.getClassLoader(pl.getClass());
                    if (cl instanceof URLClassLoader) {
                        hook((URLClassLoader) cl, pl, null);
                    }
                }
            }
        }

        private static RField<URLClassLoader, JarFile> pcl_jar;

        static {
            try {
                @SuppressWarnings("Java9ReflectionClassVisibility")
                var pcl = Class.forName("org.bukkit.plugin.java.PluginClassLoader").asSubclass(URLClassLoader.class);
                //noinspection unchecked,rawtypes
                pcl_jar = ((Reflect) Reflect.ofClass(pcl)).getField("jar", JarFile.class);
            } catch (Throwable any) {
                throw new ExceptionInInitializerError(any);
            }
        }

        private void hook(URLClassLoader l, Plugin p, HookedJarFile hd) {
            if (hd != null) { // force load.
                URL first = l.getURLs()[0];
                if (!(NetManager.getHandler(first) instanceof HookedJarURLStreamHandler))
                    HookToolkit.replace(l, first, HookToolkit.openHook(hd), hd);
                hookJPL(l, hd);
                return;
            }
            if (hooked(p.getClass()) != null) return;
            try {
                URL first = l.getURLs()[0];
                HookedJarFile hook = new HookedJarFile(BukkitToolkit.getFile((JavaPlugin) p));
                HookToolkit.replace(l, first, HookToolkit.openHook(hook), hook);
                hookJPL(l, hook);
            } catch (IOException any) {
                p.getLogger().log(Level.SEVERE, "[MXLib] [ASM] Error in init code changer.", any);
            }
        }

        private void hookJPL(URLClassLoader l, HookedJarFile hook) {
            if (pcl_jar != null) {
                pcl_jar.newContext().self(l).set(hook);
            }
        }

        @Override
        protected HookedJarFile hooked(Class<?> caller) {
            final Plugin plugin = BukkitToolkit.getPluginByClass(caller);
            if (plugin instanceof JavaPlugin) {
                var urls = ((URLClassLoader) Toolkit.Reflection.getClassLoader(plugin.getClass())).getURLs();
                for (var url : urls) {
                    var provider = NetManager.getHandler(url);
                    if (provider instanceof HookedJarURLStreamHandler) {
                        var jr = ((HookedJarURLStreamHandler) provider).getJar();
                        if (jr instanceof HookedJarFile) return (HookedJarFile) jr;
                    }
                }
            }
            return null;
        }
    }
}
