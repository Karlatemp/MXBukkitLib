/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.profile;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

/**
 *
 * @author 32798
 */
public class v1_8_R3 {
    public static Profile getPlayerProfile(CraftPlayer player) {
        EntityPlayer handle = player.getHandle();
        GameProfile profile = handle.getProfile();
        return new GameProfileEX(profile);
    }
}
