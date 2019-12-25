/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DataEncoder.java@author: karlatemp@vip.qq.com: 19-11-16 下午12:44@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data;

import cn.mcres.karlatemp.mxlib.data.attribute.AttributeMap;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface DataEncoder<T> extends DataOutputHandler {
    Predicate<DataHandler> ENCODE_FILTER = OUTPUT_FILTER;

    Object encode(@NotNull DataProcessContext context, T param, @NotNull AttributeMap attributes) throws Exception;
}
