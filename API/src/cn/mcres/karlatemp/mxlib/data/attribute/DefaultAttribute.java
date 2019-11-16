/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DefaultAttribute.java@author: karlatemp@vip.qq.com: 19-11-16 上午12:56@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data.attribute;

import java.util.function.Supplier;

public class DefaultAttribute<T> implements Attribute<T> {
    protected T value;
    protected boolean exists;

    @Override
    public T get() {
        return value;
    }

    @Override
    public boolean exists() {
        return exists;
    }

    @Override
    public Attribute<T> set(T value) {
        this.value = value;
        exists = true;
        return this;
    }

    @Override
    public void remove() {
        value = null;
        exists = false;
    }
}
