/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DefaultAttributeMap.java@author: karlatemp@vip.qq.com: 19-11-16 上午12:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data.attribute;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class DefaultAttributeMap implements AttributeMap {
    private Map<AttributeKey, Attribute> attributes;

    public DefaultAttributeMap() {
        this(new ConcurrentHashMap<>());
    }

    public DefaultAttributeMap(Map<AttributeKey, Attribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public <T> Attribute<T> attr(@NotNull AttributeKey<T> key) {
        if (attributes.containsKey(key)) {
            return attributes.get(key);
        }
        Attribute a = new DefaultAttribute();
        attributes.put(key, a);
        return a;
    }
}
