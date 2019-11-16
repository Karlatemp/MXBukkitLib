/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MXEventListener.java@author: karlatemp@vip.qq.com: 19-11-16 下午7:20@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.event;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.tools.ServiceInstallers;

/**
 * Event listener
 * <p>
 * Register by ServiceInstaller. So unused interface.
 *
 * @since 2.6
 */
public abstract class MXEventListener {
    public final void register() {
        MXBukkitLib.getBeanManager().getBeanNonNull(ServiceInstallers.class).install(getClass());
    }
}
