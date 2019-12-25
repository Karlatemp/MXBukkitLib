/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PermissibleList.java@author: karlatemp@vip.qq.com: 19-9-26 下午12:58@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.UnaryOperator;

/**
 * The permission access check list
 *
 * @param <E> The type of this list
 * @see PCollectionPermission
 * @since 2.2
 */
public class PermissibleList<E> extends PermissibleCollection<E> implements List<E> {
    protected List<E> parent;

    protected PermissibleList() {
    }

    public PermissibleList(List<E> parent) {
        super(parent);
        this.parent = parent;
    }

    public PermissibleList(List<E> parent, Set<PCollectionPermission> permissions) {
        super(parent, permissions);
        this.parent = parent;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        if (permissions.contains(PCollectionPermission.ADD))
            return parent.addAll(index, c);
        return false;
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        if (permissions.contains(PCollectionPermission.REPLACE))
            parent.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super E> c) {
        if (permissions.contains(PCollectionPermission.SORT))
            parent.sort(c);
    }

    @Override
    public E get(int index) {
        if (permissions.contains(PCollectionPermission.GET))
            return parent.get(index);

        if (index > -1) if (index < parent.size()) return null;

        throw new ArrayIndexOutOfBoundsException(index);
    }

    @Override
    public E set(int index, E element) {
        if (permissions.contains(PCollectionPermission.SET)) {
            return parent.set(index, element);
        }
        return null;
    }

    @Override
    public void add(int index, E element) {
        if (permissions.contains(PCollectionPermission.ADD))
            parent.add(index, element);
    }

    @Override
    public E remove(int index) {
        if (permissions.contains(PCollectionPermission.REMOVE))
            return parent.remove(index);
        return null;
    }

    @Override
    public int indexOf(Object o) {
        if (permissions.contains(PCollectionPermission.INDEX_OF)) {
            return parent.indexOf(o);
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (permissions.contains(PCollectionPermission.LAST_INDEX_OF)) {
            return parent.lastIndexOf(o);
        }
        return -1;
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator() {
        if (permissions.contains(PCollectionPermission.LIST_ITERATOR))
            return parent.listIterator();
        return Collections.emptyListIterator();
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator(int index) {
        if (permissions.contains(PCollectionPermission.LIST_ITERATOR))
            return parent.listIterator(index);
        return Collections.emptyListIterator();
    }

    @NotNull
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (permissions.contains(PCollectionPermission.SUB_LIST)) {
            return new PermissibleList<>(parent.subList(fromIndex, toIndex), permissions);
        }
        return Collections.emptyList();
    }
}
