/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DataEncrypter.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:04@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.internal;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.crypto.Cipher;

public class DataEncrypter extends MessageToByteEncoder<ByteBuf> {

    private final Cipher cipher;

    public DataEncrypter(Cipher cipher) {
        this.cipher = cipher;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        if (!msg.isReadable()) {
            return;
        }
        byte[] data = new byte[msg.readableBytes()];
        msg.readBytes(data);
        out.writeBytes(RSAHelper.encrypt(data, cipher));
    }
}
