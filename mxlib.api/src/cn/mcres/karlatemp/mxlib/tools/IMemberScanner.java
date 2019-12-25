/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: IMemberScanner.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.annotations.Bean;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 类成员搜索器, 在BeanManager获取
 */
@Bean
public interface IMemberScanner {
    @Contract(pure = true)
    @NotNull
    Collection<Method> getAllMethod(@NotNull Class c);

    @Contract(pure = true)
    @NotNull
    Collection<Method> getMethodByAnnotation(@NotNull Class c, @NotNull Class<? extends Annotation> ann);

    @Contract(pure = true)
    @NotNull
    Collection<Field> getAllField(@NotNull Class c);

    @Contract(pure = true)
    @NotNull
    Collection<Field> getFieldByAnnotation(@NotNull Class c, @NotNull Class<? extends Annotation> ann);
}
