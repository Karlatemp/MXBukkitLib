/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/28 23:58:58
 *
 * MXLib/mxlib.common.unsafe/Reflection.java
 */

package cn.mcres.karlatemp.unsafe;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassWriter;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.nio.ByteBuffer;
import java.security.ProtectionDomain;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 反射工具
 */
@SuppressWarnings("Java9ReflectionClassVisibility")
public abstract class Reflection {
    private static final MethodHandles.Lookup root;
    // static Reflection ref;
    public static final Class<?> MethodAccessor, FieldAccessor, ConstructorAccessor;
    public static final Function<Method, Object> MethodAccessorGetter;
    public static final Function<Constructor<?>, Object> ConstructorAccessorGetter;
    public static final BiConsumer<Method, Object> MethodAccessorSetter;
    public static final BiConsumer<Constructor<?>, Object> ConstructorAccessorSetter;
    public static final BiConsumer<Field, Object> FieldAccessorSetter;
    public static final Function<Field, Object> FieldAccessorGetter;

    /**
     * 获取最高权限的Lookup
     *
     * @return 最高权限的Lookup
     */
    @Contract(pure = true)
    public static MethodHandles.Lookup getRoot() {
        return root;
    }

    /**
     * Returns the {@code Class} object associated with the class or
     * interface with the given string name, using the given class loader.
     * Given the fully qualified name for a class or interface (in the same
     * format returned by {@code getName}) this method attempts to
     * locate, load, and link the class or interface.  The specified class
     * loader is used to load the class or interface.  If the parameter
     * {@code loader} is null, the class is loaded through the bootstrap
     * class loader.  The class is initialized only if the
     * {@code initialize} parameter is {@code true} and if it has
     * not been initialized earlier.
     *
     * <p> If {@code name} denotes a primitive type or void, an attempt
     * will be made to locate a user-defined class in the unnamed package whose
     * name is {@code name}. Therefore, this method cannot be used to
     * obtain any of the {@code Class} objects representing primitive
     * types or void.
     *
     * <p> If {@code name} denotes an array class, the component type of
     * the array class is loaded but not initialized.
     *
     * <p> For example, in an instance method the expression:
     *
     * <blockquote>
     * {@code Class.forName("Foo")}
     * </blockquote>
     * <p>
     * is equivalent to:
     *
     * <blockquote>
     * {@code Class.forName("Foo", true, this.getClass().getClassLoader())}
     * </blockquote>
     * <p>
     * Note that this method throws errors related to loading, linking or
     * initializing as specified in Sections 12.2, 12.3 and 12.4 of <em>The
     * Java Language Specification</em>.
     * Note that this method does not check whether the requested class
     * is accessible to its caller.
     *
     * @param name       fully qualified name of the desired class
     * @param initialize if {@code true} the class will be initialized.
     *                   See Section 12.4 of <em>The Java Language Specification</em>.
     * @param loader     class loader from which the class must be loaded
     * @return class object representing the desired class
     * @throws LinkageError                if the linkage fails
     * @throws ExceptionInInitializerError if the initialization provoked
     *                                     by this method fails
     * @throws NoClassDefFoundError        if the class cannot be located by
     *                                     the specified class loader
     * @throws SecurityException           if a security manager is present, and the {@code loader} is
     *                                     {@code null}, and the caller's class loader is not
     *                                     {@code null}, and the caller does not have the
     *                                     {@link RuntimePermission}{@code ("getClassLoader")}
     * @see Class#forName(String)
     * @see Class#forName(String, boolean, ClassLoader)
     * @see ClassLoader
     * @since 1.2
     */
    public static Class<?> forName(String name, boolean initialize,
                                   ClassLoader loader) {
        try {
            return Class.forName(name, initialize, loader);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.toString());
        }
    }

    @Contract("null, _ -> null; !null, _ -> !null")
    public static <T extends AccessibleObject> T setAccess(T accessibleObject, boolean b) {
        if (accessibleObject != null) AccessToolkit.setAccessible(accessibleObject, b);
        return accessibleObject;
    }

    public static <T> T allocObject(Class<T> type) {
        if (type == Class.class) throw new UnsupportedOperationException(String.valueOf(type));
        try {
            return Unsafe.getUnsafe().allocateInstance(type);
        } catch (InstantiationException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    /**
     * <p> The first class defined in a package determines the exact set of
     * certificates that all subsequent classes defined in that package must
     * contain.  The set of certificates for a class is obtained from the
     * {@link java.security.CodeSource <tt>CodeSource</tt>} within the
     * <tt>ProtectionDomain</tt> of the class.  Any classes added to that
     * package must contain the same set of certificates or a
     * <tt>SecurityException</tt> will be thrown.  Note that if
     * <tt>name</tt> is <tt>null</tt>, this check is not performed.
     * You should always pass in the <a href="#name">binary name</a> of the
     * class you are defining as well as the bytes.  This ensures that the
     * class you are defining is indeed the class you think it is.
     *
     * <p> The specified <tt>name</tt> cannot begin with "<tt>java.</tt>", since
     * all classes in the "<tt>java.*</tt> packages can only be defined by the
     * bootstrap class loader.  If <tt>name</tt> is not <tt>null</tt>, it
     * must be equal to the <a href="#name">binary name</a> of the class
     * specified by the byte array "<tt>b</tt>", otherwise a {@link
     * NoClassDefFoundError <tt>NoClassDefFoundError</tt>} will be thrown. </p>
     *
     * @param loader           The loader to be executed
     * @param name             The expected <a href="#name">binary name</a> of the class, or
     *                         <tt>null</tt> if not known
     * @param b                The bytes that make up the class data. The bytes in positions
     *                         <tt>off</tt> through <tt>off+len-1</tt> should have the format
     *                         of a valid class file as defined by
     *                         <cite>The Java&trade; Virtual Machine Specification</cite>.
     * @param off              The start offset in <tt>b</tt> of the class data
     * @param len              The length of the class data
     * @param protectionDomain The ProtectionDomain of the class
     * @return The <tt>Class</tt> object created from the data,
     * and optional <tt>ProtectionDomain</tt>.
     * @throws ClassFormatError          If the data did not contain a valid class
     * @throws NoClassDefFoundError      If <tt>name</tt> is not equal to the <a href="#name">binary
     *                                   name</a> of the class specified by <tt>b</tt>
     * @throws IndexOutOfBoundsException If either <tt>off</tt> or <tt>len</tt> is negative, or if
     *                                   <tt>off+len</tt> is greater than <tt>b.length</tt>.
     * @throws SecurityException         If an attempt is made to add this class to a package that
     *                                   contains classes that were signed by a different set of
     *                                   certificates than this class, or if <tt>name</tt> begins with
     *                                   "<tt>java.</tt>".
     */
    public static Class<?> defineClass(
            ClassLoader loader,
            String name, @NotNull byte[] b, int off, int len,
            ProtectionDomain protectionDomain)
            throws ClassFormatError {
        return Unsafe.getUnsafe().defineClass(name, b, off, len, loader, protectionDomain);
    }

    /**
     * <p> The first class defined in a package determines the exact set of
     * certificates that all subsequent classes defined in that package must
     * contain.  The set of certificates for a class is obtained from the
     * {@link java.security.CodeSource <tt>CodeSource</tt>} within the
     * <tt>ProtectionDomain</tt> of the class.  Any classes added to that
     * package must contain the same set of certificates or a
     * <tt>SecurityException</tt> will be thrown.  Note that if
     * <tt>name</tt> is <tt>null</tt>, this check is not performed.
     * You should always pass in the <a href="#name">binary name</a> of the
     * class you are defining as well as the bytes.  This ensures that the
     * class you are defining is indeed the class you think it is.
     *
     * <p> The specified <tt>name</tt> cannot begin with "<tt>java.</tt>", since
     * all classes in the "<tt>java.*</tt> packages can only be defined by the
     * bootstrap class loader.  If <tt>name</tt> is not <tt>null</tt>, it
     * must be equal to the <a href="#name">binary name</a> of the class
     * specified by the byte array "<tt>b</tt>", otherwise a {@link
     * NoClassDefFoundError <tt>NoClassDefFoundError</tt>} will be thrown. </p>
     *
     * @param loader           The loader to be executed
     * @param name             The expected <a href="#name">binary name</a> of the class, or
     *                         <tt>null</tt> if not known
     * @param code             The bytes that make up the class data. The bytes in positions
     *                         <tt>off</tt> through <tt>off+len-1</tt> should have the format
     *                         of a valid class file as defined by
     *                         <cite>The Java&trade; Virtual Machine Specification</cite>.
     * @param protectionDomain The ProtectionDomain of the class
     * @return The <tt>Class</tt> object created from the data,
     * and optional <tt>ProtectionDomain</tt>.
     * @throws ClassFormatError     If the data did not contain a valid class
     * @throws NoClassDefFoundError If <tt>name</tt> is not equal to the <a href="#name">binary
     *                              name</a> of the class specified by <tt>code</tt>
     * @throws SecurityException    If an attempt is made to add this class to a package that
     *                              contains classes that were signed by a different set of
     *                              certificates than this class, or if <tt>name</tt> begins with
     *                              "<tt>java.</tt>".
     * @since 2.11
     */
    public static Class<?> defineClass(ClassLoader loader,
                                       String name,
                                       @NotNull byte[] code,
                                       ProtectionDomain protectionDomain)
            throws ClassFormatError {
        return defineClass(loader, name, code, 0, code.length, protectionDomain);
    }

    /**
     * <p> The first class defined in a package determines the exact set of
     * certificates that all subsequent classes defined in that package must
     * contain.  The set of certificates for a class is obtained from the
     * {@link java.security.CodeSource <tt>CodeSource</tt>} within the
     * <tt>ProtectionDomain</tt> of the class.  Any classes added to that
     * package must contain the same set of certificates or a
     * <tt>SecurityException</tt> will be thrown.  Note that if
     * <tt>name</tt> is <tt>null</tt>, this check is not performed.
     * You should always pass in the <a href="#name">binary name</a> of the
     * class you are defining as well as the bytes.  This ensures that the
     * class you are defining is indeed the class you think it is.
     *
     * <p> The specified <tt>name</tt> cannot begin with "<tt>java.</tt>", since
     * all classes in the "<tt>java.*</tt> packages can only be defined by the
     * bootstrap class loader.  If <tt>name</tt> is not <tt>null</tt>, it
     * must be equal to the <a href="#name">binary name</a> of the class
     * specified by the byte array "<tt>b</tt>", otherwise a {@link
     * NoClassDefFoundError <tt>NoClassDefFoundError</tt>} will be thrown. </p>
     *
     * @param loader           The loader to be executed
     * @param code             The bytes that make up the class data. The bytes in positions
     *                         <tt>off</tt> through <tt>off+len-1</tt> should have the format
     *                         of a valid class file as defined by
     *                         <cite>The Java&trade; Virtual Machine Specification</cite>.
     * @param protectionDomain The ProtectionDomain of the class
     * @return The <tt>Class</tt> object created from the data,
     * and optional <tt>ProtectionDomain</tt>.
     * @throws ClassFormatError     If the data did not contain a valid class
     * @throws NoClassDefFoundError If <tt>name</tt> is not equal to the <a href="#name">binary
     *                              name</a> of the class specified by <tt>b</tt>
     * @throws SecurityException    If an attempt is made to add this class to a package that
     *                              contains classes that were signed by a different set of
     *                              certificates than this class, or if <tt>name</tt> begins with
     *                              "<tt>java.</tt>".
     * @since 2.11
     */
    public static Class<?> defineClass(ClassLoader loader,
                                       @NotNull byte[] code,
                                       ProtectionDomain protectionDomain)
            throws ClassFormatError {
        return defineClass(loader, null, code, 0, code.length, protectionDomain);
    }

    /**
     * <p> The first class defined in a package determines the exact set of
     * certificates that all subsequent classes defined in that package must
     * contain.  The set of certificates for a class is obtained from the
     * {@link java.security.CodeSource <tt>CodeSource</tt>} within the
     * <tt>ProtectionDomain</tt> of the class.  Any classes added to that
     * package must contain the same set of certificates or a
     * <tt>SecurityException</tt> will be thrown.  Note that if
     * <tt>name</tt> is <tt>null</tt>, this check is not performed.
     * You should always pass in the <a href="#name">binary name</a> of the
     * class you are defining as well as the bytes.  This ensures that the
     * class you are defining is indeed the class you think it is.
     *
     * <p> The specified <tt>name</tt> cannot begin with "<tt>java.</tt>", since
     * all classes in the "<tt>java.*</tt> packages can only be defined by the
     * bootstrap class loader.  If <tt>name</tt> is not <tt>null</tt>, it
     * must be equal to the <a href="#name">binary name</a> of the class
     * specified by the byte array "<tt>b</tt>", otherwise a {@link
     * NoClassDefFoundError <tt>NoClassDefFoundError</tt>} will be thrown. </p>
     *
     * @param loader           The loader to be executed
     * @param code             The bytes that make up the class data. The bytes in positions
     *                         <tt>off</tt> through <tt>off+len-1</tt> should have the format
     *                         of a valid class file as defined by
     *                         <cite>The Java&trade; Virtual Machine Specification</cite>.
     * @param protectionDomain The ProtectionDomain of the class
     * @return The <tt>Class</tt> object created from the data,
     * and optional <tt>ProtectionDomain</tt>.
     * @throws ClassFormatError     If the data did not contain a valid class
     * @throws NoClassDefFoundError If <tt>name</tt> is not equal to the <a href="#name">binary
     *                              name</a> of the class specified by <tt>b</tt>
     * @throws SecurityException    If an attempt is made to add this class to a package that
     *                              contains classes that were signed by a different set of
     *                              certificates than this class, or if <tt>name</tt> begins with
     *                              "<tt>java.</tt>".
     * @since 2.11
     */
    public static Class<?> defineClass(ClassLoader loader,
                                       @NotNull ClassWriter code,
                                       ProtectionDomain protectionDomain)
            throws ClassFormatError {
        return defineClass(loader, null, code.toByteArray(), protectionDomain);
    }

    static {
        // Loading MA for AccessToolkit initialize
        Class<?> x;
        try {
            x = Class.forName("jdk.internal.reflect.MethodAccessor");
        } catch (Throwable a) {
            try {
                x = Class.forName("sun.reflect.MethodAccessor");
            } catch (ClassNotFoundException e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        MethodAccessor = x;
        try {
            x = Class.forName("jdk.internal.reflect.FieldAccessor");
        } catch (ClassNotFoundException a) {
            try {
                x = Class.forName("sun.reflect.FieldAccessor");
            } catch (ClassNotFoundException nf) {
                throw new ExceptionInInitializerError(nf);
            }
        }
        FieldAccessor = x;
        try {
            x = Class.forName("jdk.internal.reflect.ConstructorAccessor");
        } catch (ClassNotFoundException a) {
            try {
                x = Class.forName("sun.reflect.ConstructorAccessor");
            } catch (ClassNotFoundException nf) {
                throw new ExceptionInInitializerError(nf);
            }
        }
        ConstructorAccessor = x;
        AccessToolkit.setup();
        MethodHandles.Lookup lk;
        Exception e0 = null;
        try {
            try {
                final Field implLookup = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                AccessToolkit.setAccessible(implLookup, true);
                lk = (MethodHandles.Lookup) implLookup.get(null);
            } catch (Exception err) {
                e0 = err;
                final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                AccessToolkit.setAccessible(constructor, true);
                lk = constructor.newInstance(Reflection.class, -1);
            }
        } catch (Exception e) {
            var err = new ExceptionInInitializerError(e0);
            err.addSuppressed(e);
            throw err;
        }
        root = lk;
        try {
            var mg = lk.findGetter(Method.class, "methodAccessor", MethodAccessor);
            var acquireMethodAccessor = lk.findVirtual(Method.class, "acquireMethodAccessor",
                    MethodType.methodType(MethodAccessor));
            MethodAccessorGetter = met -> {
                try {
                    var acc = mg.invoke(met);
                    if (acc == null) return acquireMethodAccessor.invoke(met);
                    return acc;
                } catch (Error | RuntimeException r) {
                    throw r;
                } catch (Throwable thr) {
                    throw new RuntimeException(thr);
                }
            };
            var get = lk.findVirtual(Field.class, "getFieldAccessor", MethodType.methodType(
                    FieldAccessor, Object.class
            ));
            FieldAccessorGetter = fie -> {
                try {
                    return get.invoke(fie, null);
                } catch (RuntimeException | Error re) {
                    throw re;
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            };
            var ms = lk.findSetter(Method.class, "methodAccessor", MethodAccessor);
            MethodAccessorSetter = (method, accessor) -> {
                if (method == null) throw new NullPointerException("method");
                if (accessor != null) {
                    MethodAccessor.cast(accessor);
                }
                try {
                    ms.invoke(method, accessor);
                } catch (RuntimeException | Error e) {
                    throw e;
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            };
            var no = lk.findSetter(Field.class, "fieldAccessor", FieldAccessor);
            var oo = lk.findSetter(Field.class, "overrideFieldAccessor", FieldAccessor);
            FieldAccessorSetter = (field, accessor) -> {
                Objects.requireNonNull(field, "field");
                if (accessor != null) {
                    FieldAccessor.cast(accessor);
                }
                try {
                    //noinspection deprecation
                    if (field.isAccessible()) {
                        oo.invoke(field, accessor);
                    } else {
                        no.invoke(field, accessor);
                    }
                } catch (Error | RuntimeException re) {
                    throw re;
                } catch (Throwable thr) {
                    throw new RuntimeException(thr);
                }
            };
            var ccg = lk.findGetter(Constructor.class, "constructorAccessor", ConstructorAccessor);
            var aqc = lk.findVirtual(Constructor.class, "acquireConstructorAccessor", MethodType.methodType(ConstructorAccessor));
            ConstructorAccessorGetter = con -> {
                try {
                    var acc = ccg.invoke(con);
                    if (acc == null) acc = aqc.invoke(con);
                    return acc;
                } catch (Error | RuntimeException err) {
                    throw err;
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            };
            var cgs = lk.findSetter(Constructor.class, "constructorAccessor", ConstructorAccessor);
            ConstructorAccessorSetter = (con, acc) -> {
                Objects.requireNonNull(con, "constructor");
                if (acc != null) ConstructorAccessor.cast(acc);
                try {
                    cgs.invoke(con, acc);
                } catch (Error | RuntimeException err) {
                    throw err;
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            };
        } catch (Throwable thr) {
            throw new ExceptionInInitializerError(thr);
        }
    }

    private static final boolean Clone$Unsafe = Boolean.getBoolean("mxlib.reflect.clone.unsafe");
    private static final Class<?> Module, ModuleLayer;

    static {
        Class<?> a = null, b = null;
        try {
            a = Class.forName("java.lang.Module");
            b = Class.forName("java.lang.ModuleLayer");
        } catch (ClassNotFoundException ignore) {
        }
        Module = a;
        ModuleLayer = b;
    }

    /**
     * Clone a object.
     *
     * @param object The source object
     * @param <T>    The type of object
     * @return the cloned object
     * @since 2.7
     */
    @SuppressWarnings({"unchecked", "CastCanBeRemovedNarrowingVariableType"})
    @Contract(pure = true, value = "null -> null; !null -> !null")
    public static <T> T clone(T object) {
        if (object == null)
            return null;
        Class<T> type = (Class<T>) object.getClass();
        if (type == Class.class) {
            throw new UnsupportedOperationException("Cannot clone a class");
        }
        if (!Clone$Unsafe) {
            if (object instanceof ClassLoader || object instanceof SecurityManager || object instanceof Runtime
                    || object instanceof Thread || object instanceof ThreadLocal || object instanceof Method || object instanceof Field || object instanceof Constructor
                    || object instanceof MethodHandle || object instanceof ThreadGroup || object instanceof Package
                    || (Module != null && Module.isInstance(object)) || (ModuleLayer != null && ModuleLayer.isInstance(object))
                    || object instanceof java.nio.channels.Channel || object instanceof ByteBuffer
                    || (object instanceof OutputStream && !(object instanceof ByteArrayOutputStream))
                    || (object instanceof InputStream && !(object instanceof ByteArrayInputStream))
                    || object instanceof Writer || object instanceof Reader) {
                throw new UnsupportedOperationException("Cannot clone unsafe object.");
            }
        }
        if (type.isArray()) {
            Object array = Array.newInstance(type.getComponentType(), Array.getLength(object));
            //noinspection SuspiciousSystemArraycopy
            System.arraycopy(object, 0, array, 0, Array.getLength(array));
            return (T) array;
        }
        Unsafe unsafe = Unsafe.getUnsafe();
        final Object instance;
        try {
            instance = unsafe.allocateInstance(type);
        } catch (InstantiationException e) {
            throw new RuntimeException("This should not happen", e);
        }
        Class<?> doit = type;
        do {
            clone$copy(doit, object, instance);
            doit = doit.getSuperclass();
        } while (doit != null);
        return (T) instance;
    }

    /**
     * Get field value with Unsafe
     *
     * @param this_ The this object of field.
     * @param field The field of object.
     * @return The field value.
     * @since 2.7
     */
    public static Object getObjectValue(Object this_, @NotNull Field field) {
        long offset;
        Unsafe unsafe = Unsafe.getUnsafe();
        if (Modifier.isStatic(field.getModifiers())) {
            offset = unsafe.staticFieldOffset(field);
            this_ = field.getDeclaringClass();
        } else {
            Objects.requireNonNull(this_);
            field.getDeclaringClass().cast(this_);
            offset = unsafe.objectFieldOffset(field);
        }
        Class<?> typ = field.getType();
        if (typ == boolean.class) {
            return unsafe.getBoolean(this_, offset);
        } else if (typ == int.class) {
            return unsafe.getInt(this_, offset);
        } else if (typ == float.class) {
            return unsafe.getFloat(this_, offset);
        } else if (typ == long.class) {
            return unsafe.getLong(this_, offset);
        } else if (typ == char.class) {
            return unsafe.getChar(this_, offset);
        } else if (typ == short.class) {
            return unsafe.getShort(this_, offset);
        } else if (typ == byte.class) {
            return unsafe.getByte(this_, offset);
        } else if (typ == double.class) {
            return unsafe.getDouble(this_, offset);
        } else {
            return unsafe.getReference(this_, offset);
        }
    }

    /**
     * Change object's field value.
     *
     * @param this_ The this variable.
     * @param field The field need change.
     * @param value The value override.
     */
    public static void setObjectValue(Object this_, @NotNull Field field, Object value) {
        long offset;
        Unsafe unsafe = Unsafe.getUnsafe();
        if (Modifier.isStatic(field.getModifiers())) {
            this_ = unsafe.staticFieldBase(field);
            offset = unsafe.staticFieldOffset(field);
        } else {
            Objects.requireNonNull(this_);
            field.getDeclaringClass().cast(this_);
            offset = unsafe.objectFieldOffset(field);
        }
        if (value != null && !field.getType().isPrimitive()) field.getType().cast(value);
        Class<?> typ = field.getType();
        if (typ == boolean.class) {
            unsafe.putBoolean(this_, offset, (boolean) value);
        } else if (typ == int.class) {
            unsafe.putInt(this_, offset, (int) value);
        } else if (typ == float.class) {
            unsafe.putFloat(this_, offset, (float) value);
        } else if (typ == long.class) {
            unsafe.putLong(this_, offset, (long) value);
        } else if (typ == char.class) {
            unsafe.putChar(this_, offset, (char) value);
        } else if (typ == short.class) {
            unsafe.putShort(this_, offset, (short) value);
        } else if (typ == byte.class) {
            unsafe.putByte(this_, offset, (byte) value);
        } else if (typ == double.class) {
            unsafe.putDouble(this_, offset, (double) value);
        } else {
            unsafe.putReference(this_, offset, value);
        }
    }

    public static void copyValues(Object from, Object to) {
        if (from.getClass().isInstance(to)) {
            clone$copy(from.getClass(), from, to);
            return;
        }
        throw new UnsupportedOperationException(to + "not cast to " + from.getClass());
    }

    private static void clone$copy(Class<?> type, Object from, Object to) {
        Unsafe unsafe = Unsafe.getUnsafe();
        for (Field f : type.getDeclaredFields()) {
            if (!Modifier.isStatic(f.getModifiers())) {
                final long offset = unsafe.objectFieldOffset(f);
                Class<?> typ = f.getType();
                if (typ == boolean.class) {
                    unsafe.putBoolean(to, offset, unsafe.getBoolean(from, offset));
                } else if (typ == int.class) {
                    unsafe.putInt(to, offset, unsafe.getInt(from, offset));
                } else if (typ == float.class) {
                    unsafe.putFloat(to, offset, unsafe.getFloat(from, offset));
                } else if (typ == long.class) {
                    unsafe.putLong(to, offset, unsafe.getLong(from, offset));
                } else if (typ == char.class) {
                    unsafe.putChar(to, offset, unsafe.getChar(from, offset));
                } else if (typ == short.class) {
                    unsafe.putShort(to, offset, unsafe.getShort(from, offset));
                } else if (typ == byte.class) {
                    unsafe.putByte(to, offset, unsafe.getByte(from, offset));
                } else if (typ == double.class) {
                    unsafe.putDouble(to, offset, unsafe.getDouble(from, offset));
                } else {
                    unsafe.putReference(to, offset, unsafe.getReference(from, offset));
                }
            }
        }
    }

}
