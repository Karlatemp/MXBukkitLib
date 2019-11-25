/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AccessToolkitImpl.java@author: karlatemp@vip.qq.com: 19-11-23 下午8:01@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.internal;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.tools.Unsafe;
import jdk.internal.access.SharedSecrets;

@SuppressWarnings("Since15")
public class AccessToolkitImpl {
    public static void openPackageAccess(Class<?> clazz) {
        Module module = clazz.getModule();
        Module caller = Toolkit.Reflection.getCallerClass(1).getModule();
        //noinspection ResultOfMethodCallIgnored
        Unsafe.getUnsafe();
        SharedSecrets.getJavaLangAccess().addExports(module, clazz.getPackageName(), caller);
    }
}
