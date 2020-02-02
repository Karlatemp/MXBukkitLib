/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MLocale.java@author: karlatemp@vip.qq.com: 19-11-11 下午10:12@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.translate;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.annotations.Resource;
import cn.mcres.karlatemp.mxlib.module.chat.BungeeChatAPI;
import cn.mcres.karlatemp.mxlib.tools.IClassScanner;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.translate.FunctionTranslate;
import cn.mcres.karlatemp.mxlib.translate.LinkedTranslate;
import cn.mcres.karlatemp.mxlib.translate.MTranslate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Level;

import static cn.mcres.karlatemp.mxlib.share.BukkitToolkit.getCallerPlugin;

public final class MLocale {
    private static final Gson g = new Gson();
    private static final TransLateSetting DEFAULT_SETTING = new TransLateSetting().load();

    public static MTranslate getTranslate(Plugin p) {
        MTranslate t = translates.get(p);
        if (t == null) return loadTranslate(p);
        return t;
    }

    static class TransLateSetting {
        enum Type {
            Properties {
                @Override
                Function<String, String> read(InputStream fis) throws IOException {
                    java.util.Properties p = new Properties();
                    try (fis) {
                        try (InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
                            p.load(reader);
                        }
                    }
                    return p::getProperty;
                }
            },
            Json {
                private void add(String path, Map<String, String> map, Map<?, ?> load) {
                    for (Map.Entry<?, ?> entry : load.entrySet()) {
                        String key = String.valueOf(entry.getKey());
                        Object v = entry.getValue();
                        String p;
                        if (path == null) {
                            p = key;
                        } else {
                            p = path + key;
                        }
                        if (v instanceof Map) {
                            add(p + '.', map, (Map<?, ?>) v);
                        } else {
                            map.put(key, String.valueOf(v));
                        }
                    }
                }

                private Map<String, String> serviced(Map<String, Object> map) {
                    Map<String, String> ss = new HashMap<>();
                    add(null, ss, map);
                    return ss;
                }

                @Override
                Function<String, String> read(InputStream fis) throws IOException {
                    try (fis) {
                        try (InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
                            Map<String, Object> map = g.fromJson(reader, new TypeToken<Map<String, Object>>() {
                            }.getType());
                            return serviced(map)::get;
                        }
                    }
                }
            },
            Yaml {
                @Override
                Function<String, String> read(InputStream file) throws IOException {
                    try (file) {
                        YamlConfiguration yc = new YamlConfiguration();
                        try {
                            yc.load(new InputStreamReader(file, StandardCharsets.UTF_8));
                        } catch (InvalidConfigurationException e) {
                            throw new IOException(e);
                        }
                        return yc::getString;
                    }
                }
            };

            abstract Function<String, String> read(InputStream file) throws IOException;
        }

        String type; // One of "properties", "json", "yaml", default "yaml"
        String prefix; // The lang file prefix. Default: "translate"
        String suffix;
        transient Type mode;

        TransLateSetting save() {
            if (prefix == null) {
                prefix = "translate";
            }
            if (mode == null) {
                type = "yaml";
            } else {
                switch (mode) {
                    case Json:
                        type = "json";
                        break;
                    case Properties:
                        type = "properties";
                        break;
                    default:
                        type = "yaml";
                        break;
                }
            }
            if (suffix == null) {
                suffix = type;
            }
            return this;
        }

        TransLateSetting load() {
            switch (String.valueOf(type).toLowerCase()) {
                case "properties":
                    mode = Type.Properties;
                    break;
                case "json":
                    mode = Type.Json;
                    break;
                case "yaml":
                default:
                    mode = Type.Yaml;
                    break;
            }
            return save();
        }
    }

    @Resource
    private static IClassScanner $ignore; // Load this class
    private static final Map<Plugin, BTranslate> translates = new HashMap<>();

    static {
        MXBukkitLib.debug("[MLocale] module loaded.");
        MXBukkitLib.debug(() -> {
            MXBukkitLib.getLogger().printStackTrace(new Throwable().fillInStackTrace());
            return "<StackTrace Dump>";
        });
    }

    private static Plugin getCaller() {
        return getCallerPlugin(1);
    }

