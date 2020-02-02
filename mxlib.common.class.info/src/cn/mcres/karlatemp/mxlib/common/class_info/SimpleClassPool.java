/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: SimpleClassPool.java@author: karlatemp@vip.qq.com: 2020/1/13 下午11:07@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info;

import cn.mcres.karlatemp.mxlib.common.class_info.internal.StaticPool;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SimpleClassPool implements ClassPool {
    protected final ClassPool parent;
    protected final Collection<ClassInfo> classes = initClassesPool();

    protected Collection<ClassInfo> initClassesPool() {
        return new ConcurrentLinkedQueue<>();
    }

    public SimpleClassPool() {
        this(null);
    }

    public SimpleClassPool(ClassPool parent) {
        this.parent = parent;
    }

    @Override
    public ClassPool getParent() {
        return parent;
    }

    @Override
    public synchronized ClassInfo getClass(String name) {
        if (parent != null) {
            final ClassInfo info = parent.getClass(name);
            if (info != null) return info;
        }
        final ClassInfo primitiveClass = StaticPool.findExist(name, this, getPoolClasses(), false, false);
        if (primitiveClass != null) return primitiveClass;
        return findClass(name);
    }

    protected ClassInfo findClass(String name) {
        return null;
    }

    protected ClassInfo findInternalClass(String name) {
        return null;
    }

    protected ClassInfo findRenamedInternalClass(String name) {
        return null;
    }

    protected ClassInfo findRenamedClass(String name) {
        return null;
    }

    @Override
    public ClassInfo getRenamedInternalClass(String name) {
        if (parent != null) {
            final ClassInfo info = parent.getRenamedInternalClass(name);
            if (info != null) return info;
        }
        final ClassInfo primitiveClass = StaticPool.findExist(name,
                this, getPoolClasses(), true, true);
        if (primitiveClass != null) return primitiveClass;
        return findRenamedInternalClass(name);
    }

    @Override
    public ClassInfo getInternalClass(String internal) {
        if (parent != null) {
            final ClassInfo info = parent.getInternalClass(internal);
            if (info != null) return info;
        }
        final ClassInfo primitiveClass = StaticPool.findExist(internal,
                this, getPoolClasses(), true, false);
        if (primitiveClass != null) return primitiveClass;
        return findInternalClass(internal);
    }

    @Override
    public ClassInfo getRenamedClass(String name) {
        if (parent != null) {
            final ClassInfo info = parent.getRenamedClass(name);
            if (info != null) return info;
        }
        final ClassInfo primitiveClass = StaticPool.findExist(name, this, getPoolClasses(), false, true);
        if (primitiveClass != null) return primitiveClass;
        return findRenamedClass(name);
    }

    protected <T extends ClassInfo> T defineClass(T info) {
        classes.add(info);
        info.initialize(this);
        return info;
    }

    @Override
    public Collection<ClassInfo> getPoolClasses() {
        return classes;
    }
}
