/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ObjectReflect.java@author: karlatemp@vip.qq.com: 19-11-10 下午1:12@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

public class ObjectReflect<T> implements Reflect<T> {
    private final T val;
    private Class matching;
    private static final NoSuchMethodException breakUp = new NoSuchMethodException();
    private static final NoSuchFieldException breakUpField = new NoSuchFieldException();
    private boolean root;

    public ObjectReflect(T val) {
        this(val, null);
    }

    public ObjectReflect(T val, Class matching) {
        this.val = val;
        if (!(root = matching != null)) {
            if (val == null) {
                matching = Object.class;
            } else matching = val.getClass();
        }
        this.matching = matching;
    }

    @Nullable
    @Override
    public T get() {
        return val;
    }

    private Class rt() {
        if (root) return null;
        return matching;
    }

    @NotNull
    @Override
    public Reflect<T> toRoot() {
        if (val != null) matching = val.getClass();
        root = true;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> Reflect<V> cast(Class<V> type) {
        root = false;
        if (type == null) {
            matching = Object.class;
            return (Reflect<V>) this;
        }
        if (val == null) {
            matching = type;
            return (Reflect<V>) this;
        }
        if (!type.isInstance(val))
            ThrowHelper.thrown(new ClassCastException(
                    "Cannot cast " + val.getClass().getName() + " to " + type.getName()
            ));
        matching = type;
        return (Reflect<V>) this;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <V> RField<T, V> getField(@NotNull String name, @Nullable Class<V> type) {
        Class<?> watching = matching;
        try {
            Field f = watching.getField(name);
            if (type != null) {
                if (f.getType() != type) {
                    throw breakUpField;
                }
            }
            return new RFieldImpl<>(f, rt(), val);
        } catch (Throwable ignore) {
        }
        if (watching.isArray())
            return null;
        while (watching != null) {
            try {
                Field f = watching.getDeclaredField(name);
                if (type != null && f.getType() != type) throw breakUpField;
                return new RFieldImpl<>(f, rt(), val);
            } catch (Throwable ignore) {
            }
            watching = watching.getSuperclass();
        }
        if (type != null) {
            MethodHandles.Lookup root = Toolkit.Reflection.getRoot();
            watching = matching;
            while (watching != null) {
                try {
//                    return new ObjectReflect(root.findGetter(watching, name, type).invoke(get()));
                    return new RFieldHandle<T, V>(
                            root.findGetter(watching, name, type),
                            root.findSetter(watching, name, type),
                            false, rt()
                    ).self(val);
                } catch (Throwable ignore) {
                }
                try {
                    return new RFieldHandle<>(
                            root.findStaticGetter(watching, name, type),
                            root.findStaticSetter(watching, name, type),
                            true, matching
                    );
                } catch (Throwable ignore) {
                }
                watching = watching.getSuperclass();
            }
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "Duplicates"})
    @Nullable
    @Override
    public Reflect<?> invoke(@NotNull String name, Object... values) {
        int plen = values.length;
        Class<?> watching = matching;
        {
            Class[] types = new Class[plen];
            for (int i = 0; i < plen; i++) {
                Object o = values[i];
                Class oc;
                if (o == null) {
                    oc = Object.class;
                } else {
                    oc = o.getClass();
                }
                types[i] = oc;
            }
            try {
                return new ObjectReflect(watching.getMethod(name, types).invoke(get(), values));
            } catch (NoSuchMethodException | IllegalAccessException ignore) {
            } catch (InvocationTargetException e) {
                ThrowHelper.thrown(e.getTargetException());
            } catch (Exception e) {
                if (!e.getClass().getName().equals("java.lang.reflect.InaccessibleObjectException"))
                    ThrowHelper.thrown(e);
            }
            Class<?> c1 = watching;
            if (!c1.isArray())
                while (c1 != null) {
                    try {
                        Method m = c1.getDeclaredMethod(name, types);
                        m.setAccessible(true);
                        return new ObjectReflect(m.invoke(get(), values));
                    } catch (NoSuchMethodException | IllegalAccessException ignore) {
                    } catch (InvocationTargetException e) {
                        ThrowHelper.thrown(e.getTargetException());
                    } catch (Exception e) {
                        if (!e.getClass().getName().equals("java.lang.reflect.InaccessibleObjectException"))
                            ThrowHelper.thrown(e);
                    }
                    c1 = c1.getSuperclass();
                }
        }
        List<Method> matched = new ArrayList<>();
        searchMethod:
        for (Method m : watching.getMethods()) {
            if (m.getParameterCount() == plen) {
                if (name.equals(m.getName())) {
                    final Class<?>[] types = m.getParameterTypes();
                    for (int i = 0; i < plen; i++) {
                        Object cv = values[i];
                        if (cv == null) {
                            continue;
                        }
                        if (!types[i].isInstance(cv)) continue searchMethod;
                        matched.add(m);
                    }
                }
            }
        }
        if (matched.isEmpty()) {
            if (watching.isArray()) return null;
            while (watching != null) {
                searchMethod:
                for (Method m : watching.getDeclaredMethods()) {
                    if (m.getParameterCount() == plen) {
                        if (name.equals(m.getName())) {
                            final Class<?>[] types = m.getParameterTypes();
                            for (int i = 0; i < plen; i++) {
                                Object cv = values[i];
                                if (cv == null) {
                                    continue;
                                }
                                if (!types[i].isInstance(cv)) continue searchMethod;
                                matched.add(m);
                            }
                        }
                    }
                }
                switch (matched.size()) {
                    case 0:
                        break;
                    case 1:
                        try {
                            return new ObjectReflect(matched.get(0).invoke(get(), values));
                        } catch (IllegalAccessException e) {
                            return ThrowHelper.thrown(e);
                        } catch (InvocationTargetException e) {
                            return ThrowHelper.thrown(e.getTargetException());
                        }
                    default:
                        throw new UnknownError("Cannot select method for " + matched);
                }
                watching = watching.getSuperclass();
            }
        }
        if (matched.size() > 1) {
            throw new UnknownError("Cannot select method for " + matched);
        }
        try {
            return new ObjectReflect(matched.get(0).invoke(get(), values));
        } catch (IllegalAccessException e) {
            return ThrowHelper.thrown(e);
        } catch (InvocationTargetException e) {
            return ThrowHelper.thrown(e.getTargetException());
        }
    }

    @Nullable
    @Override
    public Reflect<?> invoke(@NotNull String name, Class[] types, Object... values) {
        return getMethod(name, types).invoke(values);
    }

    @Override
    public RMethod<T, ?> getMethod(@NotNull String name, Class[] types) {
        return getMethod(name, null, types);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> RMethod<T, R> getMethod(@NotNull String name, Class<R> returnType, Class... types) {
        if (matching.isArray()) return null;
        try {
            Method m = matching.getMethod(name, types);
            if (returnType != null && m.getReturnType() != returnType) {
                throw breakUp;
            }
            return new RMethodImpl(m).self(val);
        } catch (NoSuchMethodException ignore) {
        }
        Class<?> mat = matching;
        while (mat != null) {
            try {
                final Method method = mat.getDeclaredMethod(name, types);
                if (returnType != null && method.getReturnType() != returnType) {
                    throw breakUp;
                }
                method.setAccessible(true);
                return new RMethodImpl(method).self(val);
            } catch (NoSuchMethodException ignore) {
            }
            mat = mat.getSuperclass();
        }
        if (returnType != null) {
            MethodHandles.Lookup root = Toolkit.Reflection.getRoot();
            mat = matching;
            MethodType type = MethodType.methodType(returnType, types);
            while (mat != null) {
                try {
                    return new RMethodHandle(
                            true, mat, root.findStatic(mat, name, type)
                    );
                } catch (Throwable ignore) {
                }
                try {
                    return new RMethodHandle(
                            false, mat, root.findVirtual(mat, name, type)
                    ).self(val);
                } catch (Throwable ignore) {
                }
                mat = mat.getSuperclass();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public RMethod<T, ?>[] getMethods(@NotNull String name) {
        if (matching.isArray())
            return new RMethod[0];
        @SuppressWarnings("WeakerAccess")
        class TW {
            String name;
            Class[] types;
            Class ret;

            TW(Method m) {
                name = m.getName();
                types = m.getParameterTypes();
                ret = m.getReturnType();
            }

            @Override
            public int hashCode() {
                return Objects.hash(name, types, ret);
            }

            @Override
            public boolean equals(Object obj) {
                if (obj.getClass() == getClass()) {
                    TW t = (TW) obj;
                    return Objects.equals(
                            name, t.name
                    ) && Objects.equals(
                            ret, t.ret
                    ) && Arrays.equals(
                            types, t.types
                    );
                }
                return false;
            }
        }
        Map<TW, Method> mtt = new HashMap<>();
        for (Method m : matching.getMethods()) {
            if (m.getName().equals(name)) {
                mtt.put(new TW(m), m);
            }
        }
        Class<?> c = matching;
        while (c != null) {
            for (Method m : c.getDeclaredMethods()) {
                if (m.getName().equals(name)) {
                    mtt.put(new TW(m), m);
                }
            }
            c = c.getSuperclass();
        }
        return mtt.values().stream().map(RMethodImpl::new).toArray(RMethod[]::new);
    }
}
