/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: $MXBukkitLibConfiguration.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.annotations.Environment;
import cn.mcres.karlatemp.mxlib.bean.IEnvironmentFactory;
import cn.mcres.karlatemp.mxlib.bukkit.IBukkitConfigurationProcessor;
import cn.mcres.karlatemp.mxlib.exceptions.ObjectCreateException;

import java.util.ArrayList;
import java.util.List;

public class $MXBukkitLibConfiguration {
    @Environment
    public static $MXBukkitLibConfiguration configuration = new $MXBukkitLibConfiguration();

    static void load(IEnvironmentFactory factory, IBukkitConfigurationProcessor bc) {
        try {
            MXBukkitLib.debug("Loading Config.yml");
            configuration = factory.loadEnvironment($MXBukkitLibConfiguration.class, bc.load(MXBukkitLibPluginStartup.plugin, "config.yml"));
            bc.save(factory.toEnvironment(configuration), "config.yml", MXBukkitLibPluginStartup.plugin);
            MXBukkitLib.debug("Loaded config.yml");
            MXBukkitLib.debug(() -> String.valueOf(factory.toEnvironment(configuration)));
        } catch (ObjectCreateException e) {
            MXBukkitLib.getLogger().printStackTrace(e);
        }
    }

    public static class $JVM {
        @SuppressWarnings("SpellCheckingInspection")
        public String passwd;
    }

    public static class $Update {
        public boolean autoupdate = false;
        public boolean enable = true;
    }

    public $Update update = new $Update();

    public static class $Events {

        public static class $NetWork {
            public boolean enable = true;
            public boolean println = true;
            public List<String> skips = new ArrayList<>();
        }

        public $NetWork network = new $NetWork();
    }

    public $JVM jvm = new $JVM();
    public $Events events = new $Events();

    public static class $Logger {
        public boolean enable = false;
        public boolean alignment = true;// 对齐
    }

    public $Logger logger = new $Logger();
}
