/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NT_Reflect.java@author: karlatemp@vip.qq.com: 19-11-25 下午5:41@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.namespace;

import cn.mcres.karlatemp.mxlib.reflect.RMethod;
import cn.mcres.karlatemp.mxlib.reflect.Reflect;
import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NT_Reflect implements NamespaceToolkitWrapper {
    @NotNull
    @Override
    public String getKey(@NotNull ItemStack stack) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public String getKey(@NotNull Material material) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public String getKey(@NotNull Material material, short data) {
        throw new UnsupportedOperationException();
    }
    /*private static final Class<?> CraftMagicNumbers = Toolkit.Reflection.forName(
            BukkitToolkit.getCraftBukkitPackage() + ".inventory.CraftMagicNumbers", false, NT_Reflect.class.getClassLoader()
    );
    private static final Class<?> NMSItem = Toolkit.Reflection.forName(
            BukkitToolkit.getNMSPackage() + ".Item", false, NT_Reflect.class.getClassLoader()
    );
    @SuppressWarnings("unchecked")
    private static final RMethod<Object, Object> getItem =
            (RMethod) Reflect.ofClass(CraftMagicNumbers).getMethod("getItem", NMSItem, Material.class);

    static {
        try {
            NMSItem.getField("REGISTRY");
        } catch (NoSuchFieldException e) {
        }
    }*/
}
