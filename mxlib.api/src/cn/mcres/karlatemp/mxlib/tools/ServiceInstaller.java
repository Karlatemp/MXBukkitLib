/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ServiceInstaller.java@author: karlatemp@vip.qq.com: 19-9-27 下午12:06@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import cn.mcres.karlatemp.mxlib.annotations.ProhibitType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The service installer. Can install the service.
 *
 * @since 2.2
 */
@ProhibitBean(ProhibitType.ONLY_CURRENT)
public interface ServiceInstaller {
    @Contract
    boolean install(@NotNull Class<?> clazz);
}
