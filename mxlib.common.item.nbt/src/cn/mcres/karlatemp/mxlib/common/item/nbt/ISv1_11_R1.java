/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ISv1_13_R1.java@author: karlatemp@vip.qq.com: 2020/1/30 下午6:56@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.item.nbt;

import cn.mcres.karlatemp.mxlib.nbt.NBTCompressedStreamTools;
import cn.mcres.karlatemp.mxlib.nbt.NBTReadLimiter;
import cn.mcres.karlatemp.mxlib.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.*;

public class ISv1_11_R1 extends ItemStackNBT {
    public NBTTagCompound compound(ItemStack item) {
        var ng = new net.minecraft.server.v1_11_R1.NBTTagCompound();
        CraftItemStack.asNMSCopy(item).save(ng);
        var out = new ByteArrayOutputStream();
        try {
            net.minecraft.server.v1_11_R1.NBTCompressedStreamTools.a(ng, (DataOutput) new DataOutputStream(out));
            return NBTCompressedStreamTools.loadCompound(
                    new DataInputStream(new ByteArrayInputStream(out.toByteArray())),
                    NBTReadLimiter.UN_LIMITED
            );
        } catch (IOException ioe) {
            return null;
        }
    }

    public ItemStack compound(NBTTagCompound nbt) {
        var out = new ByteArrayOutputStream();
        try {
            NBTCompressedStreamTools.write(nbt, new DataOutputStream(out));
            return CraftItemStack.asCraftMirror(new net.minecraft.server.v1_11_R1.ItemStack(net.minecraft.server.v1_11_R1.NBTCompressedStreamTools.a(new ByteArrayInputStream(out.toByteArray()))));
        } catch (IOException ioe) {
            return null;
        }
    }
}
