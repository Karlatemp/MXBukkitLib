/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: FilterIterator.java@author: karlatemp@vip.qq.com: 19-11-16 上午1:03@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FilterIterator<T> implements Iterator<T> {
    private final Iterator<T> parent;
    private final Predicate<T> filter;

    public FilterIterator(@NotNull Iterator<T> parent, @NotNull Predicate<T> filter) {
        this.parent = parent;
        this.filter = filter;
    }

    private boolean doIt;
    private boolean more;
    private T fq;

    @Override
    public boolean hasNext() {
        if (doIt) {
            return more;
        } else {
            if (parent.hasNext()) {
                more = false;
                doIt = true;
                do {
                    T next = parent.next();
                    if (filter.test(next)) {
                        fq = next;
                        more = true;
                        return true;
                    }
                } while (parent.hasNext());
            }
        }
        return false;
    }

    @Override
    public T next() {
        if (doIt) {
            if (more) {
                doIt = false;
                return fq;
            }
            throw new NoSuchElementException();
        }
        return parent.next();
    }

    @Override
    public void remove() {
        parent.remove();
    }

    /**
     * For-each action.
     *
     * @param action The action of for-each
     */
    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        parent.forEachRemaining(k -> {
            if (filter.test(k)) action.accept(k);
        });
    }
}
