/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.system;

import java.lang.instrument.Instrumentation;
import cn.mcres.gyhhy.MXLib.bukkit.Plugin;

public class VMHelper {

    public static final VMHelper vhelper;

    static {
        vhelper = load();
    }

    public static VMHelper getHelper() {
        return vhelper;
    }

    public static VMHelper load() {
        if (vhelper != null) {
            return vhelper;
        }
        try {
            VMHelperImpl.check();
            return new VMHelperImpl();
        } catch (Error | RuntimeException thr) {
            if (thr instanceof java.security.AccessControlException
                    || thr instanceof java.lang.ExceptionInInitializerError) {
                try {
                    Plugin.getLoggerEX().printStackTrace(thr, false);
                } catch (Throwable thrx) {
                    System.err.println(thrx);
                    System.err.println(thr);
                }
            } else {
                try {
                    Plugin.getLoggerEX().printStackTrace(thr);
                } catch (Throwable thrx) {
                    thrx.printStackTrace();
                    thr.printStackTrace();
                }
            }
            return new VMHelper();
        }
    }

    VMHelper() {
    }

    /**
     * This method is called by MXBukkitLib System<br>
     * Don't call this method.
     */
    @java.lang.Deprecated
    public void onDisable() {
    }

    public Instrumentation getInstrumentation() {
        return null;
    }

    public boolean classCkeckBoot(String name) {
        ClassLoader etc = ClassLoader.getSystemClassLoader();
        while (true) {
            ClassLoader parent = etc.getParent();
            if (parent == null) {
                break;
            }
            etc = parent;
        }
        return classCheck(name, etc);
    }

    public boolean classCheck(String name, ClassLoader loader) {
        try {
            loader.loadClass(name);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean classCheck(String name) {
        return classCheck(name, ClassLoader.getSystemClassLoader());
    }
}
