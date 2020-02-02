/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: JsonConfiguration.java@author: karlatemp@vip.qq.com: 2020/1/25 下午8:28@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.config;

import cn.mcres.karlatemp.mxlib.tools.UnclosedReader;
import cn.mcres.karlatemp.mxlib.tools.UnclosedWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class JsonConfiguration extends MemoryConfiguration {
    private JsonConfiguration(Void aVoid) {
        super(aVoid);
    }

    public JsonConfiguration() {
    }

    @Override
    protected MemoryConfiguration newInstance() {
        return new JsonConfiguration((Void) null);
    }

    @Override
    public void load(Reader stream) throws IOException {
        try (JsonReader reader = new JsonReader(new UnclosedReader(stream))) {
            load(this, reader);
        }
    }

    private static void load(ConfigurationSection configuration, JsonReader reader) throws IOException {
        reader.beginObject();
        var use = configuration.useSplitter();
        configuration.useSplitter(false);
        while (reader.hasNext()) {
            var name = reader.nextName();
            Object val = read(reader);
            configuration.set(name, val);
        }
        configuration.useSplitter(use);
        reader.endObject();
    }

    private static Object read(JsonReader reader) throws IOException {
        switch (reader.peek()) {
            case NULL:
                reader.nextNull();
                return null;
            case BEGIN_ARRAY:
                var al = new ArrayList<>();
                reader.beginArray();
                while (reader.hasNext()) {
                    al.add(read(reader));
                }
                reader.endArray();
                return al;
            case BEGIN_OBJECT:
                reader.beginObject();
                var conf = new MemoryConfigurationSection();
                conf.useSplitter(false);
                while (reader.hasNext()) {
                    var name = reader.nextName();
                    var val = read(reader);
                    conf.set(name, val);
                }
                conf.useSplitter(true);
                reader.endObject();
                return conf;
            case STRING:
                return reader.nextString();
            case BOOLEAN:
                return reader.nextBoolean();
            case NUMBER:
                return reader.nextDouble();
        }
        return null;
    }

    @Override
    public void store(Writer writer) throws IOException {
        try (var jw = new JsonWriter(new UnclosedWriter(writer))) {
            jw.setHtmlSafe(false);
            jw.setSerializeNulls(false);
            jw.setIndent("  ");
            jw.setLenient(true);
            write(jw, this, "  ");
        }
    }

    private static void write(JsonWriter jw, Object val, String indent) throws IOException {
        if (val instanceof ConfigurationSection) {
            write(jw, ((ConfigurationSection) val).values(), indent);
        } else if (val instanceof Collection<?>) {
            var c = (Collection<?>) val;
            jw.beginArray();
            var oi = indent;
            if (c.size() < 5) {
                jw.setIndent(indent = "");
            }
            for (var o : c) {
                write(jw, o, indent);
            }
            jw.setIndent(oi);
            jw.endArray();
        } else if (val instanceof Map<?, ?>) {
            var map = (Map<?, ?>) val;
            var oi = indent;
            jw.beginObject();
            if (map.size() < 3) {
                jw.setIndent(indent = "");
            }
            for (var entry : map.entrySet()) {
                jw.name(String.valueOf(entry.getKey()));
                write(jw, entry.getValue(), indent);
            }
            jw.endObject();
            jw.setIndent(oi);
        } else if (val instanceof String) {
            jw.value((String) val);
        } else if (val instanceof Number) {
            jw.value((Number) val);
        } else if (val instanceof Boolean) {
            jw.value((Boolean) val);
        } else if (val == null) {
            jw.nullValue();
        } else if (val.getClass().isArray()) {
            jw.beginArray();
            int size = Array.getLength(val);
            var oi = indent;
            if (size < 5) {
                jw.setIndent(indent = "");
            }
            for (int i = 0; i < size; i++) {
                write(jw, Array.get(jw, i), indent);
            }
            jw.setIndent(oi);
            jw.endArray();
        } else {
            throw new UnsupportedEncodingException(val + ", " + val.getClass());
        }
    }
}