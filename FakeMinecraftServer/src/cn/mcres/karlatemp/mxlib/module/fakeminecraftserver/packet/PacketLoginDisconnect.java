/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketLoginDisconnect.java@author: karlatemp@vip.qq.com: 19-12-7 下午1:47@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.BaseComponentSerializer;
import net.md_5.bungee.chat.ComponentSerializer;

public class PacketLoginDisconnect extends PacketToClient<PacketLoginDisconnect>{
    public BaseComponent[] reason;

    @Override
    public void read(PacketDataSerializer serializer) {
        reason = ComponentSerializer.parse(serializer.readString());
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.writeString(ComponentSerializer.toString(reason));
    }
}
