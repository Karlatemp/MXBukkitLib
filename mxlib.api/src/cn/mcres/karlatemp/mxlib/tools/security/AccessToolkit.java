/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AccessToolkit.java@author: karlatemp@vip.qq.com: 19-11-23 下午7:59@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools.security;

import cn.mcres.karlatemp.mxlib.exceptions.MessageDump;
import cn.mcres.karlatemp.mxlib.internal.AccessToolkitImpl;
import cn.mcres.karlatemp.mxlib.reflect.RMethod;
import cn.mcres.karlatemp.mxlib.reflect.Reflect;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.tools.Unsafe;
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
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.security.Permission;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;

/**
 * Access Tools
 *
 * @since 2.7
 */
public class AccessToolkit {
    // private static final Unsafe unsafe = Unsafe.getUnsafe();

    /**
     * Open access to the package directly. (for Java9 or above)
     *
     * @param clazz The any class of package
     */
    public static void openPackageAccess(@NotNull Class<?> clazz) {
        try {
            Class.forName("java.lang.Module");
        } catch (Throwable thr) {
            return;
        }
        AccessToolkitImpl.openPackageAccess(clazz);
    }

    public static boolean isSupportModule() {
        try {
            Class.forName("java.lang.Module");
            return true;
        } catch (Throwable ignore) {
        }
        return false;
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
                            ee.addSuppressed(MessageDump.create(String.valueOf(met)));
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

    static {
        var loader = new ClassLoader(AccessToolkit.class.getClassLoader()) {
            Class<?> define(byte[] b) {
                return defineClass(null, b, 0, b.length, null);
            }
        };
        // JDK Proxy System will help os open package access.
        var proxy = Proxy.newProxyInstance(loader, new Class[]{Toolkit.Reflection.MethodAccessor}, (proxy_, method, args) -> null);
        ClassReader reader;
        try (var source = AccessToolkit.class.getResourceAsStream("AccessToolkit$Accessor.class")) {
            reader = new ClassReader(source);
        } catch (IOException ioe) {
            throw (ExceptionInInitializerError) new ExceptionInInitializerError("Cannot load $Accessor class source.").initCause(ioe);
        }
        var name = Toolkit.Reflection.MethodAccessor.getName().replace('.', '/');
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
        try {
            node.accept(new ClassRemapper(writer, new SimpleRemapper(mapping)));
        } catch (Throwable ignore) {
            node = new ClassNode();
            new ClassReader(Base64.getDecoder().decode("yv66vgAAADQAwwEAPmNuL21jcmVzL2thcmxhdGVtcC9teGxpYi90b29scy9zZWN1cml0eS9BY2Nlc3NUb29sa2l0JEFjY2Vzc29yBwABAQAQamF2YS9sYW5nL09iamVjdAcAAwEAI2pkay9pbnRlcm5hbC9yZWZsZWN0L01ldGhvZEFjY2Vzc29yBwAFAQASQWNjZXNzVG9vbGtpdC5qYXZhAQA1Y24vbWNyZXMva2FybGF0ZW1wL214bGliL3Rvb2xzL3NlY3VyaXR5L0FjY2Vzc1Rvb2xraXQHAAgBAAhBY2Nlc3NvcgEAJWphdmEvbGFuZy9pbnZva2UvTWV0aG9kSGFuZGxlcyRMb29rdXAHAAsBAB5qYXZhL2xhbmcvaW52b2tlL01ldGhvZEhhbmRsZXMHAA0BAAZMb29rdXABAAdmYWN0b3J5AQAoTGpkay9pbnRlcm5hbC9yZWZsZWN0L1JlZmxlY3Rpb25GYWN0b3J5OwEACGFjY2Vzc29yAQAlTGpkay9pbnRlcm5hbC9yZWZsZWN0L01ldGhvZEFjY2Vzc29yOwEADnNldEFjY2Vzc2libGUwAQAGPGluaXQ+AQAoKExqZGsvaW50ZXJuYWwvcmVmbGVjdC9NZXRob2RBY2Nlc3NvcjspVgEAAygpVgwAFQAXCgAEABgMABQAEwkAAgAaAQAEdGhpcwEAQExjbi9tY3Jlcy9rYXJsYXRlbXAvbXhsaWIvdG9vbHMvc2VjdXJpdHkvQWNjZXNzVG9vbGtpdCRBY2Nlc3NvcjsBAAlzZXRBY2Nlc3MBACgoTGphdmEvbGFuZy9yZWZsZWN0L0FjY2Vzc2libGVPYmplY3Q7WilWAQATamF2YS9sYW5nL1Rocm93YWJsZQcAIAEAK2phdmEvbGFuZy9yZWZsZWN0L0ludm9jYXRpb25UYXJnZXRFeGNlcHRpb24HACIMABIAEwkAAgAkAQARamF2YS9sYW5nL0Jvb2xlYW4HACYBAAd2YWx1ZU9mAQAWKFopTGphdmEvbGFuZy9Cb29sZWFuOwwAKAApCgAnACoBAAZpbnZva2UBADkoTGphdmEvbGFuZy9PYmplY3Q7W0xqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsMACwALQsABgAuAQASZ2V0VGFyZ2V0RXhjZXB0aW9uAQAXKClMamF2YS9sYW5nL1Rocm93YWJsZTsMADAAMQoAIwAyAQABZQEALUxqYXZhL2xhbmcvcmVmbGVjdC9JbnZvY2F0aW9uVGFyZ2V0RXhjZXB0aW9uOwEABm9iamVjdAEAJExqYXZhL2xhbmcvcmVmbGVjdC9BY2Nlc3NpYmxlT2JqZWN0OwEACmFjY2Vzc2libGUBAAFaAQAiamF2YS9sYW5nL0lsbGVnYWxBcmd1bWVudEV4Y2VwdGlvbgcAOgEAB05vIGFyZ3MIADwBABUoTGphdmEvbGFuZy9TdHJpbmc7KVYMABUAPgoAOwA/AQAdRXhwZWN0IDEgYXJnIGJ1dCBnb3QgASBhcmcocykIAEEBAFhjbi9tY3Jlcy9rYXJsYXRlbXAvbXhsaWIvSmF2YTlUb0phdmE4LzM5ZDViOWE4XzYzZjRfNDQ1Ml9hMDQxXzlkMTJkMzc5ZjIwNy9TdHJpbmdGYWN0b3J5BwBDAQAXbWFrZUNvbmNhdFdpdGhDb25zdGFudHMBAJgoTGphdmEvbGFuZy9pbnZva2UvTWV0aG9kSGFuZGxlcyRMb29rdXA7TGphdmEvbGFuZy9TdHJpbmc7TGphdmEvbGFuZy9pbnZva2UvTWV0aG9kVHlwZTtMamF2YS9sYW5nL1N0cmluZztbTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvaW52b2tlL0NhbGxTaXRlOwwARQBGCgBEAEcPBgBIAQAVKEkpTGphdmEvbGFuZy9TdHJpbmc7DABFAEoSAAAASwEAA29iagEAEkxqYXZhL2xhbmcvT2JqZWN0OwEABGFyZ3MBABNbTGphdmEvbGFuZy9PYmplY3Q7AQAIPGNsaW5pdD4BACZqZGsvaW50ZXJuYWwvcmVmbGVjdC9SZWZsZWN0aW9uRmFjdG9yeQcAUgEAFGdldFJlZmxlY3Rpb25GYWN0b3J5AQAqKClMamRrL2ludGVybmFsL3JlZmxlY3QvUmVmbGVjdGlvbkZhY3Rvcnk7DABUAFUKAFMAVgwAEAARCQACAFgBACJqYXZhL2xhbmcvcmVmbGVjdC9BY2Nlc3NpYmxlT2JqZWN0BwBaCAAUAQAPamF2YS9sYW5nL0NsYXNzBwBdAQAEVFlQRQEAEUxqYXZhL2xhbmcvQ2xhc3M7DABfAGAJACcAYQEAEWdldERlY2xhcmVkTWV0aG9kAQBAKExqYXZhL2xhbmcvU3RyaW5nO1tMamF2YS9sYW5nL0NsYXNzOylMamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kOwwAYwBkCgBeAGUBABFuZXdNZXRob2RBY2Nlc3NvcgEAQShMamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kOylMamRrL2ludGVybmFsL3JlZmxlY3QvTWV0aG9kQWNjZXNzb3I7DABnAGgKAFMAaQwAFQAWCgACAGsBABNnZXREZWNsYXJlZE1ldGhvZHMwCABtAQAbW0xqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2Q7BwBvAQAYamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kBwBxAQAHZ2V0TmFtZQEAFCgpTGphdmEvbGFuZy9TdHJpbmc7DABzAHQKAHIAdQEAEGphdmEvbGFuZy9TdHJpbmcHAHcBAAZlcXVhbHMBABUoTGphdmEvbGFuZy9PYmplY3Q7KVoMAHkAegoAeAB7AQARZ2V0UGFyYW1ldGVyQ291bnQBAAMoKUkMAH0AfgoAcgB/AQARZ2V0UGFyYW1ldGVyVHlwZXMBABQoKVtMamF2YS9sYW5nL0NsYXNzOwwAgQCCCgByAIMBACl1bmZvdW5kZWQgQWNjZXNzaWJsZU9iamVjdC5zZXRBY2Nlc3NpYmxlMAgAhQEAEWphdmEvdXRpbC9PYmplY3RzBwCHAQAOcmVxdWlyZU5vbk51bGwBADgoTGphdmEvbGFuZy9PYmplY3Q7TGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvT2JqZWN0OwwAiQCKCgCIAIsBACVqYXZhL2xhbmcvRXhjZXB0aW9uSW5Jbml0aWFsaXplckVycm9yBwCNAQAYKExqYXZhL2xhbmcvVGhyb3dhYmxlOylWDAAVAI8KAI4AkAEADWFkZFN1cHByZXNzZWQMAJIAjwoAjgCTAQAmKExqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL1N0cmluZzsMACgAlQoAeACWAQAvY24vbWNyZXMva2FybGF0ZW1wL214bGliL2V4Y2VwdGlvbnMvTWVzc2FnZUR1bXAHAJgBAAZjcmVhdGUBAEUoTGphdmEvbGFuZy9TdHJpbmc7KUxjbi9tY3Jlcy9rYXJs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"))
                    .accept(node, 0);
            node.access = Opcodes.ACC_PUBLIC;
            writer = new ClassWriter(0);
            node.accept(new ClassRemapper(writer, new SimpleRemapper(mapping)));

        }
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