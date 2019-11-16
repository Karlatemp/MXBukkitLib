/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AttributeMap.java@author: karlatemp@vip.qq.com: 19-11-16 上午12:47@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data.attribute;

import org.jetbrains.annotations.NotNull;

public interface AttributeMap {
    <T> Attribute<T> attr(@NotNull AttributeKey<T> key);
}
