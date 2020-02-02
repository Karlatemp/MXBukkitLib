/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MagicAccessorMarkerLoader.java@author: karlatemp@vip.qq.com: 2020/1/29 下午7:04@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.tools.Unsafe;
import cn.mcres.karlatemp.mxlib.tools.security.AccessToolkit;
import javassist.bytecode.Opcode;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.UUID;

public class MagicAccessorMarkerLoader {
    private static volatile boolean loaded;
    private static ClassLoader LOADER = Toolkit.Reflection.getClassLoader(MagicAccessorMarkerLoader.class);
    private static MagicAccessorMarkerLoader LOADER_IMPL;

    protected void define0(Class<?> define, String name, String namespace) throws Throwable {
    }

    private static void $() {
    }

    private static void initialize(String name, String namespace) throws Throwable {
        {
            var writer = new ClassWriter(0);
            writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "cn/mcres/karlatemp/mxlib/reflect/MagicAccessorMarker", null, name, null);
            WrappedClassImplements.publicObjectConstructor(writer, name);
            Toolkit.Reflection.defineClass(LOADER, writer, null);
        }
        {
            var writer = new ClassWriter(0);
            writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "cn/mcres/karlatemp/mxlib/reflect/MagicAccessorMarker$MethodAccessor",
                    null, name, new String[]{namespace + "MethodAccessor"});
            WrappedClassImplements.publicObjectConstructor(writer, name);
            writer.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT, "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", null,
                    new String[]{
                            "java/lang/IllegalArgumentException",
                            "java/lang/InvocationTargetException"
                    });
            Toolkit.Reflection.defineClass(LOADER, writer, null);
        }
        {
            var writer = new ClassWriter(0);
            writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "cn/mcres/karlatemp/mxlib/reflect/MagicAccessorMarker$ConstructorAccessor",
                    null, name, new String[]{namespace + "ConstructorAccessor"});
            WrappedClassImplements.publicObjectConstructor(writer, name);
            writer.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT, "newInstance", "([Ljava/lang/Object;)Ljava/lang/Object;", null,
                    new String[]{
                            "java/lang/InstantiationException",
                            "java/lang/IllegalArgumentException",
                            "java/lang/InvocationTargetException"
                    });
            Toolkit.Reflection.defineClass(LOADER, writer, null);
        }
        {
            var writer = new ClassWriter(0);
            writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "cn/mcres/karlatemp/mxlib/reflect/MagicAccessorMarker$FieldAccessor",
                    null, name, new String[]{namespace + "FieldAccessor"});
            WrappedClassImplements.publicObjectConstructor(writer, name);
            put(writer, "Int", Type.INT_TYPE);
            put(writer, "Double", Type.DOUBLE_TYPE);
            put(writer, "Short", Type.SHORT_TYPE);
            put(writer, "Float", Type.FLOAT_TYPE);
            put(writer, "Long", Type.LONG_TYPE);
            put(writer, "Byte", Type.BYTE_TYPE);
            put(writer, "Char", Type.CHAR_TYPE);
            put(writer, "Boolean", Type.BOOLEAN_TYPE);
            put(writer, "", Type.getObjectType("java/lang/Object"));
            Toolkit.Reflection.defineClass(LOADER, writer, null);
        }
    }

    private static void put(ClassWriter writer, String type, Type internal) {
        var obj = Type.getObjectType("java/lang/Object");
        writer.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT,
                "get" + type, Type.getMethodDescriptor(internal, obj), null,
                new String[]{
                        "java/lang/IllegalArgumentException"
                });
        writer.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT,
                "set" + type, Type.getMethodDescriptor(Type.VOID_TYPE, obj, internal), null,
                new String[]{
                        "java/lang/IllegalArgumentException", "java/lang/IllegalAccessException"
                });
    }

    @SuppressWarnings({"unchecked", "Java9ReflectionClassVisibility"})
    private static void setup() {
        try {
            var lk = Toolkit.Reflection.getRoot();
            var cc = lk.findConstructor(ClassLoader.class, MethodType.methodType(void.class));
            var field = Class.forName("java.lang.invoke.DirectMethodHandle$Constructor").getDeclaredField("instanceClass");
            AccessToolkit.setAccessible(field, true);

            if (LOADER.getClass().getName().equals("org.bukkit.plugin.java.PluginClassLoader")) { // Bukkit Plugin
                var classes = (Map<String, Class<?>>) lk.unreflectGetter(LOADER.getClass().getDeclaredField("classes")).invoke(LOADER);
                LOADER_IMPL = new MagicAccessorMarkerLoader() {
                    @Override
                    protected void define0(Class<?> define, String name, String namespace) throws Throwable {
                        classes.put(name.replace('/', '.'), define);
                        initialize(name, namespace);
                    }
                };
            } else {
                Class<?> BC = null;
                try {
                    BC = Class.forName("jdk.internal.loader.BuiltinClassLoader");
                } catch (Throwable ignore) {
                }
                if (BC != null) {
                    AccessToolkit.openPackageAccess(BC);
                    if (BC.isInstance(LOADER)) {
                        var g = lk.findGetter(BC, "parent", BC);
                        var s = lk.findSetter(BC, "parent", BC);
                        var cw = new ClassWriter(0);
                        var name = "jdk/internal/loader/InternalBCL";
                        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, name, null, "jdk/internal/loader/BuiltinClassLoader", null);
                        cw.visitField(Opcodes.ACC_PUBLIC, "name", "Ljava/lang/String;", null, null);
                        cw.visitField(Opcodes.ACC_PUBLIC, "value", "Ljava/lang/Class;", null, null);
                        var met = cw.visitMethod(Opcodes.ACC_PROTECTED, "loadClassOrNull", "(Ljava/lang/String;Z)Ljava/lang/Class;", null, null);
                        met.visitVarInsn(Opcodes.ALOAD, 1);
                        met.visitVarInsn(Opcodes.ALOAD, 0);
                        met.visitFieldInsn(Opcodes.GETFIELD, name, "name", "Ljava/lang/String;");
                        met.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                        var l = new Label();
                        met.visitJumpInsn(Opcodes.IFEQ, l);
                        met.visitVarInsn(Opcodes.ALOAD, 0);
                        met.visitFieldInsn(Opcodes.GETFIELD, name, "value", "Ljava/lang/Class;");
                        met.visitInsn(Opcodes.ARETURN);
                        met.visitLabel(l);
                        met.visitFrame(Opcodes.F_SAME, 3, new Object[3], 0, new Object[3]);
                        met.visitInsn(Opcodes.ACONST_NULL);
                        met.visitInsn(Opcodes.ARETURN);
                        met.visitMaxs(3, 3);
                        var lm = Toolkit.Reflection.defineClass(LOADER, cw, null);
                        var name0 = lm.getField("name");
                        var value0 = lm.getField("value");
                        var llm = Unsafe.getUnsafe().allocateInstance(lm);
                        LOADER_IMPL = new MagicAccessorMarkerLoader() {
                            @Override
                            protected void define0(Class<?> define, String name, String namespace) throws Throwable {
                                name0.set(llm, name.replace('/', '.'));
                                value0.set(llm, define);
                                var o = g.invoke(LOADER);
                                s.invoke(LOADER, llm);
                                initialize(name, namespace);
                                s.invoke(LOADER, o);
                            }
                        };
                        return;
                    }
                }
                var g = lk.findGetter(ClassLoader.class, "parent", ClassLoader.class);
                var s = lk.findSetter(ClassLoader.class, "parent", ClassLoader.class);
                LOADER_IMPL = new MagicAccessorMarkerLoader() {
                    @Override
                    protected void define0(Class<?> define, String name, String namespace) throws Throwable {
                        var n = name.replace('/', '.');
                        class LD extends ClassLoader {
                            @Override
                            public Class<?> loadClass(String name) throws ClassNotFoundException {
                                if (name.equals(n)) return define;
                                return super.loadClass(name);
                            }
                        }
                        var o = g.invoke(LOADER);
                        s.invoke(LOADER, new LD());
                        initialize(name, namespace);
                        s.invoke(LOADER, o);
                    }
                };
            }
        } catch (Throwable any) {
            throw new InternalError(any);
        }
    }

    static {
        setup();
        load();
    }

    /**
     * Here is a method to load magic accessor marker
     */
    @SuppressWarnings("Java9ReflectionClassVisibility")
    public synchronized static void load() {
        if (loaded) return;
        var namespace = "sun/reflect/";
        try {
            Class.forName("jdk.internal.reflect.MagicAccessorImpl");
            namespace = "jdk/internal/reflect/";
        } catch (Throwable ignore) {
        }
        var random = "cn/mcres/karlatemp/reflect/internal/R" + UUID.randomUUID().toString();
        var writer = new ClassWriter(0);
        writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, random, null, namespace + "MagicAccessorImpl", null);
        WrappedClassImplements.publicObjectConstructor(writer, null);
        try {
            var lk = Toolkit.Reflection.getRoot();
            var ClassDefiner = Class.forName(namespace.replace('/', '.') + "ClassDefiner");
            byte[] a = writer.toByteArray();
            var defined = (Class<?>) lk.findStatic(ClassDefiner, "defineClass", MethodType.methodType(
                    Class.class, String.class, byte[].class, int.class, int.class, ClassLoader.class
            )).invoke(random, a, 0, a.length, LOADER);
            LOADER_IMPL.define0(defined, random, namespace);
        } catch (Throwable throwable) {
            throw new InternalError(throwable);
        }
        loaded = true;
    }

}
