/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Chain.java@author: karlatemp@vip.qq.com: 19-11-1 下午6:14@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

/**
 * @since 2.5
 */
public class Chain<T, V> {
    public static class ChainContext<T, V> {
        boolean end;

        public void endChain() {
            end = true;
        }

        public void endChain(V returnValue) {
            setValue(returnValue).endChain();
        }

        private V v;

        public V getValue() {
            return v;
        }

        public ChainContext<T, V> setValue(V v) {
            this.v = v;
            return this;
        }

        protected V post(T argument, Supplier<SingleChain<T, V>> supplier) {
            while (true) {
                final SingleChain<T, V> chain = supplier.get();
                if (chain == null) break;
                chain.invoke(argument, this);
                if (end) break;
            }
            return v;
        }
    }

    public interface SingleChain<T, V> {
        void invoke(T argument, @NotNull ChainContext<T, V> context);
    }

    protected final List<SingleChain<T, V>> list = initList();

    public void register(@NotNull SingleChain<T, V> sc) {
        if (!list.contains(sc))
            list.add(sc);
    }

    public void unregister(SingleChain<T, V> sc) {
        list.remove(sc);
    }

    public V post(T argument) {
        return newContext().post(argument, newSupplier());
    }

    protected List<SingleChain<T, V>> initList() {
        return new ArrayList<>();
    }

    protected ChainContext<T, V> newContext() {
        return new ChainContext<>();
    }

    protected Supplier<SingleChain<T, V>> newSupplier() {
        final Iterator<SingleChain<T, V>> iterator = list.iterator();
        return () -> {
            if (!iterator.hasNext()) return null;
            return iterator.next();
        };
    }
}
