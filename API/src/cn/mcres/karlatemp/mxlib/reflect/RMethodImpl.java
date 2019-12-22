/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RMethodImpl.java@author: karlatemp@vip.qq.com: 19-11-10 下午1:12@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class RMethodImpl<O, T> implements RMethod<O, T> {
    private final Method method;
    private O thiz;
    Class<?> root;

    @Override
    public RMethod<O, T> newContext() {
        RMethodImpl<O, T> cp = new RMethodImpl<>(method);
        cp.root = root;
        cp.thiz = thiz;
        return cp;
    }

    public RMethodImpl(@NotNull Method method) {
        this.method = method;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(method.getModifiers());
    }

    @Override
    @NotNull
    public RMethod<O, T> self(O this_) {
        if (isStatic()) return this;
        thiz = this_;
        return this;
    }

    @Override
    public O self() {
        return thiz;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    @Override
    public Reflect<T> invoke(Object... values) {
        try {
            Object rw;
            if (!isStatic() && thiz == null) {
                Object t = values[0];
                Object[] vt = new Object[values.length - 1];
                System.arraycopy(values, 1, vt, 0, vt.length);
                rw = method.invoke(t, vt);
            } else {
                rw = method.invoke(thiz, values);
            }
            return new ObjectReflect(rw, root == null ? null : method.getReturnType());
        } catch (IllegalAccessException e) {
            return ThrowHelper.thrown(e);
        } catch (InvocationTargetException e) {
            return ThrowHelper.thrown(e.getTargetException());
        }
    }
}
