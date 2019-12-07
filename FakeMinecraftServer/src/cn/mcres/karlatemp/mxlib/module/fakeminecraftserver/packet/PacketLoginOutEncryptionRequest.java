/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketLoginOutEncryptionRequest.java@author: karlatemp@vip.qq.com: 19-12-7 下午1:53@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;

public class PacketLoginOutEncryptionRequest extends PacketToClient<PacketLoginOutEncryptionRequest> {
    public String server_id;
    public byte[] public_key;
    public byte[] verify_token;

    @Override
    public void read(PacketDataSerializer serializer) {
        server_id = serializer.readString(20);
        public_key = serializer.readByteArray();
        verify_token = serializer.readByteArray();
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeString(server_id);
        serializer.writeByteArray(public_key);
        serializer.writeByteArray(verify_token);
    }
}
