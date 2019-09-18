/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ICommandSender.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.cmd;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;

@ProhibitBean
public interface ICommandSender extends IPermissible {
    String getName();

    void sendMessage(String[] messages);

    void sendMessage(String message);
}
