/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ClientWorker.java@author: karlatemp@vip.qq.com: 19-12-16 下午11:39@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn;

import cn.mcres.karlatemp.mxlib.module.mrcn.packet.PacketListener;
import cn.mcres.karlatemp.mxlib.module.mrcn.packet.PacketLoginAuth;
import cn.mcres.karlatemp.mxlib.module.mrcn.packet.PacketLoginAuthResponse;

public class ClientWorker implements PacketListener {
    public void login_disconnect(String msg) throws Exception {
    }

    public void login(PacketLoginAuth auth) {

    }

    public void authResponse(PacketLoginAuthResponse packet) {

    }

    public void doPermOverride(NetWorkManager manager) throws Exception {

    }

    public void doneLogin(NetWorkManager manager) {

    }
}
