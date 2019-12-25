/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: J9RF.java@author: karlatemp@vip.qq.com: 19-12-13 下午9:16@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.internal;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.ProtectionDomain;

import javassist.bytecode.ClassFile;
import jdk.internal.access.JavaLangAccess;
import jdk.internal.access.SharedSecrets;
import org.jetbrains.annotations.NotNull;

class J9RF extends Toolkit.Reflection {
    private static final JavaLangAccess JLA = SharedSecrets.getJavaLangAccess();

    @Override
    protected Class<?> defineClass0(ClassLoader loader, String name, @NotNull byte[] b, int off, int len, ProtectionDomain protectionDomain) throws ClassFormatError {
        if (!(off == 0 && len == b.length)) {
            byte[] arr = new byte[len];
            System.arraycopy(b, off, arr, 0, len);
            b = arr;
        }
        String source;
        try {
            source = new ClassFile(new DataInputStream(new ByteArrayInputStream(b))).getSourceFile();
        } catch (Throwable error) {
            source = null;
        }
        return JLA.defineClass(loader, name, b, protectionDomain, source);
    }
}
