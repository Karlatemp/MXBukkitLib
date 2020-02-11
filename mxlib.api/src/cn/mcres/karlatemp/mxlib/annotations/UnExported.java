/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: UnExported.java@author: karlatemp@vip.qq.com: 2020/2/7 下午6:08@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * For J9 -> J8 Module
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface UnExported {
}
