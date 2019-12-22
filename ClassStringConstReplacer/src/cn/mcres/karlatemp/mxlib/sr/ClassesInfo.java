/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ClassesInfo.java@author: karlatemp@vip.qq.com: 19-12-21 下午12:26@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.sr;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ClassesInfo implements StringReplacer {
    @Override
    public String replaceField(@NotNull String className, @NotNull String fieldName, @NotNull String defaultValue) {
        classes.computeIfAbsent(className, b -> new ClassInfo().initialize())
                .fields.put(fieldName, defaultValue);
        return null;
    }

    @Override
    public String replaceMethod(@NotNull String className, @NotNull String method, @NotNull String method_desc, @NotNull String defaultValue, int index) {
        classes.computeIfAbsent(className, b -> new ClassInfo().initialize())
                .methods.computeIfAbsent(method + method_desc, b -> new ArrayList<>())
                .add(defaultValue);
        return null;
    }

    public void load(Map<String, Map<String, Object>> data) {
        if (data != null) {
            initialize();
            for (Map.Entry<String, Map<String, Object>> e : data.entrySet()) {
                classes.put(e.getKey(), new ClassInfo(e.getValue()));
            }
        }
    }

    public ClassesInfo copy() {
        ClassesInfo ci = new ClassesInfo();
        ci.initialize();
        if (classes != null) {
            for (Map.Entry<String, ClassInfo> entry : classes.entrySet()) {
                ci.classes.put(entry.getKey(), entry.getValue().copy());
            }
        }
        return ci;
    }

    public static class ClassInfo {
        public Map<String, String> fields;
        public Map<String, List<String>> methods;

        public ClassInfo() {
        }

        @SuppressWarnings("unchecked")
        public ClassInfo(Map<String, Object> value) {
            fields = (Map<String, String>) value.get("fields");
            final Map<String, Object> methods = (Map<String, Object>) value.get("methods");
            if (methods != null) {
                this.methods = methods.entrySet().stream().collect(LinkedHashMap::new, (a, b) -> {
                    final Object o = b.getValue();
                    if (o == null) return;
                    if (o instanceof List) {
                        a.put(b.getKey(), (List<String>) o);
                    } else if (o instanceof Object[]) {
                        a.put(b.getKey(), Arrays.stream((Object[]) o).map(String::valueOf).collect(Collectors.toList()));
                    } else {
                        throw new ClassCastException(o.getClass().getName() + " cannot cast to array.");
                    }
                }, Map::putAll);
            }
            initialize();
        }

        public Map<String, Object> toMap() {
            Map<String, Object> sv = new LinkedHashMap<>();
            sv.put("fields", fields);
            sv.put("methods", methods);
            return sv;
        }

        public ClassInfo initialize() {
            if (fields == null) fields = new LinkedHashMap<>();
            if (methods == null) methods = new LinkedHashMap<>();
            return this;
        }

        public ClassInfo copy() {
            ClassInfo ci = new ClassInfo().initialize();
            if (fields != null) {
                ci.fields.putAll(fields);
            }
            if (methods != null) {
                for (Map.Entry<String, List<String>> s : methods.entrySet()) {
                    ci.methods.put(s.getKey(), new ArrayList<>(s.getValue()));
                }
            }
            return ci;
        }
    }

    public Map<String, ClassInfo> classes;

    public ClassesInfo() {
    }

    public ClassesInfo initialize() {
        if (classes == null) classes = new LinkedHashMap<>();
        return this;
    }

    public ClassesInfo(Map<String, Map<String, Object>> data) {
        initialize();
        load(data);
    }

    public Map<String, Map<String, Object>> toMap() {
        Map<String, Map<String, Object>> sv = new LinkedHashMap<>();
        for (Map.Entry<String, ClassInfo> info : classes.entrySet()) {
            sv.put(info.getKey(), info.getValue().toMap());
        }
        return sv;
    }

    public Processor newProcessor() {
        return new Processor();
    }

    public class Processor implements StringReplacer {
        @Override
        public String replaceField(@NotNull String className, @NotNull String fieldName, @NotNull String defaultValue) {
            final ClassInfo info = classes.get(className);
            if (info != null) {
                if (info.fields != null) {
                    return info.fields.get(fieldName);
                }
            }
            return null;
        }

        @Override
        public String replaceMethod(@NotNull String className, @NotNull String method, @NotNull String method_desc, @NotNull String defaultValue, int index) {
            if (index < 0) return null;
            final ClassInfo info = classes.get(className);
            if (info != null) {
                if (info.methods != null) {
                    final List<String> strings = info.methods.get(method + method_desc);
                    if (strings != null) {
                        if (index < strings.size()) return strings.get(index);
                    }
                }
            }
            return null;
        }
    }

    public static <T, V> Map<T, V> overrideMap(Map<T, V> source, Map<T, V> override) {
        Map<T, V> result;
        if (source instanceof LinkedHashMap || source == null) {
            result = new LinkedHashMap<>();
            if (override != null) result.putAll(override);

            if (source != null)
                for (Map.Entry<T, V> e : source.entrySet()) {
                    if (!result.containsKey(e.getKey())) {
                        result.put(e.getKey(), e.getValue());
                    }
                }
        } else {
            result = new HashMap<>(source);
            if (override != null) result.putAll(override);
        }
        return result;
    }

    public synchronized ClassesInfo override(ClassesInfo override) {
        initialize();
        if (override != null) {
            if (override.classes != null) {
                Map<String, ClassInfo> nw = overrideMap(classes, override.classes);
                for (Map.Entry<String, ClassInfo> ci : classes.entrySet()) {
                    String k = ci.getKey();
                    ClassInfo cw = ci.getValue();
                    ClassInfo nc = nw.get(k);
                    if (cw == nc) continue;
                    if (!override.classes.containsKey(k)) continue;
                    nc.fields = overrideMap(cw.fields, override.classes.get(k).fields);
                    nc.methods = overrideMap(cw.methods, override.classes.get(k).methods);
                }
                classes = nw;
                /*if (classes instanceof LinkedHashMap) {
                    LinkedHashMap<String, ClassInfo> newInfo = new LinkedHashMap<>(classes.size());
                    for (Map.Entry<String, ClassInfo> e : override.classes.entrySet()) {
                        final String key = e.getKey();
                        if (!classes.containsKey(key)) {
                            newInfo.put(key, e.getValue());
                        }
                    }
                    for (Map.Entry<String, ClassInfo> e : classes.entrySet()) {
                        final String key = e.getKey();
                        if (!newInfo.containsKey(key)) {
                            newInfo.put(key, e.getValue());
                        }
                    }
                } else {
                    for (Map.Entry<String, ClassInfo> e : override.classes.entrySet()) {
                        final String key = e.getKey();
                        if (!classes.containsKey(key)) {
                            classes.put(key, e.getValue());
                        }
                        final ClassInfo info = classes.get(key);
                        final ClassInfo ov = e.getValue();
                        if (ov.methods != null) {
                            info.methods.putAll(ov.methods);
                        }
                        if (ov.fields != null) {
                            info.fields.putAll(ov.fields);
                        }
                    }
                }*/
            }
        }
        return this;
    }
}
