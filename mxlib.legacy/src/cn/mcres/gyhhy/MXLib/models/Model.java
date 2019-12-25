/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Model.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.models;

import cn.mcres.gyhhy.MXLib.Core;
import cn.mcres.gyhhy.MXLib.ext.java.util.ComparatorSet;
import cn.mcres.gyhhy.MXLib.ext.java.util.KV;
import cn.mcres.gyhhy.MXLib.models.javassist.JavaSsist;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class Model<T> {

    private static final ComparatorSet<KV<String, Model<?>>> services = new ComparatorSet<>();

    static {
        registerModel("javassist", new JavaSsist());
    }

    public static boolean isSupport(Class<?> c) {
        c.asSubclass(Model.class);
        while (c != null) {
            Method[] mts = c.getDeclaredMethods();
            Field[] fd = c.getDeclaredFields();
            for (Method met : mts) {
                if (met.getReturnType() == boolean.class) {
                    int mod = met.getModifiers();
                    if (Modifier.isStatic(mod)) {
                        if (met.getName().equals("isSupport")) {
                            Class<?>[] pts = met.getParameterTypes();
                            if (pts.length == 1) {
                                if (pts[0].isAssignableFrom(Void.class)) {
                                    try {
                                        met.setAccessible(true);
                                        return (boolean) met.invoke(null);
                                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                        Core.getBL().printStackTrace(ex);
                                    } finally {
                                        met.setAccessible(false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (Field f : fd) {
                if (f.getType() == boolean.class) {
                    if (Modifier.isStatic(f.getModifiers())) {
                        String name = f.getName();
                        if (name.equals("isSupport") || name.equalsIgnoreCase("IS_SUPPORT")) {
                            try {
                                f.setAccessible(true);
                                return f.getBoolean(null);
                            } catch (IllegalArgumentException | IllegalAccessException ex) {
                                Core.getBL().printStackTrace(ex);
                            } finally {
                                f.setAccessible(false);
                            }
                        }
                    }
                }
            }
            c = c.getSuperclass();
        }
        return false;
    }

    public static boolean isRegistered(String key) {
        return services.search(key) != null;
    }

    @SuppressWarnings("unchecked")
    public static <T, C extends Model<T>> C getModelSafe(String key) {
        C m = getModel(key);
        if (m == null) {
            return (C) DefaultNonModel.non;
        }
        return m;
    }

    @SuppressWarnings("unchecked")
    public static <T, C extends Model<T>> C getModel(String key) {
        KV<String, Model<?>> kv = services.search(key);
        if (kv == null) {
            return null;
        }
        return (C) kv.v;
    }

    public static boolean registerModel(Model<?> model) {
        return registerModel(model.getClass().getSimpleName(), model);
    }

    public static boolean registerModel(String key, Model<?> model) {
        if (isRegistered(key)) {
            return false;
        }
        if (!model.isSupport()) {
            return false;
        }
        services.add(new KV<>(key, model));
        return true;
    }

    public boolean isSupport() {
        return isSupport(getClass());
    }

    public abstract T getInstance();

    public String getVersion() {
        if (isSupport()) {
            return getVersion0();
        }
        return null;
    }

    protected abstract String getVersion0();
}

@SuppressWarnings({"FinalClass", "rawtypes", "MultipleTopLevelClassesInFile"})
final class DefaultNonModel<T> extends Model<T> {

    static final Model non = new DefaultNonModel();

    @Override
    public boolean isSupport() {
        return false;
    }

    @Override
    public T getInstance() {
        return null;
    }

    @Override
    protected String getVersion0() {
        return null;
    }
}
