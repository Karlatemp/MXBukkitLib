/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: POM.java@author: karlatemp@vip.qq.com: 2020/1/31 上午1:56@version: 2.0
 */

package cn.mcres.karlatemp.common.maven;

import cn.mcres.karlatemp.common.maven.exceptions.RepositoryNotFoundException;
import cn.mcres.karlatemp.mxlib.formatter.*;
import cn.mcres.karlatemp.mxlib.formatter.Formatter;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class POM {
    public POM parent;
    public String group, artifact, version;
    public Collection<MavenRepo> depends;
    public Map<String, String> properties;
    private Element elm;

    public String getVersion(MavenRepo repo) {
        for (var r : depends) {
            if (r.group.equals(repo.group))
                if (r.artifact.equals(repo.artifact)) {
                    if (r.version != null)
                        return r.version;
                    if (parent != null) return parent.getVersion(repo);
                    return null;
                }
        }
        if (parent != null) return parent.getVersion(repo);
        return null;
    }

    public static final Formatter formatter = new PunctuateFormatter("${", "}");
    static final Map<String, String> ENV = System.getenv();

    public String parse(String line) {
        if (line == null) return null;
        return formatter.parse(line.trim(), Replacer.EMPTY).format(Locale.ROOT, new AbstractReplacer() {
            @Override
            public boolean containsKey(String key) {
                return true;
            }

            @Override
            public void apply(StringBuilder builder, String key) {
                {
                    var finding = POM.this;
                    do {
                        if (finding.properties.containsKey(key)) {
                            builder.append(finding.properties.get(key));
                            return;
                        }
                        finding = finding.parent;
                    } while (finding != null);
                }
                if (key.startsWith("project.")) {
                    var path = new ArrayList<>(Arrays.asList(key.split("\\.")));
                    path.remove(0);
                    var finding = POM.this;
                    Element current;
                    root:
                    do {
                        current = finding.elm;
                        for (String s : path) {
                            current = current.element(s);
                            if (current == null) {
                                finding = finding.parent;
                                continue root;
                            }
                        }
                        builder.append(parse(current.getText().trim()));
                        break;
                    } while (finding != null);
                    return;
                }
                for (Map.Entry<String, String> e : ENV.entrySet()) {
                    var k = e.getKey().replace('_', '.');
                    if (k.equalsIgnoreCase(key.replace('_', '.'))) {
                        builder.append(e.getValue());
                        return;
                    }
                }
                builder.append("${").append(key).append("}");

            }
        });
    }
    /*
    public static int i;
    public static String A;

    static {
        char[] cr = new char[2333];
        Arrays.fill(cr, '\t');
        A = new String(cr);
    }

    public static String w() {
        return A.substring(0, i);
    }*/

    public static POM load(String group, String artifact, String version, MavenRepositories repositories) throws IOException, RepositoryNotFoundException {
        // System.out.println(w() + "POM LOADING: " + group + ":" + artifact + ":" + version);
        // i++;
        var loc = repositories.getPOMLocation(group, artifact, version);
        Document doc;
        try {
            doc = new SAXReader().read(loc);
        } catch (DocumentException de) {
            throw new IOException(de);
        }
        var pom = new POM();
        pom.properties = new HashMap<>();
        var root = doc.getRootElement();
        var parent = root.element("parent");
        pom.elm = root;
        POM p = null;
        if (parent != null) {
            p = repositories.getPOM(pom.parse(parent.elementText("groupId")), pom.parse(parent.elementText("artifactId")),
                    pom.parse(parent.elementText("version")));
            pom.parent = p;
        }
        pom.properties = new HashMap<>();
        pom.properties.put("version", version);
        pom.properties.put("groupId", group);
        pom.properties.put("artifactId", artifact);
        pom.properties.put("pom.version", version);
        pom.properties.put("pom.groupId", group);
        pom.properties.put("pom.artifactId", artifact);
        {
            var properties = root.element("properties");
            if (properties != null) {
                var itor = properties.elementIterator();
                while (itor.hasNext()) {
                    var elm = (Element) itor.next();
                    // System.out.println(w() + elm.getName() + " = " + pom.parse(elm.getText().trim()));
                    pom.properties.put(elm.getName(), pom.parse(elm.getText().trim()));
                }
            }
        }
        pom.artifact = artifact;
        // var val = pom.parse(root.elementText("artifactId"));
        // if (val == null && p != null) val = p.artifact;
        // if (!artifact.equals(val)) {
        //     throw new IOException("Artifact Id Not match. Expect " + artifact + " but get " + val);
        // }
        // val = pom.parse(root.elementText("groupId"));
        // if (val == null && p != null) val = p.group;
        // if (!group.equals(val))
        //    throw new IOException("Group Id Not Match. Expect " + group + " but get " + group);
        pom.group = group;
        // val = pom.parse(root.elementText("version"));
        // if (val == null && p != null) val = p.version;
        // if (!version.equals(val)) throw new IOException("Version Not Match. Expect " + version + " but get " + val);
        pom.version = version;
        var dep = root.element("dependencies");
        pom.depends = new ConcurrentLinkedQueue<>();
        if (dep != null) {
            var it = dep.elementIterator("dependency");
            while (it.hasNext()) {
                var depend = (Element) it.next();
                var g = pom.parse(depend.elementText("groupId"));
                var a = pom.parse(depend.elementText("artifactId"));
                var v = pom.parse(depend.elementText("version"));
                var x = pom.parse(depend.elementText("scope"));
                if (x != null) {
                    if (!x.equalsIgnoreCase("runtime"))
                        if (!x.equalsIgnoreCase("provided"))
                            continue;
                }
                if (g != null && a != null && v != null) {
                    try {
                        pom.depends.add(repositories.loadRepo(g, a, v));
                    } catch (RepositoryNotFoundException ignore) {
                    }
                }
            }
        }
        if (p != null)
            if (p.depends != null) {
                pom.depends.addAll(p.depends);
            }
        // i--;
        return pom;
    }
}
