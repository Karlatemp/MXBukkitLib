/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MXBukkitLibPluginStartup.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.MXLib;
import cn.mcres.karlatemp.mxlib.ReadPropertiesAutoConfigs;
import cn.mcres.karlatemp.mxlib.share.system.MXBukkitAutoConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

public class MXBukkitLibPluginStartup extends JavaPlugin {
    public static MXBukkitLibPluginStartup plugin;
    public static final List<Consumer<Boolean>> hooks = new ArrayList<>();

    static {
        ReadPropertiesAutoConfigs.resourceLoaders.add(res -> {
            List<InputStream> readers = new ArrayList<>();
            for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
                final InputStream stream = p.getResource(res);
                if (stream != null) {
                    readers.add(stream);
                }
            }
            return readers;
        });
    }

    public MXBukkitLibPluginStartup() {
        plugin = this;
    }

    public static final boolean DEBUG = System.getProperty("mxlib.debug") != null;

    @Override
    public void onEnable() {
        new Metrics(this);
        hooks.forEach(c -> c.accept(true));
        getLogger().setLevel(Level.INFO);
        if (DEBUG) getLogger().setLevel(Level.ALL);
        MXLib.boot();
    }

    @Override
    public void onDisable() {
        hooks.forEach(c -> c.accept(false));
        super.onDisable();
    }

    @Override
    public File getFile() {
        return super.getFile();
    }
}
