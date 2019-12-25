/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: JsonConstructor.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.Reader;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

/**
 * @version 1.10
 */
public class JsonConstructor {

    private final Gson gson;

    public JsonConstructor(boolean pretty) {
        GsonBuilder gb = new GsonBuilder();
        if (pretty) {
            gb.setPrettyPrinting();
        }
        gb.registerTypeAdapterFactory(new SectionAdapterFactory());
        this.gson = gb.create();
    }

    @Deprecated
    public JsonConstructor(@NotNull Gson g) {
        this.gson = g;
    }

    public Gson getGson() {
        return gson;
    }

    protected String build(JsonConfiguration jc, Map<String, Object> map) {
        return gson.toJson(jc);
    }

    protected void load(Reader reader, JsonConfiguration jc) throws JsonIOException, JsonSyntaxException {
        Map<String, Object> mmp = gson.fromJson(reader, (java.lang.reflect.Type) ConfigurationSection.class);
        load(jc, mmp);
    }

    protected void load(String contents, JsonConfiguration jc) throws JsonSyntaxException {
        Map<String, Object> mmp = gson.fromJson(contents, (java.lang.reflect.Type) ConfigurationSection.class);
        load(jc, mmp);
    }

    private void load(ConfigurationSection cs, Map<String, Object> oo) {
        if (oo == null) {
            return;
        }

        for (Map.Entry<String, Object> ox : oo.entrySet()) {
            String key = ox.getKey();
            Object value = ox.getValue();
            if (value instanceof Map) {
                load(cs.createSection(key), (Map) value);
            } else {
                cs.set(key, value);
            }
        }
    }

    protected String buildHeader(JsonConfiguration jc) {
        return "";
    }
}
