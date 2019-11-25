/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NT_1_8_R1.java@author: karlatemp@vip.qq.com: 19-11-25 下午1:33@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.namespace;

import net.minecraft.server.v1_8_R1.MinecraftKey;
import net.minecraft.server.v1_8_R1.Item;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NT_1_8_R1 implements NamespaceToolkitWrapper {
    private MinecraftKey getKey(Item item) {
        return (MinecraftKey) Item.REGISTRY.c(item);
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
