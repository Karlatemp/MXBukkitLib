/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ConfSave.java@author: karlatemp@vip.qq.com: 19-9-11 下午6:26@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.system;

import java.io.File;
import java.security.AccessControlException;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author 32798
 */
public class ConfSave {
    @Deprecated
    public static void reload(FileConfiguration cf) {
        throw new AccessControlException("Method stoped.");
        /*ConfSave.jvm_enable = cf.getBoolean("jvm.enable", false);
         ConfSave.jvm_jdk_tool = cf.getString("jvm.jdktool");
         ConfSave.jvm_clib = cf.getString("jvm.clib", null);
         InstrumentationEvent = cf.getBoolean("events.InstrumentationEvent", false);
         */}

    public static boolean jvm_enable = true;
    public static boolean InstrumentationEvent = false;
    public static String jvm_jdk_tool = "https://dev.tencent.com/u/GYHHY/p/PubFiles/git/raw/master/jdktools/tools.jar";
    public static String jvm_clib = "./plugins/MXBukkitLib/attach.dll";
    public static boolean vmverbose = false;
    public static File VMHelperImplAPP_Bootstrap_store_location;
}
