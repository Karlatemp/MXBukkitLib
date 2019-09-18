/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RefUtilEx.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib;

import cn.mcres.gyhhy.MXLib.ext.lookup.Looker;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RefUtilEx {

    private static final Looker lk = new Looker(Looker.openLookup(RefUtilEx.class, ~0));
    private static final MethodHandle privateGetDeclaredFields
            = lk.findVirtual(Class.class, "privateGetDeclaredFields",
                    MethodType.methodType(Field[].class, boolean.class)),
            copy = lk.findVirtual(Field.class, "copy", MethodType.methodType(Field.class));

    public static Field searchFieldAsType(Class<?> dec, Class<?> type) {
        Field[] fields = privateGetDeclaredFields(dec);
        for (Field f : fields) {
            if (f.getDeclaringClass() == type) {
                return f;
            }
        }
        return null;
    }

    public static Looker looker(Class<?> in) {
        if (in == null) {
            return lk;
        }
        return lk.in(in);
    }

    private static Field copy(Field fe) {
        try {
            return (Field) copy.invoke(fe);
        } catch (Throwable ex) {
            return ThrowHelper.getInstance().thr(ex);
        }
    }

    public static Field[] getAllField(Class<?> c) {
        List<Field> fes = new ArrayList<>();
        Set<Class<?>> its = new HashSet<>();
        while (c != null) {
            fes.addAll(Arrays.asList(privateGetDeclaredFields(c)));
            Class<?>[] ifs = c.getInterfaces();
            for (Class cc : ifs) {
                if (!its.contains(cc)) {
                    fes.addAll(Arrays.asList(privateGetDeclaredFields(cc)));
                    its.add(cc);
                }
            }
            if (c.isArray()) {
                break;
            }
            c = c.getSuperclass();
        }
        return fes.toArray(new Field[fes.size()]);
    }

    private static Field[] privateGetDeclaredFields(Class<?> c) {
        try {
            return (Field[]) privateGetDeclaredFields.invoke(c, false);
        } catch (Throwable ex) {
            return ThrowHelper.getInstance().thr(ex);
        }
    }

    public static void main(String[] str) {
        class A {

            int a;
            int b;
        }
        class B extends A {

            int c;
            int d;
            int f;
            int e;
            int g;
            int h;
            int i;
            int ax;
        }
        class C extends B {

            private int z;
        }
        for (Field f : getAllField(C.class)) {
            System.out.println(f);
        }
        System.out.println();
        for (Field f : privateGetDeclaredFields(B.class)) {
            System.out.println(f);
        }
        System.out.println();
        for (Field f : privateGetDeclaredFields(C.class)) {
            System.out.println(copy(f));
        }
    }

    public static MethodHandle search(Class<?> c, String name, MethodType mt) {
        return lk.findVirtual(c, name, mt);
    }

    public static Field search(Class<?> in, String name, Class<?> rt) {
        Field[] fes = getAllField(in);
        if (rt == null) {
            for (Field fe : fes) {
                if (fe.getName().equals(name)) {
                    return copy(fe);
                }
            }
        } else {
            for (Field fe : fes) {
                if (fe.getType() == rt && fe.getName().equals(name)) {
                    return copy(fe);
                }
            }
        }
        return null;
    }

    public static <T> T invoke(MethodHandle mh) {
        try {
            return (T) mh.invoke();
        } catch (Throwable ex) {
            return ThrowHelper.getInstance().thr(ex);
        }
    }

    public static <T> T invoke(MethodHandle mh, Object $1) {
        try {
            return (T) mh.invoke($1);
        } catch (Throwable ex) {
            return ThrowHelper.getInstance().thr(ex);
        }
    }

    public static <T> T invoke(MethodHandle mh, Object $1, Object $2) {
        try {
            return (T) mh.invoke($1, $2);
        } catch (Throwable ex) {
            return ThrowHelper.getInstance().thr(ex);
        }
    }

    public static <T> T invoke(MethodHandle mh, Object $1, Object $2, Object $3) {
        try {
            return (T) mh.invoke($1, $2, $3);
        } catch (Throwable ex) {
            return ThrowHelper.getInstance().thr(ex);
        }
    }

    public static <T> T invoke(MethodHandle mh, Object $1, Object $2, Object $3, Object $4) {
        try {
            return (T) mh.invoke($1, $2, $3, $4);
        } catch (Throwable ex) {
            return ThrowHelper.getInstance().thr(ex);
        }
    }

    public static <T> T invoke(MethodHandle mh, Object $1, Object $2, Object $3, Object $4, Object $5) {
        try {
            return (T) mh.invoke($1, $2, $3, $4, $5);
        } catch (Throwable ex) {
            return ThrowHelper.getInstance().thr(ex);
        }
    }

    public static <T> T invoke(MethodHandle mh, Object $1, Object $2, Object $3, Object $4, Object $5, Object $6) {
        try {
            return (T) mh.invoke($1, $2, $3, $4, $5, $6);
        } catch (Throwable ex) {
            return ThrowHelper.getInstance().thr(ex);
        }
    }

    public static <T> T invoke(MethodHandle mh, Object $1, Object $2, Object $3, Object $4, Object $5, Object $6, Object $7) {
        try {
            return (T) mh.invoke($1, $2, $3, $4, $5, $6, $7);
        } catch (Throwable ex) {
            return ThrowHelper.getInstance().thr(ex);
        }
    }

    public static MethodHandle openGetter(Field fe) {
        return lk.unreflectGetter(fe);
    }

    public static MethodHandle getHandle(Method met) {
        return lk.unreflect(met);
    }

    public static MethodHandle openSetter(Field fe) {
        return lk.unreflectSetter(fe);
    }

    private RefUtilEx() {
    }
}
