/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DataOutputHandler.java@author: karlatemp@vip.qq.com: 19-11-16 下午12:21@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data;

import java.util.function.Predicate;

public interface DataOutputHandler extends DataHandler {
    Predicate<DataHandler> OUTPUT_FILTER = x -> x instanceof DataOutputHandler;
}
