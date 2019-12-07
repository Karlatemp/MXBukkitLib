/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketListener.java@author: karlatemp@vip.qq.com: 19-11-29 下午9:56@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

/**
 * A Class listener.
 * <p>
 * listen packet like<pre>{@code
 * new PacketListener(){
 *     public void listen(PacketHandshakingInSetProtocol phis){
 *         System.out.println(phis);
 *     }
 * }
 * }</pre>
 */
public class PacketListener {
    /**
     * Class will gen by {@link PacketListenerGen}
     */
    private static void error() {
        throw new InternalError("This class must be override. Why Reflection Search It???");
    }

    protected void tick() {
    }

    static {
        error();
    }
}
