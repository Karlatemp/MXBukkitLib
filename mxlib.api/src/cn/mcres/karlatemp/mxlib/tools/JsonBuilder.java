/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: JsonBuilder.java@author: karlatemp@vip.qq.com: 19-11-2 下午9:25@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Fast to create a map or list.
 * @since 2.5
 */
@SuppressWarnings("unchecked")
public class JsonBuilder {
    private static final Supplier<Map> DEFAULT_MAP_CREATOR = LinkedHashMap::new;
    private static final Supplier<List> DEFAULT_LIST_CREATOR = ArrayList::new;
    private final Supplier<List> lc;
    private final Supplier<Map> mc;
    private final List<Object> path;
    private final List<Object> paths;
    private Object name;
    private boolean named;
    private Object finished;

    protected Object last() {
        if (path.isEmpty())
            return null;
        return path.get(path.size() - 1);
    }

    public JsonBuilder(Supplier<Map> map_creator, Supplier<List> list_creator) {
        if (map_creator == null) map_creator = DEFAULT_MAP_CREATOR;
        if (list_creator == null) list_creator = DEFAULT_LIST_CREATOR;
        this.mc = map_creator;
        this.lc = list_creator;
        path = new ArrayList<>();
        paths = new ArrayList<>();
    }

    public String getPath() {
        StringBuilder sb = new StringBuilder();
        sb.append('$');
        for (Object o : paths) {
            if (o instanceof Number)
                sb.append('[').append(o).append(']');
            else
                sb.append('.').append(o);
        }
        return sb.toString();
    }

    public JsonBuilder() {
        this(null, null);
    }

    public JsonBuilder beginObject() {
        begin(mc);
        return this;
    }

    public JsonBuilder value(Object value) {
        Object last = last();
        if (last == null) throw new RuntimeException("Add a element with no parent.");
        if (last instanceof Map) {
            if (!named) {
                throw new RuntimeException("Add a element with no name at path[" + getPath() + "]");
            }
            ((Map) last).put(name, value);
            named = false;
        } else {
            ((List) last).add(value);
        }
        return this;
    }

    public JsonBuilder name(Object name) {
        Object last = last();
        if (last == null) throw new RuntimeException("Setup name with no element.");
        if (last instanceof List) {
            throw new RuntimeException("Cannot set a name with a list at path[" + getPath() + "]");
        }
        if (named)
            throw new RuntimeException("Cannot set double name at path[" + getPath() + "]");
        named = true;
        this.name = name;
        return this;
    }

    protected void begin(Supplier sup) {
        if (path.isEmpty()) {
            path.add(sup.get());
        } else {
            Object last = last();
            if (last instanceof Map) {
                if (!named) {
                    throw new RuntimeException("Unknown name at path[" + getPath() + "]");
                }
                Object nw = sup.get();
                named = false;
                ((Map) last).put(name, nw);
                path.add(nw);
                paths.add(name);
            } else {
                List l = (List) last;
                Object nw = sup.get();
                paths.add(l.size());
                l.add(nw);
                path.add(nw);
            }
        }

    }

    public JsonBuilder beginArray() {
        begin(lc);
        return this;
    }

    public JsonBuilder end() {
        if (path.isEmpty()) {
            throw new RuntimeException("Cannot end the root.");
        }
        if (path.size() == 1) {
            finished = path.get(0);
            path.clear();
            paths.clear();
        } else {
            path.remove(path.size() - 1);
            if (!paths.isEmpty()) {
                paths.remove(paths.size() - 1);
            }
        }
        return this;
    }

    public JsonBuilder endArray() {
        Object last = last();
        if (last instanceof List) {
            return end();
        }
        throw new RuntimeException("Ending array but found a map[" + getPath() + "]");
    }
    public JsonBuilder endObject() {
        Object last = last();
        if (last instanceof Map) {
            return end();
        }
        throw new RuntimeException("Ending object but found a map[" + getPath() + "]");
    }

    public Object finished() {
        return finish();
    }

    public <V> V finished(Function<Object, V> fun) {
        return fun.apply(finish());
    }

    public <T, V> V finish(Function<T, V> fun) {
        return fun.apply(finish());
    }

    public void finished(Consumer<Object> con) {
        con.accept(finish());
    }

    public <T> void finish(Consumer<T> con) {
        con.accept(finish());
    }

    public <T> T finish() {
        if (finished != null) {
            return (T) finished;
        } else {
            Object fr = path.get(0);
            path.clear();
            paths.clear();
            return (T) fr;
        }
    }

    public JsonBuilder put(Object key, Object value) {
        return name(key).value(value);
    }

}
