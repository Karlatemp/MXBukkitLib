/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ClassLoaderPool.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:20@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info;


import cn.mcres.karlatemp.mxlib.common.class_info.internal.StaticPool;
import cn.mcres.karlatemp.mxlib.common.class_info.internal.class_class_loader.ClassClassInfo;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClassLoaderPool implements ClassPool {
    private final Queue<ClassClassInfo> classes = new ConcurrentLinkedQueue<>();
    private final ClassLoader loader;

    public ClassLoaderPool(ClassLoader loader) {
        this.loader = loader;
    }

    static {
        try {
            ClassLoaderPool.class.getModule().addExports(
                    "cn.mcres.karlatemp.mxlib.common.class_info.internal.class_class_loader",
                    (Module) Toolkit.Reflection.getRoot().findStaticGetter(
                            Module.class, "EVERYONE_MODULE", Module.class
                    ).invoke()
            );
        } catch (Throwable ignore) {
        }
    }

    @Override
    public ClassPool getParent() {
        return null;
    }

    @Override
    public ClassInfo getInternalClass(String internal) {
        final ClassInfo primitiveClass = StaticPool.findExist(internal,
                this, getPoolClasses(),
                true, false);
        if (primitiveClass != null) return primitiveClass;
        return a(internal.replace('/', '.'));
    }

    private synchronized ClassInfo a(String x) {
        Class<?> c;
        try {
            c = loader.loadClass(x);
            c.getDeclaredMethods();
            c.getDeclaredFields();
        } catch (ClassNotFoundException | NoClassDefFoundError | NullPointerException error) {
            return null;
        }
        var ci = new ClassClassInfo(c);
        classes.add(ci);
        ci.initialize(this);
        return ci;
    }

    @Override
    public synchronized ClassInfo getClass(String name) {
//         System.out.println("Loading\t" + name);
        final ClassInfo primitiveClass = StaticPool.findExist(name, this, getPoolClasses(), false, false);
        if (primitiveClass != null) return primitiveClass;
        return a(name);
    }

    @Override
    public ClassInfo getRenamedClass(String name) {
        return getClass(name);
    }

    @Override
    public ClassInfo getRenamedInternalClass(String name) {
        return getInternalClass(name);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Collection<ClassInfo> getPoolClasses() {
        return (Collection) this.classes;
    }
}
