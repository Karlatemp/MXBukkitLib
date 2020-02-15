/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:58:57
 *
 * MXLib/mxlib.message/STemplateGroup.java
 */

package cn.mcres.karlatemp.mxlib.codec;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class STemplateGroup implements STemplateAdapter {
    private static final ThreadLocal<Collection<STemplateGroup>> SEARCH_CONTEXT = ThreadLocal.withInitial(HashSet::new);
    protected final Map<Class<?>, STemplate> templateMap = initializeMap(newTemplateMap());
    protected final Collection<STemplateAdapter> adapters = initializeAdapter(newAdapterCollection());

    protected @NotNull Collection<STemplateAdapter> initializeAdapter(
            @NotNull Collection<STemplateAdapter> collection) {
        return collection;
    }

    protected @NotNull Collection<STemplateAdapter> newAdapterCollection() {
        return new ArrayDeque<>();
    }

    protected @NotNull Map<Class<?>, STemplate> initializeMap(
            @NotNull Map<Class<?>, STemplate> templateMap
    ) {
        templateMap.put(String.class, STemplate.STRING);
        templateMap.put(Integer.class, STemplate.INTEGER);
        templateMap.put(AtomicInteger.class, STemplate.ATOMIC_INTEGER);
        templateMap.put(AtomicLong.class, STemplate.ATOMIC_LONG);
        templateMap.put(Float.class, STemplate.FLOAT);
        templateMap.put(Short.class, STemplate.SHORT);
        templateMap.put(Byte.class, STemplate.BYTE);
        templateMap.put(Double.class, STemplate.DOUBLE);
        templateMap.put(Long.class, STemplate.LONG);
        templateMap.put(Boolean.class, STemplate.BOOLEAN);
        templateMap.put(boolean[].class, STemplate.BOOLEAN_ARRAY);
        templateMap.put(Character.class, STemplate.CHARACTER);
        return templateMap;
    }

    protected @NotNull Map<Class<?>, STemplate> newTemplateMap() {
        return new HashMap<>();
    }

    protected Class<?> translate(Class<?> source) {
        if (source == null && templateMap instanceof ConcurrentHashMap) return void.class;
        return source;
    }

    public STemplate get(Class<?> type) {
        final Collection<STemplateGroup> groups = SEARCH_CONTEXT.get();
        if (groups.contains(this)) {
            throw new IllegalArgumentException("Deva Vu!");
        }
        groups.add(this);
        try {
            for (var adapter : adapters) {
                var temp = adapter.get(type);
                if (temp != null) return temp;
            }
            return templateMap.get(translate(type));
        } finally {
            groups.remove(this);
        }
    }

    @Override
    public STemplate get(Object val) {
        final Collection<STemplateGroup> groups = SEARCH_CONTEXT.get();
        if (groups.contains(this)) {
            throw new IllegalArgumentException("Deva Vu!");
        }
        groups.add(this);
        try {
            for (var adapter : adapters) {
                var temp = adapter.get(val);
                if (temp != null) return temp;
            }
        } finally {
            groups.remove(this);
        }
        if (val == null) {
            return get(Void.class);
        }
        return get(val.getClass());
    }

    public final int hashCode() {
        return System.identityHashCode(this);
    }

    public final boolean equals(Object other) {
        return this == other;
    }

    public STemplateGroup register(Class<?> type, STemplate template) {
        var slot = translate(type);
        var old = template == null ? templateMap.remove(slot) : templateMap.put(slot, template);
        if (old != null && old != template) {
            if (old.getGroup() == this) old.setGroup(null);
        }
        if (template != null && template.getGroup() == null)
            template.setGroup(this);
        return this;
    }

    public STemplateGroup register(@NotNull STemplateAdapter adapter) {
        if (adapter == this) throw new IllegalArgumentException("?");
        adapters.add(adapter);
        return this;
    }
}
