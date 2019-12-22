/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SharedConfigurationProcessor.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.annotations.AutoInstall;
import cn.mcres.karlatemp.mxlib.annotations.Bean;
import cn.mcres.karlatemp.mxlib.annotations.Configuration;
import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.bean.IInjector;
import cn.mcres.karlatemp.mxlib.configuration.ConfigurationProcessorPostLoadingMatcher;
import cn.mcres.karlatemp.mxlib.configuration.IConfigurationProcessor;
import cn.mcres.karlatemp.mxlib.exceptions.ScanException;
import cn.mcres.karlatemp.mxlib.tools.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

public class SharedConfigurationProcessor implements IConfigurationProcessor {
    public static final boolean DEBUG = System.getProperty("mxlib.debug") != null;
    private final ConfigurationProcessorPostLoadingMatcher matcher = new ConfigurationProcessorPostLoadingMatcher();
    private static Collection<String> loadedConfigurations = new ConcurrentLinkedQueue<>();

    @NotNull
    @Override
    public ConfigurationProcessorPostLoadingMatcher getMatcher() {
        return matcher;
    }

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

    @SuppressWarnings("deprecation")
    protected void load(Class boot, ClassLoader loader,
                        List<String> classes, RuntimeException re,
                        IBeanManager beans) {
        for (String c : classes) {
            if (c.endsWith("AutoConfig") && !MXBukkitLib.disableConfigurations.contains(c) && !loadedConfigurations.contains(c)) {
                loadedConfigurations.add(c);
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
                } catch (Throwable e) {
                    re = a(re, e);
                }
            }
        }
        re = post_load(boot, loader, classes, re, beans);
        if (re != null) throw re;
    }

    @Override
    public void load(List<String> classes) {
        load(Toolkit.Reflection.getCallerClass(), Toolkit.Reflection.getClassLoader(Toolkit.Reflection.getCallerClass()), classes, null, MXBukkitLib.getBeanManager());
    }

    @Override
    public void load(Class boot) {
        ClassLoader loader = Toolkit.Reflection.getClassLoader(boot);
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

    static final Function<ClassFile, Annotation> GET_DEPEND = f -> {
        if (f != null) {
            final List<AttributeInfo> attributes = f.getAttributes();
            if (attributes != null) {
                for (AttributeInfo inf : attributes) {
                    if (inf instanceof AnnotationsAttribute) {
                        Annotation[] as = ((AnnotationsAttribute) inf).getAnnotations();
                        if (as != null) {
                            for (Annotation a : as) {
                                if (A.c(a.getTypeName(), A.Depend)) {
                                    return a;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    };
    static final ConfigurationProcessorPostLoadingMatcher V3;

    static boolean testDepend(Annotation a) {
        return MXBukkitLib.getBeanManager().getOptional(DependChecker.class)
                .map(c -> c.isLoaded(DependChecker.fromAnnotation(a))).orElse(true);
    }

    static {
        ConfigurationProcessorPostLoadingMatcher a = new ConfigurationProcessorPostLoadingMatcher();
        V3 = a;
        {
            // Unmark @Resource, Unmark @AutoInstall, No Load.
            // Marked @Resource/@AutoInstall But Depend No Match. No Load
            // @Resource is for field.
            // @AutoInstall is for Class.
            ConfigurationProcessorPostLoadingMatcher b = new ConfigurationProcessorPostLoadingMatcher();
            // Un mark @Resource, and then do not load the class to JVM
            b.getAll().add(cf -> {
                final List<FieldInfo> fields = cf.getFields();
                if (fields != null)
                    for (FieldInfo f : fields) {
                        // We only test static field for @Resource
                        if (!Modifier.isStatic(f.getAccessFlags())) continue;
                        final List<AttributeInfo> attributes = f.getAttributes();
                        if (attributes == null) continue;

                        for (AttributeInfo inf : attributes) {
                            if (inf instanceof AnnotationsAttribute) {
                                for (Annotation aw : ((AnnotationsAttribute) inf).getAnnotations()) {
                                    if (A.c(aw.getTypeName(), A.Resource)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                // If not marked @Resource. Then check does it marked @AutoInstall
                final List<AttributeInfo> attributes = cf.getAttributes();
                if (attributes != null) {
                    for (AttributeInfo ai : attributes) {
                        if (ai instanceof AnnotationsAttribute) {
                            for (Annotation ann : ((AnnotationsAttribute) ai).getAnnotations()) {
                                if (A.c(ann.getTypeName(), A.AutoInstall)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }); // Hey. Un mark @Resource. No Load.
            b.getAll().add(f -> {
                Annotation dep = GET_DEPEND.apply(f);
                if (dep == null) return true;// Un mark @Depend. Skip match.
                return testDepend(dep);
            });

            a.getAny().add(b.asMatchRule());
        }
        {// If marked Depend. And will load it.
            a.getAny().add(f -> {
                Annotation d = GET_DEPEND.apply(f);
                if (d == null) return false;// Un marked. Skip this class.
                return testDepend(d);
            });
        }
    }

    {
        {
            matcher.getAny().add(V3.asMatchRule());
        }
    }

    protected RuntimeException post_load(Class boot,
                                         ClassLoader loader, List<String> classes,
                                         RuntimeException errors, IBeanManager beans) {
        MXBukkitLib.debug("[SharedConfigurationProcessor] Processing PostLoad");
        IInjector injector = beans.getBean(IInjector.class);
        // We using a loader set to find the bytecode.
        ClassResourceLoader bytecode_loader = beans.getBean(ClassResourceLoaders.class);
        ServiceInstaller si = beans.getBean(ServiceInstallers.class);
        if (bytecode_loader == null) {
            MXBukkitLib.getLogger().error("[SharedConfigurationProcessor] [PostLoad] Cannot found ClassResourceLoader!");
        }
        if (injector != null) {
            for (String cn : classes) {
                if (bytecode_loader != null) {
                    final byte[] found = bytecode_loader.found(cn, loader, beans);
                    if (found == null) {
                        // If cannot get the bytecode of this class.
                        // Force load this class.
                        // Doing so will cause @Depend to be invalid
                        MXBukkitLib.getLogger().error("[SharedConfigurationProcessor] [PostLoad] Cannot found byte code of class: " + cn);
                    } else {
                        try {
                            ClassFile cf = new ClassFile(new DataInputStream(new ByteArrayInputStream(found)));
                            if (!matcher.match(cf)) continue;
                        } catch (Throwable ignore) {
                        }
                    }
                }
                try {
                    Class<?> c = Class.forName(cn, true, loader);
                    injector.inject(c);
                    if (si != null)
                        if (c.getDeclaredAnnotation(AutoInstall.class) != null)
                            si.install(c);
                } catch (Throwable ignore) {
                }
            }
        }
        return errors;
    }
}
