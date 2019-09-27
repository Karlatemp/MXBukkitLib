/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Toolkit.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.MXLibBootProvider;
import cn.mcres.karlatemp.mxlib.cmd.ICommand;
import cn.mcres.karlatemp.mxlib.cmd.ICommands;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.security.ProtectionDomain;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * 一个工具集合
 */
@SuppressWarnings("JavadocReference")
public class Toolkit {
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
     * @see java.lang.Object#getClass()
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
     * 反射工具
     */
    public abstract static class Reflection {
        private static final MethodHandles.Lookup root;
        private static final Reflection ref;

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
         * @see java.lang.Class#forName(String)
         * @see java.lang.Class#forName(String, boolean, ClassLoader)
         * @see java.lang.ClassLoader
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
            if ((flags & LOAD_CLASS_THREAD_CONTENT) != 0) {
                try {
                    return Class.forName(name, false, Reflection.getCallerClass().getClassLoader());
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

        @Contract("null, _ -> null; !null, _ -> !null")
        public static <T extends AccessibleObject> T setAccess(T accessibleObject, boolean b) {
            if (accessibleObject != null) accessibleObject.setAccessible(b);
            return accessibleObject;
        }

        protected abstract Class<?> defineClass0(
                ClassLoader loader,
                String name, byte[] b, int off, int len,
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
        @Contract("null,_,_,_,_,_ -> fail;" +
                "_,_,null,_,_,_ -> fail;")
        public static Class<?> defineClass(
                ClassLoader loader,
                String name, byte[] b, int off, int len,
                ProtectionDomain protectionDomain)
                throws ClassFormatError {
            return ref.defineClass0(loader, name, b, off, len, protectionDomain);
        }

        static {
            Reflection rf;
            MethodHandles.Lookup lk;
            try {
                try {
                    final Field implLookup = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                    implLookup.setAccessible(true);
                    lk = (MethodHandles.Lookup) implLookup.get(null);
                } catch (Exception err) {
                    final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                    constructor.setAccessible(true);
                    lk = constructor.newInstance(Toolkit.class, -1);
                }
            } catch (Exception e) {
                lk = MethodHandles.lookup();
            }
            root = lk;
            try {
                MethodHandle mmh = lk.findSpecial(ClassLoader.class, "defineClass", MethodType.methodType(Class.class, String.class, byte[].class, int.class, int.class, ProtectionDomain.class), ClassLoader.class);
                rf = new $1(mmh);
            } catch (Throwable thr) {
                try {
                    Method met = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class, ProtectionDomain.class);
                    met.setAccessible(true);
                    try {
                        MethodHandle mmh = lk.unreflect(met);
                        rf = new $1(mmh);
                    } catch (Throwable t) {
                        rf = new $2(met);
                    }
                } catch (Throwable thr0) {
                    rf = null;
                }
            }
            ref = rf;
        }

        private static class $2 extends Reflection {
            private final Method met;

            $2(Method met) {
                this.met = met;
            }

            @Override
            protected Class<?> defineClass0(ClassLoader loader, String name, byte[] b, int off, int len, ProtectionDomain protectionDomain) throws ClassFormatError {
                try {
                    return (Class) met.invoke(loader, name, b, off, len, protectionDomain);
                } catch (IllegalAccessException e) {
                    ClassFormatError f = new ClassFormatError(e.toString());
                    f.addSuppressed(e);
                    throw f;
                } catch (InvocationTargetException e) {
                    Throwable own = e.getTargetException();
                    if (own == null) {
                        ClassFormatError f = new ClassFormatError(e.toString());
                        f.addSuppressed(e);
                        throw f;
                    }
                    if (own instanceof ClassFormatError) {
                        throw (ClassFormatError) own;
                    }
                    if (own instanceof RuntimeException) {
                        throw (RuntimeException) own;
                    }
                    if (own instanceof Error) {
                        throw (Error) own;
                    }
                    ClassFormatError f = new ClassFormatError(e.toString());
                    f.addSuppressed(own);
                    throw f;
                }
            }
        }

        private static class $1 extends Reflection {
            private final MethodHandle def;

            $1(MethodHandle def) {
                this.def = def;
            }

            @Override
            protected Class<?> defineClass0(ClassLoader loader, String name, byte[] b, int off, int len, ProtectionDomain protectionDomain) throws ClassFormatError {
                try {
                    return (Class) def.invoke(loader, name, b, off, len, protectionDomain);
                } catch (ClassFormatError cfe) {
                    throw cfe;
                } catch (Throwable throwable) {
                    ClassFormatError err = new ClassFormatError(throwable.toString());
                    err.addSuppressed(throwable);
                    throw err;
                }
            }
        }

        /**
         * 获取调用者的Class
         *
         * @return 获取调用者的Class
         * @throws ArrayIndexOutOfBoundsException 在程序main入口调用时抛出
         */
        @Contract(pure = true)
        public static Class<?> getCallerClass() {
            final Class[] ct = StackTrace.kit.ct();
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
            final Class[] ct = StackTrace.kit.ct();
            int at = point + 3;
            if (at < ct.length)
                return ct[at];
            return null;
        }
    }

    /**
     * 堆栈
     */
    public static class StackTrace {
        Class c;
        StackTraceElement elm;

        @Override
        @Contract(pure = true)
        public String toString() {
            return elm + "#" + c;
        }

        @Contract(pure = true)
        public Class getContent() {
            return c;
        }

        @Contract(pure = true)
        public StackTraceElement getStackTraceElement() {
            return elm;
        }

        static class $ToolKit extends SecurityManager {

            StackTrace[] classes() {
                Class[] classes = getClassContext();
                StackTraceElement[] elements = new Throwable().getStackTrace();
                int ln = Math.min(classes.length, elements.length);
                if (ln < 2) {
                    return new StackTrace[0];
                }
                ln -= 2;
                StackTrace[] traces = new StackTrace[ln];
                int off, off2, comp = classes.length - elements.length;
                if (comp > 0) {
                    off = comp + 2;
                    off2 = 2;
                } else if (comp < 0) {
                    off = 2;
                    off2 = elements.length - classes.length + 2;
                } else {
                    off = off2 = 2;
                }
                for (int i = 0; i < ln; i++) {
                    StackTrace st = new StackTrace();
                    st.c = classes[i + off];
                    st.elm = elements[i + off2];
                    traces[i] = st;
                }
                return traces;
            }

            Class[] ct() {
                return this.getClassContext();
            }
        }

        static final $ToolKit kit;

        static {
            MethodHandles.Lookup lk = Reflection.getRoot();
            SecurityManager old = System.getSecurityManager();
            $ToolKit tk;
            try {
                MethodHandle mh = lk.findStaticSetter(System.class, "security", SecurityManager.class);
                mh.invoke((Object) null);
                tk = new $ToolKit();
                mh.invoke(old);
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
            kit = tk;
        }

        /**
         * 获取当前线程堆栈
         *
         * @return 堆栈列表
         */
        @Contract(pure = true)
        public static StackTrace[] getStackTraces() {
            return kit.classes();
        }

        /**
         * 获取当前线程堆栈
         *
         * @return 堆栈列表(只有类)
         */
        @Contract(pure = true)
        public static Class[] getClassContext() {
            Class[] cc = kit.ct();
            if (cc.length < 2) return new Class[0];
            int x = cc.length - 2;
            Class[] nw = new Class[x];
            System.arraycopy(cc, 2, nw, 0, x);
            return nw;
        }
    }
}
