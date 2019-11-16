/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: EventTools.java@author: karlatemp@vip.qq.com: 19-11-16 下午7:28@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.event;

import cn.mcres.karlatemp.mxlib.reflect.RField;
import cn.mcres.karlatemp.mxlib.reflect.Reflect;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public final class EventTools {

    @SuppressWarnings("unchecked")
    public static <T extends Event, W extends Event> HandlerList<T> getHandler(@NotNull Class<W> clazz) {
        return Optional.ofNullable(Reflect.ofClass(clazz).getField("handlers", HandlerList.class))
                .filter(RField::isStatic).map(RField::get).orElse(null);
    }

    @SuppressWarnings("unchecked")
    public static boolean registerListener(Object instance, @NotNull Method method, @NotNull HandlerList<?> handlers) {
        if (instance != null) {
            method.getDeclaringClass().cast(instance);
        }
        if (method.getParameterCount() == 1) {
            Class<?> rg = method.getParameterTypes()[0];
            if (Event.class.isAssignableFrom(rg))
                handlers.register(createEventHandler(method, (Class) rg.asSubclass(Event.class), instance));
        }
        return false;

    }

    public static <T extends Event> EventHandler<T> createEventHandler(
            @NotNull Method method, @NotNull Class<T> clazz, Object instance) {
        return event -> {
            if (clazz.isInstance(event)) {
                try {
                    method.invoke(instance, event);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e.getTargetException());
                }
            }
        };
    }

    public static boolean registerListener(Object instance, @NotNull Method method) {
        if (instance != null) {
            method.getDeclaringClass().cast(instance);
        }
        if (method.getParameterCount() == 1) {
            Class<?> rg = method.getParameterTypes()[0];
            if (Event.class.isAssignableFrom(rg))
                getHandler(rg.asSubclass(Event.class)).register(createEventHandler(method, rg.asSubclass(Event.class), instance));
        }
        return false;
    }
}
