/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RFieldImpl.java@author: karlatemp@vip.qq.com: 19-11-15 下午4:48@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.tools.Unsafe;
import cn.mcres.karlatemp.mxlib.tools.security.AccessToolkit;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public class RFieldImpl<O, R> implements RField<O, R> {
    private O thiz;
    private Field field;
    private Class<?> root;

    RFieldImpl(Field f, Class root, O thiz) {
        this.field = f;
        this.root = root;
        this.thiz = thiz;
    }

    @Override
    public O self() {
        return thiz;
    }

    @Override
    public RField<O, R> self(O this_) {
        if (Modifier.isStatic(field.getModifiers())) return this;
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
        return Modifier.isStatic(field.getModifiers());
    }

    @Override
    public R get() {
        Unsafe unsafe = Unsafe.getUnsafe();
        if (unsafe == null) { // SunUnsafe - Initializing Unsafe Instance.
            AccessToolkit.setAccessible(field, true);
            try {
                return (R) field.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return (R) Toolkit.Reflection.getObjectValue(thiz, field);
    }

    @Override
    public Reflect<O> set(R value) {
        Toolkit.Reflection.setObjectValue(thiz, field, value);
        return new ObjectReflect<>(thiz, root);
    }

    @Override
    public Class<R> getType() {
        return (Class<R>) field.getType();
    }
}
