/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketDecrypter.java@author: karlatemp@vip.qq.com: 19-12-7 下午2:26@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.internal;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import javax.crypto.Cipher;

public class PacketDecrypter extends MessageToMessageDecoder<ByteBuf> {
    private final PacketEncryptionHandler a;

    public PacketDecrypter(Cipher cipher) {
        this.a = new PacketEncryptionHandler(cipher);
    }

    protected void decode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, List<Object> list) throws Exception {
        list.add(this.a.a(channelhandlercontext, bytebuf));
    }
}
