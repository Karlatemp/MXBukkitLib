/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/29 15:57:22
 *
 * MXLib/mxlib.configuration/PropertiesConfiguration.java
 */

package cn.mcres.karlatemp.mxlib.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Properties;

public class PropertiesConfiguration extends MemoryConfiguration {
    public PropertiesConfiguration() {
    }

    protected PropertiesConfiguration(Void v) {
        super(v);
    }

    @Override
    protected MemoryConfiguration newInstance() {
        return new PropertiesConfiguration(null);
    }

    @Override
    public void load(Reader stream) throws IOException {
        Properties loader = new Properties();
        loader.load(stream);
        for (var entry : loader.entrySet()) {
            set(String.valueOf(entry.getKey()), entry.getValue());
        }
    }

    @Override
    public void store(Writer writer) throws IOException {
        Properties wrapper = new Properties();
        copy(wrapper, this, new ArrayDeque<>(), pathSplitter());
        wrapper.store(writer, null);
    }

    private static String path(ArrayDeque<String> deque, String pt, char splitter) {
        StringBuilder s = new StringBuilder();
        for (String sm : deque) {
            s.append(sm).append(splitter);
        }
        return s.append(pt).toString();
    }

    private static void copy(Properties wrapper, ConfigurationSection section, ArrayDeque<String> path, char splitter) {
        for (var entry : section.values().entrySet()) {
            var val = entry.getValue();
            var k = entry.getKey();
            if (val instanceof String || val instanceof Number || val instanceof Boolean) {
                wrapper.setProperty(path(path, k, splitter), val.toString());
            } else if (val instanceof ConfigurationSection) {
                path.addLast(k);
                copy(wrapper, (ConfigurationSection) val, path, splitter);
                path.removeLast();
            } else if (val != null) {
                throw new UnsupportedOperationException("Unsupported " + val + ", " + val.getClass());
            }
        }
    }
}
