/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MXBukkitLib.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.event.core.MXLibBootEvent;
import cn.mcres.karlatemp.mxlib.logging.ILogger;
import cn.mcres.karlatemp.mxlib.logging.MessageFactoryAnsi;
import cn.mcres.karlatemp.mxlib.logging.PrefixSupplier;
import cn.mcres.karlatemp.mxlib.logging.PrintStreamLogger;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.logging.*;

/**
 * MXBukkitLib核心类, 需要先执行
 * cn.mcres.karlatemp.mxlib.MXLib.boot();
 */
@ProhibitBean
public class MXBukkitLib {
    /**
     * Unsafe value. Only run from your main(String[]) should change.
     * Configurations with tags here will not be loaded
     *
     * @since 2.2.
     */
    @Deprecated
    public static final List<String> disableConfigurations = new ArrayList<>();
    /**
     * A list of Bukkit System.
     * <pre>{@code
     *    MXBukkitLib.disableConfigurations.addAll(
     *      MXBukkitLib.BukkitDisableConfigurations
     *    );
     * }</pre>
     */
    @Deprecated
    public static final List<String> BukkitDisableConfigurations = Collections.unmodifiableList(
            Arrays.asList(
                    "cn.mcres.karlatemp.mxlib.share.PluginAutoConfig",
                    "cn.mcres.karlatemp.mxlib.impl.ImplSetupAutoConfig",
                    "cn.mcres.gyhhy.MXLib.legacy.LegacyAutoConfig")
    );
    /**
     * Use this field to get the version you using
     * <p>
     * 使用此字段获取你所使用的MXBukkitLib版本
     */
    public static final String BUILD_VERSION = "2.14.7";
    /**
     * Is MXLib Debug Mode open
     *
     * @since 2.12
     */
    public static final boolean DEBUG = System.getProperty("mxlib.debug") != null;

    public static String getCurrentVersion() {
        return BUILD_VERSION;
    }

    public static synchronized void boot() {
        if (beanManager != null) return;
        MXLibBootProvider provider = null;
        try {
            String s = System.getProperty("mxlib.provider");
            if (s != null)
                provider = Toolkit.Reflection.setAccess(Toolkit.Reflection.loadClassWith(
                        s, Toolkit.Reflection.LOAD_CLASS_CALLER_CLASSLOADER | Toolkit.Reflection.LOAD_CLASS_THREAD_CONTENT
                ).asSubclass(MXLibBootProvider.class).getDeclaredConstructor(), true).newInstance();
        } catch (Throwable dump) {
            dump.printStackTrace();
        }
        List<MXLibBootProvider> providerList = new ArrayList<>();
        final ServiceLoader<MXLibBootProvider> service = ServiceLoader.load(MXLibBootProvider.class, Toolkit.Reflection.getClassLoader(MXBukkitLib.class));
        service.reload();
        for (MXLibBootProvider p : service) {
            providerList.add(p);
        }
        providerList.sort(Comparator.comparingInt(MXLibBootProvider::getPrioritySafe));
        if (provider != null)
            providerList.add(0, provider);
        boot(providerList);
    }

    public static synchronized void boot(Collection<MXLibBootProvider> providers) {
        if (beanManager != null) return;
        providers = MXLibBootEvent.post(new MXLibBootEvent(providers)).getProviders();
        for (MXLibBootProvider p : providers) {
            if (p != null) {
                if (beanManager == null)
                    p.setBeanManager();
                else break;
            }
        }
        for (MXLibBootProvider p : providers) {
            if (p != null) p.boot();
        }
    }

    public static void main(String[] args) {
        System.out.println("MXBukkitLib v" + getCurrentVersion());
        System.out.println("Copyright Karlatemp.");
    }

    private static IBeanManager beanManager;

    /**
     * 获取BeanManager, Lib核心
     *
     * @return 管理核心
     */
    public static IBeanManager getBeanManager() {
        return beanManager;
    }

    private static ILogger logger;

    public static ILogger getLogger() {
        if (logger == null)
            return logger = new PrintStreamLogger(new MessageFactoryAnsi(), new PrefixSupplier() {
                @Override
                public @NotNull String get(boolean error, @Nullable String line, @Nullable Level level, @Nullable LogRecord record) {
                    if (record != null) {
                        var logger = record.getLoggerName();
                        if (logger != null) {
                            return "[MXBukkitLib] [" + logger + "] ";
                        }
                    }
                    return "[MXBukkitLib] ";
                }
            }, System.out);
        return logger;
    }

    public static void setLogger(ILogger logger) {
        MXBukkitLib.logger = logger;
    }

    public static synchronized void setBeanManager(@NotNull IBeanManager bm) {
        if (beanManager != null) throw new IllegalArgumentException("BeanManager was Initialized.");
        beanManager = bm;
    }

    public static synchronized void debug(String message) {
        if (DEBUG) {
            getLogger().printf(message);
        }
    }

    public static synchronized void debug(Supplier<String> message) {
        if (DEBUG) {
            getLogger().printf(message.get());
        }
    }

    private static class MLogger extends Logger {
        private static final MLogger instance = new MLogger();

        private static class MHandler extends Handler {
            private static final MHandler instance = new MHandler();

            {
                setLevel(Level.INFO);
                setFormatter(new SimpleFormatter());
            }

            @Override
            public void publish(LogRecord record) {
                if (isLoggable(record))
                    MXBukkitLib.getLogger().publish(record, this);
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        }

        protected MLogger() {
            super("MXBukkitLib", null);
        }

        @Override
        public void log(LogRecord record) {
            MXBukkitLib.getLogger().publish(record, MHandler.instance);
        }

        @Override
        public void setUseParentHandlers(boolean useParentHandlers) {
            throw new UnsupportedOperationException("setUserparentHandlers(B)V");
        }

        @Override
        public boolean getUseParentHandlers() {
            return false;
        }
    }

    public static Logger getAsJavaLogger() {
        return MLogger.instance;
    }
}
