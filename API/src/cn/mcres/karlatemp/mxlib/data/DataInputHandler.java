/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DataInputHandler.java@author: karlatemp@vip.qq.com: 19-11-16 下午12:20@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data;

import java.util.function.Predicate;

public interface DataInputHandler extends DataHandler {
    Predicate<DataHandler> INPUT_FILTER = x -> x instanceof DataInputHandler;
}
