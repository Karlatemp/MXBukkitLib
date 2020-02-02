/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: LocalRepositories.java@author: karlatemp@vip.qq.com: 2020/1/31 上午1:39@version: 2.0
 */

package cn.mcres.karlatemp.common.maven;

import cn.mcres.karlatemp.common.maven.exceptions.RepositoryNotFoundException;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.util.RAFInputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.ZipFile;

public class LocalRepositories extends BaseMavenRepositories {
    public File location;
    public boolean forceCheck = false;
    public String checkType = "sha1";
    public String MessageDigestType = "SHA-1";

    public LocalRepositories(File location) {
        this.location = location;
    }

    public LocalRepositories(LocalRepositories copy) {
        this.location = copy.location;
        this.forceCheck = copy.forceCheck;
        this.checkType = copy.checkType;
        this.MessageDigestType = copy.MessageDigestType;
        this.logger = copy.logger;
    }

    void check(File file, String g, String a, String v) throws RepositoryNotFoundException {
        var md5 = new File(file.getPath() + "." + checkType);
        if (!md5.isFile()) {
            if (forceCheck) throw new RepositoryNotFoundException(g, a, v);
            if (logger != null) {
                logger.warning(MessageDigestType + " of " + file + " not exist.");
            }
            // MD Not Exists
            return;
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try (RAFInputStream raf = new RAFInputStream(new RandomAccessFile(md5, "r"))) {
                Toolkit.IO.writeTo(raf, os);
            }
            if (new String(Toolkit.IO.digest(MessageDigest.getInstance(MessageDigestType), file)).equalsIgnoreCase(new String(os.toByteArray()))) {
                if (logger != null) {
                    logger.warning(file + "'s MD5 not match.");
                }
                throw new RepositoryNotFoundException(g, a, v);
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RepositoryNotFoundException(g, a, v);
        }
    }

    @Override
    public MavenRepo findRepo(String group, String artifact, String version) throws IOException, RepositoryNotFoundException {
        File dir = new File(new File(new File(location, group.replace('.', '/')), artifact), version);
        File file = new File(dir, artifact + '-' + version + ".jar");
        if (file.isFile()) {
            //noinspection EmptyTryBlock
            try (var ignore = new ZipFile(file)) {
            } catch (IOException ioe) {
                // ioe.printStackTrace();
                throw new RepositoryNotFoundException(group, artifact, version);
            }
            check(file, group, artifact, version);
            var repo = MavenRepo.of(group, artifact, version, file.toURI().toURL());
            register(repo);
            return repo;
        }
        throw new RepositoryNotFoundException(group, artifact, version);
    }

    @Override
    public URL findPOMLocation(String group, String artifact, String version) throws IOException, RepositoryNotFoundException {
        File dir = new File(new File(new File(location, group.replace('.', '/')), artifact), version);
        File file = new File(dir, artifact + '-' + version + ".pom");
        if (file.isFile()) {
            return file.toURI().toURL();
        }
        File jar = new File(dir, artifact + '-' + version + ".jar");
        if (jar.isFile())
            try (ZipFile zip = new ZipFile(jar)) {
                var entry = zip.getEntry("META-INF/maven/" + group + '/' + artifact + "/pom.xml");
                if (entry != null) {
                    return new URL("jar:" + jar.toURI() + "!/" + entry.getName());
                }
            }
        throw new RepositoryNotFoundException(group, artifact, version);
    }
}
