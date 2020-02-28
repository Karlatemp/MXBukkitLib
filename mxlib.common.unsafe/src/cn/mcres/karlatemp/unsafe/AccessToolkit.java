/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AccessToolkit.java@author: karlatemp@vip.qq.com: 19-11-23 下午7:59@version: 2.0
 */

package cn.mcres.karlatemp.unsafe;

import jdk.internal.reflect.MethodAccessor;
import jdk.internal.reflect.ReflectionFactory;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Objects;

/**
 * Access Tools
 *
 * @since 2.7
 */
public class AccessToolkit {

    public static boolean isSupportModule() {
        try {
            Class.forName("java.lang.Module");
            return true;
        } catch (Throwable ignore) {
        }
        return false;
    }

    static {
        Reflection.getRoot();
    }

    interface A {
        void a(@NotNull AccessibleObject a, boolean b);
    }

    static A akm;

    // Sudo set Accessible and hidden JDK9+ Reflection Warning
    // Implements for JDK8
    static class Accessor implements MethodAccessor {
        static ReflectionFactory factory = ReflectionFactory.getReflectionFactory();
        static MethodAccessor accessor;

        static {
            try {
                try {
                    accessor = factory.newMethodAccessor(
                            AccessibleObject.class.getDeclaredMethod("setAccessible0", boolean.class)
                    );
                } catch (Throwable thr) {
                    //noinspection JavaReflectionMemberAccess
                    accessor = new Accessor(factory.newMethodAccessor(
                            AccessibleObject.class.getDeclaredMethod("setAccessible0", AccessibleObject.class, boolean.class)
                    ));
                }
            } catch (Throwable e) {
                Method[] methods = null;
                try {
                    // Maybe it is hidden.
                    methods = (Method[]) factory.newMethodAccessor(Class.class.getDeclaredMethod("getDeclaredMethods0", boolean.class))
                            .invoke(AccessibleObject.class, new Object[]{false});
                    Method setAccessible0 = null;
                    for (var met : methods) {
                        if (met.getName().equals("setAccessible0"))
                            if (met.getParameterCount() == 1)
                                if (met.getParameterTypes()[0] == Boolean.TYPE)
                                    setAccessible0 = met;
                    }
                    Objects.requireNonNull(setAccessible0, "unfounded AccessibleObject.setAccessible0");
                    accessor = factory.newMethodAccessor(setAccessible0);
                } catch (Throwable err) {
                    var ee = new ExceptionInInitializerError(err);
                    ee.addSuppressed(e);
                    if (methods != null) {
                        for (var met : methods) {
                            ee.addSuppressed(new Throwable(String.valueOf(met)));
                        }
                    }
                    throw ee;
                }
            }
            try {
                // Open ClassLoader's Module access
                Accessor.class.getModule().addExports(Accessor.class.getPackageName(), Accessor.class.getClassLoader().getClass().getModule());
            } catch (Throwable ignore) { // Catch NoSuchMethodError
            }
        }

        private final MethodAccessor setAccessible0;

        public Accessor(MethodAccessor setAccessible0) {
            this.setAccessible0 = setAccessible0;
        }

        public static void setAccess(AccessibleObject object, boolean accessible) throws Throwable {
            try {
                accessor.invoke(object, new Object[]{accessible});
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }

        @Override
        public Object invoke(Object obj, Object[] args) throws IllegalArgumentException, InvocationTargetException {
            if (args == null) throw new IllegalArgumentException("No args");
            if (args.length != 1) throw new IllegalArgumentException("Expect 1 arg but got " + args.length + " arg(s)");
            return setAccessible0.invoke(null, new Object[]{obj, args[0]});
        }
    }

    static void setup() {
        var loader = new ClassLoader(AccessToolkit.class.getClassLoader()) {
            Class<?> define(byte[] b) {
                return defineClass(null, b, 0, b.length, null);
            }
        };
        // JDK Proxy System will help our open package access.
        var proxy = Proxy.newProxyInstance(loader, new Class[]{Reflection.MethodAccessor}, (proxy_, method, args) -> null);
        ClassReader reader;
        try (var source = AccessToolkit.class.getResourceAsStream("AccessToolkit$Accessor.class")) {
            reader = new ClassReader(source);
        } catch (IOException ioe) {
            throw (ExceptionInInitializerError) new ExceptionInInitializerError("Cannot load $Accessor class source.").initCause(ioe);
        }
        var name = Reflection.MethodAccessor.getName().replace('.', '/');
        var mapping = new HashMap<String, String>();
        mapping.put("jdk/internal/reflect/MethodAccessor", name);
        mapping.put("jdk/internal/reflect/ReflectionFactory", name.replace("/MethodAccessor", "/ReflectionFactory"));
        var node = new ClassNode();
        reader.accept(node, 0);
        node.access = Opcodes.ACC_PUBLIC;
        {
            var jdk_name = proxy.getClass().getName();
            var last = jdk_name.lastIndexOf('.');
            mapping.put(node.name, jdk_name.substring(0, last).replace('.', '/') + "/Accessor");
        }
        var writer = new ClassWriter(0);
        node.accept(new ClassRemapper(writer, new SimpleRemapper(mapping)));
        Class<?> IMPL;
        try { // initialize class
            IMPL = Class.forName(loader.define(writer.toByteArray()).getName(), true, loader);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
        // setAccess(AccessibleObject object, boolean accessible)
        try {
            var method = IMPL.getMethod("setAccess", AccessibleObject.class, boolean.class);
            method.invoke(null, method, true);
            akm = (a, b) -> {
                try {
                    method.invoke(null, a, b);
                } catch (IllegalAccessException e) {
                    throw new InternalError(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e.getTargetException());
                }
            };
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Force set accessible
     *
     * @param object The object need to set.
     * @param flag   The flag value.
     * @param <T>    The type of object.
     * @return The source object
     * @since 2.7.1
     */
    public static <T extends AccessibleObject> T setAccessible(T object, boolean flag) {
        if (object != null) akm.a(object, flag);
        return object;
    }

}