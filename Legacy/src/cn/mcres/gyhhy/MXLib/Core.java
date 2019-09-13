/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Core.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:59@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib;

import cn.mcres.gyhhy.MXLib.http.URLStreamManager;
import cn.mcres.gyhhy.MXLib.log.BasicLogger;
import cn.mcres.gyhhy.MXLib.ext.lookup.Looker;
import cn.mcres.gyhhy.MXLib.log.LoggerHandler;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.invoke.MethodHandle;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.BasicPermission;
import java.security.Permission;
import java.util.Enumeration;
import java.util.Locale;
import java.util.logging.*;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.logging.ILogger;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Core {

    //    public static final Permission CORE_SET_BASIC_LOGGER = new MXLibPermission("core.setBasicLogger")
//            CORE_SET_LOGGER = new MXLibPermission("core.setLogger");
    private static BasicLogger bl = null;
    private static Logger logger = null;

    private static final String JRV = System.getProperty("java.runtime.version");
    /*
    @SuppressWarnings("PublicInnerClass")
    public static class MXLibPermission extends BasicPermission {

    private static final long serialVersionUID = 15547418795486L;

    public MXLibPermission(String name, String actions) {
    super(name, actions);
    }

    public MXLibPermission(String name) {
    super(name);
    }
    }*/
    private static Plugin plugin = null;

    public static void setPlugin(Plugin pl) {
        if (plugin == pl) return;
        if (plugin != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Plugin");
        }
        plugin = pl;
    }

    public static Logger getLogger() {
        if (logger == null) {
            logger = new Logger("MXBukkitLib", null) {
            };
        }
        return logger;
    }

    public static void setLogger(Logger log) {
        log.getClass();
        logger = log;
    }

    private static ILogger getRoot() {
        return MXBukkitLib.getLogger();
    }

    /**
     * {@link MXBukkitLib#getLogger()}
     */
    @Deprecated
    public static BasicLogger getBL() {
        if (bl == null) {
            return bl = new BasicLogger((String) null, null, "MXBukkitLib") {
                public String getStackTraceElementMessage(StackTraceElement track) {
                    return getRoot().getStackTraceElementMessage(track);
                }

                @NotNull
                public BasicLogger println(String line) {
                    getRoot().println(line);
                    return this;
                }

                @Override
                public PrintStream getErrorPrintStream() {
                    return getRoot().getErrorStream();
                }

                @NotNull
                public BasicLogger printf(Object data) {
                    getRoot().printf(data);
                    return this;
                }

                @NotNull
                public ILogger printf(boolean err, String ln) {
                    return getRoot().printf(err, ln);
                }

                @NotNull
                public BasicLogger printf(String line) {
                    getRoot().printf(line);
                    return this;
                }

                @NotNull
                public BasicLogger error(String line) {
                    getRoot().error(line);
                    return this;
                }

                @NotNull
                public BasicLogger error(Object line) {
                    getRoot().error(line);
                    return this;
                }

                @NotNull
                public BasicLogger format(String format, Object... args) {
                    getRoot().format(format, args);
                    return this;
                }

                @NotNull
                public BasicLogger format(Locale locale, String format, Object... args) {
                    getRoot().format(locale, format, args);
                    return this;
                }

                @NotNull
                public BasicLogger errformat(String format, Object... args) {
                    getRoot().errformat(format, args);
                    return this;
                }

                @NotNull
                public BasicLogger errformat(Locale locale, String format, Object... args) {
                    getRoot().errformat(locale, format, args);
                    return this;
                }

                @NotNull
                public PrintStream getPrintStream() {
                    return getRoot().getPrintStream();
                }

                @NotNull
                public PrintStream getErrorStream() {
                    return getRoot().getErrorStream();
                }

                @NotNull
                public BasicLogger printThreadInfo(@NotNull ThreadInfo info, boolean fullFrames, boolean emptyPrefix) {
                    getRoot().printThreadInfo(info, fullFrames, emptyPrefix);
                    return this;
                }

                @NotNull
                public BasicLogger printThreadInfo(@NotNull Thread thread, boolean fullFrames, boolean emptyPrefix) {
                    getRoot().printThreadInfo(thread, fullFrames, emptyPrefix);
                    return this;
                }

                @NotNull
                public BasicLogger printStackTrace(@NotNull Throwable thr) {
                    getRoot().printStackTrace(thr);
                    return this;
                }

                @NotNull
                public BasicLogger printStackTrace(@NotNull Throwable thr, boolean printStacks, boolean isError) {
                    getRoot().printStackTrace(thr, printStacks, isError);
                    return this;
                }

                @NotNull
                public BasicLogger publish(LogRecord record, Handler handler) {
                    getRoot().publish(record, handler);
                    return this;
                }
            };
        }
        return bl;
    }

    private static <T> T invoke(MethodHandle setter, Object value) {
        try {
            return (T) setter.invoke(value);
        } catch (Throwable ex) {
            return ThrowHelper.getInstance().thr(ex);
        }
    }

    @Deprecated
    public static void setBL(BasicLogger bl) {
//        AccessController.checkPermission(CORE_SET_BASIC_LOGGER);
        bl.getClass();
        Core.bl = bl;
    }


    public static String getVersion() {
        return cn.mcres.gyhhy.MXLib.bukkit.MXAPI.version;
    }

    public static String getJarVersion(Class<?> c) {
        ClassLoader cl = c.getClassLoader();
        if (cl == null) {
            return getJavaVersion();
        }
        if (cl.getClass().getName().equals("org.bukkit.java.plugin.PluginClassLoader")) {
            return cn.mcres.gyhhy.MXLib.log.Logger.getPluginClassLoaderPlugin(cl).getDescription().getVersion();
        }
        /*if (cl instanceof URLClassLoader) {
            URLClassLoader uc = (URLClassLoader) cl;
            try {
                //            java.util.jar.Mani
                Enumeration<URL> rs = uc.getResources("META-INF/MANIFEST.MF");
                while (rs.hasMoreElements()) {
                    URL ur = rs.nextElement();
                    System.out.println(ur);
                }
            } catch (IOException ex) {
                Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        return null;
    }

    public static String getJavaVersion() {
        return JRV;
    }

    public static Plugin getPlugin() {
        if (plugin == null) {
            setPlugin(new BasePlugin() {

                @Override
                public Logger getLogger() {
                    return Core.getLogger();
                }

                @Override
                public String getName0() {
                    return "MXBukkitLib/StaticMode";
                }

            });
        }
        return plugin;
    }
}
