/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: UnsafeInstaller.java@author: karlatemp@vip.qq.com: 19-11-22 下午8:22@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.internal;

import cn.mcres.karlatemp.mxlib.reflect.RMethod;
import cn.mcres.karlatemp.mxlib.reflect.Reflect;
import cn.mcres.karlatemp.mxlib.tools.Unsafe;

/**
 * The unsafe install system. Call by Unsafe(MXLib Tool)
 *
 * @see Unsafe
 * @since 2.7
 */
public class UnsafeInstaller {
    public static Unsafe install() {
        try {
            Class.forName("java.lang.Module");// if over Java9
            // Java 9
            return U9Install.install();
        } catch (ClassNotFoundException ignore) {
        }
        return new SunUnsafe();
    }

    @SuppressWarnings("Since15")
    static class U9Install {
        static Unsafe install() {
            Module java_base = Object.class.getModule();
            Module current = U9Install.class.getModule();
            current.addReads(java_base);
            final RMethod<Module, Void> addExports = Reflect.ofObject(java_base).getMethod("implAddExports", void.class, String.class, Module.class);
            addExports.invoke("jdk.internal.misc", current);
            addExports.invoke("jdk.internal.access", current);
            return new JDKUnsafe();
        }
    }
}