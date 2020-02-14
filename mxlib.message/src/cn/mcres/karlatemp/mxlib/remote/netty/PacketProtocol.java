/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/PacketProtocol.java
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
