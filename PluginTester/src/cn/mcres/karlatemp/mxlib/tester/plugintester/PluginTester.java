/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PluginTester.java@author: karlatemp@vip.qq.com: 19-9-27 下午5:15@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester;

import cn.mcres.karlatemp.mxlib.MXLib;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginTester extends JavaPlugin {
    public static PluginTester plugin;

    public PluginTester() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        getLogger().info("Booting from MXLib.");
        MXLib.start(getClass());
    }
}
