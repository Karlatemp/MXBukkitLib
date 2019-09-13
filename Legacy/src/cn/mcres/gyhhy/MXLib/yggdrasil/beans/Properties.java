/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.yggdrasil.beans;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Properties implements Map<String, String> {

    private final Map<String, String> p;
    private final Map<String, String> sign;

    @Override
    public String toString() {
        return p.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    public Properties(Map<String, String> parent) {
        this.p = (Map) parent;
        this.sign = new HashMap<>();
    }

    public String getSign(String kg) {
        return sign.get(kg);
    }

    public String setSign(String kg, String sg) {
        return sign.put(kg, sg);
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
    public boolean containsKey(Object key) {
        return p.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return p.containsValue(value);
    }

    @Override
    public String get(Object key) {
        return p.get(key);
    }

    @Override
    public String put(String key, String value) {
        return p.put(key, value);
    }

    @Override
    public String remove(Object key) {
        return p.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        p.putAll(m);
    }

    @Override
    public void clear() {
        p.clear();
    }

    @Override
    public Set<String> keySet() {
        return p.keySet();
    }

    @Override
    public Collection<String> values() {
        return p.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return p.entrySet();
    }

    @Override
    public String getOrDefault(Object key, String defaultValue) {
        return p.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super String> action) {
        p.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super String, ? extends String> function) {
        p.replaceAll(function);
    }

    @Override
    public String putIfAbsent(String key, String value) {
        return p.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return p.remove(key, value); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean replace(String key, String oldValue, String newValue) {
        return p.replace(key, oldValue, newValue); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String replace(String key, String value) {
        return p.replace(key, value);
    }

    @Override
    public String computeIfAbsent(String key, Function<? super String, ? extends String> mappingFunction) {
        return p.computeIfAbsent(key, mappingFunction); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String computeIfPresent(String key, BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
        return p.computeIfPresent(key, remappingFunction); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String compute(String key, BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
        return p.compute(key, remappingFunction); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String merge(String key, String value, BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
        return p.merge(key, value, remappingFunction); //To change body of generated methods, choose Tools | Templates.
    }

}
