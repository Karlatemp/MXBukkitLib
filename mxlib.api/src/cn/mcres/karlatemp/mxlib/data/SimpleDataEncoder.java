/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SimpleDataEncoder.java@author: karlatemp@vip.qq.com: 19-11-16 下午12:45@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data;

import cn.mcres.karlatemp.mxlib.data.attribute.AttributeMap;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleDataEncoder<T> extends SimpleDataOutputHandler<T> implements DataEncoder<T> {
    @Override
    protected final Object doTranslate(@NotNull DataProcessContext context, T param, @NotNull AttributeMap attributes) throws Exception {
        return encode(context, param, attributes);
    }
}
