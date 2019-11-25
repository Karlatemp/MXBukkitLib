/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NamespaceToolkitWrapper.java@author: karlatemp@vip.qq.com: 19-11-25 下午1:51@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.namespace;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

interface NamespaceToolkitWrapper {
    @NotNull
    String getKey(@NotNull ItemStack stack);

    @NotNull
    String getKey(@NotNull Material material);

    @NotNull
    String getKey(@NotNull Material material, short data);

}
