/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: RemoteRepositories.java@author: karlatemp@vip.qq.com: 2020/1/31 上午3:04@version: 2.0
 */

package cn.mcres.karlatemp.common.maven;

import cn.mcres.karlatemp.common.maven.exceptions.RepositoryNotFoundException;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.util.RAFOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteRepositories extends BaseMavenRepositories {
    public final LocalRepositories store;
    public final URL base;
    public static final String DEFAULT = "https://repo1.maven.org/maven2/",
            DEFAULT_HTTP = "http://repo1.maven.org/maven2/",
            ALIYUN = "https://maven.aliyun.com/nexus/content/groups/public/",
            ALIYUN_HTTP = "http://maven.aliyun.com/nexus/content/groups/public/",
            SPIOGTMC = "https://hub.spigotmc.org/nexus/content/groups/public/";

    public RemoteRepositories(LocalRepositories store, URL base, Logger logger) {
        this.logger = logger;
        store.logger = logger;
        this.parent = store.parent;
        store.parent = this;
        this.store = store;
        this.base = base;
    }

    public static URLConnection openConnect(URL url, int loop) throws IOException {
        if (loop++ > 5) throw new IOException("Too many cycles");
        var connect = url.openConnection();
        if (connect instanceof HttpURLConnection) {
            var code = ((HttpURLConnection) connect).getResponseCode();
            var loc = connect.getHeaderField("Location");
            if (code == 302 || code == 301 || code == 303) {
                ((HttpURLConnection) connect).disconnect();
                return openConnect(new URL(url, loc), loop);
            }
            if (code != 200) {
                ((HttpURLConnection) connect).disconnect();
                throw new IOException("Bad HTTP Request");
            }
        }
        return connect;
    }

    private File download(String group, String artifact, String version, String type) throws IOException, RepositoryNotFoundException {
        var url = new URL(base, group.replace('.', '/') + '/' + artifact + '/' + version + '/' + artifact + '-' + version + type);
        File dir = new File(new File(new File(store.location, group.replace('.', '/')), artifact), version);
        File file = new File(dir, artifact + '-' + version + type);
        if (logger != null) {
            logger.info("Downloading " + url + " to " + file);
        }
        URLConnection connect;
        InputStream source;
        try {
            connect = openConnect(url, 0);
            source = connect.getInputStream();
            if (logger != null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, connect.getURL().toString());
                    for (var header : connect.getHeaderFields().entrySet()) {
                        logger.log(Level.FINE, "\t" + header.getKey());
                        for (var val : header.getValue()) {
                            logger.log(Level.FINE, "\t\t" + val);
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            throw new RepositoryNotFoundException(group, artifact, version);
        }
        dir.mkdirs();
        file.createNewFile();
        try (RAFOutputStream out = new RAFOutputStream(new RandomAccessFile(file, "rw"))) {
            Toolkit.IO.writeTo(source, out);
        }
        if (connect instanceof HttpURLConnection) {
            ((HttpURLConnection) connect).disconnect();
        }
        return file;
    }

    @Override
    public URL findPOMLocation(String group, String artifact, String version) throws IOException, RepositoryNotFoundException {
        try {
            return store.findPOMLocation(group, artifact, version);
        } catch (RepositoryNotFoundException notFound) {
            download(group, artifact, version, ".pom." + store.checkType);
            var file = download(group, artifact, version, ".pom");
            store.check(file, group, artifact, version);
            return file.toURI().toURL();
        }
    }

    @Override
    public MavenRepo findRepo(String group, String artifact, String version) throws IOException, RepositoryNotFoundException {
        try {
            return store.findRepo(group, artifact, version);
        } catch (RepositoryNotFoundException notFound) {
            download(group, artifact, version, ".jar." + store.checkType);
            download(group, artifact, version, ".jar");
            return store.findRepo(group, artifact, version);
        }
    }
}
