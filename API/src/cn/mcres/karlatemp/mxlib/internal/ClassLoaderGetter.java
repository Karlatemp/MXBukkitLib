/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ClassLoaderGetter.java@author: karlatemp@vip.qq.com: 19-12-13 下午9:56@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.internal;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.tools.Unsafe;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Modifier;
import java.util.function.Function;

public class ClassLoaderGetter {
    private static final String cname = "java/lang/MXLibClassLoaderGetterWithNoSecurityManager";
    public static final Function<Class, ClassLoader> impl;

    static {
        Function<Class, ClassLoader> core;
        try {
            core = Toolkit.Reflection.allocObject(Class.forName(cname.replace('/', '.')).asSubclass(Function.class));
        } catch (Throwable err) {
            ClassWriter cw = new ClassWriter(0);
            cw.visit(51, Modifier.PUBLIC, cname, null, "java/lang/Object", new String[]{"java/util/function/Function"});
            cw.visitSource(null, null);
            final MethodVisitor accept = cw.visitMethod(Modifier.PUBLIC, "apply", "(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
            {
                accept.visitCode();
                accept.visitVarInsn(Opcodes.ALOAD, 1);
                accept.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Class");
                accept.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getClassLoader0", "()Ljava/lang/ClassLoader;", false);
                accept.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Object");
                accept.visitInsn(Opcodes.ARETURN);
                accept.visitMaxs(2, 2);
                accept.visitEnd();
            }

            cw.visitEnd();
            byte[] arr = cw.toByteArray();
            Class<?> c = Toolkit.Reflection.defineClass(
                    null, null, arr, 0, arr.length, null
            );
            core = Toolkit.Reflection.allocObject(c.asSubclass(Function.class));
        }
        impl = core;
        Unsafe.$finishUnsafeInit();
    }
}
