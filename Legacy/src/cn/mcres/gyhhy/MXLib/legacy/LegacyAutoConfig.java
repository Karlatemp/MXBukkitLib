/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LegacyAutoConfig.java@author: karlatemp@vip.qq.com: 19-9-12 下午7:59@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.legacy;

import cn.mcres.gyhhy.MXLib.Core;
import cn.mcres.gyhhy.MXLib.system.VMHelper;
import cn.mcres.gyhhy.MXLib.yggdrasil.Yggdrasil;
import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.annotations.Bean;
import cn.mcres.karlatemp.mxlib.annotations.Configuration;
import cn.mcres.karlatemp.mxlib.bean.IEnvironmentFactory;
import cn.mcres.karlatemp.mxlib.network.NetWorkManager;
import cn.mcres.karlatemp.mxlib.share.MXBukkitLibPluginStartup;
import cn.mcres.karlatemp.mxlib.share.$MXBukkitLibConfiguration;

import java.lang.instrument.Instrumentation;
import java.net.URL;

@Configuration
public class LegacyAutoConfig {
    static final NListener listener = new NListener();

    @Bean
    void boot() {
        Core.setPlugin(MXBukkitLibPluginStartup.plugin);
        if ($MXBukkitLibConfiguration.configuration.logger.enable) {
            LoggerInject.inject();
        }
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
