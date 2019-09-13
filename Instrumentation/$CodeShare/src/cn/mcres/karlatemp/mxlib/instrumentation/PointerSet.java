/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PointerSet.java@author: karlatemp@vip.qq.com: 19-9-12 下午6:09@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.instrumentation;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class PointerSet<V> extends AbstractSet<V> {
    private final Vector<V> vect = new Vector<>();

    public synchronized boolean contains(Object o) {
        synchronized (vect) {
            int end = vect.size();
            for (int i = 0; i < end; i++) {
                if (vect.get(i) == o) return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean add(V v) {
        synchronized (vect) {
            if (!contains(v))
                return vect.add(v);
            return false;
        }
    }

    class PI implements Iterator<V> {
        int siz = size();
        int point;

        @Override
        public boolean hasNext() {
            return point < siz;
        }

        @Override
        public V next() {
            if (siz != size()) {
                throw new ConcurrentModificationException();
            }
            if (point >= siz) throw new NoSuchElementException();
            return vect.get(point++);
        }

        @Override
        public void remove() {
            if (siz != size()) {
                throw new ConcurrentModificationException();
            }
            if (point >= siz) throw new ArrayIndexOutOfBoundsException();
            synchronized (vect) {
                vect.remove(point);
                siz--;
                point--;
            }
        }
    }

    @NotNull
    @Override
    public Iterator<V> iterator() {
        return new PI();
    }

    @Override
    public int size() {
        return vect.size();
    }

    @Override
    public void forEach(Consumer<? super V> action) {
        vect.forEach(action);
    }

    @Override
    public boolean removeIf(Predicate<? super V> filter) {
        return vect.removeIf(filter);
    }

    @Override
    public Spliterator<V> spliterator() {
        return vect.spliterator();
    }

    @Override
    public void clear() {
        vect.clear();
    }
}
