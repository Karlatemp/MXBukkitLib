/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: IParamRule.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@ProhibitBean
public class IParamRule {
    protected final Class type;
    protected MethodHandle filter;

    @NotNull
    public Class getAppendClass() {
        return type;
    }

    public IParamRule(Class type) {
        this(type, 1);
    }

    public IParamRule(Class type, int paramCount) {
        this.type = type;
        Class c = getClass();
        try {
            paramCount = Math.max(1, paramCount);
            final Method[] methods = c.getDeclaredMethods();
            for (Method m : methods) {
                if (m.getName().equals("filter")) {
                    if (m.getParameterCount() == paramCount) {
                        if (m.getReturnType() != void.class) {
                            m.setAccessible(true);
                            filter = MethodHandles.lookup().unreflect(m);
                            if (!Modifier.isStatic(m.getModifiers())) {
                                filter = filter.bindTo(this);
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean match(Class c) {
        return c == type;
    }

    public MethodHandle filter() {
        return filter;
    }

    public String toString() {
        return "#" + type + "$" + getClass();
    }
}
