/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: vDef_def_def.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.profile;

import cn.mcres.gyhhy.MXLib.RefUtil;
import com.mojang.authlib.GameProfile;
import org.bukkit.entity.Player;
/**
 *
 * @author 32798
 */
public class vDef_def_def {
    public static Profile getPlayerProfile(Player player) {
        if(player == null)return null;
        Object handle = RefUtil.ink(player, "getHandle", RefUtil.emptyClassPar, RefUtil.emptyArgPar);
        GameProfile profile = RefUtil.ink(handle, "getProfile", RefUtil.emptyClassPar, RefUtil.emptyClassPar);
//                handle.getProfile();
        return new GameProfileEX(profile);
    }
}
