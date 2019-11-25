/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AccessToolkit.java@author: karlatemp@vip.qq.com: 19-11-23 下午7:59@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools.security;

import cn.mcres.karlatemp.mxlib.internal.AccessToolkitImpl;
import cn.mcres.karlatemp.mxlib.tools.Unsafe;
import org.jetbrains.annotations.NotNull;

/**
 * Access Tools
 *
 * @since 2.7
 */
public class AccessToolkit {
    // private static final Unsafe unsafe = Unsafe.getUnsafe();

    /**
     * Open access to the package directly. (for Java9 or above)
     *
     * @param clazz The any class of package
     */
    public static void openPackageAccess(@NotNull Class<?> clazz) {
        try {
            Class.forName("java.lang.Module");
        } catch (Throwable thr) {
            return;
        }
        AccessToolkitImpl.openPackageAccess(clazz);
    }

    public static boolean isSupportModule() {
        try {
            Class.forName("java.lang.Module");
            return true;
        } catch (Throwable ignore) {
        }
        return false;
    }
}
