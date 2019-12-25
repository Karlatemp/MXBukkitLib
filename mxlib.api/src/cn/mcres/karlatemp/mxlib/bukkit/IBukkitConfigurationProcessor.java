/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: IBukkitConfigurationProcessor.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.bukkit;

import java.util.Map;

public interface IBukkitConfigurationProcessor {
    /**
     * Load yml from [plugin.jar]/[path] and ${server_root}/plugins/[plugin]/[path]
     *
     * @param plugin The plugin object
     * @param path   The Path
     * @return The configuration data
     */
    Map<String, Object> load(Object plugin, String path);

    /**
     * Loading data from bukkit configuration.
     *
     * @param config the type of value is
     *               {@link org.bukkit.configuration.ConfigurationSection}
     * @return The configuration data
     */
    @SuppressWarnings("JavadocReference")
    Map<String, Object> load(Object config);

    void save(Map<String, Object> config, String path, Object plugin);
}
