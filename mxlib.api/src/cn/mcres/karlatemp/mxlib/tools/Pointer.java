/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Pointer.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import org.jetbrains.annotations.Contract;

import java.lang.ref.Reference;
import java.util.function.Supplier;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 指针???
 *
 * @param <T> 存储的值
 */
@ProhibitBean
public class Pointer<T> implements Supplier<T>, Consumer<T>, Function<T, T> {
    private T value;

    @Contract(pure = true)
    public T value() {
        return value;
    }

    /**
     * If you want to get the old value. please use {@link #replace(T)}
     *
     * @param val The value you want to set
     * @return The input value
     */
    @Contract("null -> null; !null -> !null")
    public T value(T val) {
        value = val;
        return val;
    }

    /**
     * Set up the value and return the old value.
     *
     * @param val The value you want to set
     * @return The value before set
     * @since 2.2
     */
    public T replace(T val) {
        T o = value;
        value = val;
        return o;
    }

    public Pointer() {
        this(null);
    }

    public Pointer(T value) {
        this.value = value;
    }

    @Override
    @Contract(pure = true)
    public T get() {
        return value;
    }

    @Override
    public void accept(T t) {
        value = t;
    }

    @Override
    public T apply(T t) {
        T old = value;
        value = t;
        return old;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Check the current value not `null`
     *
     * @return value != null
     * @since 2.2
     */
    @Contract(pure = true)
    public boolean exists() {
        return value != null;
    }
}
