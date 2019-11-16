/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RFieldHandle.java@author: karlatemp@vip.qq.com: 19-11-15 下午4:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;

import java.lang.invoke.MethodHandle;

public class RFieldHandle<O, R> implements RField<O, R> {
    private final MethodHandle getter;
    private final MethodHandle setter;
    private final boolean isStatic;
    private O thiz;
    private Class<?> root;

    public RFieldHandle(MethodHandle getter, MethodHandle setter, boolean isStatic, Class<?> root) {
        this.getter = getter;
        this.setter = setter;
        this.isStatic = isStatic;
        this.root = root;
    }

    @Override
    public O self() {
        return thiz;
    }

    @Override
    public RField<O, R> self(O this_) {
        thiz = this_;
        return this;
    }

    @Override
    public Reflect<R> got() {
        R val = get();
        return new ObjectReflect<>(val, root == null ? null : getType());
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @SuppressWarnings("unchecked")
    @Override
    public R get() {
        try {
            if (isStatic)
                return (R) getter.invoke();
            return (R) getter.invoke(thiz);
        } catch (Throwable thr) {
            return ThrowHelper.thrown(thr);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Reflect<O> set(R value) {
        try {
            if (isStatic)
                setter.invoke(value);
            else setter.invoke(thiz, value);

            return new ObjectReflect(thiz, root);
        } catch (Throwable thr) {
            return ThrowHelper.thrown(thr);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<R> getType() {
        return (Class<R>) getter.type().returnType();
    }
}
