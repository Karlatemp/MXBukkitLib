/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NetWorkManager.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:59@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn;

import cn.mcres.karlatemp.mxlib.module.mrcn.packet.PacketListener;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;
import io.netty.channel.*;

public class NetWorkManager extends ChannelInboundHandlerAdapter {
    public PacketListener handler;
    public final Channel channel;
    private boolean closed;

    public NetWorkManager(Channel channel) {
        this.channel = channel;
        channel.closeFuture().addListener(w -> {
            closed = true;
        });
    }

    public void writePacket(Packet<?> packet) {
        writePacket(packet, null);
    }

    public void writePacket(Packet<?> packet, ChannelFutureListener listener) {
        // System.out.println(handler.getName() + " Output: " + packet);
        final ChannelFuture future = channel.writeAndFlush(packet);
        if (listener != null) {
            future.addListener(listener);
        }
        future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Packet<?>) {
            // System.out.println(handler.getName() + " Input: " + msg);
            PacketListener.post(handler, this, (Packet<?>) msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (closed || (!channel.isOpen())) return;
        closed = true;
        handler.exceptionCaught(this, ctx, cause);
        channel.close();
    }

    public boolean isOpen() {
        return (!closed) && channel.isOpen();
    }

    public void disconnect(String s) throws Exception {
        if (closed || (!channel.isOpen())) return;
        closed = true;
        handler.disconnect(this, s);
        channel.close();
    }
}
