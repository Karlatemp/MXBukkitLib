/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ComparatorMap.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.ext.java.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ComparatorMap<K, V> implements Map<K, V> {

    private final ComparatorSet<KV<K, V>> set;

    public ComparatorMap() {
        this((Comparator) ComparatorSet.DEFAULT);
    }

    public ComparatorMap(Comparator<K> cp) {
        this.set = new ComparatorSet<>((Comparator) (Object a, Object b) -> {
            K x1;
            K x2;
            if (a instanceof KV) {
                x1 = (K) ((KV) a).k;
            } else {
                x1 = (K) a;
            }
            if (b instanceof KV) {
                x2 = (K) ((KV) b).k;
            } else {
                x2 = (K) b;
            }
            return cp.compare(x1, x2);
        });
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return set.search(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return set.stream().filter(a -> Objects.equals(a.v, value)).count() != 0;
    }

    @Override
    public V get(Object key) {
        KV<K, V> kk = set.search(new KV(key, null));
        if (kk == null) {
            return null;
        }
        return kk.v;
    }

    @Override
    public V put(K key, V value) {
        V od = get(key);
        set.add(new KV(key, value));
        return od;
    }

    @Override
    public V remove(Object key) {
        KV<K, V> kk = set.search(new KV(key, null));
        if (kk == null) {
            return null;
        }
        set.remove(kk);
        return kk.v;

    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach((a, b) -> put(a, b));
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public Set<K> keySet() {
        return new HashSet(Arrays.asList(set.stream().map((KV<K, V> kv) -> {
            if (kv == null) {
                return null;
            }
            return kv.k;
        }).filter(x -> x != null).toArray()));
    }

    @Override
    public Collection<V> values() {
        return (Collection) Arrays.asList(set.stream().map((KV<K, V> kv) -> {
            if (kv == null) {
                return null;
            }
            return kv.v;
        }).toArray());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        class E implements Entry<K, V> {

            K k;
            V v;

            @Override
            public K getKey() {
                return k;
            }

            @Override
            public V getValue() {
                return v;
            }

            @Override
            public V setValue(V value) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }

        return new HashSet(Arrays.asList(set.stream().map((KV<K, V> kv) -> {
            if (kv == null) {
                return null;
            }
            E e = new E();
            e.k = kv.k;
            e.v = kv.v;
            return e;
        }).filter(a -> a != null).toArray()));
    }
}
