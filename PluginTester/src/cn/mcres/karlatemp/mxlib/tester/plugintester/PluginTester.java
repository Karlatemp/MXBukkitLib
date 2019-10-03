/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PluginTester.java@author: karlatemp@vip.qq.com: 19-9-27 下午5:15@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester;

import cn.mcres.gyhhy.MXLib.bukkit.cmd.Executer;
import cn.mcres.gyhhy.MXLib.bukkit.cmd.Manager;
import cn.mcres.karlatemp.mxlib.MXLib;
import cn.mcres.karlatemp.mxlib.tester.plugintester.commands.Main;
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
        getCommand("mxtest").setExecutor(Manager.exec(Main.class));
    }
}
