/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ServerWorkBukkit.java@author: karlatemp@vip.qq.com: 19-12-17 下午11:31@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.bukkit;

import cn.mcres.karlatemp.mxlib.module.mrcn.ServerWorker;
import cn.mcres.karlatemp.mxlib.module.mrcn.packet.PacketLoginAuth;
import cn.mcres.karlatemp.mxlib.module.mrcn.packet.PacketLoginPreLogin;
import io.netty.channel.Channel;

public class ServerWorkBukkit extends ServerWorker {
    public String pre_login_passwd = "";
    public boolean remote;

    @Override
    public boolean preLoginOk(Channel client, PacketLoginPreLogin packet) {
        return pre_login_passwd.equals(packet.passwd);
    }

    @Override
    public ServerWorker auth(PacketLoginAuth auth) {

        return null;
    }
}
