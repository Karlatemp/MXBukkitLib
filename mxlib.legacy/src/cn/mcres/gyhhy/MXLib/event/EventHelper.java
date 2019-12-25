/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: EventHelper.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.event;

import cn.mcres.gyhhy.MXLib.Core;
import cn.mcres.gyhhy.MXLib.log.BasicLogger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.HandlerList;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventException;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import cn.mcres.gyhhy.MXLib.ext.lookup.Looker;
import cn.mcres.gyhhy.MXLib.log.Logger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author 32798
 */
public class EventHelper {

    public static class EventCallException extends RuntimeException {

        public EventCallException(String s) {
            super(s);
        }

        public EventCallException(String a, Throwable c) {
            super(a, c);
        }
    }

    static Server s() {
        return Bukkit.getServer();
    }

    static final Looker lk = new Looker(Looker.openLookup(EventHelper.class, ~0));

    public static HandlerList getEventHandlerList(Class<?> c) {
        if (Event.class.isAssignableFrom(c)) {
            try {
                try {
                    return (HandlerList) c.getMethod("getHandlerList").invoke(null);
                } catch (Throwable thr) {
                }
                return (HandlerList) lk.getLookup().findStatic(c, "getHandlerList", MethodType.methodType(HandlerList.class)).invoke();
            } catch (Throwable ex) {
                return null;
            }
        }
        return null;
    }

    public static void registerListener(Listener listener, Plugin plugin) {
        Class<? extends Listener> cc = listener.getClass();
        Method[] mets = cc.getMethods();
//        Looker lk = new Looker(Looker.openLookup(EventHelper.class, ~0));
        for (Method met : mets) {
            int md = met.getModifiers();
            if (met.getParameterCount() == 1) {
                Class<?> tt = met.getParameterTypes()[0];
                if (Event.class.isAssignableFrom(tt)) {
                    EventHandler eh = met.getAnnotation(EventHandler.class);
                    if (eh != null) {
                        HandlerList ls;
                        try {
                            ls = (HandlerList) tt.getMethod("getHandlerList").invoke(null);
                        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            Core.getBL().printStackTrace(ex);
                            continue;
                        }
                        MethodHandle mh = lk.unreflect(met);
                        if (!Modifier.isStatic(md)) {
                            mh = mh.bindTo(listener);
                        }
                        final MethodHandle mh_ = mh;
                        ls.register(new RegisteredListener(listener, (l, e) -> {
                            if (tt.isInstance(e)) {
                                try {
                                    mh_.invoke(e);
                                } catch (OutOfMemoryError error) {
                                    throw error;
                                } catch (Throwable thr) {
                                    throw new EventException(thr, thr.getLocalizedMessage());
                                }
                            }
                        }, eh.priority(), plugin, eh.ignoreCancelled()));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        registerListener(new Listener() {
            @EventHandler
            public void a(NetURIConnectEvent fa) {
                System.out.println(this);
            }
        }, null);
    }

    public static void fireEvent(Event event) throws EventCallException {
        HandlerList handlers = event.getHandlers();
        RegisteredListener[] listeners = handlers.getRegisteredListeners();
        for (RegisteredListener registration : listeners) {
            if (registration.getPlugin().isEnabled()) {
                try {
                    registration.callEvent(event);
                } catch (AuthorNagException ex) {
                    Plugin plugin = registration.getPlugin();
                    if (plugin.isNaggable()) {
                        plugin.setNaggable(false);

                        throw new EventCallException(String.format(
                                "Nag author(s): '%s' of '%s' about the following: %s", new Object[]{
                                        plugin.getDescription().getAuthors(),
                                        plugin.getDescription().getFullName(),
                                        ex.getMessage()}));
                    }
                } catch (Throwable ex) {
                    throw new EventCallException("Could not pass event " + event.getEventName() + " to " + registration.getPlugin().getDescription().getFullName(), ex);
                }
            }
        }
    }

    public static <T extends Event> T plon(T ev) {
        return plon(ev, true);
    }

    public static <T extends Event> T plon(T ev, boolean usePM) {
//        ev.getHandlers().getRegisteredListeners()[0].
        if (usePM) {
            Server s = s();
            if (s != null) {
                PluginManager pm = s.getPluginManager();
                if (pm != null) {
                    pm.callEvent(ev);
                    return ev;
                }
            }
        }
        synchronized (EventHelper.class) {
            try {
                fireEvent(ev);
            } catch (EventCallException thr) {
                BasicLogger log = Core.getBL();
                log.error(thr.getLocalizedMessage());
                if (thr.getCause() != null) {
                    log.printStackTrace(thr.getCause());
                }
            }

        }
        return ev;
    }
}
