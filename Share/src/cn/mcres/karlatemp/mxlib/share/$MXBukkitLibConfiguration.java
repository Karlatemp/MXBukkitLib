/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: $MXBukkitLibConfiguation.java@author: karlatemp@vip.qq.com: 19-9-11 下午6:32@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.annotations.Environment;
import cn.mcres.karlatemp.mxlib.bean.IEnvironmentFactory;
import cn.mcres.karlatemp.mxlib.bukkit.IBukkitConfigurationProcessor;
import cn.mcres.karlatemp.mxlib.exceptions.ObjectCreateException;

public class $MXBukkitLibConfiguration {
    @Environment
    public static $MXBukkitLibConfiguration configuration = new $MXBukkitLibConfiguration();

    static void load(IEnvironmentFactory factory, IBukkitConfigurationProcessor bc) {
        try {
            MXBukkitLibPluginStartup.plugin.getLogger().fine("Loading Config.yml");
            configuration = factory.loadEnvironment($MXBukkitLibConfiguration.class, bc.load(MXBukkitLibPluginStartup.plugin, "config.yml"));
            MXBukkitLibPluginStartup.plugin.getLogger().fine("Loaded config.yml");
            MXBukkitLibPluginStartup.plugin.getLogger().fine(() -> String.valueOf(factory.toEnvironment(configuration)));
        } catch (ObjectCreateException e) {
            MXBukkitLib.getLogger().printStackTrace(e);
        }
    }

    public static class $JVM {
        @SuppressWarnings("SpellCheckingInspection")
        public String passwd;
    }

    public $JVM jvm = new $JVM();

    public static class $Logger {
        public boolean enable = true;
        public boolean alignment = true;// 对齐
    }

    public $Logger logger = new $Logger();
}
