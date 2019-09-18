/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BStats.java@author: karlatemp@vip.qq.com: 19-9-17 下午6:10@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.bstats;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * bStats collects some data for plugin authors.
 * <p>
 * Check out https://bStats.org/ to learn more about bStats!
 */
@SuppressWarnings("Duplicates")
public class BStats {
    @SuppressWarnings("SpellCheckingInspection")
    public enum BStatsSendingType {
        BUKKIT, BUNGEE_CORD;
    }

    // The version of this bStats class
    public static final int B_STATS_VERSION = 1;
    private static String URL;
    private static String SERVER_ID;

    private static final String javaVersion = System.getProperty("java.version");
    private static final String osName = System.getProperty("os.name");
    private static final String osArch = System.getProperty("os.arch");
    private static final String osVersion = System.getProperty("os.version");

    private static final boolean
            OVERRIDE_SENDING_TYPE = System.getProperty("mxlib.bstat.override.type") != null,
            OVERRIDE_SERVER_ID = System.getProperty("mxlib.bstat.override.serverid") != null;

    // Is bStats enabled on this server?
    boolean enabled;

    // Should failed requests be logged?
    public static boolean logFailedRequests;

    // Should the sent data be logged?
    public static boolean logSentData;

    // Should the response text be logged?
    public static boolean logResponseStatusText;

    public static BStatsProvider getProvider() {
        return provider;
    }

    public static void setProvider(BStatsProvider provider) {
        BStats.provider = provider;
    }

    static BStatsProvider provider;

    public static String getServerId() {
        return SERVER_ID;
    }

    public static void setServerId(@NotNull String sid) {
        if (SERVER_ID != null && !OVERRIDE_SERVER_ID) {
            throw new RuntimeException("Error: Cannot override the server id");
        }
        SERVER_ID = sid;
    }

    public static void setSendingType(@NotNull BStatsSendingType type) {
        if (URL != null && !OVERRIDE_SENDING_TYPE)
            throw new RuntimeException("Error: Cannot override the sending type.");
        URL = "https://bStats.org/submitData/" + type.name().toLowerCase();
    }

    private String pluginName, pluginVersion;

    /**
     * Gets the plugin specific data.
     * This method is called using Reflection.
     *
     * @return The plugin specific data.
     */
    public Map<String, Object> getPluginData() {
        Map<String, Object> data = new LinkedHashMap<>(3);
        data.put("pluginName", pluginName);
        data.put("pluginVersion", pluginVersion);
        data.put("customCharts", getCustomCharts());
        return data;
    }

    protected static int getPlayerAmount() {
        return provider.getPlayerAmount();
    }

    protected static boolean isOnlineMode() {
        return provider.isOnlineMode();
    }

    protected static String getBukkitVersion() {
        return provider.getBukkitVersion();
    }

    protected static String getBukkitName() {
        return provider.getBukkitName();
    }

    /**
     * Gets the server specific data.
     *
     * @return The server specific data.
     */
    protected static Map<String, Object> getServerData() {
        Map<String, Object> data = new LinkedHashMap<>();
        // Minecraft specific data
        int playerAmount = getPlayerAmount();
        int onlineMode = isOnlineMode() ? 1 : 0;
        String bukkitVersion = getBukkitVersion();
        String bukkitName = getBukkitName();

        // OS/Java specific data
        int coreCount = Runtime.getRuntime().availableProcessors();

        data.put("serverUUID", SERVER_ID);

        data.put("playerAmount", playerAmount);
        data.put("onlineMode", onlineMode);
        data.put("bukkitVersion", bukkitVersion);
        data.put("bukkitName", bukkitName);

        data.put("javaVersion", javaVersion);
        data.put("osName", osName);
        data.put("osArch", osArch);
        data.put("osVersion", osVersion);
        data.put("coreCount", coreCount);

        return data;
    }

    public List<Object> getCustomCharts() {
        return null;
    }

    public static void submitData() {
        final Map<String, Object> data = getServerData();
        data.put("plugins", provider.getPluginDataList());
        provider.sendData(URL, data);
    }
}
