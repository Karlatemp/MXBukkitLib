/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: NetWorkDumper.java@author: karlatemp@vip.qq.com: 2020/1/25 下午2:20@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.agent.nwd;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Map;

@SuppressWarnings("unchecked")
public class NetWorkDumper extends URLStreamHandler {
    private static final Field
            URL$handler, URL$handlers;
    private static final Method URL$getURLStreamHandler;
    private static final PrintStream out = System.out;

    static {
        try {
            (URL$handler = URL.class.getDeclaredField("handler")).setAccessible(true);
            (URL$handlers = URL.class.getDeclaredField("handlers")).setAccessible(true);
            (URL$getURLStreamHandler = URL.class.getDeclaredMethod("getURLStreamHandler", String.class)).setAccessible(true);
            Map<String, URLStreamHandler> handlers = (Map<String, URLStreamHandler>) URL$handlers.get(null);
            handlers.put("http", new NetWorkDumper(URL$getURLStreamHandler.invoke(null, "http")));
            handlers.put("https", new NetWorkDumper(URL$getURLStreamHandler.invoke(null, "https")));
        } catch (Throwable thr) {
            throw new ExceptionInInitializerError(thr);
        }
        out.println("[NetWorkDumper] Loaded.");
    }

    private final URLStreamHandler handler;

    public NetWorkDumper(Object http) {
        this.handler = (URLStreamHandler) http;
    }

    public static void premain(String s, Instrumentation ins) {
    }

    @SuppressWarnings("ThrowFromFinallyBlock")
    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        Throwable tr = null;
        try {
            out.println("[NWD] URL       Connect: " + u);
            URL$handler.set(u, handler);
            return u.openConnection();
        } catch (Throwable thr) {
            tr = thr;
        } finally {
            try {
                URL$handler.set(u, this);
            } catch (IllegalAccessException e) {
                tr = t(tr, e);
            }
            if (tr instanceof RuntimeException) {
                throw (RuntimeException) tr;
            }
            if (tr instanceof IOException) {
                throw (IOException) tr;
            }
            if (tr instanceof Error) {
                throw (Error) tr;
            }
            if (tr != null) throw new IOException(tr);
        }
        throw new InternalError();
    }

    @SuppressWarnings("ThrowFromFinallyBlock")
    @Override
    protected URLConnection openConnection(URL u, Proxy proxy) throws IOException {
        Throwable tr = null;
        try {
            out.println("[NWD] URL Proxy Connect: " + u + " [" + proxy + "]");
            URL$handler.set(u, handler);
            return u.openConnection(proxy);
        } catch (Throwable thr) {
            tr = thr;
        } finally {
            try {
                URL$handler.set(u, this);
            } catch (IllegalAccessException e) {
                tr = t(tr, e);
            }
            if (tr instanceof RuntimeException) {
                throw (RuntimeException) tr;
            }
            if (tr instanceof IOException) {
                throw (IOException) tr;
            }
            if (tr instanceof Error) {
                throw (Error) tr;
            }
            if (tr != null) throw new IOException(tr);
        }
        throw new InternalError();
    }

    private Throwable t(Throwable tr, Throwable e) {
        if (tr == null) return e;
        tr.addSuppressed(e);
        return tr;
    }
}
