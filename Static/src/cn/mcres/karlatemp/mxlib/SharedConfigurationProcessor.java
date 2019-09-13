package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.annotations.Bean;
import cn.mcres.karlatemp.mxlib.annotations.Configuration;
import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.bean.IInjector;
import cn.mcres.karlatemp.mxlib.configuration.IConfigurationProcessor;
import cn.mcres.karlatemp.mxlib.exceptions.ScanException;
import cn.mcres.karlatemp.mxlib.tools.IClassScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SharedConfigurationProcessor implements IConfigurationProcessor {
    public static final boolean DEBUG = System.getProperty("mxlib.debug") != null;

    protected static RuntimeException a(RuntimeException re, Throwable thr) {
        if (re == null) {
            if (thr instanceof RuntimeException) {
                return (RuntimeException) thr;
            } else {
                return new RuntimeException(thr);
            }
        }
        re.addSuppressed(thr);
        return re;
    }

    protected void load(Class boot, ClassLoader loader,
                        List<String> classes, RuntimeException re,
                        IBeanManager beans) {
        for (String c : classes) {
            if (c.endsWith("AutoConfig")) {
                try {
                    Class<?> conf = Class.forName(c, true, loader);
                    Configuration co = conf.getAnnotation(Configuration.class);
                    if (co == null) continue;
                    Object instance = null;
                    if (DEBUG)
                        MXBukkitLib.getLogger().printf("[ConfigurationProcessor] Loading Configuration from " + conf);
                    for (Method method : MXLib.getMethods(conf)) {
                        Bean b = method.getAnnotation(Bean.class);
                        if (b != null) {
                            if (DEBUG)
                                MXBukkitLib.getLogger().printf("[ConfigurationProcessor] \tInvoking " + method);
                            Object[] params = new Object[method.getParameterCount()];
                            int i = 0;
                            for (Class m : method.getParameterTypes()) {
                                params[i++] = beans.getBean(m);
                            }
                            int modi = method.getModifiers();
                            Class type = method.getReturnType();
                            try {
                                if (Modifier.isStatic(modi)) {
                                    Object invoke = method.invoke(null, params);
                                    if (invoke != null)
                                        beans.addBean(type, invoke);
                                } else {
                                    if (instance == null) {
                                        try {
                                            final Constructor<?> constructor = conf.getDeclaredConstructor();
                                            constructor.setAccessible(true);
                                            instance = constructor.newInstance();
                                        } catch (NoSuchMethodException | InstantiationException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    Object invoke = method.invoke(instance, params);
                                    if (invoke != null)
                                        beans.addBean(type, invoke);
                                }
                            } catch (InvocationTargetException | IllegalAccessException err) {
                                re = a(re, err);
                            }
                        }
                    }
                } catch (ClassNotFoundException | NoClassDefFoundError e) {
                    //e.printStackTrace();
                }
            }
        }
        re = post_load(boot, loader, classes, re, beans);
        if (re != null) throw re;
    }

    @Override
    public void load(Class boot) {
        ClassLoader loader = boot.getClassLoader();
        if (loader == null) return;// Cannot load configs from `rt.jar`
        IBeanManager beans = MXBukkitLib.getBeanManager();
        if (beans == null) return;
        IClassScanner scanner = beans.getBean(IClassScanner.class);
        ArrayList<String> list = new ArrayList<>();
        RuntimeException re = null;
        try {
            scanner.scan(boot, list);
        } catch (ScanException e) {
            re = a(null, e);
        }
        load(boot, loader, list, re, beans);
    }

    @SuppressWarnings("WeakerAccess")
    protected RuntimeException post_load(Class boot,
                                         ClassLoader loader, List<String> classes,
                                         RuntimeException errors, IBeanManager beans) {
        IInjector injector = beans.getBean(IInjector.class);
        if (injector != null) {
            for (String cn : classes) {
                try {
                    Class<?> c = Class.forName(cn, true, loader);
                    injector.inject(c);
                } catch (Throwable thr) {
                }
            }
        }
        return errors;
    }
}
