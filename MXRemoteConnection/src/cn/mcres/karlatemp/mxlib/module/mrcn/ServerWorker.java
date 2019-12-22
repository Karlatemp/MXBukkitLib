/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ServerWorker.java@author: karlatemp@vip.qq.com: 19-12-16 下午11:13@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn;

import cn.mcres.karlatemp.mxlib.module.mrcn.packet.*;
import io.netty.channel.Channel;

public class ServerWorker implements PacketListener {
    public boolean preLoginOk(Channel client, PacketLoginPreLogin packet) {
        return true;
    }

    @Override
    public void disconnect(NetWorkManager netWorkManager, String s) {
        netWorkManager.writePacket(new PacketSystemDisconnect(s));
    }

    public ServerWorker auth(PacketLoginAuth auth) {
        return this;
    }

    public void initAuthResponse(PacketLoginAuthResponse response) {
    }
}
