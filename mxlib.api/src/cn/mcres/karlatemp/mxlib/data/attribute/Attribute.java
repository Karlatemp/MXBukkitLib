/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Attribute.java@author: karlatemp@vip.qq.com: 19-11-16 上午12:50@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data.attribute;

import java.util.function.Supplier;

public interface Attribute<T> {
    T get();

    default T getAndSet(T newValue) {
        T val = get();
        set(newValue);
        return val;
    }

    Attribute<T> set(T value);

    boolean exists();

    default T setIfAbsent(T value) {
        return ifAbsentSet(() -> value);
    }

    default T ifAbsentSet(Supplier<T> value) {
        if (exists()) return get();
        T val = value.get();
        set(val);
        return val;
    }

    default void remove() {
        set(null);
    }

    default T getAndRemove() {
        T val = get();
        remove();
        return val;
    }
}
