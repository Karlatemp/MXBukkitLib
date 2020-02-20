/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/20 20:49:38
 *
 * MXLib/expend.plugin.base/I18n.java
 */

package cn.mcres.karlatemp.mxlib.plugin.base;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class I18n {
    private final Map<String, String> keys;

    public I18n(Map<String, String> keys) {
        this.keys = keys;
    }

    public String translate(String key, Object... params) {
        var format = keys.get(key);
        try {
            if (format != null) return String.format(format, params);
        } catch (Throwable thr) {
            return "<Exception in formatting: " + thr + ">";
        }
        return key;
    }

    public void put(String key, String format) {
        keys.put(key, format);
    }

    public String getFormat(String key) {
        return keys.get(key);
    }

    public static final String version = "1.0.0";

    public static I18n load(ConfigurationSection section) {
        var map = new HashMap<String, String>();
        for (var entry : section.getValues(true).entrySet()) {
            var key = entry.getKey();
            var val = entry.getValue();
            if (!(val instanceof ConfigurationSection)) {
                map.put(key, String.valueOf(val));
            }
        }
        return new I18n(map);
    }

    public I18n extend(Map<String, String> other) {
        for (var entry : other.entrySet()) {
            if (!keys.containsKey(entry.getKey())) {
                keys.put(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }
}
