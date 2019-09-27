/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PermissibleIterable.java@author: karlatemp@vip.qq.com: 19-9-26 下午1:11@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * The permission access check iterable
 *
 * @param <E> The type of this iterable
 * @see PCollectionPermission
 * @since 2.2
 */
public class PermissibleIterable<E> implements Iterable<E> {
    protected Iterable<E> parent;
    protected Set<PCollectionPermission> permissions;

    protected PermissibleIterable() {
    }

    public PermissibleIterable(Iterable<E> parent) {
        this(parent, PCollectionPermission.ALL);
    }

    public PermissibleIterable(Iterable<E> parent, Set<PCollectionPermission> permissions) {
        this.parent = parent;
        this.permissions = permissions;
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        if (permissions.contains(PCollectionPermission.ITERATOR))
            return new PermissibleIterator<>(parent.iterator(), permissions);
        return Collections.emptyIterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        if (permissions.contains(PCollectionPermission.FOR_EACH))
            parent.forEach(action);
    }

    @Override
    public Spliterator<E> spliterator() {
        if (permissions.contains(PCollectionPermission.SPLITERATOR))
            return parent.spliterator();
        //noinspection unchecked
        return ((Iterable<E>) Collections.emptyList()).spliterator();
    }

    @Override
    public int hashCode() {
        return 92387 + parent.hashCode();
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
    public String toString() {
        if (permissions.contains(PCollectionPermission.TO_STRING))
            return super.toString();
        return parent.toString();
    }
}
