/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketLoginInEncryptionResponse.java@author: karlatemp@vip.qq.com: 19-12-7 下午1:56@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;

public class PacketLoginInEncryptionResponse extends PacketToServer<PacketLoginInEncryptionResponse> {
    public byte[] shared_secret;
    public byte[] verify_token;

    @Override
    public void read(PacketDataSerializer serializer) {
        shared_secret = serializer.readByteArray();
        verify_token = serializer.readByteArray();
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeByteArray(shared_secret);
        serializer.writeByteArray(verify_token);
    }
}
