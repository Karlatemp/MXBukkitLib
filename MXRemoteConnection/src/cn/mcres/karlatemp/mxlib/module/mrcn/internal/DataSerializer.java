/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DataSerializer.java@author: karlatemp@vip.qq.com: 19-12-16 下午9:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.internal;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;

public class DataSerializer extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg == null) {
            throw new EncoderException("Null Message.");
        }
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            int size = buf.readableBytes();
            out.ensureWritable(size + Integer.BYTES);
            out.writeInt(size);
            out.writeBytes(buf, buf.readerIndex(), size);
            return;
        }
        throw new EncoderException("Only support ByteBuf. But found " + msg.getClass());
    }
}
