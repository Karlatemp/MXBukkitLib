/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BCP.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:33@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public interface BCP {
    String toJSON();

    BaseComponent[] getComponents();

}
