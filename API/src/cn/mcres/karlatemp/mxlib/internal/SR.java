/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SR.java@author: karlatemp@vip.qq.com: 19-12-20 下午6:01@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.internal;

import cn.mcres.karlatemp.mxlib.reflect.WrappedClassImplements;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.objectweb.asm.Type;

import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;

class SR {
    interface Wrapper {
        Class<?> defineClass(Object unsafe, String name, byte[] class_file, int off, int len, ClassLoader classLoader, ProtectionDomain domain);
    }

    static Wrapper defineClass, defineClass0;

    private static Wrapper load(byte[] code) {
        try {
            return Toolkit.Reflection.defineClass(Wrapper.class.getClassLoader(), null, code, 0, code.length, null)
                    .asSubclass(Wrapper.class)
                    .getConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return ThrowHelper.thrown(e);
        }
    }

    static Object init() {
        return "";
    }

    static {
        byte[] clazz1 = WrappedClassImplements.createMethodWrapper(
                Type.getInternalName(Wrapper.class),
                "defineClass",
                "(Ljava/lang/Object;Ljava/lang/String;[BIILjava/lang/ClassLoader;Ljava/security/ProtectionDomain;)Ljava/lang/Class;",
                WrappedClassImplements.genImplementClassName("cn.mcres.karlatemp.mxlib.internal", null),
                "sun/misc/Unsafe",
                "defineClass", WrappedClassImplements.MethodWrapperType.OBJECT
        ).toByteArray(), clazz2 = WrappedClassImplements.createMethodWrapper(
                Type.getInternalName(Wrapper.class),
                "defineClass",
                "(Ljava/lang/Object;Ljava/lang/String;[BIILjava/lang/ClassLoader;Ljava/security/ProtectionDomain;)Ljava/lang/Class;",
                WrappedClassImplements.genImplementClassName("cn.mcres.karlatemp.mxlib.internal", null),
                "sun/misc/Unsafe",
                "defineClass0", WrappedClassImplements.MethodWrapperType.OBJECT
        ).toByteArray();
        defineClass = load(clazz1);
        defineClass0 = load(clazz2);
    }
}
