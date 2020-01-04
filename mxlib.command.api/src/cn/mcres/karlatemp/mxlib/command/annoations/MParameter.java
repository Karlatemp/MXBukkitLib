/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MParameter.java@author: karlatemp@vip.qq.com: 2019/12/29 下午1:39@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command.annoations;

import cn.mcres.karlatemp.mxlib.command.CommandParamParser;
import cn.mcres.karlatemp.mxlib.command.internal.DefaultParameterParser;

import java.lang.annotation.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Define a parameter.
 *
 * @since 2.11
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MParameter {
    /**
     * Mark this parameter must exists
     *
     * @return Must exists
     */
    boolean require() default false;

    /**
     * This attribute only for field type {@code boolean}
     *
     * @return If this parameter was more parameter
     */
    boolean hasParameter() default false;

    /**
     * @return The name of this option.
     */
    String name();

    String description() default "";

    /**
     * The param parser
     *
     * @return The parser of this parameter.
     */
    Class<? extends CommandParamParser> parser() default DefaultParameterParser.class;
}
