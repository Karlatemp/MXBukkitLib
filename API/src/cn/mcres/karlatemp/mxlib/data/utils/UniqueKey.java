/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: UniqueKey.java@author: karlatemp@vip.qq.com: 19-11-16 上午12:39@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class UniqueKey<T extends UniqueKey<T>> implements Comparable<T> {
    protected final String name;

    protected void validArguments(Object[] args) {
    }

    protected void processArguments(Object[] args) {
    }

    @SuppressWarnings("unchecked")
    protected UniqueKey(Map<String, ?> registeredKeys, String name, Object... args) {
        this.name = name;
        if (registeredKeys != null) {
            if (registeredKeys.containsKey(name)) {
                throw new IllegalArgumentException("'" + name + "' is already in use");
            }
            validArguments(args);
            if (((Map) registeredKeys).put(name, this) != null) {
                throw new IllegalArgumentException("'" + name + "' is already in use");
            }
        } else {
            validArguments(args);
        }
        processArguments(args);
    }

    public final String name() {
        return name;
    }

    @Override
    public final int hashCode() {
        return name.hashCode() + getClass().hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public int compareTo(@NotNull T o) {
        if (o == this)
            return 0;
        return name.compareTo(o.name);
    }
}
