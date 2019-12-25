/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AttributeKey.java@author: karlatemp@vip.qq.com: 19-11-16 上午12:47@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data.attribute;

import cn.mcres.karlatemp.mxlib.data.utils.UniqueKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AttributeKey<T> extends UniqueKey<AttributeKey<T>> {
    private static final Map<String, AttributeKey> keys = new ConcurrentHashMap<>();

    public static AttributeKey valueOf(String name) {
        if (keys.containsKey(name)) return keys.get(name);
        return new AttributeKey(name);
    }

    private AttributeKey(String name) {
        super(keys, name);
    }
}
