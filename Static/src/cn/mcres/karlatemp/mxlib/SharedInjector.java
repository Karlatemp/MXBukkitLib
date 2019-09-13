package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.annotations.Environment;
import cn.mcres.karlatemp.mxlib.annotations.Resource;
import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.bean.IEnvironmentFactory;
import cn.mcres.karlatemp.mxlib.bean.IInjector;
import cn.mcres.karlatemp.mxlib.tools.IMemberScanner;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class SharedInjector implements IInjector {
    @Override
    public <T> T inject(@NotNull T obj) {
        final IBeanManager manager = MXBukkitLib.getBeanManager();
        if (manager != null) {
            final Class<?> clazz = obj.getClass();
            inject(clazz, obj, manager);
            callInited(manager, clazz, obj);
        }
        return obj;
    }

    private void callInited(final IBeanManager manager, Class<?> clazz, Object obj) {
        final IMemberScanner s = manager.getBean(IMemberScanner.class);
        if (s != null) {
            boolean smode = obj == null;
            for (Method met : s.getAllMethod(clazz)) {
                if (met.getName().equals("$init")) {
                    if (Modifier.isStatic(met.getModifiers()) != smode) continue;
                    Class<?>[] types = met.getParameterTypes();
                    Object[] params = new Object[types.length];
                    SharedObjectCreator.fill(types, params, manager);
                    try {
                        met.invoke(obj, params);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public <T> void inject(@NotNull Class<T> clazz) {
        final IBeanManager manager = MXBukkitLib.getBeanManager();
        if (manager != null) {
            inject(clazz, null, manager);
            callInited(manager, clazz, null);
        }
    }

    private <T> void inject(Class<?> clazz, T obj, IBeanManager manager) {
        IEnvironmentFactory env = manager.getBean(IEnvironmentFactory.class);
        if (clazz == null) return;
        if (clazz.isArray()) throw new ClassCastException("Cannot inject a array class.");
        if (clazz.getName().startsWith("java.")) return;
        boolean st = obj == null;
        for (Field field : clazz.getDeclaredFields()) {
            //System.out.println("CHECK " + field);
            int m = field.getModifiers();
            if (Modifier.isFinal(m)) {
                //System.out.println("RETURN FINAL " + field);
                continue;
            }
            if (st != Modifier.isStatic(m)) {
                //System.out.println("RETURN " + field + ", " + st + ", " + Modifier.isStatic(m));
                continue;
            }
            final Resource resource = field.getAnnotation(Resource.class);
            //System.out.println(field + " #" + resource);
            if (resource != null) {
                Class<?> c = resource.value();
                Class<?> type = field.getType();
                if (c == Object.class) c = type;
                else if (type.isAssignableFrom(c)) {
                    throw new ClassCastException(c + " cannot cast to " + type);
                }
                Object data = manager.getBean(c);
                field.setAccessible(true);
                try {
                    field.set(obj, data);
                    //System.out.println("SET " + field + " to " + data);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        inject(clazz.getSuperclass(), obj, manager);
    }
}
