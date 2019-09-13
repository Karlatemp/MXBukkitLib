/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MessageFactory$return.java@author: karlatemp@vip.qq.com: 19-9-12 下午5:28@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

@SuppressWarnings("Duplicates")
class MessageFactory$return implements MessageFactory$return$inter {
    static MessageFactory$return$inter ret;

    static {
        ret = new MessageFactory$return();
        try {
            Class<StackTraceElement> c = StackTraceElement.class;
            c.getMethod("getModuleName");
            ret = new $ret_OpenJ12();
        } catch (Throwable thr) {
        }
    }
}
