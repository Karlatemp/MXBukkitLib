/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 32798
 */
public class RefUtil {

    @SuppressWarnings({"rawtypes"})
    public static final Class<?>[] emptyClassPar = new Class[0];
    public static final Object[] emptyArgPar = new Object[0];
    @SuppressWarnings({"rawtypes","BroadCatchBlock", "TooBroadCatch", "UseSpecificCatch", "AssignmentToMethodParameter"})
    private static Method getMethod(Class<?> clazz, String name, Class<?>[] par) {
        if (clazz != null) {
            if (par == null) {
                par = new Class[0];
            }
            Class<?> last = null;
            do {
                last = clazz;
                try {
                    Method fe = clazz.getDeclaredMethod(name, par);
                    if (fe != null) {
                        return fe;
                    }
                } catch (Exception ex) {
                }
                clazz = clazz.getSuperclass();
            } while (last != clazz && clazz != null);
        }
        return null;
    }

    @SuppressWarnings({"rawtypes"})
    public static <T> T ink(Object thiz, String name, Class[] par, Object[] obj) {
        return ink(thiz, null, name, par, obj);
    }

    @SuppressWarnings({"rawtypes"})
    public static <T> T ink(Class<?> cl, String name, Class[] par, Object[] obj) {
        return ink(null, cl, name, par, obj);
    }

    @SuppressWarnings({"rawtypes","unchecked","AssignmentToMethodParameter", "BroadCatchBlock", "TooBroadCatch", "UseSpecificCatch"})
    public static <T> T ink(Object thiz, Class<?> clazz, String name, Class[] par, Object[] obj) {
        if (clazz == null) {
            clazz = thiz.getClass();
        }
        if (par == null) {
            par = new Class[0];
        }
        if (obj == null) {
            obj = new Object[0];
        }
        if (par.length != obj.length) {
            throw new java.lang.IllegalArgumentException();
        }
        Method met = getMethod(clazz, name, par);
        boolean acc = met.isAccessible();
        try {
            met.setAccessible(true);
            return (T) met.invoke(thiz, obj);
        } catch (RuntimeException rt) {
            throw rt;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            met.setAccessible(acc);
        }
    }

    public static <T> T get(Object obj, String field) {
        if (obj == null) {
            return null;
        }
        return get(obj.getClass(), obj, field);
    }

    public static <T> T get(Class<?> clazz, String field) {
        return get(clazz, null, field);
    }

    public static <T> T set(Field fe, Object value) {
        return set(fe, (Object) null, value);
    }

    public static <T> T set(Object thiz, String field, Object value) {
        return set(thiz.getClass(), field, thiz, value);
    }

    public static <T> T set(Class<?> clazz, String field, Object value) {
        return set(clazz, field, null, value);
    }

    public static <T> T set(Class<?> clazz, String field, Object thiz, Object value) {
        return set(getField(clazz, field), thiz, value);
    }

    @SuppressWarnings({"rawtypes","AssignmentToMethodParameter", "UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch", "CallToPrintStackTrace"})
    public static <T> T set(Field fe, Object thiz, Object value) {
        T old = get(fe, thiz);
        boolean acc = fe.isAccessible();
        fe.setAccessible(true);
        try {
            int mod = fe.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
                thiz = fe.getDeclaringClass();
                Field mi = getField(Field.class, "modifiers");
                mi.setAccessible(true);
                mi.setInt(fe, Modifier.STATIC);

                Class[] var0 = new Class[]{Object.class};
                Object[] var1 = new Object[1];
                Object var2 = RefUtil.ink(fe, "getFieldAccessor", var0, var1);
                Object var3 = RefUtil.ink(fe, "getFieldAccessor", var0, var1);
                if (var2 == var3) {
                    Field field = getField(var3.getClass(), "isReadOnly");
                    if (field != null) {
                        set(field, var3, false);
                    }
                    set(var3, "isFinal", false);
                }

                Class<?> tp = fe.getType();
                if (tp == int.class) {
                    fe.setInt(thiz, (int) value);
                } else if (tp == double.class) {
                    fe.setDouble(thiz, (double) value);
                } else if (tp == boolean.class) {
                    fe.setBoolean(thiz, (boolean) value);
                } else if (tp == short.class) {
                    fe.setShort(thiz, (short) value);
                } else if (tp == byte.class) {
                    fe.setByte(thiz, (byte) value);
                } else if (tp == float.class) {
                    fe.setFloat(thiz, (float) value);
                } else if (tp == long.class) {
                    fe.setLong(thiz, (long) value);
                } else {
                    fe.set(thiz, value);
                }
                mi.setInt(fe, mod);
                mi.setAccessible(false);
            } else {
                Class<?> tp = fe.getType();
                if (tp == int.class) {
                    fe.setInt(thiz, (int) value);
                } else if (tp == double.class) {
                    fe.setDouble(thiz, (double) value);
                } else if (tp == boolean.class) {
                    fe.setBoolean(thiz, (boolean) value);
                } else if (tp == short.class) {
                    fe.setShort(thiz, (short) value);
                } else if (tp == byte.class) {
                    fe.setByte(thiz, (byte) value);
                } else if (tp == float.class) {
                    fe.setFloat(thiz, (float) value);
                } else if (tp == long.class) {
                    fe.setLong(thiz, (long) value);
                } else {
                    fe.set(thiz, value);
                }
            }
        } catch (Throwable thr) {
            thr.printStackTrace();
        } finally {
            fe.setAccessible(acc);
        }
        return old;
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Field fe, Object o) {
        if (fe == null) {
            return null;
        }
        synchronized (fe) {
            boolean acc = fe.isAccessible();
            try {
                fe.setAccessible(true);
                return (T) fe.get(o);
            } catch (Throwable thr) {
            } finally {
                fe.setAccessible(acc);
            }
        }
        return null;
    }

    public static <T> T get(Class<?> clazz, Object o, String field) {
        if (field == null) {
            return null;
        }
        return get(getField(clazz, field), o);
    }

    public static Field getField(Class<?> clazz, String field) {
        return getField(clazz, field, true);
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch", "UseSpecificCatch", "AssignmentToMethodParameter"})
    public static Field getField(Class<?> clazz, String field, boolean checkPublic) {
        if (checkPublic) {
            try {
                Field f = clazz.getField(field);
                if (f != null) {
                    return f;
                }
            } catch (Exception ex) {
            }
        }
        Class<?> last = null;
        do {
            last = clazz;
            try {
                Field fe = clazz.getDeclaredField(field);
                if (fe != null) {
                    return fe;
                }
            } catch (Exception ex) {
            }
            clazz = clazz.getSuperclass();
        } while (last != clazz && clazz != null);
        return null;
    }

    private RefUtil() {
    }
}
