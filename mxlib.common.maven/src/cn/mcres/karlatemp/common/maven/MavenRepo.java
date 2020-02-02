/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MavenRepo.java@author: karlatemp@vip.qq.com: 2020/1/31 上午1:15@version: 2.0
 */

package cn.mcres.karlatemp.common.maven;

import java.net.URL;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MavenRepo {
    public static final ThreadLocal<Boolean> HIDDEN_LOCATION = ThreadLocal.withInitial(() -> true);
    public String group, artifact, version;
    public URL location;
    public Collection<MavenRepo> depends;

    public String toString() {
        return build(new StringBuilder(), 0, new ConcurrentLinkedQueue<>()).toString();
    }

    private StringBuilder build(StringBuilder builder, int i, Collection<MavenRepo> dejaVu) {
        prefix(builder, i).append(group).append(':').append(artifact).append(':').append(version);
        if (dejaVu.contains(this)) return builder;
        dejaVu.add(this);
        if (!HIDDEN_LOCATION.get()) if (location != null) builder.append(" at ").append(location);
        if (depends != null) {
            for (var rep : depends) {
                builder.append('\n');
                rep.build(builder, i + 1, dejaVu);
            }
        }
        return builder;
    }

    private static StringBuilder prefix(StringBuilder builder, int i) {
        while (i-- > 0) builder.append('\t');
        return builder;
    }

    public static MavenRepo of(String group, String artifact, String version, URL location) {
        var rep = new MavenRepo();
        rep.group = group;
        rep.artifact = artifact;
        rep.version = version;
        rep.location = location;
        rep.depends = new ConcurrentLinkedQueue<>();
        return rep;
    }

    private void dejaVu(Collection<MavenRepo> dejaVu, Collection<URL> all) {
        if (dejaVu.contains(this)) return;
        if (location != null)
            all.add(location);
        dejaVu.add(this);
        if (depends != null) {
            for (var repo : depends) {
                repo.dejaVu(dejaVu, all);
            }
        }
    }

    public Collection<URL> getAllURL() {
        Collection<URL> all = new ConcurrentLinkedQueue<>();
        Collection<MavenRepo> dejaVu = new ConcurrentLinkedQueue<>();
        dejaVu(dejaVu, all);
        return all;
    }
}
