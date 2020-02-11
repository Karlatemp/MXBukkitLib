/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Toolkit.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.MXLibBootProvider;
import cn.mcres.karlatemp.mxlib.cmd.ICommand;
import cn.mcres.karlatemp.mxlib.cmd.ICommands;
import cn.mcres.karlatemp.mxlib.internal.ClassLoaderGetter;
import cn.mcres.karlatemp.mxlib.internal.UFRF;
import cn.mcres.karlatemp.mxlib.internal.UnsafeInstaller;
import cn.mcres.karlatemp.mxlib.tools.security.AccessToolkit;
import cn.mcres.karlatemp.mxlib.util.FilesIterator;
import cn.mcres.karlatemp.mxlib.util.IteratorSupplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Permission;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 一个工具集合
 */
public class Toolkit {
    /**
     * @param k   The entry key
     * @param v   The entry value
     * @param <K> The key type
     * @param <V> The value type.
     * @return A new Entry.
     * @since 2.11
     */
    public static <K, V> Map.Entry<K, V> entry(K k, V v) {
        return new Map.Entry<>() {
            V vx = v;

            @Override
            public K getKey() {
                return k;
            }

            @Override
            public V getValue() {
                return vx;
            }

            @Override
            public V setValue(V value) {
                V vv = vx;
                vx = value;
                return vv;
            }
        };
    }

