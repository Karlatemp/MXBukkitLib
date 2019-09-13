/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.ext.java.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ComparatorSet<T> implements Set<T> {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComparatorSet) {
            @SuppressWarnings("unchecked")
            ComparatorSet<T> cs = (ComparatorSet<T>) obj;
            synchronized (this) {
                synchronized (cs) {
                    return Objects.equals(ct, cs.ct) && Arrays.equals(list, cs.list);
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 142 + Arrays.hashCode(list);
    }

    @Override
    public String toString() {
        return Arrays.toString(list);
    }
    public static final Comparator<Object> DEFAULT = (a, b) -> {
        if (a instanceof Comparable) {
            @SuppressWarnings("unchecked")
            Comparable<Object> c = (Comparable) a;
            return c.compareTo(b);
        }
        throw new ClassCastException(a + "(" + a.getClass().getName() + ") cannot case to java.lang.Comparable");
    };
    private T[] list;
    private final Comparator<T> ct;

    public Comparator<T> getComparator() {
        return ct;
    }

    @SuppressWarnings("unchecked")
    public ComparatorSet(Comparator<T> ct) {
        this.ct = ct;
        list = (T[]) new Object[0];
    }

    @SuppressWarnings("unchecked")
    public ComparatorSet() {
        this((Comparator<T>) DEFAULT);
    }

    @SuppressWarnings("unchecked")
    public <TT> ListAndSet<TT> asListCustom() {
        return (ListAndSet<TT>) asList();
    }

    public ListAndSet<T> asList() {
        return ListAndSet.mapping(this);
    }

    @Override
    public int size() {
        return list.length;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        T[] list = this.list;
        switch (list.length) {
            case 0:
                return false;
            case 1:
                return ct.compare(list[0], (T) o) == 0;
            default: {
                return Arrays.binarySearch((Object[]) list, o, (Comparator) ct) > -1;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public T search(Object k) {
        @SuppressWarnings("LocalVariableHidesMemberVariable")
        T[] list = this.list;
        switch (list.length) {
            case 0:
                return null;
            case 1: {
                T kk = list[0];
                if (ct.compare(kk, (T) k) == 0) {
                    return kk;
                }
                return null;
            }
            default: {
                int id = Arrays.binarySearch((Object[]) list, k, (Comparator) ct);
                return id > -1 ? list[id] : null;
            }
        }
    }

    @Override
    public Iterator<T> iterator() {
        return Arrays.asList(list).iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return Arrays.asList(list).spliterator();
    }

    public T get(int index) {
        return list[index];
    }

    @Override
    public Object[] toArray() {
        return list.clone();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] now = (T[]) list;
        int end = Math.min(now.length, a.length);
        System.arraycopy(now, 0, a, 0, end);
        return a;
    }

    public static void main(String[] test) throws InterruptedException {
        ComparatorSet<Object> sst = new ComparatorSet<>();
        for (int i = 0; i < 147; i += 5) {
            sst.add(i);
            sst.add(147 - i);
        }
        Thread.sleep(200L);
        sst.removeIf((a) -> {
            int num = (int) a;
            return num < 30 || num > 50;
        });
        System.err.println(Arrays.toString(sst.list));
        Object[] list = new Object[]{0, 1, 2, 3, 4, 5, 7, 8, 9, 10};
        System.out.println(Arrays.binarySearch(list, 9, DEFAULT));
    }

    @Override
    public boolean add(T e) {
        synchronized (this) {
            if (this.contains(e)) {
                return false;
            }
            T[] nw = (T[]) new Object[list.length + 1];
            int insert = 0;
            int start = 0;
            int end = list.length;
//            com.sun.java.swing.ui.CommonToolBar;
            // <editor-fold>
            switch (list.length) {
                case 0: {
                    insert = 0;
                    break;
                }
                case 1: {
                    insert = ct.compare(list[0], e) > 0 ? 0 : 1;
                    break;
                }
                default: {
                    while (start < end) {
                        int read = start + ((end - start) / 2);
                        if (read == start || read == end) {
                            int lc = ct.compare(list[read], e);
                            if (lc == 0) {
                                insert = read;
                            } else if (lc > 0) {
                                insert = start;
                            } else {
                                insert = end;
                            }
                            break;
                        }
                        int lc = ct.compare(list[read], e);
//                        System.out.format("%s: %s%n", start, end);
                        if (lc == 0) {
                            insert = lc;
                            break;
                        } else if (lc > 0) {
                            end = read;
                        } else {
                            start = read;
                        }
                    }
                    break;
                }
            }
            // </editor-fold>
            System.arraycopy(list, 0, nw, 0, insert);
            System.arraycopy(list, insert, nw, insert + 1, list.length - insert);
            nw[insert] = e;
            list = nw;
//            System.out.println(Arrays.toString(nw));
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        synchronized (this) {
            switch (list.length) {
                case 0:
                    return false;
                case 1: {
                    if (ct.compare(list[0], t) == 0) {
                        clear();
                        return true;
                    }
                    return false;
                }
                default: {
                    int ssd = Arrays.binarySearch(list, t, ct);
                    if (ssd > -1) {
                        remove(ssd);
                        return true;
                    }
                    return false;
                }
            }
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        c.forEach(this::remove);
        return true;
    }

    @Override
    public void clear() {
        synchronized (this) {
            list = (T[]) new Object[0];
        }
    }

    public synchronized void remove(int id) {
        synchronized (this) {
            switch (list.length) {
                case 0: {
                    throw new ArrayIndexOutOfBoundsException(id);
                }
                case 1: {
                    if (id != 0) {
                        throw new ArrayIndexOutOfBoundsException(id);
                    }
                    clear();
                }
                default: {
                    if (id < 0 || id >= list.length) {
                        throw new ArrayIndexOutOfBoundsException(id);
                    }
                    T[] nw = (T[]) new Object[list.length - 1];
                    System.arraycopy(list, 0, nw, 0, id);
                    System.arraycopy(list, id + 1, nw, id, nw.length - id);
                    list = nw;
                }
            }
        }
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        int removed = 0;
        synchronized (this) {
            T[] list = this.list;
            for (int i = 0; i < list.length; i++) {
                T tt = list[i];
                if (filter.test(tt)) {
                    remove(i - removed);
                    removed++;
                }
            }
        }
        return removed != 0;
    }

    @Override
    public Stream<T> stream() {
        return Set.super.stream(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Stream<T> parallelStream() {
        return Set.super.parallelStream(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        T[] now = list;
        for (T nw : now) {
            action.accept(nw);
        }
    }

    public void sort() {
        synchronized (this) {
            if (list.length > 1) {
                Arrays.sort(list, ct);
            }
        }
    }
}
