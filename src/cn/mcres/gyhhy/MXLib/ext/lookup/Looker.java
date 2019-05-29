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
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author 32798
 */
public class Looker {

    final static MethodHandle lookup_create, lc2;

    static {
        MethodHandle mh = null, m2 = null;
        try {
            Constructor<MethodHandles.Lookup> ct = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
            ct.setAccessible(true);
            MethodHandles.Lookup lk = ct.newInstance(MethodHandles.Lookup.class, ~0);
            ct.setAccessible(false);
            mh = lk.findConstructor(MethodHandles.Lookup.class, MethodType.methodType(void.class, Class.class));
            m2 = lk.findConstructor(MethodHandles.Lookup.class, MethodType.methodType(void.class, Class.class, int.class));
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {

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
        return null;
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
}
