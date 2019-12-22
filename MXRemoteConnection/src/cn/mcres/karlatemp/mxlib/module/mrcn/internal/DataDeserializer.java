/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DataDeserializer.java@author: karlatemp@vip.qq.com: 19-12-16 下午9:46@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.internal;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class DataDeserializer extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        do {

            in.markReaderIndex();
            if (in.readableBytes() > Integer.BYTES) {
                int length = in.readInt();
                if (in.readableBytes() < length) {
                    in.resetReaderIndex();
                    return;
                }
                out.add(in.readBytes(length));
                continue;
            }
            in.resetReaderIndex();
            return;
        } while (true);
    }
}
