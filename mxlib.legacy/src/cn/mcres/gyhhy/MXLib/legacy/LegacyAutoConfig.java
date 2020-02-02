/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LegacyAutoConfig.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.legacy;

import cn.mcres.gyhhy.MXLib.Core;
import cn.mcres.gyhhy.MXLib.event.EventHelper;
import cn.mcres.gyhhy.MXLib.event.NetURIConnectEvent;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.gyhhy.MXLib.system.VMHelper;
import cn.mcres.gyhhy.MXLib.yggdrasil.Yggdrasil;
import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.annotations.Bean;
import cn.mcres.karlatemp.mxlib.annotations.Configuration;
import cn.mcres.karlatemp.mxlib.bean.IEnvironmentFactory;
import cn.mcres.karlatemp.mxlib.event.Event;
import cn.mcres.karlatemp.mxlib.event.HandlerList;
import cn.mcres.karlatemp.mxlib.event.network.URLConnectEvent;
import cn.mcres.karlatemp.mxlib.files.DefaultFileListenerProvider;
import cn.mcres.karlatemp.mxlib.files.FileListenerProvider;
import cn.mcres.karlatemp.mxlib.impl.UpdateModule;
import cn.mcres.karlatemp.mxlib.impl.VersionInfo;
import cn.mcres.karlatemp.mxlib.module.chat.BungeeChatAPI;
import cn.mcres.karlatemp.mxlib.network.NetManager;
import cn.mcres.karlatemp.mxlib.network.NetWorkManager;
import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import cn.mcres.karlatemp.mxlib.share.MXBukkitLibPluginStartup;
import cn.mcres.karlatemp.mxlib.share.$MXBukkitLibConfiguration;
import cn.mcres.karlatemp.mxlib.tools.Pointer;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.scheduler.BukkitScheduler;

import java.lang.instrument.Instrumentation;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class LegacyAutoConfig {

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
                        logger = Bukkit.getLogger();
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
            URLConnectEvent.handlers.register(ev -> {
                Plugin plugin = null;
                var context = Toolkit.StackTrace.getClassContext();
                for (int i = 5; i < context.length; i++) {
                    var ct = context[i];
                    if (ct == HandlerList.class || Event.class.isAssignableFrom(ct)
                            || ct == NetManager.URLStreamHandler$class || WebHelper.class.isAssignableFrom(ct)
                            || ct == NetManager.URLStreamHandlerProvider$class) {
                        continue;
                    }
                    int i0 = i;
                    // MXBukkitLib.debug(() -> "ST: " + i0 + ", " + ct);
                    plugin = BukkitToolkit.getPluginByClass(ct);
                    if (plugin != null) break;
                }
                NetURIConnectEvent ev0 = new NetURIConnectEvent(URI.create(ev.getURL().toExternalForm()), plugin);
                ev0.setCancelled(ev.isCancelled());
                ev0.setCancelThrow(ev.getCancel());
                EventHelper.fireEvent(ev0);
                ev.setCancelled(ev0.isCancelled());
                if (ev0.isCancelled()) {
                    ev.setCancel(ev0.getCancelThrow());
                }
            });
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

    @Bean
    BungeeChatAPI bungeeChatAPI() {
        return cn.mcres.gyhhy.MXLib.chat.BungeeChatAPI.api;
    }

    @Bean
    FileListenerProvider fileListenerProvider(FileListenerProvider provider) {
        if (provider != null) return provider;
        final Object token = new Object();
        final DefaultFileListenerProvider fileListenerProvider = new DefaultFileListenerProvider(
                DefaultFileListenerProvider.DEFAULT_MAP_CONSTRUCTOR,
                DefaultFileListenerProvider.DEFAULT_SET_CONSTRUCTOR,
                error -> MXBukkitLibPluginStartup.plugin.getLogger().log(
                        Level.SEVERE, "[FileListenerProvider] Error in checking update.",
                        error
                ),
                task -> Bukkit.getScheduler().runTaskAsynchronously(MXBukkitLibPluginStartup.plugin, task),
                token
        );
        final Runnable task = () -> {
            while (true) {
                fileListenerProvider.doTick();
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    break;
                }
            }
        };
        String name = "MXLib - FileListenerProvider Thread";
        final Pointer<Thread> thread = new Pointer<>(new Thread(task, name));
        thread.value().setDaemon(true);
        thread.value().start();
        MXBukkitLibPluginStartup.hooks.add(isEnable -> {
            if (isEnable) {
                if (thread.exists()) {
                    if (thread.value().getState() == Thread.State.RUNNABLE) {
                        return;
                    }
                }
                Thread tt = new Thread(task, name);
                tt.setDaemon(true);
                thread.value(tt).start();
            } else {
                if (thread.exists()) {
                    if (thread.value().getState() == Thread.State.RUNNABLE) {
                        thread.apply(null).interrupt();
                    }
                }
            }
        });
        return fileListenerProvider;
    }

    @Bean
    void CommandAPIBootstrap() {
        cn.mcres.karlatemp.mxlib.impl.cmd.Bootstrap.load();
    }
}
