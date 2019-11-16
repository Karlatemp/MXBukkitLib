/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: FiltedMessageEncoder.java@author: karlatemp@vip.qq.com: 19-11-16 下午5:30@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl;

import cn.mcres.karlatemp.mxlib.reflect.Reflect;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterMessageEncoder extends MessageToByteEncoder<Object> {
    List<MessageToByteEncoder> encoders;

    public FilterMessageEncoder(@NotNull List<MessageToByteEncoder> encoders) {
        super(Object.class);
        this.encoders = encoders;
    }

    public FilterMessageEncoder(MessageToByteEncoder... encoders) {
        super(Object.class);
        this.encoders = new ArrayList<>(Arrays.asList(encoders));
    }

    @Override
    public boolean acceptOutboundMessage(Object o) throws Exception {
        for (MessageToByteEncoder e : encoders) {
            if (e.acceptOutboundMessage(o))
                return true;
        }
        return false;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        for (MessageToByteEncoder e : encoders) {
            if (e.acceptOutboundMessage(o)) {
                Reflect.ofObject(e).invoke("encode", new Class[]{
                        ChannelHandlerContext.class,
                        Object.class,
                        ByteBuf.class
                }, channelHandlerContext, o, byteBuf);
                return;
            }
        }
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
        for (MessageToByteEncoder e : encoders) {
            if (e.acceptOutboundMessage(o)) {
                e.write(channelHandlerContext, o, channelPromise);
                return;
            }
        }
        channelHandlerContext.write(o, channelPromise);
    }
}
