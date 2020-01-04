/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: CommandParameter.java@author: karlatemp@vip.qq.com: 2020/1/4 下午1:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command;

import cn.mcres.karlatemp.mxlib.command.annoations.MParameter;

/**
 * The parameter of command.
 *
 * @see cn.mcres.karlatemp.mxlib.command.annoations.MParameter
 */
public interface CommandParameter {
    /**
     * Get the option name of this parameter.
     *
     * @return The option name.
     */
    String name();

    /**
     * The mark type.
     *
     * @return The type.
     */
    Class<?> type();

    /**
     * The value of {@link MParameter#description()}
     *
     * @return Parameter description.
     */
    String description();

    /**
     * Get the parameter parser.
     *
     * @return The parser.
     */
    CommandParamParser parser();

    /**
     * Does this parameter must exists?
     *
     * @return Need exists.
     */
    boolean require();
}
