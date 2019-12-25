/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MapKeySet.java@author: karlatemp@vip.qq.com: 19-11-15 下午7:19@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Make a map to set
 *
 * @param <V> The type of this set
 * @since 2.6
 */
public class MapKeySet<V> extends AbstractSet<V> {
    private static final Object VALUE = new Object();
    private final Map<V, Object> map;

    @SuppressWarnings("unchecked")
    public MapKeySet(@NotNull Map<V, ?> map) {
        this.map = (Map<V, Object>) map;
    }

    public MapKeySet() {
        this(new HashMap<>());
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return map.keySet().removeAll(c);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return map.keySet().toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return map.keySet().toArray(a);
    }

    @Override
    public boolean add(V v) {
        map.put(v, VALUE);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o, VALUE);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return map.keySet().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        for (V w : c) {
            map.put(w, VALUE);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return map.keySet().retainAll(c);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public String toString() {
        return map.keySet().toString();
    }

    @Override
    public Spliterator<V> spliterator() {
        return map.keySet().spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super V> filter) {
        return map.keySet().removeIf(filter);
    }

    @Override
    public Stream<V> stream() {
        return map.keySet().stream();
    }

    @Override
    public Stream<V> parallelStream() {
        return map.keySet().parallelStream();
    }

    @Override
    public void forEach(Consumer<? super V> action) {
        map.keySet().forEach(action);
    }

    @Override
    @NotNull
    public Iterator<V> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }
}
