/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ItemStackNBT.java@author: karlatemp@vip.qq.com: 2020/1/30 下午6:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.item.nbt;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.nbt.NBTTagCompound;
import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.bukkit.inventory.ItemStack;

public class ItemStackNBT {
    public NBTTagCompound compound(ItemStack item) {
        throw new UnsupportedOperationException();
    }

    public ItemStack compound(NBTTagCompound item) {
        throw new UnsupportedOperationException();
    }

    private static final ItemStackNBT INSTANCE;

    static {
        ItemStackNBT instance = new ItemStackNBT();
        try {
            instance = Toolkit.Reflection.allocObject(
                    Class.forName("cn.mcres.karlatemp.mxlib.common.item.nbt.IS" + BukkitToolkit.getNMS()).asSubclass(ItemStackNBT.class)
            );
        } catch (Throwable any) {
            MXBukkitLib.getLogger().printStackTrace(any);
        }
        INSTANCE = instance;
    }
}
