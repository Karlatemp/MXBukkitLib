package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.bean.IInjector;
import cn.mcres.karlatemp.mxlib.exceptions.ObjectCreateException;
import cn.mcres.karlatemp.mxlib.tools.IMemberScanner;
import cn.mcres.karlatemp.mxlib.tools.IObjectCreator;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class SharedObjectCreator implements IObjectCreator {
    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public <T> T newInstance(@NotNull Class<T> clazz) throws ObjectCreateException {
        final Constructor<T>[] constructors = (Constructor<T>[]) clazz.getDeclaredConstructors();
        Exception last = null;
        for (Constructor<T> constructor : constructors) {
            constructor.setAccessible(true);
            try {
                if (constructor.getParameterCount() == 0) {
                    return inject(constructor.newInstance());
                } else {
                    Class<?>[] types = constructor.getParameterTypes();
                    Object[] params = new Object[types.length];
                    IBeanManager beans = MXBukkitLib.getBeanManager();
                    fill(types, params, beans);
                    return inject(constructor.newInstance(params));
                }
            } catch (Exception e) {
                last = e;
            }
        }
        if (last == null) throw new ObjectCreateException();
        throw new ObjectCreateException(last);
    }

    static void fill(Class<?>[] types, Object[] params, IBeanManager beans) {
        for (int i = 0; i < types.length; i++) {
            params[i] = beans.getBean(types[i]);
        }
    }

    private <T> T inject(T instance) throws InvocationTargetException, IllegalAccessException {
        IBeanManager beans = MXBukkitLib.getBeanManager();
        final IInjector injector = beans.getBean(IInjector.class);
        if (injector != null) {
            injector.inject(instance);
        }
        return instance;
    }
}
