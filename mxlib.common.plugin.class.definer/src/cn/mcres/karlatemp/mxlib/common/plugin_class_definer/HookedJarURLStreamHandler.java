/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: HookedJarURLStreamHandler.java@author: karlatemp@vip.qq.com: 2020/1/15 下午10:53@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.plugin_class_definer;

import cn.mcres.karlatemp.mxlib.event.HandlerList;
import cn.mcres.karlatemp.mxlib.internal.IHookedJarURLStreamHandler;
import cn.mcres.karlatemp.mxlib.tools.CharCompiler;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class HookedJarURLStreamHandler extends URLStreamHandler implements IHookedJarURLStreamHandler {
    private final JarFile jar;
    private final URL jarFileURL;

    private static class JRConnection extends JarURLConnection {
        private JarEntry path;
        private InputStream stream;
        private JarFile file;
        private URL jarFileURL;

        private static JRConnection create(JarEntry entry, JarFile file, URL hook, URL jarFileURL) {
            JRConnection connection = Toolkit.Reflection.allocObject(JRConnection.class);
            connection.init(entry, file, hook, jarFileURL);
            return connection;
        }

        private JRConnection(URL a) throws MalformedURLException {
            super(a);
        }

        @Override
        public JarFile getJarFile() throws IOException {
            return file;
        }

        @Override
        public URL getJarFileURL() {
            return jarFileURL;
        }

        @Override
        public JarEntry getJarEntry() throws IOException {
            return path;
        }

        private void init(JarEntry entry, JarFile file, URL url, URL jarFileURL) {
            this.path = entry;
            this.file = file;
            this.url = url;
            this.jarFileURLConnection = null;
            this.jarFileURL = jarFileURL;
        }

        @Override
        public String getEntryName() {
            return path.getName();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            connect();
            return stream;
        }

        @Override
        public int getContentLength() {
            if (file instanceof HookedJarFile) {
                if (size(((HookedJarFile) file).getJarStreamGetEventHandlers())) {
                    return -1;
                }
            }
            return (int) path.getSize();
        }

        private boolean size(HandlerList<HookedJarFile.JarStreamGetEvent> jarStreamGetEventHandlers) {
            AtomicBoolean ab = new AtomicBoolean(false);
            jarStreamGetEventHandlers.forEach(x -> ab.set(true));
            return ab.get();
        }

        @Override
        public synchronized void connect() throws IOException {
            if (connected) return;
            stream = file.getInputStream(path);
            Objects.requireNonNull(stream);
            connected = true;
        }
    }

    public HookedJarURLStreamHandler(JarFile jar) {
        this.jar = jar;
        try {
            jarFileURL = new URL("jar:" + new File(jar.getName()).toURI().toASCIIString() + "!/");
        } catch (MalformedURLException mal) {
            throw new RuntimeException(mal);
        }
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        String path = CharCompiler.decode(u.getFile().substring(1));
        final JarEntry entry = jar.getJarEntry(path);
        if (entry == null) throw new FileNotFoundException(path);
        return JRConnection.create(entry, jar, u, jarFileURL);
    }

    public JarFile getJar() {
        return jar;
    }
}
