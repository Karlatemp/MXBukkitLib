/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: WrappedObject.java@author: karlatemp@vip.qq.com: 19-11-22 下午12:20@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class WrappedObject<T> {
    protected T object;

    protected WrappedObject() {
    }

    protected WrappedObject(T value) {
        object = value;
    }

    protected WrappedObject(@Nullable Consumer<Consumer<T>> hook_setter) {
        this(null, hook_setter);
    }

    protected WrappedObject(T value, @Nullable Consumer<Consumer<T>> hook_setter) {
        this.object = value;
        if (hook_setter != null) {
            hook_setter.accept(this::setWrapped);
        }
    }

    protected void setWrapped(T value) {
        this.object = value;
    }

    protected T getWrapped() {
        return object;
    }
}
