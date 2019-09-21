/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MXBukkitLib.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.logging.ILogger;
import cn.mcres.karlatemp.mxlib.logging.MessageFactoryAnsi;
import cn.mcres.karlatemp.mxlib.logging.PrintStreamLogger;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.logging.*;

/**
 * MXBukkitLib核心类, 需要先执行
 * cn.mcres.karlatemp.mxlib.MXLib.boot();
 */
@ProhibitBean
public class MXBukkitLib {
    /**
     * Use this field to get the version you using
     * <p>
     * 使用此字段获取你所使用的MXBukkitLib版本
     */
    public static final String BUILD_VERSION = "2.1.3";
    private static final boolean DEBUG = System.getProperty("mxlib.debug") != null;

    public static String getCurrentVersion() {
        return BUILD_VERSION;
    }

    public static void main(String[] args) {
        System.out.println("MXBukkitLib v" + getCurrentVersion());
        System.out.println("Copyright Karlatemp.");
    }

    private static IBeanManager beanManager;

    /**
     * 获取BeanManager, Lib核心
     * @return 管理核心
     */
    public static IBeanManager getBeanManager() {
        return beanManager;
    }

    private static ILogger logger;

    public static ILogger getLogger() {
        if (logger == null)
            return logger = new PrintStreamLogger(new MessageFactoryAnsi(), "[MXBukkitLib] ", System.out);
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
