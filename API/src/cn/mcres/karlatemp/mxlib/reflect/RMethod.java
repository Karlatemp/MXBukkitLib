/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RMethod.java@author: karlatemp@vip.qq.com: 19-11-10 下午1:03@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public interface RMethod<O, R> {
    @NotNull
    @Contract(pure = true)
    default Class<O> getOwner() {
        return (Class<O>) getMethod().getDeclaringClass();
    }

    @Contract(pure = true)
    @NotNull
    default Class<R> getReturnType() {
        return (Class<R>) getMethod().getReturnType();
    }

    @NotNull
    @Contract(pure = true)
    default Class<?>[] getParamTypes() {
        return getMethod().getParameterTypes();
    }

    Method getMethod();

    @NotNull
    @Contract
    default Annotation[][] getParamAnnotations() {
        return getMethod().getParameterAnnotations();
    }

    boolean isStatic();

    @NotNull
    @Contract(pure = true)
    RMethod<O, R> self(O this_);

    @Contract(pure = true)
    O self();

    @NotNull
    Reflect<R> invoke(Object... values);

    /**
     * Copy context.
     *
     * @return New context for this method.
     */
    default RMethod<O, R> newContext() {
        return Toolkit.Reflection.clone(this);
    }
}
