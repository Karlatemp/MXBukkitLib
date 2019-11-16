/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LoggerInject.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.legacy;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.logging.*;
import cn.mcres.karlatemp.mxlib.share.$MXBukkitLibConfiguration;
import cn.mcres.karlatemp.mxlib.share.MXBukkitLibPluginStartup;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerInject extends PrintStreamLogger implements Listener, EventExecutor {

    private static final LoggerInject instance = new LoggerInject(null, MXBukkitLib.getBeanManager().getOptional(IMessageFactory.class).orElse(new BukkitMessageFactory()), null, System.out, System.err);
    private static final MLoggerHandler handler = new MLoggerHandler(instance);
    private static final RegisteredListener listener = new RegisteredListener(instance, instance, EventPriority.LOWEST, MXBukkitLibPluginStartup.plugin, false) {
        @Override
        public void callEvent(Event event) throws EventException {
            if (event instanceof PluginEnableEvent) {
                PluginEnableEvent pe = (PluginEnableEvent) event;
                final Plugin plugin = pe.getPlugin();
                if (plugin instanceof JavaPlugin) {
                    replaceLogger((JavaPlugin) plugin);
                }
            }
        }
    };
    private static Field lg;
    final static Map<String, String> loggerNameMapping = new HashMap<>(20);

    static {
        Class<?> c = JavaPlugin.class;
        try {
            final Field logger = c.getDeclaredField("logger");
            logger.setAccessible(true);
            lg = logger;
        } catch (Throwable e) {
            instance.printStackTrace(e);
        }
        MXBukkitLib.setLogger(instance);
    }

    private static void replaceLogger(@NotNull JavaPlugin pl) {
        try {
            Logger log = pl.getLogger();
            if (log instanceof PluginMXLibLogger) {
                return;
            }
            PluginMXLibLogger logger = new PluginMXLibLogger(pl, handler);
            if (log != null) {
                final Level lv = log.getLevel();
                if (lv != null) logger.setLevel(lv);
                logger.setFilter(log.getFilter());
                final ResourceBundle resourceBundle = log.getResourceBundle();
                if (resourceBundle != null)
                    logger.setResourceBundle(resourceBundle);
            }
            lg.set(pl, logger);
            if (log instanceof PluginLogger) {
                log.setUseParentHandlers(false);
                log.addHandler(new Handler() {
                    @Override
                    public void publish(LogRecord record) {
                        logger.log(record);
                    }

                    @Override
                    public void flush() {
                    }

                    @Override
                    public void close() throws SecurityException {
                    }
                });
                log.setLevel(Level.ALL);
            }
        } catch (Throwable thr) {
            instance.printStackTrace(thr);
        }
    }

    public LoggerInject(Object lock, IMessageFactory factory, PrefixSupplier prefix, @NotNull PrintStream out, @NotNull PrintStream err) {
        super(lock, factory, prefix, out, err);
    }

    static final Logger root;
    static final List<Handler> handlers = new ArrayList<>();

    static {
        Logger root_ = Logger.getGlobal(), tmp = root_;
        while (tmp != null) {
            root_ = tmp;
            tmp = tmp.getParent();
        }
        root = root_;
        handler.setLevel(Level.ALL);
    }

    static void run(boolean a) {
        if (a) {
            boolean add = true;
            for (Handler h : root.getHandlers()) {
                if (h != handler) {
                    root.removeHandler(h);
                    handlers.add(h);
                } else {
                    add = false;
                }
            }
            if (add) root.addHandler(handler);
            for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
                if (p instanceof JavaPlugin) {
                    replaceLogger((JavaPlugin) p);
                }
            }
            PluginEnableEvent.getHandlerList().register(listener);
        } else {
            root.removeHandler(handler);
            for (Handler h : handlers) {
                if (h != handler)
                    root.addHandler(h);
            }
            handlers.clear();
        }
    }

    private static int max = 6, pmax = 5;

    @SuppressWarnings("Duplicates")
    private static synchronized String pne(String pn) {
        if (!$MXBukkitLibConfiguration.configuration.logger.alignment) return pn;
        int ln = pn.length() - 2; // The Prefix §.
        if (ln >= pmax) {
            pmax = ln;
        } else {
            char[] c = new char[pmax + 2]; // Store prefix
            int left = (pmax - ln) / 2;
            Arrays.fill(c, ' ');
            System.arraycopy(pn.toCharArray(), 0, c, left, ln + 2);
            return new String(c);
        }
        return pn;
    }

    @SuppressWarnings("Duplicates")
    private static synchronized String lvv(Level lv) {
        String n = lv.getName();
        if (!$MXBukkitLibConfiguration.configuration.logger.alignment) return n;
        int ln = n.length();
        if (ln >= max) {
            max = ln;
        } else {
            char[] c = new char[max];
            int left = (max - ln) / 2;
            Arrays.fill(c, ' ');
            System.arraycopy(n.toCharArray(), 0, c, left, ln);
            return new String(c);
        }
        return n;
    }

    private String map(LogRecord lr) {
        if (lr instanceof PluginLogRecord) {
            return ((PluginLogRecord) lr).getPrefix();
        }
        String lname = lr.getLoggerName();
        if (loggerNameMapping.containsKey(lname)) return loggerNameMapping.get(lname);
        return "§d" + lname;
    }

    @NotNull
    @Override
    protected String getPrefix(boolean error, String line, Level lv, LogRecord lr) {
        if (lr == null) {
            return "§r[" + pne("§6" + Thread.currentThread().getName()) + "§r] §b";
        }
        if (lv == null) {
            return "§r[" + pne(map(lr)) + "§r] §b";
        }

        return "§r[" + pne(map(lr)) + "§r] [§6" + lvv(lv) + "§r] §b";
    }

    static void inject() {
        MXBukkitLibPluginStartup.hooks.add(LoggerInject::run);
        run(true);
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {

    }
}
