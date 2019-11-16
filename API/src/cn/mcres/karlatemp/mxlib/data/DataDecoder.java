/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DataDecoder.java@author: karlatemp@vip.qq.com: 19-11-16 下午12:45@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data;

import cn.mcres.karlatemp.mxlib.data.attribute.AttributeMap;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface DataDecoder<T> extends DataInputHandler {
    Predicate<DataHandler> DECODE_FILTER = INPUT_FILTER;

    Object decode(@NotNull DataProcessContext context, T param, @NotNull AttributeMap attributes) throws Exception;
}
