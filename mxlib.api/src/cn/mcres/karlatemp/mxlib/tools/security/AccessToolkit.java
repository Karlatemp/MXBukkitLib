/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AccessToolkit.java@author: karlatemp@vip.qq.com: 19-11-23 下午7:59@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools.security;

import cn.mcres.karlatemp.mxlib.internal.AccessToolkitImpl;
import cn.mcres.karlatemp.mxlib.reflect.Reflect;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.tools.Unsafe;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AccessibleObject;

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

    /**
     * Force set accessible
     *
     * @param object The object need to set.
     * @param flag   The flag value.
     * @param <T>    The type of object.
     * @return The source object
     * @since 2.7.1
     */
    public static <T extends AccessibleObject> T setAccessible(T object, boolean flag) {
        try {
            object.setAccessible(flag);
        } catch (Throwable error) {
            try {
                Reflect.ofObject(object)
                        .getMethod("setAccessible0", boolean.class, boolean.class)
                        .invoke(flag); // JDK 12
            } catch (Throwable error2) {
                try {
                    Reflect.ofClass(AccessibleObject.class)
                            .getMethod("setAccessible0", void.class, AccessibleObject.class, boolean.class)
                            .invoke(object, flag);
                } catch (Throwable error3) {
                    try {
                        Toolkit.Reflection.getRoot().findSetter(AccessibleObject.class, "override", boolean.class).invoke(object, flag);
                    } catch (Throwable error4) {
                        return ThrowHelper.thrown(new LinkageError("Fail to set accessible for field " + object, error4));
                    }
                }
            }
        }
        return object;
    }

}