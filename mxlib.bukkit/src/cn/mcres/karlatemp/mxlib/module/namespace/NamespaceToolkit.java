/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NamespaceToolkit.java@author: karlatemp@vip.qq.com: 19-11-24 下午9:51@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.namespace;

import cn.mcres.karlatemp.mxlib.annotations.Bean;
import cn.mcres.karlatemp.mxlib.tools.MinecraftKey;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The tools to get the MinecraftKey for Material.
 *
 * @since 2.7
 */
@Bean
public interface NamespaceToolkit {
    @NotNull
    MinecraftKey getKey(@NotNull ItemStack stack);

    @NotNull
    MinecraftKey getKey(@NotNull Material material);

    @NotNull
    MinecraftKey getKey(@NotNull Material material, short data);
}
