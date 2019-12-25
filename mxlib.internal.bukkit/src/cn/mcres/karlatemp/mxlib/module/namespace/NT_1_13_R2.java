/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NT_1_13_R2.java@author: karlatemp@vip.qq.com: 19-11-25 下午1:56@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.namespace;

import net.minecraft.server.v1_13_R2.IRegistry;
import net.minecraft.server.v1_13_R2.Item;
import net.minecraft.server.v1_13_R2.MinecraftKey;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NT_1_13_R2 implements NamespaceToolkitWrapper {
    private MinecraftKey getKey(Item item) {
        return IRegistry.ITEM.getKey(item);
    }

    @NotNull
    @Override
    public String getKey(@NotNull ItemStack stack) {
        Item item = CraftItemStack.asNMSCopy(stack).getItem();
        return getKey(item).toString();
    }

    @NotNull
    @Override
    public String getKey(@NotNull Material material) {
        return getKey(
                CraftMagicNumbers.getItem(material)
        ).toString();
    }

    @NotNull
    @Override
    public String getKey(@NotNull Material material, short data) {
        return getKey(
                CraftMagicNumbers.getItem(material)
        ).toString();
    }
}
