/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: HookToolkit.java@author: karlatemp@vip.qq.com: 2020/1/20 下午8:00@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.plugin_class_definer;

import cn.mcres.karlatemp.mxlib.reflect.RField;
import cn.mcres.karlatemp.mxlib.reflect.RMethod;
import cn.mcres.karlatemp.mxlib.reflect.Reflect;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.jar.JarFile;

@SuppressWarnings({"unchecked", "rawtypes"})
public class HookToolkit {
    private static final RField<URLClassLoader, Object> URLClassLoader$ucp;
    private static final RField<Object, List<Object>> ucp_loader;
    private static final RField<Object, List<URL>> ucp_path;
    private static final RField<Object, Collection<URL>> ucp_unopenedUrls;
    private static final RField<Object, URL> ucp$loader_base;
    private static final RField<Object, JarFile> ucp$loader_jarfile;
    private static final RField<Object, JarFile> ucp$jarloader_jar;
    private static final Class<?> URLClasssPath$JLoader;
    private static final RMethod<URLClassLoader, Void> URLClassLoader$addURL;

    private static String getPackage(String c) {
        int i = c.lastIndexOf('.');
        if (i == -1) return "";
        return c.substring(0, i + 1);
    }

    public static URL openHook(@NotNull JarFile jar) {
        return openHook(null, jar);
    }

    public static URL openHook(String protocol, @NotNull JarFile jar) {
        return openHook(protocol, new HookedJarURLStreamHandler(jar));
    }

    public static URL openHook(@NotNull HookedJarURLStreamHandler handler) {
        return openHook(null, handler);
    }

    public static URL openHook(String protocol, @NotNull HookedJarURLStreamHandler handler) {
        if (protocol == null) {
            protocol = "x" + Long.toHexString(UUID.randomUUID().getMostSignificantBits() & 0xFFFF);
        }
        try {
            return new URL(null, protocol + ":/", handler);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to gen new url.", e);
        }
    }

    static {
        try {
            URLClassLoader$addURL = Reflect.ofClass(URLClassLoader.class)
                    .getMethod("addURL", void.class, URL.class);
            Class<Object> URLClassPath = (Class) Toolkit.Reflection.loadClassLink(
                    Arrays.asList("jdk.internal.loader.URLClassPath", "sun.misc.URLClassPath"),
                    ClassLoader.getSystemClassLoader()
            );
            Objects.requireNonNull(URLClassPath);
            Class<Object> URLClassPath$Loader = (Class) Class.forName(getPackage(URLClassPath.getName()) + "URLClassPath$Loader"),
                    URLClassPath$JarLoader = (Class) Class.forName(getPackage(URLClassPath.getName()) + "URLClassPath$JarLoader");
            URLClassLoader$ucp = Reflect.ofClass(URLClassLoader.class).getField("ucp", null);
            ucp_loader = Reflect.ofClass(URLClassPath).getField("loaders", null);
            ucp_path = Reflect.ofClass(URLClassPath).getField("path", null);
            RField<Object, Collection<URL>> ucp_uu = Reflect.ofClass(URLClassPath).getField("unopenedUrls", null);
            if (ucp_uu == null) {
                ucp_uu = Reflect.ofClass(URLClassPath).getField("urls", null);
            }
            ucp_unopenedUrls = ucp_uu;
            ucp$loader_base = Reflect.ofClass(URLClassPath$Loader).getField("base", URL.class);
            ucp$loader_jarfile = Reflect.ofClass(URLClassPath$Loader).getField("jarfile", JarFile.class);
            ucp$jarloader_jar = Reflect.ofClass(URLClassPath$JarLoader).getField("jar", JarFile.class);
            URLClasssPath$JLoader = URLClassPath$JarLoader;
        } catch (Throwable any) {
            throw new ExceptionInInitializerError(any);
        }
    }

    public static void replace(
            @NotNull URLClassLoader uLoader,
            @NotNull URL old,
            @NotNull URL base,
            @NotNull JarFile hook
    ) {
        int index = -1;
        {
            URL[] paths = uLoader.getURLs();
            for (int i = 0; i < paths.length; i++) {
                if (paths[i] == old) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                URLClassLoader$addURL.newContext().self(uLoader).invoke(base);
                return;
            }
        }
        var ucp = URLClassLoader$ucp.newContext().self(uLoader).get();
        var loaders = ucp_loader.newContext().self(ucp).get();
        var path = ucp_path.newContext().self(ucp).get();
        var unopenedUrls = ucp_unopenedUrls.newContext().self(ucp).get();
        path.set(index, base);
        for (var x : unopenedUrls) {
            if (x == old) {
                synchronized (unopenedUrls) {
                    var copy = new ConcurrentLinkedQueue<>(unopenedUrls);
                    unopenedUrls.clear();
                    for (var u : copy) {
                        if (u == old) {
                            u = base;
                        }
                        unopenedUrls.add(u);
                    }
                    return;
                }
            }
        }
        var loader = loaders.get(index);
        ucp$loader_base.newContext().self(loader).set(base);
        var x = ucp$loader_jarfile.newContext().self(loader);
        var jar = x.get();
        x.set(hook);
        if (jar != null) {
            try {
                jar.close();
            } catch (IOException ignore) {
            }
        }
        if (URLClasssPath$JLoader.isInstance(loader)) {
            var w = ucp$jarloader_jar.newContext().self(loader);
            var jw = w.get();
            w.set(hook);
            if (jw != null) {
                try {
                    jw.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}
