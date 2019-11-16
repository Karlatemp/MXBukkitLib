/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DataHandler.java@author: karlatemp@vip.qq.com: 19-11-15 下午11:51@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data;

import cn.mcres.karlatemp.mxlib.data.attribute.AttributeMap;
import org.jetbrains.annotations.NotNull;

public interface DataHandler {
    void translate(@NotNull DataProcessContext context, Object param, @NotNull AttributeMap attributes)
            throws Exception;
}
