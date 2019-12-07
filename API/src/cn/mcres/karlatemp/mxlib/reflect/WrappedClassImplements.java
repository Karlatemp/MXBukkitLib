/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: WrappedClassImplements.java@author: karlatemp@vip.qq.com: 19-11-29 下午2:08@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import io.netty.buffer.ByteBuf;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Wrapped Abstract class.
 *
 * @since 2.8
 */
public class WrappedClassImplements {
    public static Class<?> getRootAbstractClass(Class<?> own) {
        Class<?> bb = Object.class;
        for (Method m : own.getMethods()) {
            if (Modifier.isAbstract(m.getModifiers())) {
                Class<?> cv = m.getDeclaringClass();
                if (bb.isAssignableFrom(cv)) {
                    bb = cv;
                }
            }
        }
        return bb;
    }

    public static ClassWriter doImplement(Class<?> own) {
        if (!Modifier.isAbstract(own.getModifiers())) {
            throw new UnsupportedOperationException("Only wrap a abstract class.");
        }
        Class<?> bb = getRootAbstractClass(own);
        if (bb == Object.class) {
            throw new UnsupportedOperationException("No any abstract method.");
        }
        String initCon;
        boolean initParam;
        {
            final Constructor<?>[] constructors = own.getDeclaredConstructors();
            Constructor<?> using = null;
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() == 1) {
                    final int modifiers = constructor.getModifiers();
                    if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
                        Class<?> newer = constructor.getParameterTypes()[0];
                        if (newer.isAssignableFrom(bb)) {
                            if (using == null) {
                                using = constructor;
                            } else {
                                Class<?> last = using.getParameterTypes()[0];
                                if (last.isAssignableFrom(newer)) {
                                    using = constructor;
                                }
                            }
                        }
                    }
                }
            }
            if (using == null) {
                for (Constructor<?> c : constructors) {
                    if (c.getParameterCount() == 0) {
                        int mod = c.getModifiers();
                        if (Modifier.isPublic(mod) || Modifier.isProtected(mod))
                            using = c;
                        break;
                    }
                }
            }
            if (using == null)
                throw new UnsupportedOperationException("Cannot found constructor for class(" + bb + ")");
            initCon =
                    '(' + ((initParam = using.getParameterCount() == 1) ?
                            'L' + using.getParameterTypes()[0].getName().replace('.', '/') + ';' :
                            "")
                            + ")V";
        }
        String field = "wrapped_" + UUID.randomUUID().toString().replace('-', '_');
        ClassWriter writer = new ClassWriter(0);
        String createClass = "cn/mcres/karlatemp/mxlib/internal/WrappedClassImplementW" + UUID.randomUUID().toString().replace("-", "");
        String desByteBuffer = 'L' + bb.getName().replace('.', '/') + ';';
        writer.visit(52, Modifier.PUBLIC, createClass, null,
                own.getName().replace('.', '/'), new String[0]);
        writer.visitSource("JVMWrap-" + own.getName(), null);
        writer.visitField(Modifier.PRIVATE, field, desByteBuffer, null, null);
        {
            final MethodVisitor init = writer.visitMethod(Modifier.PUBLIC, "<init>", "(L" + bb.getName().replace('.', '/') + ";)V", null, null);
            init.visitCode();
            init.visitVarInsn(Opcodes.ALOAD, 0);
            if (initParam)
                init.visitVarInsn(Opcodes.ALOAD, 1);
            init.visitMethodInsn(Opcodes.INVOKESPECIAL, own.getName().replace('.', '/'), "<init>", initCon, false);
            init.visitVarInsn(Opcodes.ALOAD, 0);
            init.visitVarInsn(Opcodes.ALOAD, 1);
            init.visitFieldInsn(Opcodes.PUTFIELD, createClass, field, desByteBuffer);
            init.visitInsn(Opcodes.RETURN);
            init.visitMaxs(2, 2);
            init.visitEnd();
        }
        {
            for (Method m : bb.getMethods()) {
                if (Modifier.isAbstract(m.getModifiers()) &&
                        (Modifier.isProtected(m.getModifiers()) || Modifier.isPublic(m.getModifiers())) &&
                        !Modifier.isStatic(m.getModifiers())) {
                    // System.out.println("OMethod: " + m);
                    String DR;
                    final MethodVisitor impl = writer.visitMethod(
                            m.getModifiers() & ~Modifier.ABSTRACT,
                            m.getName(),
                            DR = MethodType.methodType(m.getReturnType(), m.getParameterTypes()).toMethodDescriptorString(),
                            null, Optional.of(m.getExceptionTypes()).filter(
                                    array -> array.length != 0
                            ).map(cArray -> Stream.of(cArray).map(
                                    clazz -> clazz.getName().replace('.', '/')
                            ).toArray(String[]::new)).orElse(null)
                    );
                    impl.visitCode();
                    impl.visitVarInsn(Opcodes.ALOAD, 0);
                    impl.visitFieldInsn(Opcodes.GETFIELD, createClass, field, desByteBuffer);
                    int stacks;
                    {
                        Class[] params = m.getParameterTypes();
                        int w = 1;
                        for (int i = 0; i < params.length; ) {
                            Class type = params[i++];
                            if (type == double.class) {
                                impl.visitVarInsn(Opcodes.DLOAD, w);
                                w += 2;
                            } else if (type == float.class) {
                                impl.visitVarInsn(Opcodes.FLOAD, w++);
                            } else if (type == int.class || type == short.class || type == boolean.class ||
                                    type == byte.class || type == char.class) {
                                impl.visitVarInsn(Opcodes.ILOAD, w++);
                            } else if (type == long.class) {
                                impl.visitVarInsn(Opcodes.LLOAD, w);
                                w += 2;
                            } else {
                                impl.visitVarInsn(Opcodes.ALOAD, w++);
                            }
                        }
                        stacks = w;
                    }
                    impl.visitMethodInsn(
                            m.getDeclaringClass().isInterface() ?
                                    Opcodes.INVOKEINTERFACE :
                                    Opcodes.INVOKEVIRTUAL, m.getDeclaringClass().getName().replace('.', '/'),
                            m.getName(), DR, m.getDeclaringClass().isInterface());
                    {
                        Class type = m.getReturnType();
                        if (type == double.class) {
                            impl.visitInsn(Opcodes.DRETURN);
                        } else if (type == float.class) {
                            impl.visitInsn(Opcodes.FRETURN);
                        } else if (type == int.class || type == short.class || type == boolean.class ||
                                type == byte.class || type == char.class) {
                            impl.visitInsn(Opcodes.IRETURN);
                        } else if (type == long.class) {
                            impl.visitInsn(Opcodes.LRETURN);
                        } else if (type == void.class) {
                            impl.visitInsn(Opcodes.RETURN);
                        } else {
                            impl.visitInsn(Opcodes.ARETURN);
                        }
                    }
                    impl.visitMaxs(stacks + 5, 1 + stacks);
                    impl.visitEnd();
                }
            }
        }
//        byte[] created = writer.toByteArray();
//        File f = new File("G:\\IDEAProjects\\MXBukkitLibRebuild\\out\\test.class");
//        f.createNewFile();
//        new FileOutputStream(f).write(created);
//        Class<?> inited = Toolkit.Reflection.defineClass(serializerClass.getClassLoader(), null, created, 0, created.length, null);
//        Object ser = inited.getConstructor(ByteBuf.class).newInstance(io.netty.buffer.Unpooled.buffer());
//        System.out.println(ser);
        return writer;
    }
}
