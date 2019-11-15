/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RMethodHandle.java@author: karlatemp@vip.qq.com: 19-11-10 下午2:14@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RMethodHandle<O, R> implements RMethod<O, R> {
    private final boolean st;
    private final Class<O> owner;
    private final MethodHandle handle;
    private O self;
    Class<?> root;

    public RMethodHandle(boolean st, Class<O> owner, MethodHandle handle) {
        this.st = st;
        this.owner = owner;
        this.handle = handle;
    }

    @NotNull
    @Override
    public Class<O> getOwner() {
        return owner;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public Class<R> getReturnType() {
        return (Class<R>) handle.type().returnType();
    }

    @NotNull
    @Override
    public Class<?>[] getParamTypes() {
        if (st) return handle.type().parameterArray();
        else {
            MethodType typ = handle.type();
            Class[] ct = new Class[typ.parameterCount() - 1];
            System.arraycopy(typ.parameterArray(), 1, ct, 0, ct.length);
            return ct;
        }
    }

    @NotNull
    @Override
    public Annotation[][] getParamAnnotations() {
        int len = handle.type().parameterCount();
        if (!st) len--;
        return new Annotation[len][0];
    }

    @Override
    public Method getMethod() {
        return null;
    }

    @Override
    public boolean isStatic() {
        return st;
    }

    @NotNull
    @Override
    public RMethod<O, R> self(O this_) {
        if (!st) self = this_;
        return this;
    }

    @Override
    public O self() {
        return self;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Reflect<R> invoke(Object... values) {
        Object ret;
        if (st) {
            try {
                ret = handle.invokeWithArguments(values);
            } catch (Throwable throwable) {
                return ThrowHelper.thrown(throwable);
            }
        } else {
            List<Object> args = new ArrayList<>();
            args.add(self);
            args.addAll(Arrays.asList(values));
            try {
                ret = handle.invokeWithArguments(args);
            } catch (Throwable throwable) {
                return ThrowHelper.thrown(throwable);
            }
        }
        return new ObjectReflect(ret, root == null ? null : handle.type().returnType());
    }
}
