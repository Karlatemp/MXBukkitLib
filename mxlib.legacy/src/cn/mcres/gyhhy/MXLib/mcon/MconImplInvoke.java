/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MconImplInvoke.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.mcon;

public class MconImplInvoke {

    static final TListener tl = new TListener();

    public static void start(String pwd, int port) {
        tl.setPort(port);
        tl.setPwd(pwd.getBytes(Types.UTF_8));
        tl.start();
    }

    public static void close() {
        tl.shutdown();
    }
}
