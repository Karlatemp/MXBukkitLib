/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: IterableLink.java@author: karlatemp@vip.qq.com: 19-9-27 下午1:30@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A link of iterable.
 *
 * @since 2.2
 */
public class IterableLink<E> implements Iterable<E> {
    private static class Node<E> {
        Node<E> next;
        Iterable<E> current;
    }

    private static class NodeIterator<E> implements Iterator<E> {
        Node<E> current;
        Iterator<E> ci;

        private boolean set() {
            while (ci == null) {
                if (current == null) return true;
                current = current.next;
                if (current == null) return true;
                ci = current.current.iterator();
            }
            return false;
        }

        @Override
        public boolean hasNext() {
            if (set()) return false;
            while (!ci.hasNext()) {
                ci = null;
                if (set()) return false;
            }
            return true;
        }

        @Override
        public E next() {
            if (set()) throw new NoSuchElementException();
            return ci.next();
        }

        @Override
        public void remove() {
            if (set()) throw new NullPointerException();
            ci.remove();
        }
    }

    private Node<E> current;

    @NotNull
    @Override
    public Iterator<E> iterator() {
        NodeIterator<E> ni = new NodeIterator<>();
        ni.current = current;
        if (current != null) ni.ci = current.current.iterator();
        return ni;
    }

    public synchronized void appendAtFirst(Iterable<E> first) {
        Node<E> o = current, nw;
        (nw = current = new Node<>()).current = first;
        nw.next = o;
    }

    public synchronized void insert(int at, Iterable<E> ins) {
        if (current == null)
            appendAtFirst(ins);
        else {
            int lc = at;
            Node<E> last = current, lm = last;
            while (lm != null && lc-- > 0) {
                last = lm;
                lm = lm.next;
            }
            Node<E> over = last.next, nw;
            (nw = last.next = new Node<>()).current = ins;
            nw.next = over;
        }
    }

    public synchronized void append(Iterable<E> next) {
        if (current == null)
            (current = new Node<>()).current = next;
        else {
            Node<E> last = current, lm = last;
            while (lm != null) {
                last = lm;
                lm = lm.next;
            }
            (last.next = new Node<>()).current = next;
        }
    }

}