    public static <T, V, M extends Map<T, V>> Collector<Map.Entry<T, V>, M, M> toMapCollector(Supplier<M> mapCreator) {
        return Collector.of(mapCreator,
                (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                (m1, m2) -> {
                    m1.putAll(m2);
                    return m1;
                });
    }

    /**
     * @param cb  The function to invoke.
     * @param <T> The type of the callable
     * @return The callable need check
     * @since 2.5
     */
    @SuppressWarnings("unchecked")
    @Contract(pure = true, value = "null -> null")
    public static <T> Callable<T> toCallable(Object cb) {
        if (cb == null) return null;
        if (cb instanceof Callable) {
            return (Callable<T>) cb;
        }
        if (cb instanceof Runnable) {
            return () -> {
                ((Runnable) cb).run();
                return null;
            };
        }
        if (cb instanceof Supplier) {
            return ((Supplier<T>) cb)::get;
        }
        if (cb instanceof Throwable) {
            if (cb instanceof Exception) {
                return () -> {
                    throw (Exception) cb;
                };
            } else if (cb instanceof Error) {
                return () -> {
                    throw (Error) cb;
                };
            } else {
                return toCallable(new RuntimeException((Throwable) cb));
            }
        }
        throw new UnsupportedOperationException("Unsupported " + cb);
    }

    /**
     * Get class's package name.<br>
     * 截取类名的包名字
     *
     * @param name 类的全名
     * @return 类的包名
     */
    @NotNull
    @Contract(pure = true)
    public static String getPackageByClassName(String name) {
        if (name == null) return "";
        int x = name.lastIndexOf('.');
        if (x != -1) {
            return name.substring(0, x);
        }
        return "";
    }

    /**
     * Get class's simple name<br>
     * 获取类名的精简名
     *
     * @param name 类全名
     * @return 类精简名
     */
    @Contract(pure = true)
    public static String getClassSimpleName(String name) {
        int last = name.lastIndexOf('.');
        if (last == -1) return name;
        return name.substring(last);
    }

    private static final Comparator<String> PACKAGE_COMPARATOR = Comparator.comparing(Toolkit::getPackageByClassName);
    private static final Function<String, String> PACKAGE_BY_CLASS_NAME = Toolkit::getPackageByClassName;

    /**
     * @return The function of {@link #getPackageByClassName(String)}
     * @see #getPackageByClassName(String)
     */
    @Contract(pure = true)
    public static Function<String, String> getPackageByClassName() {
        return PACKAGE_BY_CLASS_NAME;
    }

    /**
     * Get the package name comparator.<br>
     * 获取包名比较器
     *
     * @return The comparator of package.
     * @see #getPackageByClassName(String)
     */
    @Contract(pure = true)
    public static Comparator<String> getPackageComparator() {
        return PACKAGE_COMPARATOR;
    }

    /**
     * @param <T> 对象类型
     * @return 对象的类|null
     * @see Object#getClass()
     */
    @Contract(value = "null -> null", pure = true)
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(T obj) {
        if (obj == null) return null;
        return (Class<T>) obj.getClass();
    }

    @Contract(pure = true)
    public static boolean isNum(String s) {
        if (s.isEmpty()) return false;
        for (char c : s.toCharArray()) {
            if (!(c >= '0' && c <= '9')) {
                return false;
            }
        }
        return true;
    }

    @Contract(pure = true)
    public static ICommands getRoot(ICommand command) {
        ICommands current;
        if (command instanceof ICommands) {
            current = (ICommands) command;
        } else {
            current = command.getParent();
        }
        ICommands loop_ = current;
        while (loop_ != null) {
            current = loop_;
            loop_ = loop_.getParent();
        }
        return current;
    }

    private static final Pattern CLASS_NAME_CHECKER = Pattern.compile(
            "^[a-z_$][a-z_$0-9]*(\\.[a-z$_][a-z$_0-9]*)$", Pattern.CASE_INSENSITIVE | Pattern.UNIX_LINES);

    /**
     * Check if the given string is a valid class name<br>
     * 检查给定的字符串是否是有效的类名
     *
     * @param check The name of check
     * @return true if the class name valid
     * @since 2.2
     */
    @Contract(value = "null -> false", pure = true)
    public static boolean isClassName(String check) {
        if (check != null) {
            return CLASS_NAME_CHECKER.matcher(check).find();
        }
        return false;
    }

    /**
     * fill the array, 填充数组
     *
     * @param args     The array 数组
     * @param supplier The supplier 获取器
     * @param <T>      The type of array.
     * @since 2.4
     */
    public static <T> void fill(@NotNull T[] args, @NotNull Supplier<T> supplier) {
        fill(args, 0, args.length, supplier);
    }

    /**
     * Fill the array 填充数组
     *
     * @param args     The array 数组
     * @param off      The start 起始点
     * @param length   length 长度限制
     * @param supplier The supplier 获取器
     * @param <T>      The type of array
     * @since 2.4
     */
    public static <T> void fill(@NotNull T[] args, int off, int length, @NotNull Supplier<T> supplier) {
        for (int i = off; i < args.length && length-- > 0; i++) {
            args[i] = supplier.get();
        }
    }

    public static <T> void fill(@NotNull T[] args, @Nullable T o) {
        Arrays.fill(args, o);
    }

    /**
     * File the array with same value. 使用同一个值填充数组
     *
     * @param args   The array 数组
     * @param off    start position 起始点
     * @param length The fill size of array 填充的数组长度
     * @param o      The value 填充值
     * @param <T>    The type of array
     * @since 2.4
     */
    public static <T> void fill(T[] args, int off, int length, @Nullable T o) {
        for (int i = off; i < args.length && length-- > 0; i++) {
            args[i] = o;
        }
    }

    /**
     * 把字符串转为 ByteBuffer(UTF_8)
     *
     * @param x 字符串
     * @return 编码后的ByteBuffer, 如果需要附加字符串长度请使用 position(0)
     * @since 2.4
     */
    @Contract(pure = true)
    public static ByteBuffer ofUTF8ByteBuffer(String x) {
        final ByteBuffer encode = StandardCharsets.UTF_8.encode(x);
        ByteBuffer cp = ByteBuffer.allocateDirect(encode.remaining() + Short.BYTES);
        cp.putShort((short) encode.remaining()).put(encode).flip().position(Short.BYTES);
        return cp;
    }

    /**
     * return value's boolean value
     *
     * @param o The value
     * @return The boolean match
     * @since 2.5
     */
    @Contract(value = "null -> false", pure = true)
    public static boolean asBoolean(Object o) {
        if (o == null) return false;
        if (o instanceof String) {
            String s = String.valueOf(o);
            return !s.trim().isEmpty();
        }
        if (o instanceof Number) {
            if (o instanceof Double) {
                return Double.doubleToRawLongBits((Double) o) != 0;
            }
            return ((Number) o).intValue() != 0;
        }
        if (o instanceof Boolean) return (Boolean) o;
        return false;
    }

    /**
     * 反射工具
     */
    @SuppressWarnings("Java9ReflectionClassVisibility")
    public abstract static class Reflection {
        private static final MethodHandles.Lookup root;
        static Reflection ref;
        public static final Class<?> MethodAccessor, FieldAccessor, ConstructorAccessor;
        public static final Function<Method, Object> MethodAccessorGetter;
        public static final Function<Constructor<?>, Object> ConstructorAccessorGetter;
        public static final BiConsumer<Method, Object> MethodAccessorSetter;
        public static final BiConsumer<Constructor<?>, Object> ConstructorAccessorSetter;
        public static final BiConsumer<Field, Object> FieldAccessorSetter;
        public static final Function<Field, Object> FieldAccessorGetter;

        /**
         * 这只能用来判断使用哪种方法{@link Object#getClass()}
         *
         * @return 使用的实例
         */
        @Contract(pure = true)
        public static Reflection getInstance() {
            return ref;
        }

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

        public static final int LOAD_CLASS_THREAD_CONTENT = 1,
                LOAD_CLASS_CALLER_CLASSLOADER = 0b10;

        /**
         * Load class from class loaders
         *
         * @param name  The class name
         * @param flags Loading flags
         * @return The class loaded
         * @throws ClassNotFoundException If class not found
         * @since 2.2
         */
        @Contract("null,_ -> null; !null, _ -> !null")
        @Nullable
        public static Class<?> loadClassWith(String name, int flags) throws ClassNotFoundException {
            if (name == null) return null;
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException ignore) {
            }
            if ((flags & LOAD_CLASS_THREAD_CONTENT) != 0) {
                try {
                    return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
                } catch (ClassNotFoundException ignore) {
                }
            }
            if ((flags & LOAD_CLASS_CALLER_CLASSLOADER) != 0) {
                try {
                    return Class.forName(name, false, getClassLoader(Reflection.getCallerClass()));
                } catch (ClassNotFoundException ignore) {
                }
            }
            throw new ClassNotFoundException(name);
        }

        /**
         * Load class from class loaders
         *
         * @param name    The name of class
         * @param loaders Loaders
         * @return The class loaded
         * @throws ClassNotFoundException If class not found in any loader
         * @since 2.2
         */
        @Contract("null,_ -> null;!null,_ -> !null")
        public static Class<?> loadClassFrom(String name, @NotNull Collection<ClassLoader> loaders) throws ClassNotFoundException {
            if (name == null) return null;
            for (ClassLoader loader : loaders) {
                try {
                    return loader.loadClass(name);
                } catch (ClassNotFoundException ignore) {
                }
            }
            throw new ClassNotFoundException(name);
        }

        public static Class<?> loadClassLink(Collection<String> types, @Nullable ClassLoader loader) throws ClassNotFoundException {
            final ClassLoader caller = getClassLoader(Reflection.getCallerClass()), context = Thread.currentThread().getContextClassLoader();
            if (types == null || types.isEmpty()) return null;
            for (String name : types) {
                try {
                    if (loader != null)
                        return loader.loadClass(name);
                } catch (ClassNotFoundException ignore) {
                }
                try {
                    return Class.forName(name);
                } catch (ClassNotFoundException ignore) {
                }
                try {
                    return Class.forName(name, false, context);
                } catch (ClassNotFoundException ignore) {
                }
                try {
                    return Class.forName(name, false, caller);
                } catch (ClassNotFoundException ignore) {
                }

            }
            throw new ClassNotFoundException();
        }

        @Contract("null, _ -> null; !null, _ -> !null")
        public static <T extends AccessibleObject> T setAccess(T accessibleObject, boolean b) {
            if (accessibleObject != null) accessibleObject.setAccessible(b);
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

        protected abstract Class<?> defineClass0(
                ClassLoader loader,
                String name, @NotNull byte[] b, int off, int len,
                ProtectionDomain protectionDomain)
                throws ClassFormatError;

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
            return ref.defineClass0(loader, name, b, off, len, protectionDomain);
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
            MethodHandles.Lookup lk;
            try {
                try {
                    final Field implLookup = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                    AccessToolkit.setAccessible(implLookup, true);
                    lk = (MethodHandles.Lookup) implLookup.get(null);
                } catch (Exception err) {
                    final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                    AccessToolkit.setAccessible(constructor, true);
                    lk = constructor.newInstance(Toolkit.class, -1);
                }
            } catch (Exception e) {
                lk = MethodHandles.lookup();
            }
            root = lk;
            ref = UnsafeInstaller.installReflection();
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

        /**
         * 获取调用者的Class
         *
         * @return 获取调用者的Class
         * @throws ArrayIndexOutOfBoundsException 在程序main入口调用时抛出
         */
        @NotNull
        @Contract(pure = true)
        public static Class<?> getCallerClass() {
            final Class<?>[] ct = StackTrace.kit.ct();
            return ct[3];
        }

        /**
         * 获取调用者的Class
         *
         * @param point 指定的堆栈位置
         * @return 获取调用者的Class
         */
        @Nullable
        @Contract(pure = true)
        public static Class<?> getCallerClass(int point) {
            if (point < 0) return null;
            final Class<?>[] ct = StackTrace.kit.ct();
            int at = point + 3;
            if (at < ct.length)
                return ct[at];
            return null;
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

        @Contract(pure = true, value = "null -> fail;")
        public static ClassLoader getClassLoader(Class<?> clazz) {
            return ClassLoaderGetter.impl.apply(clazz);
        }
    }

    /**
     * 堆栈
     */
    public static class StackTrace {
        Class<?> c;
        StackTraceElement elm;

        @Override
        @Contract(pure = true)
        public String toString() {
            return elm + "#" + c;
        }

        @Contract(pure = true)
        public Class<?> getContent() {
            return c;
        }

        @Contract(pure = true)
        public StackTraceElement getStackTraceElement() {
            return elm;
        }

        static class $ToolKit extends SecurityManager {
            @Override
            public void checkPermission(Permission perm, Object context) {
            }

            @Override
            public void checkPermission(Permission perm) {
            }

            @Override
            public void checkCreateClassLoader() {
            }

            @Override
            public void checkAccess(Thread t) {
            }

            @Override
            public void checkAccess(ThreadGroup g) {
            }

            @Override
            public void checkExit(int status) {
            }

            @Override
            public void checkExec(String cmd) {
            }

            @Override
            public void checkLink(String lib) {
            }

            @Override
            public void checkRead(FileDescriptor fd) {
            }

            @Override
            public void checkRead(String file) {
            }

            @Override
            public void checkRead(String file, Object context) {
            }

            @Override
            public void checkWrite(FileDescriptor fd) {
            }

            @Override
            public void checkWrite(String file) {
            }

            @Override
            public void checkDelete(String file) {
            }

            @Override
            public void checkConnect(String host, int port) {
            }

            @Override
            public void checkConnect(String host, int port, Object context) {
            }

            @Override
            public void checkListen(int port) {
            }

            @Override
            public void checkAccept(String host, int port) {
            }

            @Override
            public void checkMulticast(InetAddress maddr) {
            }

            @SuppressWarnings("deprecation")
            @Override
            @Deprecated
            public void checkMulticast(InetAddress maddr, byte ttl) {
            }

            @Override
            public void checkPropertiesAccess() {
            }

            @Override
            public void checkPropertyAccess(String key) {
            }

            @Override
            public void checkPrintJobAccess() {
            }

            @Override
            public void checkPackageAccess(String pkg) {
            }

            @Override
            public void checkPackageDefinition(String pkg) {
            }

            @Override
            public void checkSetFactory() {
            }

            @Override
            public void checkSecurityAccess(String target) {
            }

            private static StackTrace create(Class<?> c, StackTraceElement elm) {
                StackTrace t = new StackTrace();
                t.c = c;
                t.elm = elm;
                return t;
            }

            StackTrace[] classes() {
                var cs = Stream.of(Toolkit.StackTrace.getClassContext()).skip(3).filter(c -> c.getName().indexOf('/') == -1).collect(
                        Collectors.toList()
                );
                var st = Arrays.asList(new Throwable().getStackTrace());
                var i1 = cs.iterator();
                var i2 = st.iterator();
                i2.next();
                i2.next();
                i2.next();
                var list = new ArrayList<StackTrace>();
                while (i1.hasNext()) {
                    var c = i1.next();
                    StackTraceElement elm;
                    do {
                        elm = i2.next();
                    } while (!elm.getClassName().equals(c.getName()));
                    var st0 = new StackTrace();
                    st0.c = c;
                    st0.elm = elm;
                    list.add(st0);
                }
                return list.toArray(new StackTrace[0]);
            }

            Class<?>[] ct() {
                return this.getClassContext();
            }
        }

        static final $ToolKit kit;
        static StackTrace IMPL;

        public String getMethodName() {
            return null;
        }

        public MethodType getMethodType() {
            return null;
        }

        static {
            MethodHandles.Lookup lk = Reflection.getRoot();
            SecurityManager old = System.getSecurityManager();
            $ToolKit tk;
            try {
                try {
                    tk = new $ToolKit();
                } catch (Throwable thr) {
                    MethodHandle mh = lk.findStaticSetter(System.class, "security", SecurityManager.class);
                    mh.invoke((Object) null);
                    tk = new $ToolKit();
                    mh.invoke(old);
                }
            } catch (Throwable thr) {
                try {
                    for (Field f : System.class.getDeclaredFields()) {
                        if (f.getType() == SecurityManager.class) {
                            f.setAccessible(true);
                            f.set(null, null);
                        }
                    }
                    tk = new $ToolKit();
                } catch (Throwable thrx) {
                    tk = new $ToolKit();
                }
            } finally {
                if (System.getSecurityManager() != old) {
                    System.setSecurityManager(old);
                }
            }
            IMPL = new StackTrace();
            kit = tk;
            try {
                Class.forName("java.lang.StackWalker");
                IMPL = new StackTrace() {
                    StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

                    @Override
                    StackTrace[] st() {
                        return walker.walk(frames -> frames.skip(2).map(info -> new StackTrace() {
                            @Override
                            public String toString() {
                                return info.toString() + '#' + info.getDeclaringClass();
                            }

                            @Override
                            public Class<?> getContent() {
                                return info.getDeclaringClass();
                            }

                            @Override
                            public StackTraceElement getStackTraceElement() {
                                return info.toStackTraceElement();
                            }

                            @Override
                            public String getMethodName() {
                                return info.getMethodName();
                            }

                            @Override
                            public MethodType getMethodType() {
                                return info.getMethodType();
                            }
                        }).toArray(StackTrace[]::new));
                    }
                };
            } catch (Throwable ignore) {
            }
        }

        StackTrace[] st() {
            return kit.classes();
        }

        /**
         * 获取当前线程堆栈
         *
         * @return 堆栈列表
         */
        @Contract(pure = true)
        public static StackTrace[] getStackTraces() {
            return IMPL.st();
        }

        /**
         * 获取当前线程堆栈
         *
         * @return 堆栈列表(只有类)
         */
        @Contract(pure = true)
        public static Class<?>[] getClassContext() {
            Class<?>[] cc = kit.ct();
            if (cc.length < 2) return new Class<?>[0];
            int x = cc.length - 2;
            Class<?>[] nw = new Class<?>[x];
            System.arraycopy(cc, 2, nw, 0, x);
            return nw;
        }
    }

    /**
     * IO 数据操作
     *
     * @since 2.5
     */
    public static class IO {
        public static long writeTo(@NotNull InputStream is, @NotNull OutputStream os) throws IOException {
            return writeTo(is, os, null);
        }

        public static long writeTo(@NotNull InputStream is, @NotNull OutputStream os, @Nullable byte[] buffer) throws IOException {
            return writeTo(is, os, buffer, 0);
        }

        public static long writeTo(@NotNull InputStream is, @NotNull OutputStream os, @Nullable byte[] buffer, long length) throws IOException {
            long buf = 0;
            if (buffer == null) {
                buffer = new byte[1024];
            }
            if (length == 0) {
                while (true) {
                    int leng = is.read(buffer);
                    if (leng == -1) {
                        break;
                    }
                    os.write(buffer, 0, leng);
                    buf += leng;
                }
            } else {
                final int bl = buffer.length;
                while (length > 0) {
                    int leng = is.read(buffer, 0, Math.max(0, Math.min((int) length, bl)));
                    if (leng == -1) {
                        break;
                    }
                    os.write(buffer, 0, leng);
                    buf += leng;
                    length -= leng;
                }
            }
            return buf;
        }

        /**
         * get bytes from bits
         *
         * @param bits  Bits of cut
         * @param bytes byte array size
         * @return The array of bits
         * @see #toBytes(long, int)
         * @since 2.7
         */
        public static byte[] toBytes(int bits, int bytes) {
            return toBytes(Integer.toUnsignedLong(bits), bytes);
        }

        /**
         * get bytes from bits
         *
         * @param bits  Bits of cut
         * @param bytes byte array size
         * @return The array of bits
         * @since 2.7
         */
        public static byte[] toBytes(long bits, int bytes) {
            byte[] ref = new byte[bytes];
            for (int i = 0; i < bytes; i++) {
                ref[i] = (byte) ((bits >> (i * Byte.SIZE)) & 0xFF);
            }
            return ref;

        }

        /**
         * Insert data to file
         *
         * @param position The insert position
         * @param data     The data insert
         * @param access   The random access file.(writable and readable)
         * @throws IOException The runtime exception.
         * @since 2.7
         */
        public static void insertData(long position, ByteBuffer data, @NotNull RandomAccessFile access) throws IOException {
            insertData(position, data, access, false);
        }

        /**
         * Insert data to file
         *
         * @param position The insert position
         * @param data     The data insert
         * @param access   The random access file.(writable and readable)
         * @param raw      Set write all data.(Unsafe)
         * @throws IOException The runtime exception.
         * @since 2.7
         */
        public static void insertData(long position, ByteBuffer data, @NotNull RandomAccessFile access, boolean raw) throws IOException {
            if (!data.hasRemaining()) return;
            /*
            long pos = access.getFilePointer();
            access.seek(position);
            byte[] write = new byte[size], read = new byte[size], tmp;
            int writeSize = size;
            do {
                int w;
                long pp = access.getFilePointer();
                if ((w = access.read(read)) < 0) {
                    if (writeSize > 0) {
                        access.write(write, 0, writeSize);
                    }
                    break;
                }
                access.seek(pp);
                access.write(write, 0, writeSize);
                tmp = write;
                write = read;
                read = tmp;
                writeSize = w;
            } while (true);
            access.seek(pos);
            */
            ByteBuffer r, w, tmp;
            if (raw) {
                if (data.position() != 0 || data.limit() != data.capacity()) {
                    throw new IOException("Cannot write a limited raw byte buffer.");
                }
                r = ByteBuffer.allocateDirect(data.capacity());
                w = data;
                data.clear();
            } else {
                w = ByteBuffer.allocateDirect(data.remaining());
                w.put(data);
                w.flip();
                r = ByteBuffer.allocateDirect(w.capacity());
            }
            final FileChannel channel = access.getChannel();
            final long at = channel.position(), aw = access.getFilePointer();
            channel.position(position);
            do {
                long pos = channel.position();
                if (channel.read(r) < 0) {
                    if (w.hasRemaining()) {
                        channel.write(w);
                    }
                    break;
                }
                channel.position(pos);
                channel.write(w);
                tmp = w;
                w = r;
                r = tmp;
                w.flip();
                r.clear();
            } while (true);
            channel.position(at);
            access.seek(aw);
        }

        /**
         * Insert empty data to position
         *
         * @param position The insert position
         * @param size     The empty size
         * @param access   The random access file.(writable and readable)
         * @throws IOException The runtime exception.
         * @since 2.7
         */
        public static void insertEmpty(long position, int size, @NotNull RandomAccessFile access) throws IOException {
            if (size < 1) return;
            insertData(position, ByteBuffer.allocateDirect(size), access, true);
        }

        /**
         * Quick calculation of file/directory
         *
         * @param md   The meddage digest
         * @param file The file or dir
         * @return the digest
         * @throws IOException Throw when make any exception.
         * @since 2.11
         */
        public static byte[] digest(@NotNull MessageDigest md, @NotNull File file) throws IOException {
            if (file.isDirectory()) {
                int root = file.toString().length();
                FilesIterator files = new FilesIterator(file, -1, false, false);
                ByteArrayInputStream EMPTY = new ByteArrayInputStream(new byte[0]);
                return digest(md, new IteratorSupplier<>(
                        new Iterator<>() {
                            private File current;

                            @Override
                            public boolean hasNext() {
                                if (current == null)
                                    return files.hasNext();
                                return true;
                            }

                            @Override
                            public InputStream next() {
                                if (current != null) {
                                    File f = current;
                                    current = null;
                                    if (f.isDirectory()) return EMPTY;
                                    try {
                                        return new FileInputStream(f);
                                    } catch (FileNotFoundException e) {
                                        throw new IOError(e);
                                    }
                                } else {
                                    if (files.hasNext()) {
                                        current = files.next();
                                        return new ByteArrayInputStream(('.' + current.toString().substring(root)).getBytes(StandardCharsets.UTF_8));
                                    }
                                    return null;
                                }
                            }
                        }
                ), null);
            }
            return digest(md, new IteratorSupplier<>(
                    Collections.singletonList(new FileInputStream(file)).iterator()
            ), null);
        }

        /**
         * Quick calculation of input streams.
         *
         * @param md     The message digest using.
         * @param data   Streams.
         * @param buffer reading buffer.
         * @return Result
         * @throws IOException Stream Reading Error.
         */
        public static byte[] digest(@NotNull MessageDigest md, @NotNull Supplier<? extends InputStream> data, @Nullable byte[] buffer) throws IOException {
            if (buffer == null) {
                buffer = new byte[2048];
            }
            while (true) {
                var next = data.get();
                if (next == null) break;
                do {
                    var length = next.read(buffer);
                    if (length == -1) break;
                    md.update(buffer, 0, length);
                } while (true);
                next.close();
            }
            return md.digest();
        }
    }
}
