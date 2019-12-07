/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketLoginOutSetCompression.java@author: karlatemp@vip.qq.com: 19-12-7 下午2:38@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;

public class PacketLoginOutSetCompression extends PacketToClient<PacketLoginOutSetCompression> {
    /**
     * Maximum size of a packet before it is compressed
     */
    public int threshold;

    @Override
    public void read(PacketDataSerializer serializer) {
        threshold = serializer.readVarInt();
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeVarInt(threshold);
    }
}
