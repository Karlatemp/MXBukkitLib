package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.logging.ILogger;
import cn.mcres.karlatemp.mxlib.logging.MessageFactoryAnsi;
import cn.mcres.karlatemp.mxlib.logging.PrintStreamLogger;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@ProhibitBean
public class MXBukkitLib {
    /**
     * Use this field to get the version you using
     * <p>
     * 使用此字段获取你所使用的MXBukkitLib版本
     */
    public static final String BUILD_VERSION = "2.0";
    private static final boolean DEBUG = System.getProperty("mxlib.debug") != null;

    public static String getCurrentVersion() {
        return BUILD_VERSION;
    }

    public static void main(String[] args) {
        System.out.println("MXBukkitLib v" + getCurrentVersion());
        System.out.println("Copyright Karlatemp.");
    }

    private static IBeanManager beanManager;

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
}
