/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BungeeChatPacket.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.chat;

import cn.mcres.karlatemp.mxlib.impl.chat.BCP;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class BungeeChatPacket implements BCP {
    private final BaseComponent[] bcs;

    public BungeeChatPacket(BaseComponent[] bcs) {
        this.bcs = bcs;
    }

    public String toJSON() {
        return ComponentSerializer.toString(bcs);
    }

    public BaseComponent[] getComponents() {
        return bcs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (BaseComponent bb : bcs) sb.append(bb);
        return sb.toString();
    }
}
