/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: IdeaDocGenerator.java@author: karlatemp@vip.qq.com: 2019/12/31 下午6:29@version: 2.0
 */

package cn.mcres.karlatemp.mxli.exts.Java12DocGenerator;

import cn.mcres.karlatemp.mxlib.formatter.Formatter;
import cn.mcres.karlatemp.mxlib.formatter.Replacer;
import cn.mcres.karlatemp.mxlib.util.ClassPathBuilder;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class IdeaDocGenerator {
    private final Map<String, List<Path>> globalLibraries = new ConcurrentHashMap<>();
    private final Map<String, ModuleInfo> modules = new ConcurrentHashMap<>();
    private final Map<String, String> module_iml = new ConcurrentHashMap<>();
    private Formatter formatter;
    private String project;

    public IdeaDocGenerator IDEConfigLocation(String loc) throws IOException, XMLStreamException {
        File f = new File(loc, "config/options/applicationLibraries.xml");
        try (var reader = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)) {
            final XMLEventReader xml = XMLInputFactory.newDefaultFactory().createXMLEventReader(reader);
            while (xml.hasNext()) {
                final XMLEvent event = xml.nextEvent();
                if (event.isStartElement()) {
                    StartElement el = event.asStartElement();
                    if (el.getName().getLocalPart().equals("library")) {
                        List<Path> list = new ArrayList<>();
                        globalLibraries.put(el.getAttributeByName(new QName("name")).getValue(), list);
                        while (xml.hasNext()) {
                            XMLEvent next = xml.nextTag();
                            if (next.isStartElement()) {
                                if (next.asStartElement().getName().getLocalPart().equals("CLASSES")) {
                                    while (xml.hasNext()) {
                                        XMLEvent e = xml.nextTag();
                                        if (e.isStartElement()) {
                                            final StartElement element = e.asStartElement();
                                            if (element.getName().getLocalPart().equals("root")) {
                                                String path = element.getAttributeByName(new QName("url")).getValue();
                                                if (path.startsWith("file:")) {
                                                    list.add(Path.of(URI.create(path)));
                                                } else if (path.startsWith("jar://")) {
                                                    if (path.endsWith("!/")) {
                                                        list.add(
                                                                Path.of(path.substring(6, path.length() - 2))
                                                        );
                                                    }
                                                }
                                            }
                                        } else if (e.isEndElement()) {
                                            if (e.asEndElement().getName().getLocalPart().equals("CLASSES")) {
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            } else if (next.isEndElement()) {
                                if (next.asEndElement().getName().getLocalPart().equals("library")) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return this;
    }

    public Map<String, ModuleInfo> getModules() {
        return modules;
    }

    public Map<String, String> getModule_iml() {
        return module_iml;
    }

    public Map<String, List<Path>> getGlobalLibraries() {
        return globalLibraries;
    }

    public IdeaDocGenerator project(String projectLocation) throws IOException, XMLStreamException {
        this.project = projectLocation;
        File modules_xml = new File(projectLocation, ".idea/modules.xml");
        try (var IR = new InputStreamReader(new FileInputStream(modules_xml), StandardCharsets.UTF_8)) {
            final XMLEventReader reader = XMLInputFactory.newDefaultFactory().createXMLEventReader(IR);
            while (reader.hasNext()) {
                final XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    final StartElement se = event.asStartElement();
                    if (se.getName().getLocalPart().endsWith("module")) {
                        String path = se.getAttributeByName(new QName("filepath")).getValue();
                        int i = path.lastIndexOf('/') + 1;
                        int e = path.length();
                        if (path.endsWith(".iml")) {
                            e -= 4;
                        }
                        module_iml.put(path.substring(i, e), path);
                    }
                }
            }
        }
        return this;
    }

    public IdeaDocGenerator env(Formatter formatter) {
        this.formatter = formatter;
        return this;
    }

    public IdeaDocGenerator buildImiInfo() throws IOException, XMLStreamException {
        HashMap<String, String> vars = new HashMap<>();
        vars.put("USER_HOME", System.getenv("user.home"));
        vars.put("PROJECT_DIR", project);
        final var mapping = Formatter.mapping(vars);
        for (Map.Entry<String, String> imi : module_iml.entrySet()) {
            final var mi = new ModuleInfo();
            String dir;
            try (var r = new InputStreamReader(new FileInputStream(
                    dir = formatter.parse(imi.getValue(), Replacer.EMPTY).format(Locale.getDefault(), mapping)
            ), StandardCharsets.UTF_8)) {
                dir = new File(dir, "..").getCanonicalPath();
                vars.put("MODULE_DIR", dir);
                var reader = XMLInputFactory.newDefaultFactory().createXMLEventReader(r);
                while (reader.hasNext()) {
                    final XMLEvent event = reader.nextEvent();
                    if (event.isStartElement()) {
                        final StartElement element = event.asStartElement();
                        switch (element.getName().getLocalPart()) {
                            case "sourceFolder": {
                                if (!element.getAttributeByName(new QName("isTestSource")).getValue().equals("true")) {
                                    mi.source.add(formatter.parse(element.getAttributeByName(new QName("url")).getValue().substring(7),
                                            Replacer.EMPTY).format(Locale.getDefault(), mapping));
                                }
                                break;
                            }
                            case "orderEntry": {
                                if (element.getAttributeByName(new QName("type")).getValue().equals("library")) {
                                    switch (element.getAttributeByName(new QName("level")).getValue()) {
                                        case "application": {
                                            mi.GLOBAL_libraries.add(element.getAttributeByName(new QName("name")).getValue());
                                            break;
                                        }
                                        case "project": {
                                            mi.PROJECT_libraries.add(element.getAttributeByName(new QName("name")).getValue());
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
            modules.put(imi.getKey(), mi);
        }
        return this;
    }

    public IdeaDocGenerator forModule(String... modules) {
        return forModule(Arrays.asList(modules));
    }

    public IdeaDocGenerator forModule(List<String> modules) {
        modules.removeIf(key -> !modules.contains(key));
        return this;
    }

    public IdeaDocGenerator build(boolean legacy) {
        return this;
    }

    public IdeaDocGenerator addCustomArgs(Consumer<ClassPathBuilder> action) {
        return null;
    }

    public String getProjectType() {
        return null;
    }

    public static class ModuleInfo {
        // List<String> modules = new ArrayList<>();
        public List<String> GLOBAL_libraries = new ArrayList<>();
        public List<String> PROJECT_libraries = new ArrayList<>();
        public List<String> source = new ArrayList<>();
    }
}
