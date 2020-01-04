/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DefaultParameterParser.java@author: karlatemp@vip.qq.com: 2019/12/29 下午1:52@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command.internal;

import cn.mcres.karlatemp.mxlib.command.CommandParamParser;
import cn.mcres.karlatemp.mxlib.command.exceptions.ParserFailToParseException;

import java.util.List;

public class DefaultParameterParser implements CommandParamParser {
    public static Object getDefault(Class<?> type) {
        if (type == boolean.class) return false;
        if (type == int.class) return 0;
        if (type == long.class) return 0L;
        if (type == double.class) return 0D;
        if (type == short.class) return (short) 0;
        if (type == float.class) return 0f;
        if (type == char.class) return '\000';
        if (type == byte.class) return (byte) 0;
        return null;
    }

    @Override
    public boolean skipParams(List<String> args, Class<?> field_type) {
        if (args.size() < 2) return false;
        args.remove(0);
        return true;
    }

    @Override
    public Object apply(String option, List<String> arguments, boolean hasParam, Class<?> field_type) throws ParserFailToParseException {
        if (!hasParam && field_type == boolean.class) return true;
        if (arguments.isEmpty()) {
            throw new ParserFailToParseException("Option " + option + " need more parameter.", "option.need.more.param", new Object[]{option}, null);
        }
        String next = arguments.remove(0);
        if (field_type == boolean.class) return parseBool(next);
        try {
            if (field_type == int.class) return Integer.parseInt(next);
            if (field_type == long.class) return Long.parseLong(next);
            if (field_type == double.class) return Double.parseDouble(next);
            if (field_type == short.class) return Short.parseShort(next);
            if (field_type == float.class) return Float.parseFloat(next);
            if (field_type == byte.class) return Byte.parseByte(next);
        } catch (NumberFormatException nfe) {
            throw new ParserFailToParseException("Please input a valid number: " + next, "not.a.valid.number", new Object[]{next}, nfe);
        }
        if (field_type == char[].class) return next.toCharArray();
        return next;
    }

    @Override
    public void tabCompile(List<String> result, List<String> args, Class<?> type) {
        if (type == boolean.class) {
            String kw = args.isEmpty() ? null : args.remove(0);
            Tools.keyword(result, kw, "true", "false");
        }
    }

    @Override
    public Object getDefaultValue(Class<?> type) {
        return getDefault(type);
    }

    public static boolean parseBool(String next) {
        if (next.equals("true")) {
            return true;
        }
        if (next.equals("false")) {
            return false;
        }
        if (next.trim().isEmpty()) return false;
        if (next.trim().replaceFirst("^(-|\\+|)0(\\.0+)$", "").isEmpty()) return false; //zero
        return true;
    }
}
