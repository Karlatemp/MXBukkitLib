/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: v1_13_R1.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.profile;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;

/**
 *
 * @author 32798
 */
public class v1_13_R1 {
    public static Profile getPlayerProfile(CraftPlayer player) {
        EntityPlayer handle = player.getHandle();
        GameProfile profile = handle.getProfile();
        return new GameProfileEX(profile);
    }
}
