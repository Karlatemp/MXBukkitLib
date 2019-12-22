/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketLoginAuthResponse.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:29@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;

public class PacketLoginAuthResponse implements Packet<PacketLoginAuthResponse> {
    public boolean allow_set_permission;
    public boolean is_op, is_sudo;
    public String server_name = "";
    public String server_version = "";
    public String bukkit_name = "";
    public String bukkit_version = "";

    @Override
    public void read(PacketDataSerializer serializer) {
        allow_set_permission = serializer.readBoolean();
        is_op = serializer.readBoolean();
        is_sudo = serializer.readBoolean();
        server_name = serializer.readString();
        server_version = serializer.readString();
        bukkit_name = serializer.readString();
        bukkit_version = serializer.readString();
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeBoolean(allow_set_permission);
        serializer.writeBoolean(is_op);
        serializer.writeBoolean(is_sudo);
        serializer.writeString(server_name);
        serializer.writeString(server_version);
        serializer.writeString(bukkit_name);
        serializer.writeString(bukkit_version);
    }
}
