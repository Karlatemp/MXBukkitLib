/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NamespaceToolkitInternal.java@author: karlatemp@vip.qq.com: 19-11-25 下午1:58@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.namespace;

import cn.mcres.karlatemp.mxlib.share.MinecraftKey;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NamespaceToolkitInternal {
    public static NamespaceToolkit wrap(Object o) {
        if (o instanceof NamespaceToolkit) return (NamespaceToolkit) o;
        if (o instanceof NamespaceToolkitWrapper) {
            NamespaceToolkitWrapper wp = (NamespaceToolkitWrapper) o;
            return new NamespaceToolkit() {
                @NotNull
                @Override
                public MinecraftKey getKey(@NotNull ItemStack stack) {
                    return Objects.requireNonNull(MinecraftKey.valueOf(wp.getKey(stack)));
                }

                @NotNull
                @Override
                public MinecraftKey getKey(@NotNull Material material) {
                    return Objects.requireNonNull(MinecraftKey.valueOf(wp.getKey(material)));
                }

                @NotNull
                @Override
                public MinecraftKey getKey(@NotNull Material material, short data) {
                    return Objects.requireNonNull(MinecraftKey.valueOf(wp.getKey(material, data)));
                }
            };
        }
        throw new NullPointerException();
    }
}
