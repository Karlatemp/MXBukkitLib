package cn.mcres.karlatemp.mxlib.tools;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map快速构建(链式)
 */
public class MapBuilder<K, V> extends LinkedHashMap<K, V> {
    public MapBuilder(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MapBuilder(int initialCapacity) {
        super(initialCapacity);
    }

    public MapBuilder() {
    }

    public MapBuilder(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public MapBuilder(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public MapBuilder<K, V> add(K key, V value) {
        put(key, value);
        return this;
    }
}
