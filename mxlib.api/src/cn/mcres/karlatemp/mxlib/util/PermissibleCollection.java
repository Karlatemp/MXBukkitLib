/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PermissibleCollection.java@author: karlatemp@vip.qq.com: 19-9-25 下午10:51@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * The permission access check collection
 *
 * @param <E> The type of this collection
 * @see PCollectionPermission
 * @since 2.2
 */
public class PermissibleCollection<E> extends AbstractCollection<E> {
    protected Collection<E> parent;
    protected Set<PCollectionPermission> permissions;

    protected PermissibleCollection() {
    }

    public PermissibleCollection(Collection<E> parent) {
        this(parent, PCollectionPermission.ALL);
    }

    public PermissibleCollection(Collection<E> parent, Set<PCollectionPermission> permissions) {
        this.parent = parent;
        this.permissions = permissions;
    }

    @Override
    public Spliterator<E> spliterator() {
        if (permissions.contains(PCollectionPermission.SPLITERATOR))
            return parent.spliterator();
        //noinspection unchecked
        return ((List<E>) Collections.emptyList()).spliterator();
    }

    @Override
    public Stream<E> stream() {
        if (permissions.contains(PCollectionPermission.STREAM))
            return parent.stream();
        return Stream.empty();
    }

    @Override
    public Stream<E> parallelStream() {
        if (permissions.contains(PCollectionPermission.PARALLEL_STREAM))
            return parent.parallelStream();
        return Stream.empty();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        if (permissions.contains(PCollectionPermission.FOR_EACH))
            parent.forEach(action);
    }

    @Override
    public int size() {
        if (permissions.contains(PCollectionPermission.SIZE))
            return parent.size();
        return 0;
    }

    @Override
    public boolean isEmpty() {
        if (permissions.contains(PCollectionPermission.IS_EMPTY))
            return parent.isEmpty();
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (permissions.contains(PCollectionPermission.CONTAINS))
            return parent.contains(o);
        return false;
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        if (permissions.contains(PCollectionPermission.ITERATOR))
            return new PermissibleIterator<>(parent.iterator(), permissions);
        return Collections.emptyIterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        if (permissions.contains(PCollectionPermission.TO_ARRAY_RAW)) {
            return parent.toArray();
        }
        return new Object[0];
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        if (permissions.contains(PCollectionPermission.TO_ARRAY_ARR)) {
            return parent.toArray(a);
        }
        return a;
    }


    @Override
    public boolean add(E e) {
        if (permissions.contains(PCollectionPermission.ADD)) {
            return parent.add(e);
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (permissions.contains(PCollectionPermission.REMOVE)) {
            return parent.remove(o);
        }
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        if (permissions.contains(PCollectionPermission.CONTAINS))
            return parent.containsAll(c);
        return false;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        if (permissions.contains(PCollectionPermission.ADD))
            return parent.addAll(c);
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        if (permissions.contains(PCollectionPermission.REMOVE))
            return parent.removeAll(c);
        return false;
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        if (permissions.contains(PCollectionPermission.REMOVE))
            return parent.removeIf(filter);
        return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        if (permissions.contains(PCollectionPermission.RETAIN_ALL))
            return parent.retainAll(c);
        return false;
    }

    @Override
    public void clear() {
        if (permissions.contains(PCollectionPermission.CLEAR))
            parent.clear();
    }

    @Override
    public String toString() {
        if (permissions.contains(PCollectionPermission.TO_STRING))
            return "[]";
        return parent.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof PermissibleIterable) {
            return parent.equals(((PermissibleIterable) obj).parent);
        }
        if (obj instanceof PermissibleCollection)
            return parent.equals(((PermissibleCollection) obj).parent);
        return false;
    }

    @Override
    public int hashCode() {
        return 92387 + parent.hashCode();
    }
}
