/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AutoListener.java@author: karlatemp@vip.qq.com: 19-9-27 下午5:19@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester;

import cn.mcres.karlatemp.mxlib.annotations.AutoInstall;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@AutoInstall
public class AutoListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent je) {
        je.getPlayer().sendMessage("[MXLib] [AutoListener] Listener registered.");
    }

    private void $init() {
        PluginTester.plugin.getLogger().info("[AutoListener] $init called.");
    }
}
