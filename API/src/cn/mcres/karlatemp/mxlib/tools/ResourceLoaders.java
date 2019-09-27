/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ResourceLoaders.java@author: karlatemp@vip.qq.com: 19-9-26 下午9:29@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.annotations.Bean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Resource loader set
 *
 * @since 2.2
 */
@Bean
public final class ResourceLoaders extends ArrayList<ResourceLoader> implements ResourceLoader {
    @Nullable
    @Override
    public InputStream found0(@NotNull String path, @Nullable ClassLoader loader) {
        return null;
    }

    @Nullable
    @Override
    public InputStream found(@NotNull String path, @Nullable ClassLoader loader) {
        for (ResourceLoader rl : this) {
            InputStream is = rl.found(path, loader);
            if (is != null) return is;
        }
        return ResourceLoader.super.found(path, loader);
    }
}
