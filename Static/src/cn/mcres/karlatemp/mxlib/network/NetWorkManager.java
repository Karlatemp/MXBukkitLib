/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NetWorkManager.java@author: karlatemp@vip.qq.com: 19-9-13 下午12:05@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.network;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.*;
import java.lang.ref.Reference;

public class NetWorkManager {
    private static final List<Reference<NetWorkListener>> listeners = new ArrayList<>();

    public static boolean containsListener(@NotNull NetWorkListener listener) {
        synchronized (listeners) {
            for (Reference<NetWorkListener> ref : listeners) {
                if (ref != null) {
                    if (!ref.isEnqueued()) {
                        return ref.get() == listener;
                    }
                }
            }
        }
        return false;
    }

    public static void registerListener(@NotNull Reference<NetWorkListener> listener) {
        synchronized (listeners) {
            MXBukkitLib.debug(() -> "[NetWorkManager] Registered NetWorkListener: " + listener);
            listeners.add(listener);
        }
    }

    public static Reference<NetWorkListener> registerListener(@NotNull NetWorkListener listener) {
        Reference<NetWorkListener> ref = new WeakReference<>(listener);
        registerListener(ref);
        return ref;
    }

    interface PostCall<T, S> {
        T accept(NetWorkListener listener, T req, S sec);
    }

    private static <T, D> T post(T val, D sec, PostCall<T, D> fun) {
        T t = val;
        synchronized (listeners) {
            int size = listeners.size();
            for (int i = 0; i < size; i++) {
                final Reference<NetWorkListener> pos = listeners.get(i);
                if (pos == null || pos.isEnqueued()) {
                    listeners.remove(i--);
                    size--;
                } else {
                    final NetWorkListener listener = pos.get();
                    if (listener == null) {
                        pos.enqueue();
                        listeners.remove(i--);
                        size--;
                    } else {
                        t = fun.accept(listener, t, sec);
                    }
                }
            }
        }
        return t;
    }

    public interface NetWorkListener {
        URLStreamHandler getURLStreamHandler(URLStreamHandler handler, String key);

        URLStreamHandler changeURLStreamHandler(URLStreamHandler handler, String key);

        boolean doClear(boolean run, Void unused);
    }

    static class ProtocolListener extends Hashtable<String, URLStreamHandler> {
        static PostCall<URLStreamHandler, String> getURLStreamHandler =
                NetWorkListener::getURLStreamHandler,
                changeURLStreamHandler = NetWorkListener::changeURLStreamHandler;
        static PostCall<Boolean, Void> doClear = NetWorkListener::doClear;

        @Override
        public synchronized URLStreamHandler put(String key, URLStreamHandler value) {
            return super.put(key, post(value, key, changeURLStreamHandler));
        }

        @Override
        public synchronized void clear() {
            if (post(true, null, doClear))
                super.clear();
        }

        @Override
        public synchronized URLStreamHandler get(Object key) {
            String s = (String) key;
            URLStreamHandler handler = super.get(s);
            return post(handler, s, getURLStreamHandler);
        }

        void put0(String key, URLStreamHandler value) {
            super.put(key, value);
        }
    }

    static final Field handlers;

    @NotNull
    static Map<String, URLStreamHandler> getHandlers() {
        try {
            //noinspection unchecked
            Map<String, URLStreamHandler> m = (Map) handlers.get(null);
            MXBukkitLib.debug(() -> "[NetWorkManager] Get URLStreamHandlers [ " + m.getClass() + "]");
            return m;
        } catch (IllegalAccessException e) {
            return ThrowHelper.thrown(e);
        }
    }

    static void setHandlers(@NotNull Hashtable table) {
        try {
            MXBukkitLib.debug(() -> "[NetWorkManager] Set URLStreamHandlers [" + table.getClass() + "]");
            handlers.set(null, table);
        } catch (IllegalAccessException e) {
            ThrowHelper.thrown(e);
        }
    }

    static {
        Class<URL> c = URL.class;
        Field f = null;
        try {
            f = c.getDeclaredField("handlers");
            f.setAccessible(true);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        handlers = f;
    }

    public static void install(boolean install) { // Http Check
        MXBukkitLib.debug("[NetWorkManager] Update NWM Status: " + install);
        if (install) {
            Map<String, URLStreamHandler> handlers = getHandlers();
            if (handlers instanceof ProtocolListener) return;
            ProtocolListener listener = new ProtocolListener();
            for (Map.Entry<String, URLStreamHandler> handler : handlers.entrySet()) {
                listener.put0(handler.getKey(), handler.getValue());
                MXBukkitLib.debug(() -> "[NetWorkManager] [Install] load [" + handler.getKey() + "][" + handler.getValue() + "]");
            }
            setHandlers(listener);
        } else {
            // UN Install
            Map<String, URLStreamHandler> listener = getHandlers(); // UnMatch, Force Replace
            Hashtable<String, URLStreamHandler> handlers = new Hashtable<>();
            for (Map.Entry<String, URLStreamHandler> l : listener.entrySet()) {
                handlers.put(l.getKey(), l.getValue());
                MXBukkitLib.debug(() -> "[NetWorkManager] [UN-Install] load [" + l.getKey() + "][" + l.getValue() + "]");
            }
            setHandlers(handlers);
        }
    }
}
