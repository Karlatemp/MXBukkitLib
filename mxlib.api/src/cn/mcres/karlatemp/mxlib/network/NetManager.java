/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NetManager.java@author: karlatemp@vip.qq.com: 19-12-21 下午5:21@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.network;

import cn.mcres.karlatemp.mxlib.event.Event;
import cn.mcres.karlatemp.mxlib.event.network.URLConnectEvent;
import cn.mcres.karlatemp.mxlib.reflect.RField;
import cn.mcres.karlatemp.mxlib.reflect.Reflect;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.*;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class NetManager {
    public static Class<?> URLStreamHandler$class = URLStreamHandler.class;
    public static Class<?> URLStreamHandlerProvider$class = URLStreamHandlerProvider.class;

    static class URLStreamHandlerProvider extends URLStreamHandler {
        private final URLStreamHandler parent;

        URLStreamHandlerProvider(URLStreamHandler parent) {
            this.parent = parent;
        }

        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            a(u, null);
            return new URL(null, u.toExternalForm(), parent).openConnection();
        }

        private void a(URL u, Proxy p) throws IOException {
            URLConnectEvent e = new URLConnectEvent(u, p, Toolkit.Reflection.getCallerClass(2));
            if (Event.post(e).isCancelled()) {
                final IOException cancel = e.getCancel();
                if (cancel != null) throw cancel;
                throw new IOException("URL Connect was cancelled.");
            }
        }

        @Override
        protected URLConnection openConnection(URL u, Proxy p) throws IOException {
            a(u, p);
            return new URL(null, u.toExternalForm(), parent).openConnection(p);
        }
    }

    static class Manager extends Hashtable<String, URLStreamHandler> {
        private final Map<String, URLStreamHandler> parent;

        Manager(Map<String, URLStreamHandler> parent) {
            this.parent = parent;
        }

        @Override
        public synchronized URLStreamHandler get(Object key) {
            return parent.get(key);
        }

        @Override
        public synchronized URLStreamHandler put(String key, URLStreamHandler value) {
            return parent.put(key, value);
        }

        @Override
        public synchronized boolean containsKey(Object key) {
            return parent.containsKey(key);
        }

        @Override
        public synchronized void clear() {
            parent.clear();
        }

        @Override
        public boolean containsValue(Object value) {
            return parent.containsValue(value);
        }

        @Override
        public int size() {
            return parent.size();
        }

        @Override
        public boolean isEmpty() {
            return parent.isEmpty();
        }

        @Override
        public URLStreamHandler remove(Object key) {
            return parent.remove(key);
        }

        @Override
        public void putAll(@NotNull Map<? extends String, ? extends URLStreamHandler> m) {
            parent.putAll(m);
        }

        @Override
        public Set<String> keySet() {
            return parent.keySet();
        }

        @Override
        public Collection<URLStreamHandler> values() {
            return parent.values();
        }

        @Override
        public Set<Map.Entry<String, URLStreamHandler>> entrySet() {
            return parent.entrySet();
        }

        @Override
        public boolean equals(Object o) {
            return parent.equals(o);
        }

        @Override
        public int hashCode() {
            return parent.hashCode();
        }

        @Override
        public URLStreamHandler getOrDefault(Object key, URLStreamHandler defaultValue) {
            return parent.getOrDefault(key, defaultValue);
        }

        @Override
        public void forEach(BiConsumer<? super String, ? super URLStreamHandler> action) {
            parent.forEach(action);
        }

        @Override
        public void replaceAll(BiFunction<? super String, ? super URLStreamHandler, ? extends URLStreamHandler> function) {
            parent.replaceAll(function);
        }

        @Override
        public URLStreamHandler putIfAbsent(String key, URLStreamHandler value) {
            return parent.putIfAbsent(key, value);
        }

        @Override
        public boolean remove(Object key, Object value) {
            return parent.remove(key, value);
        }

        @Override
        public boolean replace(String key, URLStreamHandler oldValue, URLStreamHandler newValue) {
            return parent.replace(key, oldValue, newValue);
        }

        @Override
        public URLStreamHandler replace(String key, URLStreamHandler value) {
            return parent.replace(key, value);
        }

        @Override
        public URLStreamHandler computeIfAbsent(String key, Function<? super String, ? extends URLStreamHandler> mappingFunction) {
            return parent.computeIfAbsent(key, mappingFunction);
        }

        @Override
        public URLStreamHandler computeIfPresent(String key, BiFunction<? super String, ? super URLStreamHandler, ? extends URLStreamHandler> remappingFunction) {
            return parent.computeIfPresent(key, remappingFunction);
        }

        @Override
        public URLStreamHandler compute(String key, BiFunction<? super String, ? super URLStreamHandler, ? extends URLStreamHandler> remappingFunction) {
            return parent.compute(key, remappingFunction);
        }

        @Override
        public URLStreamHandler merge(String key, URLStreamHandler value, BiFunction<? super URLStreamHandler, ? super URLStreamHandler, ? extends URLStreamHandler> remappingFunction) {
            return parent.merge(key, value, remappingFunction);
        }
    }

    private static final RField<URL, Map<String, URLStreamHandler>> handlers = Reflect.ofClass(URL.class).getField("handlers", null);
    private static final RField<URL, URLStreamHandler> handler = Reflect.ofClass(URL.class).getField("handler", null);

    static {
        install(true);
    }

    public static URLStreamHandler getHandler(@NotNull URL url) {
        return handler.newContext().self(url).get();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface HandlerSetter {
    }

    public static void setHandler(@NotNull URL url, @NotNull URLStreamHandler handler) {
        Class<?> caller = Toolkit.Reflection.getCallerClass();
        var h = NetManager.handler.newContext().self(url);
        boolean allow = false;
        if (caller == h.get().getClass()) {
            allow = true;
        }
        if (caller.getDeclaredAnnotation(HandlerSetter.class) != null) allow = true;
        if (SecurityManager.class.isAssignableFrom(caller)) allow = true;
        if (!allow) throw new IllegalAccessError("Caller must url provider or marked @HandlerSetter");
        h.set(handler);
    }

    @SuppressWarnings("unchecked")
    public static synchronized void install(boolean b) {
        final Map<String, URLStreamHandler> map = handlers.get(Map.class);
        if (b) {
            Map<String, URLStreamHandler> p;
            if (!(map instanceof Manager)) {
                Manager mgr = new Manager(map);
                handlers.set(p = mgr);
                try {
                    new URL("https://localhost");
                } catch (Throwable ignore) {
                }
                try {
                    new URL("http://localhost");
                } catch (Throwable ignore) {
                }
            } else p = map;
            a(p, "http", true);
            a(p, "https", true);
        } else {
            Map<String, URLStreamHandler> w;
            if (map instanceof Manager) {
                handlers.set(w = ((Manager) map).parent);
            } else {
                w = map;
            }
            a(w, "http", false);
            a(w, "https", false);
        }
    }

    private static void a(Map<String, URLStreamHandler> mgr, String protocol, boolean a) {
        final URLStreamHandler handler = mgr.get(protocol);
        if (a) {
            if (!(handler instanceof URLStreamHandlerProvider)) {
                mgr.put(protocol, new URLStreamHandlerProvider(handler));
            }
        } else {
            if (handler instanceof URLStreamHandlerProvider) {
                mgr.put(protocol, ((URLStreamHandlerProvider) handler).parent);
            }
        }
    }

    /**
     * Remove protocol from URL NetWork
     *
     * @param protocol The target protocol
     * @param handler  The target handler
     * @return true if success to remove protocol
     * @since 2.12
     */
    public static boolean removeProtocol(String protocol, URLStreamHandler handler) {
        return handlers.get().remove(protocol, handler);
    }

    public static boolean registerProtocol(String protocol, URLStreamHandler handler) {
        final Map<String, URLStreamHandler> map = handlers.get();
        if (map.containsKey(protocol)) {
            return map.get(protocol) == handler;
        } else {
            map.put(protocol, handler);
            return true;
        }
    }

    public static void main(String[] args) throws Throwable {
        URLConnectEvent.handlers.register(e -> {
            System.out.println(e.getCaller());
            System.out.println(e.getURL());
        });
        new URL("https://www.baidu.com").openConnection();
    }
}
