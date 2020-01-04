/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CommandParamParser.java@author: karlatemp@vip.qq.com: 2019/12/29 下午3:46@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command;

import cn.mcres.karlatemp.mxlib.command.exceptions.ParserFailToParseException;

import java.util.List;

public interface CommandParamParser {

    boolean skipParams(List<String> args, Class<?> field_type);

    Object apply(String option, List<String> arguments, boolean hasParam, Class<?> field_type) throws ParserFailToParseException;

    void tabCompile(List<String> result, List<String> args, Class<?> type);

    Object getDefaultValue(Class<?> type);
}
