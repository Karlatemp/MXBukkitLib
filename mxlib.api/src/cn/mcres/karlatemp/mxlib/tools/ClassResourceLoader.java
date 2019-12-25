/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ClassResourceLoader.java@author: karlatemp@vip.qq.com: 19-9-26 下午6:39@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import cn.mcres.karlatemp.mxlib.annotations.ProhibitType;
import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


/**
 * Used in found class byte code.
 */
@ProhibitBean(ProhibitType.ONLY_CURRENT)
public interface ClassResourceLoader extends ResourceLoader {
    @Nullable
    @Contract(pure = true)
    byte[] found(@NotNull String className, @Nullable ClassLoader loader, @NotNull IBeanManager bm);

    @Nullable
    @Override
    default InputStream found0(@NotNull String path, @Nullable ClassLoader loader) {
        return null;
    }

    @Nullable
    @Override
    default InputStream found(@NotNull String path, @Nullable ClassLoader loader) {
        if (Toolkit.isClassName(path)) {
            byte[] f = found(path, loader, MXBukkitLib.getBeanManager());
            if (f != null)
                return new ByteArrayInputStream(f);
        }
        return ResourceLoader.super.found(path, loader);
    }
}
