/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: RepositoriesLink.java@author: karlatemp@vip.qq.com: 2020/1/31 上午2:58@version: 2.0
 */

package cn.mcres.karlatemp.common.maven;

import cn.mcres.karlatemp.common.maven.exceptions.RepositoryNotFoundException;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RepositoriesLink extends BaseMavenRepositories {
    public final ConcurrentLinkedQueue<BaseMavenRepositories> repositories = new ConcurrentLinkedQueue<>();

    public <T extends BaseMavenRepositories> T register(T repositories) {
        this.repositories.add(repositories);
        repositories.parent = this;
        return repositories;
    }

    public RepositoriesLink() {
    }

    @Override
    public URL findPOMLocation(String group, String artifact, String version) throws IOException, RepositoryNotFoundException {
        for (BaseMavenRepositories mv : repositories) {
            try {
                return mv.findPOMLocation(group, artifact, version);
            } catch (RepositoryNotFoundException ignore) {
            }
        }
        throw new RepositoryNotFoundException(group, artifact, version);
    }

    @Override
    public MavenRepo findRepo(String group, String artifact, String version) throws IOException, RepositoryNotFoundException {
        for (BaseMavenRepositories mv : repositories) {
            var cached = mv.findCached(group, artifact, version);
            if (cached != null) return cached;
            try {
                return mv.findRepo(group, artifact, version);
            } catch (RepositoryNotFoundException ignore) {
            }
        }
        throw new RepositoryNotFoundException(group, artifact, version);
    }
}
