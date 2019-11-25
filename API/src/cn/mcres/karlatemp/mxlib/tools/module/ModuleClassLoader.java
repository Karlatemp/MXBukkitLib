/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ModuleClassLoader.java@author: karlatemp@vip.qq.com: 19-11-23 下午10:59@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools.module;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Module Class Loader
 * <p>
 * It can decide if the external can access the protection class.
 *
 * @since 2.7
 */
public class ModuleClassLoader extends ClassLoader {
    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();
    private final ClassLoader parent;
    protected Predicate<String> exportChecker;

    public ModuleClassLoader(ClassLoader parent, Predicate<String> checker) {
        super(parent);
        this.parent = parent;
        this.exportChecker = checker;
    }

    public ModuleClassLoader(ClassLoader parent) {
        this(parent, null);
    }

    public ModuleClassLoader(Predicate<String> checker) {
        this(null, checker);
    }

    public ModuleClassLoader() {
        this(null, null);
    }

    @Override
    protected Object getClassLoadingLock(String className) {
        Object lock, newLock = new Object();
        lock = locks.putIfAbsent(className, newLock);
        if (lock == null) {
            lock = newLock;
        }
        return lock;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (parent != null)
            try {
                return parent.loadClass(name);
            } catch (ClassNotFoundException ignore) {
            }
        Class<?> loaded = findLoadedClass(name);
        if (loaded != null) return loaded;
        return findClass(name);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (name.startsWith("java.lang.")) return super.loadClass(name);
        if (exportChecker != null) {
            synchronized (getClassLoadingLock(name)) {
                Class<?> caller = Toolkit.Reflection.getCallerClass();
                ClassLoader loader = caller.getClassLoader();
                if (caller == ClassLoader.class) {
                    if (new Throwable().getStackTrace()[1].getMethodName().startsWith("defineClass")) {
                        return loadClass(name, false);
                    }
                } else if (loader == this || (parent != null && loader == parent)) {
                    return loadClass(name, false);
                } else if (exportChecker.test(name)) {
                    return loadClass(name, false);
                }
            }
        } else {
            return loadClass(name, false);
        }
        throw new ClassNotFoundException(name);
    }
}
