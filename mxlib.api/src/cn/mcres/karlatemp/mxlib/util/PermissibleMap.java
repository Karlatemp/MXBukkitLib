/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PermissibleMap.java@author: karlatemp@vip.qq.com: 19-9-26 下午1:21@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Permission access check map
 *
 * @param <K> The key type of this map
 * @param <V> The value type of this map
 * @see PCollectionPermission
 * @since 2.2
 */
public class PermissibleMap<K, V> extends AbstractMap<K, V> {
    protected Map<K, V> parent;
    protected Set<PCollectionPermission> permissions;

    protected PermissibleMap() {
    }

    public PermissibleMap(Map<K, V> parent) {
        this(parent, PCollectionPermission.ALL);
    }

    public PermissibleMap(Map<K, V> parent, Set<PCollectionPermission> permissions) {
        this.parent = parent;
        this.permissions = permissions;
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        if (permissions.contains(PCollectionPermission.ENTRY_SET)) {
            return new PermissibleSet<>(parent.entrySet(), permissions);
        }
        return Collections.emptySet();
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return defaultValue;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        if (permissions.contains(PCollectionPermission.FOR_EACH))
            parent.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        if (permissions.contains(PCollectionPermission.REPLACE))
            parent.replaceAll(function);
    }

    @Nullable
    @Override
    public V putIfAbsent(K key, V value) {
        if (permissions.contains(PCollectionPermission.SET))
            return parent.putIfAbsent(key, value);
        return value;
    }

    @Override
    public boolean remove(Object key, Object value) {
        if (permissions.contains(PCollectionPermission.REMOVE))
            return parent.remove(key, value);
        return false;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        if (permissions.contains(PCollectionPermission.REPLACE))
            return parent.replace(key, oldValue, newValue);
        return false;
    }

    @Nullable
    @Override
    public V replace(K key, V value) {
        if (permissions.contains(PCollectionPermission.REPLACE))
            return parent.replace(key, value);
        return value;
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        if (permissions.contains(PCollectionPermission.COMPUTE))
            return parent.computeIfAbsent(key, mappingFunction);
        return null;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (permissions.contains(PCollectionPermission.COMPUTE))
            return parent.computeIfPresent(key, remappingFunction);
        return null;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (permissions.contains(PCollectionPermission.COMPUTE))
            return parent.compute(key, remappingFunction);
        return null;
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        if (permissions.contains(PCollectionPermission.MERGE))
            return parent.merge(key, value, remappingFunction);
        return null;
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
    public boolean containsValue(Object value) {
        if (permissions.contains(PCollectionPermission.CONTAINS_VALUE))
            return parent.containsValue(value);
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        if (permissions.contains(PCollectionPermission.CONTAINS_KEY))
            return parent.containsKey(key);
        return false;
    }

    @Override
    public V get(Object key) {
        if (permissions.contains(PCollectionPermission.GET))
            return parent.get(key);
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (permissions.contains(PCollectionPermission.SET))
            return parent.put(key, value);
        return null;
    }

    @Override
    public V remove(Object key) {
        if (permissions.contains(PCollectionPermission.REMOVE))
            return parent.remove(key);
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (permissions.contains(PCollectionPermission.SET))
            parent.putAll(m);
    }

    @Override
    public void clear() {
        if (permissions.contains(PCollectionPermission.CLEAR))
            parent.clear();
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        if (permissions.contains(PCollectionPermission.KEY_SET))
            return new PermissibleSet<>(parent.keySet(), permissions);
        return Collections.emptySet();
    }

    @NotNull
    @Override
    public Collection<V> values() {
        if (permissions.contains(PCollectionPermission.VALUES))
            return new PermissibleCollection<>(parent.values(), permissions);
        return Collections.emptySet();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o instanceof PermissibleMap) {
            return parent.equals(((PermissibleMap) o).parent);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 92387 + parent.hashCode();
    }

    @Override
    public String toString() {
        if (permissions.contains(PCollectionPermission.TO_STRING))
            return parent.toString();
        return "{}";
    }
}
