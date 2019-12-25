/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ServiceInstallers.java@author: karlatemp@vip.qq.com: 19-9-27 下午12:07@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.annotations.Bean;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Service installers.
 *
 * @since 2.2
 */
@Bean
public final class ServiceInstallers extends ArrayList<ServiceInstaller> implements ServiceInstaller {
    @Override
    public boolean install(@NotNull Class<?> clazz) {
        synchronized (this) {
            for (final ServiceInstaller installer : this) {
                if (installer != null)
                    synchronized (installer) {
                        if (installer.install(clazz)) return true;
                    }
            }
        }
        return false;
    }
}
