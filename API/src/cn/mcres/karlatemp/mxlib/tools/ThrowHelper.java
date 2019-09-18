/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ThrowHelper.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.logging.MessageFactoryAnsi;
import cn.mcres.karlatemp.mxlib.logging.PrintStreamLogger;
import javassist.bytecode.*;
import javassist.ClassMap;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.invoke.MethodType;
import java.lang.reflect.Modifier;

/**
 * 错误抛出器
 * <code>
 *    return ThrowHelper.thrown(new Throwable("A checked throwable."));
 * </code>
 */
public class ThrowHelper {
    private static final ThrowHelper impl;

    public static final ThrowHelper getInstance() {
        return impl;
    }

    public static <T> T thrown(Throwable throwable) {
        impl.thr(throwable);
        throw new RuntimeException(throwable);
    }

    public void thr(Throwable thr) {
        if (thr instanceof RuntimeException) {
            throw (RuntimeException) thr;
        } else if (thr instanceof Error) {
            throw (Error) thr;
        }
        thr0(thr);
    }

    protected void thr0(Throwable t) {
        throw new RuntimeException(t);
    }

    static {
        ThrowHelper th = new ThrowHelper();
        try {
            String name = ClassMap.toJvmName(ThrowHelper.class.getName());
            ClassFile cf = new ClassFile(false, name + "$Impl", name);
            final ConstPool pool = cf.getConstPool();
            MethodInfo mi = new MethodInfo(pool, "thr0",
                    MethodType.methodType(void.class, Throwable.class).toMethodDescriptorString());
            mi.setAccessFlags(Modifier.PROTECTED | Modifier.FINAL);
            int ref = pool.addMethodrefInfo(pool.addClassInfo(name), "<init>", "()V");
            CodeAttribute codi = new CodeAttribute(pool, 2, 2, new byte[]{
                    0x2B, // aload_1
                    (byte) 0xBF // athrow
            }, new ExceptionTable(pool));
            mi.setCodeAttribute(codi);
            cf.addMethod(mi);
            MethodInfo constructor = new MethodInfo(pool, "<init>", MethodType.methodType(void.class).toMethodDescriptorString());
            constructor.setCodeAttribute(
                    new CodeAttribute(pool, 2, 2, new byte[]{
                            0x2A, // aload_0
                            (byte) 0xb7, (byte) ((ref >> Byte.SIZE) & 0xFF), (byte) (ref & 0xFF),// invokespecial
                            (byte) 0xB1 // return
                    }, new ExceptionTable(pool)));
            constructor.setAccessFlags(Modifier.PUBLIC);
            cf.addMethod(constructor);
            ByteArrayOutputStream save = new ByteArrayOutputStream();
            cf.write(new DataOutputStream(save));
            byte[] code = save.toByteArray();
            Class<?> c = Toolkit.Reflection.defineClass(ThrowHelper.class.getClassLoader(),
                    null, code, 0, code.length, null);
            th = c.asSubclass(ThrowHelper.class).newInstance();
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        impl = th;
    }

    private static void a() {
        for (Object o : Toolkit.StackTrace.getStackTraces()) {
            System.out.println(o);
        }
        for (Class c : Toolkit.StackTrace.getClassContext()) {
            System.out.println(c);
        }
    }

    public static void main(String[] args) {
        a();
        PrintStreamLogger logger = new PrintStreamLogger(new MessageFactoryAnsi(), "§r[§6Test§r] §b", System.out);
        logger.printf("Hello World");
        logger.printStackTrace(new Throwable());
        class a {
            void a() {
                System.out.println(Toolkit.Reflection.getCallerClass());
            }
        }
        class b {
            void x() {
                new a().a();
            }
        }
        new b().x();
    }
}
