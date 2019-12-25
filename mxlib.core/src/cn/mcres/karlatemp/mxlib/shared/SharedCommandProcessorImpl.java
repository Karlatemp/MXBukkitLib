/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SharedCommandProcessorImpl.java@author: karlatemp@vip.qq.com: 2019/12/24 下午10:07@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.shared;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.annotations.CommandHandle;
import cn.mcres.karlatemp.mxlib.annotations.CommandTabHandle;
import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.bean.IInjector;
import cn.mcres.karlatemp.mxlib.cmd.ICommand;
import cn.mcres.karlatemp.mxlib.configuration.ICommandConfig;
import cn.mcres.karlatemp.mxlib.exceptions.ObjectCreateException;
import cn.mcres.karlatemp.mxlib.tools.IMemberScanner;
import cn.mcres.karlatemp.mxlib.tools.IObjectCreator;
import cn.mcres.karlatemp.mxlib.tools.Pointer;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

public class SharedCommandProcessorImpl extends SharedCommandProcessor {

    @Override
    public ICommand parse(@NotNull ICommandConfig config, @NotNull Class clazz) {
        IBeanManager ib = MXBukkitLib.getBeanManager();
        IMemberScanner scanner = ib.getBeanNonNull(IMemberScanner.class);
        final Collection<Method> command = scanner.getMethodByAnnotation(clazz, CommandHandle.class);
        final Collection<Method> tabl = scanner.getMethodByAnnotation(clazz, CommandTabHandle.class);
        if (command.isEmpty()) return null;
        Method t = null;
        if (tabl.size() > 0) {
            t = tabl.iterator().next();
        }
        return build(command.iterator().next(), t, clazz, config, ib);
    }

    private ICommand build(Method cmd, Method tab, Class clazz, ICommandConfig config, IBeanManager b) {
        String name = clazz.getSimpleName();

        String[] alias = new String[0];
        IMemberScanner scanner = b.getBeanNonNull(IMemberScanner.class);
        for (Field f : scanner.getAllField(clazz)) {
            if (f.getName().equalsIgnoreCase("$NAME")) {
                if (Modifier.isStatic(f.getModifiers())) {
                    try {
                        name = String.valueOf(f.get(null));
                    } catch (IllegalAccessException e) {
                    }
                    break;
                }
            }
        }
        for (Field f : scanner.getAllField(clazz)) {
            if (f.getName().equalsIgnoreCase("$ALIAS")) {
                if (Modifier.isStatic(f.getModifiers())) {
                    try {
                        Object got = f.get(null);
                        if (got instanceof String[]) alias = (String[]) got;
                        else if (got != null && got.getClass().isArray()) {
                            alias = new String[Array.getLength(got)];
                            for (int i = 0; i < alias.length; i++) {
                                alias[i] = String.valueOf(Array.get(got, i));
                            }
                        } else
                            alias = new String[]{String.valueOf(got)};
                    } catch (IllegalAccessException e) {
                    }
                    break;
                }
            }
        }
        Pointer p = new Pointer();
        return create(b, p, clazz, cmd, tab, name, alias);
    }

    protected ICommand create(IBeanManager b, Pointer p, Class clazz, Method cmd, Method tab, String name, String[] alias) {
        return b.getBeanNonNull(IInjector.class).inject(
                new SharedCommandMethodHandle(name, handle(cmd, p, clazz), handle(tab, p, clazz), alias)
        );
    }

    protected MethodHandle handle(Method met, Pointer p, Class clazz) {
        if (met == null) return null;
        IBeanManager ib = MXBukkitLib.getBeanManager();
        MethodHandles.Lookup lookup = ib.getBean(MethodHandles.Lookup.class);
        if (lookup == null) lookup = MethodHandles.lookup();
        try {
            MethodHandle mh = lookup.unreflect(met);
            if (!Modifier.isStatic(met.getModifiers())) {
                Object instance = p.value();
                if (instance == null) {
                    instance = p.value(
                            ib.getBeanNonNull(IObjectCreator.class)
                                    .newInstance(clazz));
                }
                mh = mh.bindTo(instance);
            }
            return mh;
        } catch (IllegalAccessException | ObjectCreateException e) {
            return null;
        }
    }
}
