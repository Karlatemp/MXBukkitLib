/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.system;

import cn.mcres.gyhhy.MXLib.Core;
import cn.mcres.gyhhy.MXLib.RefUtil;
import cn.mcres.karlatemp.mxlib.MXBukkitLib;

import java.lang.instrument.Instrumentation;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

public class VMHelper {

    public static final VMHelper vhelper;
    protected static Instrumentation rcc = null;
    static Instrumentation root = null;

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
            try {
                return new VMHelperImpl();
            } catch (Throwable thr) {
                return new VMHelperImplAPP();
            }
        } catch (Error | RuntimeException thr) {
            if (thr instanceof java.security.AccessControlException
                    || thr instanceof ExceptionInInitializerError || thr instanceof NoClassDefFoundError) {
                MXBukkitLib.getLogger().printStackTrace(thr, ConfSave.vmverbose, true);
            } else {
                MXBukkitLib.getLogger().printStackTrace(thr);
            }
            return new VMHelper();
        }
    }

    static ClassLoader getExt() {
        return RefUtil.getExtClassLoader();
    }

    static Instrumentation rx() {
        if (rcc != null) {
            return rcc;
        }
        try {
            if (root == null) return null;
            Instrumentation i = root;
            return rcc = i;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getLocalizedMessage(), ex);
        }
    }

    VMHelper() {
    }

    /**
     * This method is called by MXBukkitLib System<br>
     * Don't call this method.
     */
    @Deprecated
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
