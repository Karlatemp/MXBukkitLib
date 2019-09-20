/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LegacyAutoConfig.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.legacy;

import cn.mcres.gyhhy.MXLib.Core;
import cn.mcres.gyhhy.MXLib.event.NetURIConnectEvent;
import cn.mcres.gyhhy.MXLib.system.VMHelper;
import cn.mcres.gyhhy.MXLib.yggdrasil.Yggdrasil;
import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.annotations.Bean;
import cn.mcres.karlatemp.mxlib.annotations.Configuration;
import cn.mcres.karlatemp.mxlib.bean.IEnvironmentFactory;
import cn.mcres.karlatemp.mxlib.impl.UpdateModule;
import cn.mcres.karlatemp.mxlib.impl.VersionInfo;
import cn.mcres.karlatemp.mxlib.network.NetWorkManager;
import cn.mcres.karlatemp.mxlib.share.MXBukkitLibPluginStartup;
import cn.mcres.karlatemp.mxlib.share.$MXBukkitLibConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.scheduler.BukkitScheduler;

import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.logging.Logger;

@Configuration
public class LegacyAutoConfig {
    static final NListener listener = new NListener();

    @Bean
    void boot(BukkitScheduler bs) {
        Core.setPlugin(MXBukkitLibPluginStartup.plugin);
        if ($MXBukkitLibConfiguration.configuration.logger.enable) {
            LoggerInject.inject();
        }
        if ($MXBukkitLibConfiguration.configuration.events.network.println) {
            NetURIConnectEvent.getHandlerList().register(new RegisteredListener(new Listener() {
            }, (a, b) -> {
                if (b instanceof NetURIConnectEvent) {
                    NetURIConnectEvent ev = (NetURIConnectEvent) b;
                    final Plugin plugin = ev.getPlugin();
                    final String name;
                    final Object toString;
                    final Logger logger;
                    if (plugin == null) {
                        toString = name = "<unknown>";
                        logger = MXBukkitLibPluginStartup.plugin.getLogger();
                    } else {
                        toString = plugin;
                        name = plugin.getName();
                        logger = plugin.getLogger();
                    }
                    if ($MXBukkitLibConfiguration.configuration.events.network.skips.contains(name))
                        return;
                    logger.info("[NetWorkListener] " + toString + " was connect to " + ev.getURI());
                }
            }, EventPriority.MONITOR, MXBukkitLibPluginStartup.plugin, false));
        }
        if ($MXBukkitLibConfiguration.configuration.events.network.enable) {
            try {
                new URL("http://www.bilibili.com");
                new URL("https://www.bilibili.com"); // Load Protocol
            } catch (Throwable thr) {
            }
            MXBukkitLibPluginStartup.hooks.add(NetWorkManager::install);
            NetWorkManager.install(true);
            if (!NetWorkManager.containsListener(listener)) {
                NetWorkManager.registerListener(listener);
            }
        }
        if ($MXBukkitLibConfiguration.configuration.update.enable) {
            UpdateModule.run(MXBukkitLibPluginStartup.plugin);
            bs.runTaskAsynchronously(MXBukkitLibPluginStartup.plugin, () -> VersionInfo.check(MXBukkitLibPluginStartup.plugin));
        }
    }

    @Bean
    Instrumentation instrumentation() {
        try {
            return VMHelper.getHelper().getInstrumentation();
        } catch (Throwable thr) {
            MXBukkitLib.getLogger().printStackTrace(thr);
            return null;
        }
    }

    @Bean
    Yggdrasil yggdrasil() {
        return Yggdrasil.getServerYggdrasil();
    }
}
