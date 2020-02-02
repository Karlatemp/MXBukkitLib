/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MavenRepositories.java@author: karlatemp@vip.qq.com: 2020/1/31 下午4:50@version: 2.0
 */

package cn.mcres.karlatemp.common.maven.annotations;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MavenLocations {
    @Language("http")
    String[] value();
}
