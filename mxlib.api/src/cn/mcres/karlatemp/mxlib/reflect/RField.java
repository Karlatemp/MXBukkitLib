/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RField.java@author: karlatemp@vip.qq.com: 19-11-15 下午4:41@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;

import java.util.function.Supplier;

public interface RField<O, R> extends Supplier<R> {
    O self();

    RField<O, R> self(O this_);

    R get();

    default Reflect<R> got() {
        return Reflect.ofObject(get());
    }

    boolean isStatic();

    Reflect<O> set(R value);

    Class<R> getType();

    @SuppressWarnings("unchecked")
    default <T> T get(Class<T> typeOfT) {
        R ret = get();
        if (ret == null) return null;
        if (typeOfT != null) {
            return typeOfT.cast(ret);
        }
        return (T) ret;
    }

    /**
     * Copy context.
     *
     * @return New context for this method.
     * @since 2.12
     */
    default RField<O, R> newContext() {
        return Toolkit.Reflection.clone(this);
    }
}
