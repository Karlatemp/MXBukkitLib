/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DataProcessContext.java@author: karlatemp@vip.qq.com: 19-11-16 上午12:23@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data;

import cn.mcres.karlatemp.mxlib.data.attribute.AttributeMap;

import java.util.function.Predicate;

public interface DataProcessContext {
    void writeTo(Object result, AttributeMap attributes);

    <T> T process(Predicate<DataHandler> filter, Object param, AttributeMap attributes)
            throws Exception;

    <T> T result();

}
