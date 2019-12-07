/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketEncrypter.java@author: karlatemp@vip.qq.com: 19-12-7 下午2:27@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.internal;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.crypto.Cipher;

public class PacketEncrypter extends MessageToByteEncoder<ByteBuf> {
    private final PacketEncryptionHandler a;

    public PacketEncrypter(Cipher cipher) {
        this.a = new PacketEncryptionHandler(cipher);
    }

    protected void encode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, ByteBuf bytebuf1) throws Exception {
        this.a.a(bytebuf, bytebuf1);
    }
}
