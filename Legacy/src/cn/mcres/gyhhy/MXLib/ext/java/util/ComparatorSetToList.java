/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.ext.java.util;

import java.util.Comparator;
import java.util.Set;

public class ComparatorSetToList<T> extends SetToList<T> {

    private final ComparatorSet<T> parent;

    @Override
    public T get(int index) {
        return parent.get(index);
    }

    public Comparator<T> getComparator() {
        return parent.getComparator();
    }

    @Override
    public void sort(Comparator<? super T> c) {
        if (c != null) {
            if (c != parent.getComparator()) {
                throw new SecurityException("Woring Comparator, please input real comparator or null.");
            }
        }
        parent.sort();
    }

    @Override
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public ComparatorSet<T> getParent() {
        return parent;
    }

    public ComparatorSetToList(ComparatorSet<T> parent) {
        super(parent);
        this.parent = parent;
    }

}
