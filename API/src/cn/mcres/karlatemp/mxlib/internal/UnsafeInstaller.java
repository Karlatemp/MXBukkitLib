/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: UnsafeInstaller.java@author: karlatemp@vip.qq.com: 19-11-22 下午8:22@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.internal;

import cn.mcres.karlatemp.mxlib.reflect.RMethod;
import cn.mcres.karlatemp.mxlib.reflect.Reflect;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.tools.Unsafe;

/**
 * The unsafe install system. Call by Unsafe(MXLib Tool)
 *
 * @see Unsafe
 * @since 2.7
 */
public class UnsafeInstaller {
    private static boolean j9;

    static {
        j9 = false;
        try {
            Class.forName("java.lang.Module");// if over Java9
            // Java 9
            U9Install.open();
            j9 = true;
        } catch (ClassNotFoundException ignore) {
        }
    }

    public static Unsafe install() {
        if (j9) return U9Install.install();
        return new SunUnsafe();
    }

    public static Toolkit.Reflection installReflection() {
        if (j9) return new J9RF();
        return new InternalDefineRF();
    }

    @SuppressWarnings("Since15")
    static class U9Install {
        static void open() {
            Module java_base = Object.class.getModule();
            Module current = U9Install.class.getModule();
            current.addReads(java_base);
            final RMethod<Module, Void> addExports = Reflect.ofObject(java_base).getMethod("implAddExports", void.class, String.class, Module.class);
            addExports.invoke("jdk.internal.misc", current);
            addExports.invoke("jdk.internal.access", current);
        }

        static Unsafe install() {
            return new JDKUnsafe();
        }
    }
}