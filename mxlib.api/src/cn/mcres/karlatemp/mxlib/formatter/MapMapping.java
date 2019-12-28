/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MapMapping.java@author: karlatemp@vip.qq.com: 2019/12/26 下午10:05@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.formatter;

import java.util.Map;
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
    @SuppressWarnings({"element-type-mismatch", "SuspiciousMethodCalls"})
    public String apply(String t) {
        return String.valueOf(m.get(t));
    }

    @Override
    public void apply(StringBuilder builder, String key) {
        builder.append(apply(key));
    }

    @Override
    public void apply(StringBuilder builder, int key) {
        builder.append(getSlot(key));
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
