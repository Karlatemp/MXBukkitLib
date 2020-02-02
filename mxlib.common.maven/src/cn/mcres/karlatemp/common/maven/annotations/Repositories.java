/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: Repositories.java@author: karlatemp@vip.qq.com: 2020/1/31 下午5:00@version: 2.0
 */

package cn.mcres.karlatemp.common.maven.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Repositories {
    String[] value();
}
