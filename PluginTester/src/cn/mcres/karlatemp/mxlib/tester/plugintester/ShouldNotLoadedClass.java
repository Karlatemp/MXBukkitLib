/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ShouldNotLoadedClass.java@author: karlatemp@vip.qq.com: 19-9-27 下午5:23@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester;

import cn.mcres.karlatemp.mxlib.exceptions.MessageDump;

import java.util.logging.Level;

public class ShouldNotLoadedClass {
    static {
        PluginTester.plugin.getLogger().warning("[ERROR] [SNLC] This class should not load.");
        PluginTester.plugin.getLogger().log(Level.WARNING, null, new MessageDump("Stack Trace Dump."));
    }
}
