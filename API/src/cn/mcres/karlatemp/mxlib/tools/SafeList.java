/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SafeList.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@ProhibitBean
public class SafeList<T> implements List<T> {
    private final List<T> parent;
    private T def;

    public T getDefault() {
        return def;
    }

    public SafeList<T> setDefault(T def) {
        this.def = def;
        return this;
    }

    @NotNull
    public List<T> getParent() {
        return parent;
    }

    public SafeList(@NotNull List<T> parent) {
        this.parent = parent;
    }

    @Override
    public int size() {
        return parent.size();
    }

    @Override
    public boolean isEmpty() {
        return parent.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return parent.contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return parent.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return parent.toArray();
    }

    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        return parent.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return parent.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return parent.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return parent.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return parent.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        return parent.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return parent.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return parent.retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        parent.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        parent.sort(c);
    }

    @Override
    public void clear() {
        parent.clear();
    }

    @Override
    public boolean equals(Object o) {
        return parent.equals(o);
    }

    @Override
    public int hashCode() {
        return parent.hashCode();
    }

    @Override
    public T get(int index) {
        if (index < 0) {
            return def;
        }
        int size = size();
        if (index < size)
            return parent.get(index);
        return def;
    }

    @Override
    public T set(int index, T element) {
        return parent.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        parent.add(index, element);
    }

    @Override
    public T remove(int index) {
        return parent.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return parent.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return parent.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return parent.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return parent.listIterator(index);
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return new SafeList<>(parent.subList(fromIndex, toIndex));
    }

    @Override
    public Spliterator<T> spliterator() {
        return parent.spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return parent.removeIf(filter);
    }

    @Override
    public Stream<T> stream() {
        return parent.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return parent.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        parent.forEach(action);
    }
}
