/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DataDecrypter.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:09@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.internal;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.crypto.Cipher;
import java.util.List;

public class DataDecrypter extends ByteToMessageDecoder {

    private final Cipher a;

    public DataDecrypter(Cipher cipher) {
        this.a = cipher;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!in.isReadable()) return;
        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        out.add(Unpooled.wrappedBuffer(RSAHelper.decrypt(data, a)));
    }
}
