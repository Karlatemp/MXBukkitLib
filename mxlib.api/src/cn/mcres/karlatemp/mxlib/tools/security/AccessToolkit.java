/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AccessToolkit.java@author: karlatemp@vip.qq.com: 19-11-23 下午7:59@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools.security;

import cn.mcres.karlatemp.mxlib.internal.AccessToolkitImpl;
import cn.mcres.karlatemp.mxlib.reflect.RMethod;
import cn.mcres.karlatemp.mxlib.reflect.Reflect;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.tools.Unsafe;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
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

    interface A {
        void a(@NotNull AccessibleObject a, boolean b);
    }

    static A akm;

    static {
        akm = new A() {
            @Override
            public void a(@NotNull AccessibleObject a, boolean b) {
                try {
                    a.setAccessible(b);
                } catch (Throwable t) {
                    synchronized (AccessToolkit.class) {
                        if (akm == this) {
                            final RMethod<AccessibleObject, Boolean> set =
                                    Reflect.ofClass(AccessibleObject.class).getMethod("setAccessible0", boolean.class, boolean.class);
                            akm = new A() {
                                @Override
                                public void a(@NotNull AccessibleObject a, boolean b) {
                                    try {
                                        set.newContext().self(a).invoke(b);
                                    } catch (Throwable t) {
                                        synchronized (AccessToolkit.class) {
                                            if (akm == this) {
                                                final RMethod<AccessibleObject, Void> method = Reflect.ofClass(AccessibleObject.class)
                                                        .getMethod("setAccessible0", void.class, AccessibleObject.class, boolean.class);
                                                akm = new A() {
                                                    @Override
                                                    public void a(@NotNull AccessibleObject a, boolean b) {
                                                        try {
                                                            method.invoke(a, b);
                                                        } catch (Throwable t) {
                                                            synchronized (AccessToolkit.class) {
                                                                if (akm == this) {
                                                                    A uns = (a0, b0) -> {
                                                                        throw new UnsupportedOperationException();
                                                                    };
                                                                    try {
                                                                        final MethodHandle handle = Toolkit.Reflection.getRoot().findSetter(AccessibleObject.class, "override", boolean.class);
                                                                        akm = new A() {
                                                                            @Override
                                                                            public void a(@NotNull AccessibleObject a, boolean b) {
                                                                                try {
                                                                                    handle.invoke(a, b);
                                                                                } catch (Throwable throwable) {
                                                                                    synchronized (AccessToolkit.class) {
                                                                                        if (akm == this) {
                                                                                            akm = uns;
                                                                                        }
                                                                                    }
                                                                                    akm.a(a, b);
                                                                                }
                                                                            }
                                                                        };
                                                                    } catch (NoSuchFieldException | IllegalAccessException e) {
                                                                        akm = uns;
                                                                    }
                                                                }
                                                                akm.a(a, b);
                                                            }
                                                        }
                                                    }
                                                };
                                            }
                                        }
                                        akm.a(a, b);
                                    }
                                }
                            };
                        }
                    }
                    akm.a(a, b);
                }
            }
        };
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
        if (object != null) akm.a(object, flag);
        return object;
    }

}