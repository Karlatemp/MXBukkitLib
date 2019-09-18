/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ReadPropertiesAutoConfigs.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

public class ReadPropertiesAutoConfigs {
    public static List<Function<String, Collection<Reader>>> resourceLoaders = new ArrayList<>();

    public static void read(Reader reader, ClassLoader loader,
                            Collection<String> founded,
                            List<String> configs) {
        try (Scanner scanner = new Scanner(reader)) {
            while (scanner.hasNextLine()) {
                String ln = scanner.nextLine().trim();
                if (SharedConfigurationProcessor.DEBUG)
                    MXBukkitLib.getLogger().printf("[AutoConfig] Read rule: " + ln);
                if (ln.isEmpty()) continue;
                if (ln.charAt(0) == '@') {
                    search(loader, founded, configs, ln.substring(1));
                } else if (ln.charAt(0) == '#') {
                    // LORE
                } else {
                    configs.add(ln.trim());
                }
            }
        }
    }

    public static List<String> search(ClassLoader loader,
                                      Collection<String> founded,
                                      List<String> configs,
                                      String res) {

        try {
            String low = res.toLowerCase();
            if (founded == null) founded = new HashSet<>();
            else if (founded.contains(low)) return configs;
            founded.add(low);
            final Enumeration<URL> resources = loader.getResources(res);
            while (resources.hasMoreElements()) {
                URL next = resources.nextElement();
                try (InputStream inp = next.openStream()) {
                    try (Reader reader = new InputStreamReader(inp, StandardCharsets.UTF_8)) {
                        read(reader, loader, founded, configs);
                    }
                }
            }
        } catch (FileNotFoundException fne) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Function<String, Collection<Reader>> resourceLoader : resourceLoaders) {
            Collection<Reader> readers = resourceLoader.apply(res);
            if (readers != null) {
                for (Reader reader : readers)
                    try (Reader r = reader) {
                        read(r, loader, founded, configs);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
            }
        }
        return configs;
    }

    public static void load() {
        SharedConfigurationProcessor cp = new SharedConfigurationProcessor();
        ClassLoader loader = ReadPropertiesAutoConfigs.class.getClassLoader();
        ArrayList<String> list = new ArrayList<>();
        search(loader, null, list, "META-INF/mxlib.autoconfig.list");
        search(loader, null, list, "/META-INF/mxlib.autoconfig.list");
//        System.out.println(list);
        ArrayList<String> link = new ArrayList<>(list.size());
        for (String s : list) {
            if (!link.contains(s)) {
                link.add(s);
                if (SharedConfigurationProcessor.DEBUG) {
                    MXBukkitLib.getLogger().printf("[AutoConfig] Found class " + s);
                }
            }
        }
        cp.load(null, loader, link, null, MXBukkitLib.getBeanManager());
    }
}
