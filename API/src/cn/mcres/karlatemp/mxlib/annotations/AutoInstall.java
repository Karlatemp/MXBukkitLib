/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AutoInstall.java@author: karlatemp@vip.qq.com: 19-9-27 下午12:05@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The type annotation AutoInstall will load into ServiceInstaller
 *
 * @see cn.mcres.karlatemp.mxlib.tools.ServiceInstaller#install(Class)
 * @since 2.2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoInstall {
}
