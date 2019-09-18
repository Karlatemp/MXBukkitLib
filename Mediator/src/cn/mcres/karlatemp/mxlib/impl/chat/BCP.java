/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BCP.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public interface BCP {
    String toJSON();

    BaseComponent[] getComponents();

}
