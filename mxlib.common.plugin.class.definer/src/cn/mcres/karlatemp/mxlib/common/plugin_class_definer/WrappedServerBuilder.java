/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: WrappedServerBuilder.java@author: karlatemp@vip.qq.com: 2020/1/24 上午2:17@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.plugin_class_definer;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.reflect.WrappedClassImplements;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.*;

public class WrappedServerBuilder {
    public static byte[] processClass(PluginDescriptionFile pdf, String path, byte[] clazz) {
        MXBukkitLib.debug(() -> "[WSB]\t\tProcess Class: " + pdf.getName() + ", " + path);
        return clazz;
    }

    static Class<? extends Server> WrappedClass;
    static Constructor<? extends Server> constructor;

    static {
        try {
            ClassWriter writer = new ClassWriter(0);
            var CREATE = "cn/mcres/karlatemp/mxlib/common/plugin_class_definer/WrappedServer";
            writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC,
                    CREATE, null, "java/lang/Object", new String[]{
                            "org/bukkit/Server"
                    });
            writer.visitField(Opcodes.ACC_PRIVATE, "server", "Lorg/bukkit/Server;", null, null);
            var init = writer.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "(Lorg/bukkit/Server;)V", null, null);
            init.visitCode();
            init.visitVarInsn(Opcodes.ALOAD, 0);
            init.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            init.visitVarInsn(Opcodes.ALOAD, 0);
            init.visitVarInsn(Opcodes.ALOAD, 1);
            init.visitFieldInsn(Opcodes.PUTFIELD, CREATE, "server", "Lorg/bukkit/Server;");
            init.visitInsn(Opcodes.RETURN);
            init.visitMaxs(2, 2);
            init.visitEnd();


