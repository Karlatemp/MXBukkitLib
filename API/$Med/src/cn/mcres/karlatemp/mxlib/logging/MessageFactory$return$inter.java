/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MessageFactory$return$inter.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

interface MessageFactory$return$inter {

    default String getModuleName(StackTraceElement elm) {
        return null;
    }

    default String getClassLoaderName(StackTraceElement elm) {
        return null;
    }

    default String getModuleVersion(StackTraceElement elm) {
        return null;
    }
}
