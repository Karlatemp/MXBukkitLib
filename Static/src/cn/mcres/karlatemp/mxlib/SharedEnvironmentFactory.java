/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SharedEnvironmentFactory.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.bean.IEnvironmentFactory;
import cn.mcres.karlatemp.mxlib.exceptions.ObjectCreateException;
import cn.mcres.karlatemp.mxlib.tools.IMemberScanner;
import cn.mcres.karlatemp.mxlib.tools.IObjectCreator;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;

public class SharedEnvironmentFactory implements
        IEnvironmentFactory, Function<Class, IEnvironmentFactory.IField[]> {
    private Function<Class, IField[]> factory = this;
    private final Map<Class, IField[]> fields = new HashMap<>();

    @Override
    public <T> T getEnvironment(Class<T> type) throws ObjectCreateException {
        return loadEnvironment(type, Collections.emptyMap());
    }

    @Override
    public <T> T loadEnvironment(Class<T> type, Map<String, Object> env) throws ObjectCreateException {
        IObjectCreator creator = MXBukkitLib.getBeanManager().getBeanNonNull(IObjectCreator.class);
        IField<T>[] fields = getFields(type);
        T t = creator.newInstance(type);
        for (IField<T> field : fields) {
            String name = field.getName();
            if (env.containsKey(name)) {
                field.set(t, env.get(name));
            }
        }
        return t;
    }

    @Override
    public <T> IField<T>[] getFields(Class<T> type) {
        synchronized (fields) {
            if (!fields.containsKey(type)) {
                synchronized (factory) {
                    final IField[] fields = factory.apply(type);
                    this.fields.put(type, fields);
                    return fields;
                }
            }
            return fields.get(type);
        }
    }

    @Override
    public <T> IEnvironmentFactory setFields(Class<T> type, IField<T>[] fields) {
        if (fields == null) this.fields.remove(type);
        else this.fields.put(type, fields);
        return this;
    }

    @Override
    public <T> Function<Class<T>, IField<T>[]> getDefaultFactory() {
        return (Function) factory;
    }

    @Override
    public <T> IEnvironmentFactory settDefaultFactory(Function<Class<T>, IField<T>[]> factory) {
        this.factory = (Function) factory;
        return this;
    }

    @Override
    public <T> Map<String, Object> toEnvironment(@NotNull Class<T> type, T obj) {
        if (obj == null) return Collections.emptyMap();
        IField<T>[] fields = getFields(type);
        Map<String, Object> map = new LinkedHashMap<>();
        for (IField<T> field : fields) {
            Object val = field.get(obj);
            if (val != null) {
                Class c = val.getClass();
                if (val instanceof String || val instanceof Number ||
                        val instanceof Boolean || val instanceof Character || val instanceof Map
                        || val instanceof List) {
                } else if (c.isPrimitive()) {
                } else {
                    val = toEnvironment(c, val);
                }
            }
            map.put(field.getName(), val);
        }
        return map;
    }

    private String a(String a) {
        if (a.isEmpty()) return a;
        char first = a.charAt(0);
        if (first == '_') return a.substring(1);
        if (Character.isUpperCase(first)) {
            char[] copy = a.toCharArray();
            copy[0] = Character.toLowerCase(first);
            return new String(copy);
        }
        return a;
    }

    @Override
    public IField[] apply(Class typ) {
        if (typ != null) {
            IBeanManager bm = MXBukkitLib.getBeanManager();
            class $ implements IField {
                String n;
                Method g, s;
                Field f;
                Class typ;

                @NotNull
                @Override
                public String getName() {
                    return n;
                }

                @NotNull
                @Override
                public Class getType() {
                    return typ;
                }

                @Override
                public void set(@NotNull Object thiz, @Nullable Object value) {
                    if (value != null) {
                        if (!typ.isInstance(value)) {
                            if (value instanceof Map) {
                                try {
                                    value = SharedEnvironmentFactory.this.loadEnvironment(typ, (Map) value);
                                } catch (ObjectCreateException e) {
                                    ThrowHelper.getInstance().thr(e);
                                }
                            }
                        }
                    }
                    if (g != null || s != null) {
                        if (s != null) {
                            try {
                                s.invoke(thiz, value);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                ThrowHelper.getInstance().thr(e);
                            }
                        }
                    } else {
                        if (f != null) {
                            try {
                                f.set(thiz, value);
                            } catch (IllegalAccessException e) {
                                ThrowHelper.getInstance().thr(e);
                            }
                        }
                    }
                }

                @Nullable
                @Override
                public Object get(@NotNull Object thiz) {
                    if (g != null || s != null) {
                        if (g != null) {
                            try {
                                return g.invoke(thiz);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                ThrowHelper.getInstance().thr(e);
                            }
                        }
                    } else {
                        if (f != null) {
                            try {
                                return f.get(thiz);
                            } catch (IllegalAccessException e) {
                                ThrowHelper.getInstance().thr(e);
                            }
                        }
                    }
                    return null;
                }

                @Override
                public String toString() {
                    return n + "$" + typ + "$" + g + "$" + s + "$" + f;
                }
            }
            final Collection<$> ct = new ArrayList<>();
            final IMemberScanner scanner = bm.getBeanNonNull(IMemberScanner.class);
            r:
            for (Field f : scanner.getAllField(typ)) {
                if (Modifier.isTransient(f.getModifiers())) continue;
                if (Modifier.isStatic(f.getModifiers())) continue;
                if (f.isSynthetic()) continue;
                String n = f.getName();
                for ($ m : ct) {
                    if (m.n.equals(n)) {
                        if (m.typ != f.getType()) {
                            throw new ClassCastException("Changed type in field: " + f + "(need " + m.typ + " but found " + f.getType() + ")");
                        }
                        m.f = f;
                        continue r;
                    }
                }
                $ w = new $();
                w.f = f;
                w.n = n;
                w.typ = f.getType();
                ct.add(w);
            }
            rt:
            for (Method met : scanner.getAllMethod(typ)) {
                if (met.isBridge() || met.isSynthetic())
                    continue;
                if (Modifier.isTransient(met.getModifiers()) ||
                        Modifier.isStatic(met.getModifiers()))
                    continue;
                if (met.getDeclaringClass() == Object.class) continue; // Skip GetClass()
                String name = met.getName();
                if (name.length() > 3) {
                    String pre = name.substring(0, 3);
                    String ne = a(name.substring(3));
                    byte x = 0;
                    switch (pre) {
                        case "set": {
                            x = 1;
                            break;
                        }
                        case "get": {
                            x = 2;
                            break;
                        }
                    }
                    if (x == 0) continue;
                    if (x == 1) {
                        if (met.getParameterCount() != 1) continue;
                        Class typx = met.getParameterTypes()[0];
                        for ($ a : ct) {
                            if (a.n.equals(ne)) {
                                if (a.typ != typx) {
                                    throw new ClassCastException("Changed type in setter: " + met + "(need " + a.typ + " but found " + typx + ")");
                                }
                                a.s = met;
                                continue rt;
                            }
                        }
                        $ a = new $();
                        a.s = met;
                        a.n = ne;
                        a.typ = typx;
                        ct.add(a);
                    } else {
                        if (met.getParameterCount() != 0) continue;
                        Class typx = met.getReturnType();
                        for ($ a : ct) {
                            if (a.n.equals(ne)) {
                                if (a.typ != typx) {
                                    throw new ClassCastException("Changed type in setter: " + met + "(need " + a.typ + " but found " + typx + ")");
                                }
                                a.g = met;
                                continue rt;
                            }
                        }
                        $ a = new $();
                        a.g = met;
                        a.n = ne;
                        a.typ = typx;
                        ct.add(a);
                    }
                }
            }
            return ct.toArray(new IField[0]);
        }
        return null;
    }
}
