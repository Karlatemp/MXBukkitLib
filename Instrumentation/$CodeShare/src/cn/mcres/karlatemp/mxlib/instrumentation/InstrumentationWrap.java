/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: InstrumentationWrap.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.instrumentation;

import org.jetbrains.annotations.NotNull;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Array;
import java.security.AccessControlException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class InstrumentationWrap extends HashMap/*Support Data store*/ implements Instrumentation, BiConsumer<String[], Object>/*Commands*/ {
    protected static List<Object[]> requests;
    protected final Instrumentation p;
    protected static Logger LOGGER;

    protected final Set<ClassFileTransformer> transformers = new PointerSet<>(),
            UNMODIFY = Collections.unmodifiableSet(transformers);
    protected final Set<ObjIntConsumer<Object>> listeners = new PointerSet<>(),
            UN_MODIFIABLE_LISTENERS = Collections.unmodifiableSet(listeners);

    @Override
    public void addTransformer(ClassFileTransformer transformer, boolean canRetransform) {
        for (ObjIntConsumer<Object> consumer : listeners) {
            consumer.accept(new Object[]{transformer, canRetransform}, 0);
        }
        p.addTransformer(transformer, canRetransform);
        transformers.add(transformer);
    }

    @Override
    public void addTransformer(ClassFileTransformer transformer) {
        for (ObjIntConsumer<Object> consumer : listeners) {
            consumer.accept(transformer, 1);
        }
        p.addTransformer(transformer);
        transformers.add(transformer);
    }

    @Override
    public boolean removeTransformer(ClassFileTransformer transformer) {
        for (ObjIntConsumer<Object> consumer : listeners) {
            consumer.accept(transformer, 2);
        }
        if (p.removeTransformer(transformer)) {
            transformers.remove(transformer);
            return true;
        }
        return false;
    }

    @Override
    public boolean isRetransformClassesSupported() {
        return p.isRetransformClassesSupported();
    }

    @Override
    public void retransformClasses(Class<?>... classes) throws UnmodifiableClassException {
        for (ObjIntConsumer<Object> consumer : listeners) {
            consumer.accept(classes, 3);
        }
        p.retransformClasses(NoNull(classes));
    }

    private <T> T[] NoNull(T[] arr) {
        if (arr == null) {
            return null;
        }
        if (arr.length == 0) return arr;
        int len = arr.length, end = arr.length;
        for (int i = 0; i < end; i++) {
            T t = arr[i];
            if (t == null) {
                len--;
                end--;
                System.arraycopy(arr, i + 1, arr, i, len - i - 1);
                i--;
            }
        }
        if (len != arr.length) {
            //noinspection unchecked
            T[] nw = (T[]) Array.newInstance(arr.getClass().getComponentType(), len);
            System.arraycopy(arr, 0, nw, 0, len);
            return nw;
        }
        return arr;
    }

    @Override
    public boolean isRedefineClassesSupported() {
        return p.isRedefineClassesSupported();
    }

    @Override
    public void redefineClasses(ClassDefinition... definitions) throws ClassNotFoundException, UnmodifiableClassException {
        for (ObjIntConsumer<Object> consumer : listeners) {
            consumer.accept(definitions, 4);
        }
        p.redefineClasses(NoNull(definitions));
    }

    @Override
    public boolean isModifiableClass(Class<?> theClass) {
        return p.isModifiableClass(theClass);
    }

    @Override
    public Class[] getAllLoadedClasses() {
        for (ObjIntConsumer<Object> consumer : listeners) {
            consumer.accept(null, 5); // Pre Post
        }
        Class[] classes = p.getAllLoadedClasses();
        for (ObjIntConsumer<Object> consumer : listeners) {
            consumer.accept(classes, 5); // Post Filters
        }
        return NoNull(classes);
    }

    @Override
    public Class[] getInitiatedClasses(ClassLoader loader) {
        for (ObjIntConsumer<Object> consumer : listeners) {
            consumer.accept(loader, 6); // Pre Post
        }
        Class[] classes = p.getInitiatedClasses(loader);
        for (ObjIntConsumer<Object> consumer : listeners) {
            consumer.accept(classes, 6); // Post Filters
        }
        return NoNull(classes);
    }

    @Override
    public long getObjectSize(Object objectToSize) {
        return p.getObjectSize(objectToSize);
    }

    @Override
    public void appendToBootstrapClassLoaderSearch(JarFile jarfile) {
        for (ObjIntConsumer<Object> consumer : listeners) {
            consumer.accept(jarfile, 7);
        }
        p.appendToBootstrapClassLoaderSearch(jarfile);
    }

    @Override
    public void appendToSystemClassLoaderSearch(JarFile jarfile) {
        for (ObjIntConsumer<Object> consumer : listeners) {
            consumer.accept(jarfile, 8);
        }
        p.appendToSystemClassLoaderSearch(jarfile);
    }

    @Override
    public boolean isNativeMethodPrefixSupported() {
        return p.isNativeMethodPrefixSupported();
    }

    @Override
    public void setNativeMethodPrefix(ClassFileTransformer transformer, String prefix) {
        for (ObjIntConsumer<Object> consumer : listeners) {
            consumer.accept(new Object[]{transformer, prefix}, 9);
        }
        p.setNativeMethodPrefix(transformer, prefix);
    }

    InstrumentationWrap(Instrumentation p) {
        this.p = p;
    }

    @Override
    public void accept(@NotNull String[] args, Object o) {
        LOGGER.fine(() ->
                "Warp Invoking command: " + Arrays.toString(args) + ", " + o
        );
        if (args.length < 2) return;
        @NotNull
        String pwd = args[0];
        {
            // Check pwd
            ReentrantLock lk = new ReentrantLock();
            lk.lock();
            try {
                final Condition condition = lk.newCondition();
                Object[] req = new Object[]{pwd, lk, condition};
                requests.add(req);
                condition.await(3, TimeUnit.SECONDS);
                if (req[0] != this) {
                    throw new AccessControlException("Wrong Token/Error Instrumentation Instance");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lk.unlock();
            }
        }
        @NotNull
        String s = args[1];
        switch (s) {
            case "unregister": {
                synchronized (transformers) {
                    for (ClassFileTransformer transformer : transformers) {
                        if (transformer != null) {
                            p.removeTransformer(transformer);
                        }
                    }
                    transformers.clear();
                }
                break;
            }
            case "transformers": {
                //noinspection unchecked
                ((Consumer) o).accept(UNMODIFY);
                break;
            }
            case "events.register": {
                //noinspection unchecked
                listeners.add((ObjIntConsumer<Object>) o);
                break;
            }
            case "events.unregister": {
                //noinspection RedundantClassCall
                listeners.remove(ObjIntConsumer.class.cast(o));
                break;
            }
            default: {
                throw new ClassCastException("Unknown command: " + s);
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(p.hashCode()) + "#Power by Karlatemp";
    }

}