            for (Method m : Server.class.getMethods()) {
                if (m.getDeclaringClass() != Object.class) {
                    if (m.getReturnType().getName().equals("org.bukkit.UnsafeValues")) {
                        ClassWriter cw = new ClassWriter(0);
                        var CT = "cn/mcres/karlatemp/mxlib/common/plugin_class_definer/WrappedUV";
                        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, CT, null, "java/lang/Object", new String[]{"org/bukkit/UnsafeValues"});
                        cw.visitField(Opcodes.ACC_PRIVATE, "unsafe", "Lorg/bukkit/UnsafeValues;", null, null);
                        var it = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "(Lorg/bukkit/UnsafeValues;)V", null, null);
                        it.visitCode();
                        it.visitVarInsn(Opcodes.ALOAD, 0);
                        it.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
                        it.visitVarInsn(Opcodes.ALOAD, 0);
                        it.visitVarInsn(Opcodes.ALOAD, 1);
                        it.visitFieldInsn(Opcodes.PUTFIELD, CT, "unsafe", "Lorg/bukkit/UnsafeValues;");
                        it.visitInsn(Opcodes.RETURN);
                        it.visitEnd();
                        it.visitMaxs(2, 2);

                        for (Method m2 : Class.forName("org.bukkit.UnsafeValues").getMethods()) {
                            if (m2.getDeclaringClass() != Object.class) {
                                var met = cw.visitMethod(Opcodes.ACC_PUBLIC, m2.getName(), Type.getMethodDescriptor(m2), null, null);
                                met.visitCode();
                                met.visitVarInsn(Opcodes.ALOAD, 0);
                                met.visitFieldInsn(Opcodes.GETFIELD, CREATE, "unsafe", "Lorg/bukkit/UnsafeValues;");
                                if (m2.getName().equals("processClass")) {
                                    met.visitVarInsn(Opcodes.ALOAD, 1);
                                    met.visitVarInsn(Opcodes.ALOAD, 2);

                                    met.visitVarInsn(Opcodes.ALOAD, 1);
                                    met.visitVarInsn(Opcodes.ALOAD, 2);
                                    met.visitVarInsn(Opcodes.ALOAD, 3);
                                    met.visitMethodInsn(Opcodes.INVOKESTATIC, "cn/mcres/karlatemp/mxlib/common/plugin_class_definer/WrappedServerBuilder", "processClass",
                                            Type.getMethodDescriptor(
                                                    Type.getType(byte[].class),
                                                    Type.getType(PluginDescriptionFile.class), Type.getType(String.class), Type.getType(byte[].class)
                                            ), false);
                                    met.visitMethodInsn(Opcodes.INVOKEINTERFACE, "org/bukkit/UnsafeValues", "processClass", Type.getMethodDescriptor(m2), true);
                                    met.visitInsn(Opcodes.ARETURN);
                                    met.visitMaxs(8, 4);
                                    continue;
                                }
                                int slot = 1;
                                for (Class<?> t : m2.getParameterTypes()) {
                                    slot = WrappedClassImplements.putTypeInsn(t, slot, false, met);
                                }
                                met.visitMethodInsn(Opcodes.INVOKEINTERFACE, "org/bukkit/UnsafeValues", m2.getName(), Type.getMethodDescriptor(m2), true);
                                slot = WrappedClassImplements.putTypeInsn(m.getReturnType(), slot, true, met);
                                met.visitMaxs(slot, slot);
                                met.visitEnd();
                            }
                        }

                        write(Paths.get("debug.mxlib", "WrappedUV.class"), cw.toByteArray());

                        Toolkit.Reflection.defineClass(
                                Toolkit.Reflection.getClassLoader(WrappedServerBuilder.class), cw, null
                        );
                        var met = writer.visitMethod(Opcodes.ACC_PUBLIC, m.getName(), Type.getMethodDescriptor(m), null, null);
                        met.visitCode();
                        met.visitTypeInsn(Opcodes.NEW, CT);
                        met.visitInsn(Opcodes.DUP);
                        met.visitVarInsn(Opcodes.ALOAD, 0);
                        met.visitFieldInsn(Opcodes.GETFIELD, CREATE, "server", "Lorg/bukkit/Server;");
                        int slot = 1;
                        for (Class<?> t : m.getParameterTypes()) {
                            slot = WrappedClassImplements.putTypeInsn(t, slot, false, met);
                        }
                        met.visitMethodInsn(Opcodes.INVOKEINTERFACE, "org/bukkit/Server", m.getName(), Type.getMethodDescriptor(m), true);
                        met.visitMethodInsn(Opcodes.INVOKESPECIAL, CT, "<init>", "(Lorg/bukkit/UnsafeValues;)V", false);
                        met.visitInsn(Opcodes.ARETURN);
                        met.visitMaxs(slot + 4, slot);
                        continue;
                    }
                    var met = writer.visitMethod(Opcodes.ACC_PUBLIC, m.getName(), Type.getMethodDescriptor(m), null, null);
                    met.visitCode();
                    met.visitVarInsn(Opcodes.ALOAD, 0);
                    met.visitFieldInsn(Opcodes.GETFIELD, CREATE, "server", "Lorg/bukkit/Server;");
                    int slot = 1;
                    for (Class<?> t : m.getParameterTypes()) {
                        slot = WrappedClassImplements.putTypeInsn(t, slot, false, met);
                    }
                    met.visitMethodInsn(Opcodes.INVOKEINTERFACE, "org/bukkit/Server", m.getName(), Type.getMethodDescriptor(m), true);
                    slot = WrappedClassImplements.putTypeInsn(m.getReturnType(), slot, true, met);
                    met.visitMaxs(slot, slot);
                    met.visitEnd();
                }
            }
            write(Paths.get("debug.mxlib", "WrappedServer.class"), writer.toByteArray());
            WrappedClass = Toolkit.Reflection.defineClass(
                    Toolkit.Reflection.getClassLoader(WrappedServerBuilder.class), writer, null
            ).asSubclass(Server.class);
            constructor = WrappedClass.getConstructor(Server.class);
        } catch (Throwable throwable) {
            throw new ExceptionInInitializerError(throwable);
        }
    }

    private static void write(Path path, byte[] toByteArray) throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        Files.write(path, toByteArray);
    }
}
