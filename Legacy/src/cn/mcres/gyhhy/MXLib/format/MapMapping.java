package cn.mcres.gyhhy.MXLib.format;

import java.util.Map;
import java.util.function.Function;
//@SuppressWarnings({"rawtypes"})

public class MapMapping<K, V> implements Replacer {

    public static <K, V> MapMapping<K, V> mapping(Map<K, V> map) {
        return new MapMapping<>(map);
    }
    private final Map<K, V> m;

    public MapMapping(Map<K, V> map) {
        this.m = map;
    }

    public Map<K, V> getMap() {
        return m;
    }

    @Override
    @SuppressWarnings("element-type-mismatch")
    public String apply(String t) {
        return String.valueOf(m.get(t));
    }

    @Override
    public boolean containsKey(String key) {
        return m.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return m.isEmpty();
    }

}
