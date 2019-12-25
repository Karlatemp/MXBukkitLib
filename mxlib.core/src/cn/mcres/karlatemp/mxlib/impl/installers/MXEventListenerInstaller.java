/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MXEventListenerInstaller.java@author: karlatemp@vip.qq.com: 19-11-16 下午7:19@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl.installers;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.event.*;
import cn.mcres.karlatemp.mxlib.exceptions.ObjectCreateException;
import cn.mcres.karlatemp.mxlib.tools.IMemberScanner;
import cn.mcres.karlatemp.mxlib.tools.IObjectCreator;
import cn.mcres.karlatemp.mxlib.tools.ServiceInstaller;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class MXEventListenerInstaller implements ServiceInstaller {
    @Override
    public boolean install(@NotNull Class<?> clazz) {
        if (MXEventListener.class.isAssignableFrom(clazz)) {
            final List<Method> methods = MXBukkitLib.getBeanManager().getBeanNonNull(IMemberScanner.class).getAllMethod(clazz).stream()
                    .filter(w -> w.getParameterCount() == 1)
                    .filter(w -> Event.class.isAssignableFrom(w.getParameterTypes()[0]))
                    .filter(w -> w.isAnnotationPresent(MXEventHandler.class)).collect(Collectors.toList());
            if (methods.isEmpty()) {
                return false;
            }
            if (methods.stream().allMatch(m -> Modifier.isStatic(m.getModifiers()))) {
                for (Method m : methods) {
                    EventTools.registerListener(null, m);
                }
            } else {
                Object instance;
                try {
                    instance = MXBukkitLib.getBeanManager().getBeanNonNull(IObjectCreator.class).newInstance(clazz);
                } catch (ObjectCreateException e) {
                    return ThrowHelper.thrown(e);
                }
                for (Method m : methods) {
                    EventTools.registerListener(instance, m);
                }
            }
            return true;
        }
        return false;
    }
}
