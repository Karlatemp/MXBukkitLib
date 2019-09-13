/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MXAPI.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:50@version: 2.0
 */
package cn.mcres.gyhhy.MXLib.bukkit;


import cn.mcres.karlatemp.mxlib.MXBukkitLib;

/**
 * <pre><code>public static void main(String[] args){
 *      System.out.println("MXBukkitLib built using version: " + MXAPI.version);
 *      System.out.println("MXBukkitLib version: " + MXAPI.getVersion());
 * }</code></pre>
 *
 * @author 32798
 */
public class MXAPI {
    /**
     * Get NMS Class<br>
     * Add in version 0.6
     */
    public static Class<?> getNMSClass(String clazz) {
        try {
            return Class.forName("net.minecraft.server." + getInfo().getServerNMSVersion() + "." + clazz);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    /**
     * Get Obc Class<br>
     * Add in version 0.6
     */
    public static Class<?> getOBCClass(String clazz) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getInfo().getServerNMSVersion() + "." + clazz);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    /**
     * Lib Current Version
     */
    public static final String version = MXBukkitLib.BUILD_VERSION;

    public static CommandHelper getCommandHelper() {
        return CommandHelper.getHelper();
    }

    /**
     * Lib Current Version
     */
    public static String getVersion() {
        return version;
    }

    /**
     * Get Server Info
     */
    public static Info getInfo() {
        return Info.getInfo();
    }

    @Deprecated
    static TitleAPI titleAPI = new TitleAPIWrap(MXBukkitLib.getBeanManager().getBean(cn.mcres.karlatemp.mxlib.bukkit.TitleAPI.class));

    @Deprecated
    public static TitleAPI getTitleAPI() {
        return titleAPI;
    }

    /**
     * Is there Gson support?<br>
     * Added in version 0.5
     */
    public static final boolean gson = true;

    /**
     * Check if the provided version number information is supported in the current version.<br>
     * <hr>E.g.(If current version is 0.5.1)<br>
     * <table border="1"><tbody>
     * <tr><td>Input</td><td>Return</td></tr>
     * <tr><td>"0.5"</td><td>true</td></tr>
     * <tr><td>"0.5.1"</td><td>true</td></tr>
     * <tr><td>"0.4"</td><td>true</td></tr>
     * <tr><td>"0.5.2"</td><td>false</td></tr>
     * <tr><td>"0.6"</td><td>false</td></tr>
     * </tbody></table>
     * Added in version 0.5.1
     *
     * @param ver Version to be tested
     * @return Whether to support (more than)
     */
    public static boolean support(final String ver) {
        return support(version, ver);
    }

    public static boolean support(final String now, final String need) {
//        final String now = version,need = ver;
        if (need.equals(now)) return true;
        final String[] a = need.split("\\."), b = now.split("\\.");
        int off = Math.max(a.length, b.length);
        for (int i = 0; i < off; i++) {
            if (i < a.length && i < b.length) {
                String aa = a[i], bb = b[i];
                int ca = Integer.parseInt(aa), cb = Integer.parseInt(bb);
                if (ca > cb) {
                    return false;
                }
                if (ca < cb) {
                    return true;
                }
            } else if (i >= a.length && i < b.length) {
                return true;
            } else {
                return false;
            }

        }
        return false;
    }
}
