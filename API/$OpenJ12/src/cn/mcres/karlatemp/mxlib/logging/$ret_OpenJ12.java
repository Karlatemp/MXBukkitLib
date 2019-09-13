/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: $ret_OpenJ12.java@author: karlatemp@vip.qq.com: 19-9-12 下午5:28@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

class $ret_OpenJ12 implements MessageFactory$return$inter {
    @Override
    public String getModuleName(StackTraceElement elm) {
        return elm.getModuleName();
    }

    @Override
    public String getClassLoaderName(StackTraceElement elm) {
        return elm.getClassLoaderName();
    }

    @Override
    public String getModuleVersion(StackTraceElement elm) {
        return elm.getModuleVersion();
    }
}