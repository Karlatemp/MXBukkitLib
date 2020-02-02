/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: StaticPool.java@author: karlatemp@vip.qq.com: 2020/1/13 下午10:02@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.internal;

import cn.mcres.karlatemp.mxlib.common.class_info.ClassInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.ClassLoaderPool;
import cn.mcres.karlatemp.mxlib.common.class_info.ClassPool;
import cn.mcres.karlatemp.mxlib.common.class_info.internal.class_class_loader.ClassClassInfo;
import cn.mcres.karlatemp.mxlib.exceptions.MessageDump;
import org.objectweb.asm.Type;

import java.util.Collection;

public class StaticPool {
    private static final ClassLoader BootstrapClassLoader = new ClassLoader() {
    };
    public static final ClassLoaderPool Bootstrap = new ClassLoaderPool(BootstrapClassLoader);

    private static class PClassInfo extends ClassClassInfo {
        private final String j;
        private final String i;

        public PClassInfo(Class<?> target, String j, String i) {
            super(target);
            this.j = j;
            this.i = i;
        }

        @Override
        public boolean isPrimitive() {
            return true;
        }

        @Override
        public String getJavaName() {
            return j;
        }

        @Override
        public String getInternalName() {
            return i;
        }
    }

    public static final ClassInfo
            INT = new PClassInfo(int.class, "int", "I"),
            LONG = new PClassInfo(long.class, "long", "J"),
            DOUBLE = new PClassInfo(double.class, "double", "D"),
            SHORT = new PClassInfo(short.class, "short", "S"),
            VOID = new PClassInfo(void.class, "void", "V"),
            FLOAT = new PClassInfo(float.class, "float", "F"),
            BYTE = new PClassInfo(byte.class, "byte", "B"),
            CHAR = new PClassInfo(char.class, "char", "C"),
            BOOLEAN = new PClassInfo(boolean.class, "boolean", "Z");

    public static ClassInfo getPrimitiveClass(String name, ClassPool pool, boolean internal) {
        if (internal) {
            switch (name) {
                case "I":
                    return StaticPool.INT;
                case "Z":
                    return StaticPool.BOOLEAN;
                case "S":
                    return StaticPool.SHORT;
                case "D":
                    return StaticPool.DOUBLE;
                case "V":
                    return StaticPool.VOID;
                case "J":
                    return StaticPool.LONG;
                case "F":
                    return StaticPool.FLOAT;
                case "C":
                    return StaticPool.CHAR;
                case "B":
                    return StaticPool.BYTE;
            }
        } else {
            switch (name) {
                case "int":
                    return StaticPool.INT;
                case "boolean":
                    return StaticPool.BOOLEAN;
                case "short":
                    return StaticPool.SHORT;
                case "double":
                    return StaticPool.DOUBLE;
                case "void":
                    return StaticPool.VOID;
                case "long":
                    return StaticPool.LONG;
                case "float":
                    return StaticPool.FLOAT;
                case "char":
                    return StaticPool.CHAR;
                case "byte":
                    return StaticPool.BYTE;
            }
        }
        if (name.charAt(0) == '[') {
            var a = Type.getType(name).getElementType();
            var x = (internal ? pool.getInternalClass(a.getInternalName()) : pool.getClass(a.getClassName()));
            if (x == null) return null;
            return x.array();
        }
        return null;
    }

    public static ClassInfo findExist(String name, ClassPool pool, Collection<ClassInfo> classes, boolean internal, boolean renamed) {
        final ClassInfo primitiveClass = StaticPool.getPrimitiveClass(name, pool, internal);
        if (primitiveClass != null) return primitiveClass;
        // name = name.replace('/', '.');
        for (var ci : classes) {
            var a = internal ?
                    renamed ? ci.getRenamedInternalName() : ci.getInternalName()
                    :
                    renamed ? ci.getRenamedJavaName() : ci.getJavaName();
            if (a.hashCode() == name.hashCode())
                if (a.equals(name)) {
                    return ci;
                }
        }
        return null;
    }
}
