/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitConfigurationProcessor.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.bukkit.IBukkitConfigurationProcessor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BukkitConfigurationProcessor implements IBukkitConfigurationProcessor {
    @Override
    public void save(Map<String, Object> config, String path, Object plugin) {
        Logger logger = MXBukkitLib.getAsJavaLogger();
        try {
            MXBukkitLib.debug(() -> "Saving configuration for plugin " + plugin + " in path " + path + " with data: " + config);
            Plugin pl = (Plugin) plugin;
            logger = pl.getLogger();
            YamlConfiguration yc = new YamlConfiguration();
            for (Map.Entry<String, Object> data : config.entrySet()) {
                yc.set(data.getKey(), data.getValue());
            }
            yc.save(new File(pl.getDataFolder(), path));
        } catch (Throwable thr) {
            logger.log(Level.SEVERE, "Error in saving config \"" + path + "\" of plugin " + plugin);
        }
    }

    @Override
    public Map<String, Object> load(@NotNull Object plugin, @NotNull String path) {
        Plugin p = (Plugin) plugin;
        YamlConfiguration yc = new YamlConfiguration();
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        final InputStream resource = p.getResource(path);
        if (resource != null) {
            try (InputStream read = resource) {
                try (Reader reader = new InputStreamReader(read, StandardCharsets.UTF_8)) {
                    yc.load(reader);
                }
            } catch (IOException | InvalidConfigurationException ioe) {
                p.getLogger().log(Level.SEVERE, null, ioe);
            }
        }
        File file = new File(p.getDataFolder(), path);
        if (file.exists() && file.isFile()) {
            try {
                yc.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                p.getLogger().log(Level.SEVERE, null, e);
            }
        }
        return load(yc);
    }

    @Override
    public Map<String, Object> load(Object config) {
        if (config == null) return null;
        ConfigurationSection section = (ConfigurationSection) config;
        Map<String, Object> map = new LinkedHashMap<>();
        final Map<String, Object> values = section.getValues(false);
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String ky = entry.getKey();
            Object ov = entry.getValue();
            if (ov instanceof ConfigurationSection) {
                ov = load(ov);
            }
            map.put(ky, ov);
        }
        return map;
    }
}
