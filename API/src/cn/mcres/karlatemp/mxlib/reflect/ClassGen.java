/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ClassGen.java@author: karlatemp@vip.qq.com: 19-9-18 下午12:44@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

import javassist.ClassMap;
import javassist.bytecode.*;

import java.io.IOException;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class ClassGen {
    static void check(Class[] c) {
        if (c == null || c.length == 0) {
            throw new RuntimeException("No Interfaces.");
        }
        for (Class cx : c) {
            if (!cx.isInterface()) {
                throw new RuntimeException("Class " + cx + " not a interface.");
            }
        }
    }

    private static final String PROXY_CLASS;

    static {
        PROXY_CLASS = ClassMap.toJvmName(Proxy.class.getName());
    }

    static byte[] gen(String name, Class[] interfaces) throws DuplicateMemberException {
        check(interfaces);
        Method[] mets = getMethods(interfaces);
        ClassFile cf = new ClassFile(false, name, PROXY_CLASS);
        final ConstPool cp = cf.getConstPool();
        final String LPH = "Lcn/mcres/karlatemp/mxlib/reflect/ProxyHandler";
        {
            FieldInfo field = new FieldInfo(cp, "mh", LPH);
            field.setAccessFlags(Modifier.PRIVATE | Modifier.FINAL);
            cf.addField(field);
        }
        final String VOID = MethodType.methodType(void.class).toMethodDescriptorString();
        final int super_init = cp.addMethodrefInfo(
                cp.addClassInfo(PROXY_CLASS),
                "<init>",
                VOID
        );
        final int field = cp.addFieldrefInfo(cp.addClassInfo(name), "mh", LPH);
        MethodType tt = MethodType.methodType(void.class, int.class, Object[].class);
        for (Method m : mets) {
            int args = m.getParameterCount();
            MethodInfo nw = new MethodInfo(cp, m.getName(), MethodType.methodType(m.getReturnType(), m.getParameterTypes()).toMethodDescriptorString());
            nw.setAccessFlags(Modifier.PUBLIC | Modifier.FINAL);
            ArrayList<Number> num = new ArrayList<>();
            num.add(0x2a);
            num.add(0xb4);
            num.add((byte) ((field >> Byte.SIZE) & 0xFF));
            num.add((byte) (field & 0xFF));
            int ret;
            {
                Class rt = m.getReturnType();
                if (rt == boolean.class) {
                    ret = cp.addMethodrefInfo(cp.addClassInfo(PROXY_CLASS), "runAsBoolean",
                            MethodType.methodType(rt, tt).toMethodDescriptorString());
                } else if (rt == int.class) {
                    ret = cp.addMethodrefInfo(cp.addClassInfo(PROXY_CLASS), "runAsInt",
                            MethodType.methodType(rt, tt).toMethodDescriptorString());
                } else if (rt == void.class) {
                    ret = cp.addMethodrefInfo(cp.addClassInfo(PROXY_CLASS), "runAsVoid",
                            MethodType.methodType(rt, tt).toMethodDescriptorString());
                } else if (rt == long.class) {
                    ret = cp.addMethodrefInfo(cp.addClassInfo(PROXY_CLASS), "runAsLong",
                            MethodType.methodType(rt, tt).toMethodDescriptorString());
                } else if (rt == short.class) {
                    ret = cp.addMethodrefInfo(cp.addClassInfo(PROXY_CLASS), "runAsShort",
                            MethodType.methodType(rt, tt).toMethodDescriptorString());
                } else if (rt == byte.class) {
                    ret = cp.addMethodrefInfo(cp.addClassInfo(PROXY_CLASS), "runAsByte",
                            MethodType.methodType(rt, tt).toMethodDescriptorString());
                } else if (rt == char.class) {
                    ret = cp.addMethodrefInfo(cp.addClassInfo(PROXY_CLASS), "runAsChar",
                            MethodType.methodType(rt, tt).toMethodDescriptorString());
                } else if (rt == double.class) {
                    ret = cp.addMethodrefInfo(cp.addClassInfo(PROXY_CLASS), "runAsDouble",
                            MethodType.methodType(rt, tt).toMethodDescriptorString());
                } else if (rt == float.class) {
                    ret = cp.addMethodrefInfo(cp.addClassInfo(PROXY_CLASS), "runAsFloat",
                            MethodType.methodType(rt, tt).toMethodDescriptorString());
                } else {
                    ret = cp.addMethodrefInfo(cp.addClassInfo(PROXY_CLASS), "run",
                            MethodType.methodType(Object.class, tt).toMethodDescriptorString());
                }
            }
            for (int i = 0; i < args; i++) {
                if (i < 3) {
                    num.add(0x2b + i);
                } else {
                    num.add(0x19);
                    num.add(i + 1);
                }
            }
            nw.setCodeAttribute(
                    new CodeAttribute(cp, args + 2, args + 2, toBytes(num), new ExceptionTable(cp))
            );
        }
        {
            MethodInfo init = new MethodInfo(cp, "<init>", VOID);
            init.setAccessFlags(Modifier.PUBLIC);
            init.setCodeAttribute(new CodeAttribute(cp, 5, 5, new byte[]{
                    0x2A,
                    (byte) 0xb7,
                    (byte) (super_init >> Byte.SIZE),
                    (byte) (super_init & 0xFF)
            }, new ExceptionTable(cp)));
        }
        return null;
    }

    private static byte[] toBytes(List<Number> num) {
        byte[] ret = new byte[num.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = (byte) (num.get(i).intValue() & 0xFF);
        }
        return ret;
    }

    private static Method[] getMethods(Class[] interfaces) {
        List<Method> mets = new ArrayList<>();
        for (Class c : interfaces) {
            a:
            for (Method glo : c.getMethods()) {
                final Iterator<Method> iterator = mets.iterator();
                while (iterator.hasNext()) {
                    Method m = iterator.next();
                    if (m.getName().equals(glo.getName())) {
                        if (m.getParameterCount() == glo.getParameterCount()) {
                            if (Arrays.equals(m.getParameterTypes(), glo.getParameterTypes())) {
                                Class<?> oret = m.getReturnType();
                                Class<?> nret = glo.getReturnType();
                                if (oret.isAssignableFrom(nret)) {
                                    iterator.remove();
                                    mets.add(glo);
                                    continue a;
                                } else if (!nret.isAssignableFrom(oret)) {
                                    throw new RuntimeException("Conflict method: " + m + ", " + glo);
                                }
                            }
                        }
                    }
                }
                mets.add(glo);
            }
        }
        return mets.toArray(new Method[0]);
    }
}
