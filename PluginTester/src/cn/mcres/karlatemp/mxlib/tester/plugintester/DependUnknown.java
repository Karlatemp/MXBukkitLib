/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DependUnknown.java@author: karlatemp@vip.qq.com: 19-9-27 下午5:40@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester;

import cn.mcres.karlatemp.mxlib.annotations.AutoInstall;
import cn.mcres.karlatemp.mxlib.annotations.Depend;
import cn.mcres.karlatemp.mxlib.exceptions.MessageDump;

import java.util.logging.Level;

@AutoInstall
@Depend("aowjex98weoadjioawiue89ascmiasd")
public class DependUnknown {
    static {
        PluginTester.plugin.getLogger().warning("[ERROR] [DU] This class should not load.");
        PluginTester.plugin.getLogger().log(Level.WARNING, null, new MessageDump("Stack Trace Dump."));
    }
}
