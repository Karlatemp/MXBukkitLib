/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PermissibleIteror.java@author: karlatemp@vip.qq.com: 19-9-26 下午1:16@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

/**
 * The permission access check iterator
 *
 * @param <E> The type of this iterator
 * @see PCollectionPermission
 * @since 2.2
 */
public class PermissibleIterator<E> implements Iterator<E> {
    protected Iterator<E> parent;
    protected Set<PCollectionPermission> permissions;

    protected PermissibleIterator() {
    }

    public PermissibleIterator(Iterator<E> parent) {
        this(parent, PCollectionPermission.ALL);
    }

    public PermissibleIterator(Iterator<E> parent, Set<PCollectionPermission> permissions) {
        this.parent = parent;
        this.permissions = permissions;
    }

    @Override
    public boolean hasNext() {
        if (permissions.contains(PCollectionPermission.GET))
            return parent.hasNext();
        return false;
    }

    @Override
    public E next() {
        if (permissions.contains(PCollectionPermission.GET))
            return parent.next();
        return null;
    }

    @Override
    public void remove() {
        if (permissions.contains(PCollectionPermission.REMOVE))
            parent.remove();
        else Iterator.super.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        if (permissions.contains(PCollectionPermission.FOR_EACH_REMAINING))
            parent.forEachRemaining(action);
    }
}
