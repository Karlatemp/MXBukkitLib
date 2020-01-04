/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CacheCommandProviders.java@author: karlatemp@vip.qq.com: 2019/12/29 下午2:25@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheCommandProviders {
    private static final Cache<Class<?>, CommandProvider> PROVIDERS = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.MILLISECONDS).build();

    public static CommandProvider getProvider(Class<? extends CommandProvider> provider) {
        CommandProvider p = PROVIDERS.getIfPresent(provider);
        if (p != null) return p;
        try {
            CommandProvider provider1 = Toolkit.Reflection.allocObject(provider);
            PROVIDERS.put(provider, provider1);
            return provider1;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
