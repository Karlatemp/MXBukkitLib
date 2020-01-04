/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: IteratorSupplier.java@author: karlatemp@vip.qq.com: 2020/1/1 下午11:33@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IteratorSupplier<T> implements Supplier<T>, Iterator<T>, Iterable<T> {
    @NotNull
    protected Iterator<T> parent;

    public IteratorSupplier(@NotNull Iterator<T> parent) {
        this.parent = parent;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return parent;
    }

    @Override
    public boolean hasNext() {
        return parent.hasNext();
    }

    @Override
    public T next() {
        return parent.next();
    }

    @Override
    public void remove() {
        parent.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        parent.forEachRemaining(action);
    }

    @Override
    public T get() {
        if (hasNext())
            return next();
        return noNextValue();
    }

    protected T noNextValue() {
        return null;
    }
}
