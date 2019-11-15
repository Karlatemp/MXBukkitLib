/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RFieldImpl.java@author: karlatemp@vip.qq.com: 19-11-15 下午4:48@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;

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
    public R get() {
        try {
            return (R) field.get(thiz);
        } catch (IllegalAccessException e) {
            return ThrowHelper.thrown(e);
        }
    }

    @Override
    public Reflect<O> set(R value) {
        try {
            field.set(thiz, value);
            return new ObjectReflect(thiz, root);
        } catch (IllegalAccessException e) {
            return ThrowHelper.thrown(e);
        }
    }

    @Override
    public Class<R> getType() {
        return (Class<R>) field.getType();
    }
}
