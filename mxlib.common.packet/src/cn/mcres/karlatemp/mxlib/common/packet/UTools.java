/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: UTools.java@author: karlatemp@vip.qq.com: 2020/1/23 下午2:42@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.packet;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

@SuppressWarnings("Java9ReflectionClassVisibility")
public class UTools {
    @SuppressWarnings("unchecked")
    public static <T> T allocate(Class<T> type) {
        return (T) allocate.apply(type);
    }

    private static final Function<Class<?>, Object> allocate;

    static {
        try {
            final MethodHandles.Lookup lk = MethodHandles.lookup();
            final Class<?> Const = Class.forName("java.lang.invoke.DirectMethodHandle$Constructor");
            final Class<?> Direct = Class.forName("java.lang.invoke.DirectMethodHandle");
            final Constructor<Object> constructor = Object.class.getConstructor();

            Field instanceClass = null;
            for (Field f : Const.getDeclaredFields()) {
                if (f.getType() == Class.class) {
                    instanceClass = f;
                    break;
                }
            }
            assert instanceClass != null;
            instanceClass.setAccessible(true);
            Field instanceClass0 = instanceClass;


            Method allocateInstance = null;
            for (Method met : Direct.getDeclaredMethods()) {
                if (met.getParameterCount() == 1) {
                    if (met.getName().equals("allocateInstance")) {
                        allocateInstance = met;
                    }
                }
            }
            assert allocateInstance != null;
            allocateInstance.setAccessible(true);
            Method allocateInstance0 = allocateInstance;


            allocate = clazz -> {
                try {
                    final MethodHandle base = lk.unreflectConstructor(constructor);
                    instanceClass0.set(base, clazz);
                    try {
                        return allocateInstance0.invoke(null, base);
                    } catch (InvocationTargetException target) {
                        throw target.getTargetException();
                    }
                } catch (RuntimeException | Error re) {
                    throw re;
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (Throwable any) {
            throw new ExceptionInInitializerError(any);
        }
    }
}
