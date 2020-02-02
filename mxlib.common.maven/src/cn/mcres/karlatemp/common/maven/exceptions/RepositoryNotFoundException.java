/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: RepositoryNotFoundException.java@author: karlatemp@vip.qq.com: 2020/1/31 上午1:37@version: 2.0
 */

package cn.mcres.karlatemp.common.maven.exceptions;

public class RepositoryNotFoundException extends Exception {
    private final String group;
    private final String artifact;
    private final String version;

    public RepositoryNotFoundException(String group, String artifact, String version) {
        super(group + ':' + artifact + ':' + version);
        this.group = group;
        this.artifact = artifact;
        this.version = version;
    }

    public String getArtifact() {
        return artifact;
    }

    public String getGroup() {
        return group;
    }

    public String getVersion() {
        return version;
    }
}
