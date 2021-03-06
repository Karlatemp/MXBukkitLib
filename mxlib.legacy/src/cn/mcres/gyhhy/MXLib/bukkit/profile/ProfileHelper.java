/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ProfileHelper.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.profile;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import cn.mcres.gyhhy.MXLib.bukkit.MXAPI;
import org.bukkit.entity.Player;

/**
 * @author 32798
 */
public class ProfileHelper {

    public static Profile getPlayerProfile(Player player) {
        Method met = getMethod();
        if (met != null) {
            return run(met, player);
        }
        return vDef_def_def.getPlayerProfile(player);
    }

    private static Method met;
    private static boolean fa = true;

    private static Method getMethod() {
        if (fa) {
            fa = false;
            if (met == null) {
                try {
                    Class c = Class.forName(ProfileHelper.class.getPackage() + "." + MXAPI.getInfo().getServerNMSVersion());
                    Method[] mets = c.getMethods();
                    for (Method m : mets) {
                        if (m.getName().equals("getPlayerProfile")) {
                            if (m.getParameterCount() == 1) {
                                if (Modifier.isStatic(m.getModifiers())) {
                                    met = m;
                                    m.setAccessible(true);
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                }
            }
        }
        return met;
    }

    private static Profile run(Method met, Player player) {
        try {
            return (Profile) met.invoke(met, player);
        } catch (RuntimeException | Error rt) {
            throw rt;
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }
}