    private static void load(TransLateSetting setting, LinkedTranslate link, File root, String prefix, String suffix,
                             String lst, Plugin plugin) {
        File using = new File(root, prefix + lst + suffix);
        MXBukkitLib.debug(() -> "[MLocale] Loading translate for " + plugin + " with file " + using);
        {
            InputStream stream = plugin.getResource("META-INF/lang" + lst + suffix);
            if (stream != null) {
                try (stream) {
                    link.getTranslates().add(0, new FunctionTranslate(setting.mode.read(stream)));
                } catch (IOException e) {
                    plugin.getLogger().log(Level.SEVERE, "[MXBukkitLib] Error in loading translate locale " + lst, e);
                }
            }
        }
        if (using.isFile()) {
            try {
                link.getTranslates().add(0,
                        new FunctionTranslate(setting.mode.read(new FileInputStream(using)))
                );
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "[MXBukkitLib] Error in loading translate locale " + lst + " (" + using + ")", e);
            }
        } else {
            InputStream stream = plugin.getResource("META-INF/lang" + lst + suffix);
            if (stream != null) {
                try (stream) {
                    new File(using, "..").mkdirs();
                    using.createNewFile();
                    try (var output = new FileOutputStream(using)) {
                        Toolkit.IO.writeTo(stream, output);
                    }
                } catch (IOException error) {
                    plugin.getLogger().log(Level.SEVERE, "[MXBukkitLib] Error in saving translate locale " + lst + " (" + using + ")", error);
                }
            }
        }
    }

    private static synchronized BTranslate loadTranslate(@NotNull Plugin plugin) {
        final InputStream resource = plugin.getResource("META-INF/translate.json");
        TransLateSetting setting = DEFAULT_SETTING;
        if (resource != null) {
            try (final InputStream stream = resource) {
                try (final InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                    setting = g.fromJson(reader, TransLateSetting.class).load();
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "[MXBukkitLib] Error in loading translate setting.", e);
            }
        }
        File root = plugin.getDataFolder();
        String pts = setting.prefix;
        String ed = '.' + setting.suffix;

        Locale lc = Locale.getDefault();
        {
            final FileConfiguration config = plugin.getConfig();
            final String locale = config.getString("mxlib.locale", null);
            if (locale != null) {
                try {
                    lc = Locale.forLanguageTag(locale);
                } catch (Throwable thr) {
                    plugin.getLogger().log(Level.SEVERE, "[MXBukkitLib] Error in loading translate locale [" + locale + "]", thr);
                }
            }
        }

        String lang = lc.getLanguage();
        String region = lc.getCountry();
        String varInt = lc.getVariant();

        LinkedBukkitTranslate translate = new LinkedBukkitTranslate();
        load(setting, translate, root, pts, ed,
                "", plugin);
        if (lang != null) {
            load(setting, translate, root, pts, ed,
                    "_" + lang, plugin);
            if (region != null) {
                load(setting, translate, root, pts, ed,
                        "_" + lang + "_" + region, plugin);
                if (varInt != null) {
                    load(setting, translate, root, pts, ed,
                            "_" + lang + "_" + region + "_" + varInt, plugin);
                }
            }
        }
        {
            WrappedTranslate wrapped = (WrappedTranslate) translates.get(plugin);
            if (wrapped == null) {
                wrapped = new WrappedTranslate(translate, plugin);
                translates.put(plugin, wrapped);
                return wrapped;
            }
            wrapped.mapped = translate;
            return wrapped;
        }
    }

    public static BTranslate reloadTranslate() {
        Plugin caller = getCallerPlugin();
        if (caller == null) {
            throw new NullPointerException("Cannot find the caller.");
        }
        return loadTranslate(caller);
    }

    public static BTranslate getTranslate() {
        return getTranslate0();
    }

    private static synchronized BTranslate getTranslate0() {
        Plugin caller = getCallerPlugin(1);
        if (caller == null) {
            throw new NullPointerException("Cannot find the caller.");
        }
        final BTranslate translate = translates.get(caller);
        if (translate == null) {
            return loadTranslate(caller);
        }
        return translate;
    }

    public static void sendMessage(@NotNull CommandSender sender, @NotNull String key) {
        sender.sendMessage(getTranslate0().asMessage(key));
    }

    public static void sendComponent(@NotNull CommandSender sender, @NotNull String key) {
        final BaseComponent[] components = getTranslate0().asComponents(key);
        if (components == null) {
            sender.sendMessage(key);
        } else if (sender instanceof Player) {
            try {
                MXBukkitLib.getBeanManager().getBeanNonNull(
                        BungeeChatAPI.class
                ).send((Player) sender, components);
            } catch (Throwable thr) {
                sender.sendMessage(new TextComponent(components).toLegacyText());
            }
        } else {
            sender.sendMessage(new TextComponent(components).toLegacyText());
        }
    }

    public static void sendMessage(@NotNull CommandSender sender, @NotNull String key, Object... params) {
        sender.sendMessage(getTranslate0().asMessage(key, params));
    }
}
