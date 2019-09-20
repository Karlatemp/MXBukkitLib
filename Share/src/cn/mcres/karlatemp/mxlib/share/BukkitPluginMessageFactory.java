/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitPluginMessageFactory.java@author: karlatemp@vip.qq.com: 19-9-20 下午7:42@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.logging.MessageFactoryAnsi;

public class BukkitPluginMessageFactory extends MessageFactoryAnsi {
    @Override
    protected Class<?> forName(String name) throws ClassNotFoundException {
        try {
            return Class.forName(name, false, getClass().getClassLoader());
        } catch (ClassNotFoundException nfe) {
            return super.forName(name);
        }
    }
}
