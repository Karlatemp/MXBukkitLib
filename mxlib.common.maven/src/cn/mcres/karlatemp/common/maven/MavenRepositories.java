/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MavenRepositories.java@author: karlatemp@vip.qq.com: 2020/1/31 上午1:26@version: 2.0
 */

package cn.mcres.karlatemp.common.maven;

import cn.mcres.karlatemp.common.maven.exceptions.RepositoryNotFoundException;

import java.io.IOException;
import java.net.URL;

public interface MavenRepositories {
    MavenRepo loadRepo(String group, String artifact, String version) throws IOException, RepositoryNotFoundException;

    URL getPOMLocation(String group, String artifact, String version) throws IOException, RepositoryNotFoundException;

    POM getPOM(String group, String artifact, String version) throws IOException, RepositoryNotFoundException;

    default MavenRepo loadRepo(String info) throws IOException, RepositoryNotFoundException {
        int f = info.indexOf(':');
        if (f == -1) throw new IOException("Group id missing");
        String group = info.substring(0, f++);
        int w = info.indexOf(':', f);
        if (w == -1) throw new IOException("Artifact id missing");
        String artifact = info.substring(f, w++);
        String ver = info.substring(w);
        return loadRepo(group, artifact, ver);
    }
}
