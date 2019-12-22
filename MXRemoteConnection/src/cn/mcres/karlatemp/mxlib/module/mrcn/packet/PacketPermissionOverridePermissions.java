/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketPermissionOverridePermissions.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:33@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;

import java.util.ArrayList;
import java.util.Collection;

public class PacketPermissionOverridePermissions implements Packet<PacketPermissionOverridePermissions> {
    public boolean allow;
    public Collection<String> perm;

    @Override
    public void read(PacketDataSerializer serializer) {
        allow = serializer.readBoolean();
        int size = serializer.readVarInt();
        perm = new ArrayList<>();
        while (size-- > 0) {
            perm.add(serializer.readString());
        }
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeBoolean(allow);
        serializer.writeVarInt(perm.size());
        for(String p : perm){
            serializer.writeString(p);
        }
    }
}
