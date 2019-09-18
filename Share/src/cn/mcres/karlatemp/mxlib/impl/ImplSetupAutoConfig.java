/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ImplSetupAutoConfig.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.annotations.Bean;
import cn.mcres.karlatemp.mxlib.annotations.Configuration;
import cn.mcres.karlatemp.mxlib.annotations.Resource;
import cn.mcres.karlatemp.mxlib.bean.IInjector;
import cn.mcres.karlatemp.mxlib.bukkit.TitleAPI;
import cn.mcres.karlatemp.mxlib.logging.BukkitMessageFactory;
import cn.mcres.karlatemp.mxlib.logging.IMessageFactory;
import cn.mcres.karlatemp.mxlib.share.MXBukkitLibPluginStartup;
import cn.mcres.karlatemp.mxlib.tools.IObjectCreator;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.logging.Level;

@SuppressWarnings("unused")
@Configuration
public class ImplSetupAutoConfig {
    private String nms;
    private IObjectCreator creator;

    ImplSetupAutoConfig() {
        IObjectCreator creator = MXBukkitLib.getBeanManager().getBeanNonNull(IObjectCreator.class);
        this.creator = creator;
        MXBukkitLib.getBeanManager().getBeanNonNull(IInjector.class).inject(this);
        Server ser = Bukkit.getServer();
        String cn = ser.getClass().getName();
        // org.bukkit.craftbukkit.
        String pre = "org.bukkit.craftbukkit.";
        MXBukkitLib.debug("[ImplSetupAutoConfig] Server Class = " + cn);
        if (cn.startsWith(pre)) {
            String ct = cn.substring(pre.length());
            MXBukkitLib.debug("[ImplSetupAutoConfig] CT = " + ct);
            int ed = ct.indexOf('.');
            MXBukkitLib.debug("[ImplSetupAutoConfig] ED = " + ed);
            // v1_13_R1
            String cut = ct.substring(0, ed);
            MXBukkitLib.debug("[ImplSetupAutoConfig] MT = " + cut);
            if (!cut.isEmpty()) { // Version Checkup
                MXBukkitLib.debug("[ImplSetupAutoConfig] NMS = " + cut);
                if (cut.charAt(0) == 'v') {
                    String nnn = cut.substring(1);
                    String[] cued = nnn.split("_");
                    if (cued.length == 3) {
                        String last = cued[2];
                        MXBukkitLib.debug("[ImplSetupAutoConfig] Lastest = " + last);
                        if (!last.isEmpty()) {
                            if (last.charAt(0) == 'R') {
                                MXBukkitLib.debug("[ImplSetupAutoConfig] Open R");
                                if (Toolkit.isNum(cued[0]) && Toolkit.isNum(cued[1])) {
                                    MXBukkitLib.debug("[ImplSetupAutoConfig] Number OK 0,1");
                                    if (Toolkit.isNum(last.substring(1))) {
                                        MXBukkitLib.debug("[ImplSetupAutoConfig] Number OK 2");
                                        nms = nnn;
                                        load(nnn);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static TitleAPI titleapi;

    private void load(String nmsl) {
        String s = "cn.mcres.karlatemp.mxlib.impl.titleapi.TitleAPI_";
        try {
            MXBukkitLib.debug("Loading TitleAPI");
            titleapi = (TitleAPI) creator.newInstance(Class.forName(s + nmsl));
            MXBukkitLib.debug("Loaded " + titleapi);
        } catch (Exception e) {
            MXBukkitLibPluginStartup.plugin.getLogger().log(Level.SEVERE, null, e);
        }
    }

    @Bean
    TitleAPI titleAPI(TitleAPI old) {
        if (old != null) return old;
        return titleapi;
    }

    @Bean
    Server 卢本伟吐痰上香服() {
        return Bukkit.getServer();
    }

    @Bean
    BukkitScheduler bsch() {
        return Bukkit.getScheduler();
    }

    @Bean
    PluginManager pm() {
        return Bukkit.getPluginManager();
    }

    @Bean
    ConsoleCommandSender 控你马台() {
        return Bukkit.getConsoleSender();
    }

    @Bean
    ScoreboardManager 抖SM() {
        return Bukkit.getScoreboardManager();
    }

    @Bean
    ServicesManager SM二号() {
        return Bukkit.getServicesManager();
    }

    @Bean
    IMessageFactory factory() {
        return new BukkitMessageFactory();
    }
}
