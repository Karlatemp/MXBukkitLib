/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MessageFactoryBukkitCommandSender.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

/**
 * 带Bukkit颜色支持的信息工厂
 */
public class MessageFactoryBukkitCommandSender extends MessageFactoryAnsi {
    @Override
    public String toConsole(String cons) {
        return cons;
    }
}
