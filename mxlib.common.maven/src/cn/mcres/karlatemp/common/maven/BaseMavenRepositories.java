/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: BaseMavenRepositories.java@author: karlatemp@vip.qq.com: 2020/1/31 上午1:32@version: 2.0
 */

package cn.mcres.karlatemp.common.maven;

import cn.mcres.karlatemp.common.maven.exceptions.RepositoryNotFoundException;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public abstract class BaseMavenRepositories implements MavenRepositories {
    protected final Collection<MavenRepo> repos = new ConcurrentLinkedQueue<>();
    protected final Collection<POM> poms = new ConcurrentLinkedQueue<>();
    public Logger logger;
    public MavenRepositories parent;

    public BaseMavenRepositories() {
    }

    public BaseMavenRepositories(MavenRepositories parent) {
        this.parent = parent;
    }

    @Override
    public POM getPOM(String group, String artifact, String version) throws IOException, RepositoryNotFoundException {
        for (POM p : poms) {
            if (p.group.equals(group)) {
                if (p.artifact.equals(artifact))
                    if (p.version.equals(version))
                        return p;
            }
        }
        if (parent != null) return parent.getPOM(group, artifact, version);
        return findPOM(group, artifact, version);
    }

    protected POM findPOM(String group, String artifact, String version) throws IOException, RepositoryNotFoundException {
        var pom = POM.load(group, artifact, version, this);
        poms.add(pom);
        return pom;
    }

    @Override
    public URL getPOMLocation(String group, String artifact, String version) throws IOException, RepositoryNotFoundException {
        if (parent != null) {
            try {
                return parent.getPOMLocation(group, artifact, version);
            } catch (RepositoryNotFoundException ignore) {
            }
        }
        return findPOMLocation(group, artifact, version);
    }

    public abstract URL findPOMLocation(String group, String artifact, String version) throws IOException, RepositoryNotFoundException;

    public MavenRepo findCached(String group, String artifact, String version) {
        for (MavenRepo repo : repos) {
            if (repo.artifact.equals(artifact))
                if (repo.group.equals(group))
                    if (repo.version.equals(version))
                        return repo;
        }
        return null;
    }

    @Override
    public MavenRepo loadRepo(String group, String artifact, String version) throws IOException, RepositoryNotFoundException {
        var c = findCached(group, artifact, version);
        if (c != null) return c;
        if (parent != null) {
            try {
                return parent.loadRepo(group, artifact, version);
            } catch (RepositoryNotFoundException ignore) {
            }
        }
        return findRepo(group, artifact, version);
    }

    protected MavenRepo register(MavenRepo repo) throws IOException, RepositoryNotFoundException {
        repos.add(repo);
        try {
            POM pom = getPOM(repo.group, repo.artifact, repo.version);
            if (pom.depends != null) {
                r:
                for (MavenRepo rep : pom.depends) {
                    for (var x : repo.depends) {
                        if (x.group.equals(rep.group))
                            if (x.artifact.equals(rep.artifact))
                                if (x.version.equals(rep.version))
                                    continue r;
                    }
                    try {
                        repo.depends.add(loadRepo(rep.group, rep.artifact, rep.version));
                    } catch (RepositoryNotFoundException ignore) {
                    }
                }
            }
        } catch (RepositoryNotFoundException ignore) {
        }
        return repo;
    }

    public abstract MavenRepo findRepo(String group, String artifact, String version) throws IOException, RepositoryNotFoundException;
}