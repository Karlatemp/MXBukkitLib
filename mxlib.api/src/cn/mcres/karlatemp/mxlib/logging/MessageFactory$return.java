/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MessageFactory$return.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

@SuppressWarnings({"Duplicates", "Since15"})
class MessageFactory$return {
    static MessageFactory$return ret;


    String getModuleName(StackTraceElement elm) {
        return null;
    }

    String getClassLoaderName(StackTraceElement elm) {
        return null;
    }

    String getModuleVersion(StackTraceElement elm) {
        return null;
    }

    static {
        ret = new MessageFactory$return();
        try {
            Class<StackTraceElement> c = StackTraceElement.class;
            c.getMethod("getModuleName");
            ret = new MessageFactory$return() {
                @Override
                String getModuleName(StackTraceElement elm) {
                    return elm.getModuleName();
                }

                @Override
                String getClassLoaderName(StackTraceElement elm) {
                    return elm.getClassLoaderName();
                }

                @Override
                String getModuleVersion(StackTraceElement elm) {
                    return elm.getModuleVersion();
                }
            };
        } catch (Throwable thr) {
        }
    }
}
