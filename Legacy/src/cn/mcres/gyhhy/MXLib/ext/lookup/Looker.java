/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.ext.lookup;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import cn.mcres.gyhhy.MXLib.Helper;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 32798
 */
public class Looker {

    final static MethodHandle lookup_create, lc2;

    private static MethodHandles.Lookup getIMPL() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field f = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
        f.setAccessible(true);
        return (MethodHandles.Lookup) f.get(null);
    }
    static MethodHandles.Lookup IMPL;

    static {
        MethodHandle mh = null, m2 = null;
        try {
            Constructor<MethodHandles.Lookup> ct = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
            ct.setAccessible(true);
            MethodHandles.Lookup lk = ct.newInstance(MethodHandles.Lookup.class, ~0);
            ct.setAccessible(false);
            mh = lk.findConstructor(MethodHandles.Lookup.class, MethodType.methodType(void.class, Class.class));
            m2 = lk.findConstructor(MethodHandles.Lookup.class, MethodType.methodType(void.class, Class.class, int.class));
        } catch (Throwable ex) {
            try {
                MethodHandles.Lookup lk = getIMPL();
                IMPL = lk;
                mh = lk.findConstructor(MethodHandles.Lookup.class, MethodType.methodType(void.class, Class.class));
            } catch (Throwable exw) {
                ex.printStackTrace();
            }
        }
        lookup_create = mh;
        lc2 = m2;
    }

    public static MethodHandles.Lookup openLookup(Class<?> looker, int mode) {
        if (lc2 != null) {
            try {
                return (MethodHandles.Lookup) lc2.invoke(looker, mode);
            } catch (Error | RuntimeException e) {
                throw e;
            } catch (Throwable ex) {
                throw new RuntimeException(ex.getLocalizedMessage(), ex);
            }
        }
        if (mode == ~0 && IMPL != null) {
            return IMPL;
        }
        throw new RuntimeException("No constructor.");
    }

    public static MethodHandles.Lookup openLookup(Class<?> looker) {
        if (lookup_create != null) {
            try {
                return (MethodHandles.Lookup) lookup_create.invokeExact(looker);
            } catch (Error | RuntimeException e) {
                throw e;
            } catch (Throwable ex) {
                throw new RuntimeException(ex.getLocalizedMessage(), ex);
            }
        }
        return null;
    }
    private final MethodHandles.Lookup lk;

    public Looker(MethodHandles.Lookup lk) {
        this.lk = lk;
    }

    public Looker(Class<?> c) {
        this(openLookup(c));
    }

    public Looker(Class<?> c, int opt) {
        this(openLookup(c, opt));
    }

    public MethodHandles.Lookup getLookup() {
        return lk;
    }

    public Class<?> lookupClass() {
        return lk.lookupClass();
    }

    public int lookupModes() {
        return lk.lookupModes();
    }

    public Looker in(Class<?> requestedLookupClass) {
        return new Looker(lk.in(requestedLookupClass));
    }

    public String toString() {
        return lk.toString();
    }

    public MethodHandle findStatic(Class<?> refc, String name, MethodType type) {
        try {
            return lk.findStatic(refc, name, type);
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            return Helper.thr(ex);
        }
    }

    public MethodHandle findVirtual(Class<?> refc, String name, MethodType type) {
        try {
            return lk.findVirtual(refc, name, type);
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            return Helper.thr(ex);
        }
    }

    public MethodHandle findSpecial(Class<?> refc, String name, MethodType type,
            Class<?> specialCaller) {
        try {
            return lk.findSpecial(refc, name, type, specialCaller);
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            return Helper.thr(ex);
        }
    }

    public MethodHandle findGetter(Class<?> refc, String name, Class<?> type) {
        try {
            return lk.findGetter(refc, name, type);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            return Helper.thr(ex);
        }
    }

    public MethodHandle findSetter(Class<?> refc, String name, Class<?> type) {
        try {
            return lk.findSetter(refc, name, type);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            return Helper.thr(ex);
        }
    }

    public MethodHandle findStaticGetter(Class<?> refc, String name, Class<?> type) {
        try {
            return lk.findStaticGetter(refc, name, type);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            return Helper.thr(ex);
        }
    }

    public MethodHandle findStaticSetter(Class<?> refc, String name, Class<?> type) {
        try {
            return lk.findStaticSetter(refc, name, type);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            return Helper.thr(ex);
        }
    }

    public MethodHandle unreflect(Method m) {
        try {
            return lk.unreflect(m);
        } catch (IllegalAccessException ex) {
            return Helper.thr(ex);
        }
    }

    public MethodHandle unreflectSpecial(Method m, Class<?> specialCaller) {
        try {
            return lk.unreflectSpecial(m, specialCaller);
        } catch (IllegalAccessException ex) {
            return Helper.thr(ex);
        }
    }

    public MethodHandle unreflectConstructor(Constructor<?> c) {
        try {
            return lk.unreflectConstructor(c);
        } catch (IllegalAccessException ex) {
            return Helper.thr(ex);
        }
    }

    public MethodHandle unreflectGetter(Field f) {
        try {
            return lk.unreflectGetter(f);
        } catch (IllegalAccessException ex) {
            return Helper.thr(ex);
        }
    }

    public MethodHandle unreflectSetter(Field f) {
        try {
            return lk.unreflectSetter(f);
        } catch (IllegalAccessException ex) {
            return Helper.thr(ex);
        }
    }

    public MethodHandleInfo revealDirect(MethodHandle target) {
        return lk.revealDirect(target);
    }

    public MethodHandle bind(Object receiver, String name, MethodType type) {
        try {
            return lk.bind(receiver, name, type);
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            return Helper.thr(ex);
        }
    }

    public MethodHandle findConstructor(Class<?> refc, MethodType type) {
        try {
            return lk.findConstructor(refc, type);
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            return Helper.thr(ex);
        }
    }

}
