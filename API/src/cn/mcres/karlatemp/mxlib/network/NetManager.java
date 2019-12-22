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

import java.io.IOException;
import java.net.*;
import java.util.Hashtable;
import java.util.Map;

public class NetManager {
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
    }

    static {
        install(true);
    }

    @SuppressWarnings("unchecked")
    public static synchronized void install(boolean b) {
        final RField<URL, Object> handlers = Reflect.ofClass(URL.class).getField("handlers", null);
        final Map map = handlers.get(Map.class);
        if (b) {
            Map p;
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
            Map w;
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

    public static void main(String[] args) throws Throwable {
        URLConnectEvent.handlers.register(e -> {
            System.out.println(e.getCaller());
            System.out.println(e.getURL());
        });
        new URL("https://www.baidu.com").openConnection();
    }
}
