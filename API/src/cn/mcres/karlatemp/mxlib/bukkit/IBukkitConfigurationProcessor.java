package cn.mcres.karlatemp.mxlib.bukkit;

import java.util.Map;

public interface IBukkitConfigurationProcessor {
    /**
     * Load yml from [plugin.jar]/[path] and ${server_root}/plugins/[plugin]/[path]
     *
     * @param plugin The plugin object
     * @param path   The Path
     */
    Map<String, Object> load(Object plugin, String path);

    /**
     * Loading data from bukkit configuration.
     *
     * @param config the type of value is
     *               {@link org.bukkit.configuration.ConfigurationSection}
     */
    @SuppressWarnings("JavadocReference")
    Map<String, Object> load(Object config);
}
