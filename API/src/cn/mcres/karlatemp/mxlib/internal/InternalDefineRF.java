/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: InternalDefineRF.java@author: karlatemp@vip.qq.com: 19-12-13 下午9:27@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.internal;

import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;

class InternalDefineRF extends Toolkit.Reflection {
    private static final MethodHandle mh;

    static {
        try {
            mh = getRoot().findVirtual(ClassLoader.class, "defineClass", MethodType.methodType(
                    Class.class, String.class, byte[].class, int.class, int.class, ProtectionDomain.class
            ));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    protected Class<?> defineClass0(ClassLoader loader, String name, @NotNull byte[] b, int off, int len, ProtectionDomain protectionDomain) throws ClassFormatError {
        try {
            return (Class<?>) mh.invoke(loader, name, b, off, len, protectionDomain);
        } catch (Throwable throwable) {
            return ThrowHelper.thrown(throwable);
        }
    }
}
