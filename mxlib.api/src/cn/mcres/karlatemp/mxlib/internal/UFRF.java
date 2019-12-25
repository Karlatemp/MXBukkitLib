/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: UFRF.java@author: karlatemp@vip.qq.com: 19-12-20 下午5:01@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.internal;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.tools.Unsafe;
import org.jetbrains.annotations.NotNull;

import java.security.ProtectionDomain;

public class UFRF extends Toolkit.Reflection {
    private UFRF() {
        throw new AssertionError("Use Unsafe.allocObject to initialize this object.");
    }

    private static final Unsafe unsafe = Unsafe.getUnsafe();

    @Override
    protected Class<?> defineClass0(ClassLoader loader, String name, @NotNull byte[] b, int off, int len, ProtectionDomain protectionDomain) throws ClassFormatError {
        return unsafe.defineClass(name, b, off, len, loader, protectionDomain);
    }
}
