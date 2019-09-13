/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SectionAdapterFactory.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:47@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.configuration;

import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class SectionAdapterFactory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        com.google.gson.internal.bind.TypeAdapters.newFactory(String.class, null);
        Class<? super T> c = type.getRawType();
        if (ConfigurationSection.class.isAssignableFrom(c)) {
            return new Adapter(gson);
        }
        return null;
    }

    private static class Adapter extends TypeAdapter {

        private final Gson gson;

        public Adapter(Gson gson) {
            this.gson = gson;
        }

        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public void write(JsonWriter out, Object value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else if (value instanceof String) {
                out.value((String) value);
            } else if (value instanceof Boolean) {
                out.value((Boolean) value);
            } else if (value instanceof Number) {
                out.value((Number) value);
            } else if (value instanceof ConfigurationSection) {
                out.beginObject();
                Map<String, Object> values = ((ConfigurationSection) value).getValues(false);
                for (Map.Entry<String, Object> so : values.entrySet()) {
                    out.name(so.getKey());
                    write(out, so.getValue());
                }
                out.endObject();
            } else if (value instanceof List) {
                out.beginArray();
                for (Object o : (List) value) {
                    write(out, o);
                }
                out.endArray();
            } else {
                Class c = value.getClass();
                out.beginObject();
                out.name(ConfigurationSerialization.SERIALIZED_TYPE_KEY)
                        .value(c.getName()).name("-");
                gson.getAdapter(c).write(out, value);
                out.endObject();
            }
        }

        @Override
        public Object read(JsonReader in) throws IOException {
            switch (in.peek()) {
                case NULL: {
                    in.nextNull();
                    return null;
                }
                case NUMBER: {
                    return in.nextDouble();
                }
                case STRING: {
                    return in.nextString();
                }
                case BOOLEAN: {
                    return in.nextBoolean();
                }
                case BEGIN_ARRAY: {
                    List l = new ArrayList();
                    in.beginArray();
                    while (in.hasNext()) {
                        l.add(read(in));
                    }
                    in.endArray();
                    return l;
                }
                case BEGIN_OBJECT: {
                    in.beginObject();
                    try {
                        if (in.hasNext()) {
                            String n1 = in.nextName();
                            Object v = read(in);
                            if (n1.equals(ConfigurationSerialization.SERIALIZED_TYPE_KEY)
                                    && in.hasNext()) {
                                String n2 = in.nextName();
                                if (n2.equals("-")) {
                                    try {
                                        Class<?> c = Class.forName(String.valueOf(v));
                                        return gson.getAdapter(c).read(in);
                                    } catch (ClassNotFoundException | NoClassDefFoundError ex) {
                                        throw new IOException(ex.getLocalizedMessage(), ex);
                                    }
                                } else {
                                    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                                    map.put(n1, v);
                                    map.put(n2, read(in));
                                    while (in.hasNext()) {
                                        map.put(in.nextName(), read(in));
                                    }
                                    return map;
                                }
                            } else {
                                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                                map.put(n1, v);
                                while (in.hasNext()) {
                                    map.put(in.nextName(), read(in));
                                }
                                return map;
                            }
                        } else {
                            return new HashMap<>();
                        }
                    } finally {
                        in.endObject();
                    }
                }
                default: {
                    in.skipValue();
                }
            }
            return null;
        }
    }

}
