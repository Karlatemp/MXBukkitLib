/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.ext.java.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class ListToSet<T> implements ListAndSet<T> {

    private final List<T> p;

    @Override
    public int hashCode() {
        return p.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + p;
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public List<T> getParent() {
        return p;
    }

    @Override
    public boolean equals(Object obj) {
        if (p.equals(obj)) {
            return true;
        }
        if (obj instanceof ListToSet) {
            @SuppressWarnings({"unchecked", "AccessingNonPublicFieldOfAnotherObject"})
            List<T> ls = ((ListToSet) obj).p;
            return p.equals(ls);
        }
        return false;
    }

    public ListToSet(List<T> parent) {
        this.p = parent;
    }

    @Override
    public int size() {
        return p.size();
    }

    @Override
    public boolean isEmpty() {
        return p.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return p.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return p.iterator();
    }

    @Override
    public Object[] toArray() {
        return p.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return p.toArray(a);
    }

    @Override
    public boolean add(T e) {
        return p.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return p.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return p.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return p.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return p.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return p.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return p.retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        p.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        p.sort(c);
    }

    @Override
    public void clear() {
        p.clear();
    }

    @Override
    public T get(int index) {
        return p.get(index);
    }

    @Override
    public T set(int index, T element) {
        return p.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        p.add(index, element);
    }

    @Override
    public T remove(int index) {
        return p.remove(index);

    }

    @Override
    public int indexOf(Object o) {
        return p.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return p.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return p.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return p.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return p.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<T> spliterator() {
        return p.spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return p.removeIf(filter);
    }

    @Override
    public Stream<T> stream() {
        return p.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return p.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        p.forEach(action);
    }
}
