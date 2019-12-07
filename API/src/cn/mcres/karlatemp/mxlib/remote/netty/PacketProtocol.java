/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketProtocol.java@author: karlatemp@vip.qq.com: 19-11-29 下午6:40@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.remote.netty;

public class PacketProtocol {
    private String name;

    protected PacketProtocol(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PacketProtocol[" + name + "]";
    }

    public static PacketProtocol newProtocol(String name) {
        return new PacketProtocol(name);
    }
}
