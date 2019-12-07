/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketToClient.java@author: karlatemp@vip.qq.com: 19-11-29 下午11:04@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.karlatemp.mxlib.remote.netty.Packet;
import io.netty.channel.ChannelHandlerContext;

public abstract class PacketToClient<T extends PacketToClient<T>> implements Packet<T> {
    protected final ChannelHandlerContext context = findContext();

    protected ChannelHandlerContext findContext() {
        return null;
    }
}
