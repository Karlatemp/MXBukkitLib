/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.system;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author 32798
 */
public class ConfSave {
    public static void reload(FileConfiguration cf){
        ConfSave.jvm_enable = cf.getBoolean("jvm.enable", false);
        ConfSave.jvm_jdk_tool = cf.getString("jvm.jdktool");
        ConfSave.jvm_clib = cf.getString("jvm.clib", null);
    }
    public static boolean jvm_enable = true;
    public static String jvm_jdk_tool = "https://dev.tencent.com/u/GYHHY/p/PubFiles/git/raw/master/jdktools/tools.jar";
    public static String jvm_clib = "./plugins/MXBukkitLib/attach.dll";
}
