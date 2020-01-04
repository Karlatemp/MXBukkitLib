/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Tools.java@author: karlatemp@vip.qq.com: 2019/12/29 下午2:04@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.configuation.bukkit;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class Tools {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object dump(Object o) {
        if (o instanceof ConfigurationSection) {
            return dump(((ConfigurationSection) o).getValues(false));
        } else if (o instanceof Collection) {
            ArrayList aw = new ArrayList();
            for (Object oc : (Collection<?>) o) {
                aw.add(dump(oc));
            }
            return aw;
        } else if (o instanceof Map) {
            LinkedHashMap l = new LinkedHashMap();
            for (Map.Entry me : ((Map<?, ?>) o).entrySet()) {
                l.put(dump(me.getKey()), dump(me.getValue()));
            }
            return l;
        } else if (o instanceof Number || o instanceof String || o instanceof Boolean || o instanceof Character) {
            return o;
        } else if (o instanceof ConfigurationSerializable) {
            return ((ConfigurationSerializable) o).serialize();
        } else {
            return null;
        }
    }

    public static Map<String, Object> dump(Map<String, Object> values) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> w : values.entrySet()) {
            result.put(w.getKey(), dump(w.getValue()));
        }
        return result;
    }

    public static Object load(Map datas) {
        if (datas.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
            LinkedHashMap<String, Object> oox = new LinkedHashMap<>();
            for (Map.Entry me : ((Map<?, ?>) datas).entrySet()) {
                oox.put(String.valueOf(me.getKey()), me.getValue());
            }
            return ConfigurationSerialization.deserializeObject(oox);
        }
        return datas;
    }

    public static void put(ConfigurationSection c, Map values) {
        for (Map.Entry<Object, Object> me : ((Map<Object, Object>) values).entrySet()) {
            Object k = me.getKey();
            String sk = String.valueOf(k);
            Object v = me.getValue();
            if (v instanceof Map) {
                ConfigurationSection cs = c.getConfigurationSection(sk);
                if (cs == null) {
                    cs = c.createSection(sk);
                }
                put(cs, (Map) v);
            } else {
                c.set(sk, v);
            }
        }
    }
}
