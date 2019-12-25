/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SimpleDataDecoder.java@author: karlatemp@vip.qq.com: 19-11-16 下午12:48@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data;

import cn.mcres.karlatemp.mxlib.data.attribute.AttributeMap;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleDataDecoder<T> extends SimpleDataInputHandler<T> implements DataDecoder<T> {
    @Override
    protected final Object doTranslate(@NotNull DataProcessContext context, T param, @NotNull AttributeMap attributes) throws Exception {
        return decode(context, param, attributes);
    }
}
