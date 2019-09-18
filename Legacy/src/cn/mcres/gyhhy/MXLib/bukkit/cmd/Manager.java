/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Manager.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.bukkit.cmd;

@SuppressWarnings({"rawtypes"})
public class Manager {

    public static Options opt() {
        return new Options();
    }

    public static ExecuterEX exec(Class cx) {
        return opt().main(cx).loadType(Options.TYPE_COMMAND_IN_PACKAGE)
                .build();
    }

    public static ExecuterEX exec(Class cx, Object thiz) {
        return opt().main(cx).loadType(Options.TYPE_COMMAND_IN_ONE_CLASS)
                .thiz(thiz).build();
    }

}
