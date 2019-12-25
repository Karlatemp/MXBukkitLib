/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SpigotHelper.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.spigot;

/**
 *
 * @author 32798
 */
public class SpigotHelper {
    private static final boolean supportSpigot;
    public static boolean isSupportSpigot(){
        return supportSpigot;
    }
    public static void spigotCheck(){
        if(!isSupportSpigot()){
            throw new InternalError("This plugin can only be run on servers that support Spigot!");
        }
    }
    static{
        boolean m = false;
        try{
            Package.getPackage("org.spigotmc");
            org.bukkit.entity.Player.class.getMethod("spigot");
            m = true;
        }catch(Throwable thr){}
        supportSpigot = m;
    }
}
